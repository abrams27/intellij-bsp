package org.jetbrains.magicmetamodel.impl.workspacemodel.impl.updaters

import com.intellij.workspaceModel.storage.WorkspaceEntityStorageBuilder
import com.intellij.workspaceModel.storage.bridgeEntities.LibraryEntity
import com.intellij.workspaceModel.storage.bridgeEntities.LibraryRoot
import com.intellij.workspaceModel.storage.bridgeEntities.LibraryRootTypeId
import com.intellij.workspaceModel.storage.bridgeEntities.LibraryTableId
import com.intellij.workspaceModel.storage.bridgeEntities.ModuleEntity
import com.intellij.workspaceModel.storage.bridgeEntities.ModuleId
import com.intellij.workspaceModel.storage.bridgeEntities.addLibraryEntity

internal data class Library(
  val displayName: String,
  val jar: String,
) : WorkspaceModelEntity()

internal class LibraryEntityUpdater(
  private val workspaceModelDetails: WorkspaceModelDetails,
) : WorkspaceModelEntityUpdater<Library, LibraryEntity> {

  override fun addEntity(entityToAdd: Library, parentModuleEntity: ModuleEntity): LibraryEntity {
    return workspaceModelDetails.workspaceModel.updateProjectModel {
      addLibraryEntity(it, parentModuleEntity, entityToAdd)
    }
  }

  private fun addLibraryEntity(
    builder: WorkspaceEntityStorageBuilder,
    parentModuleEntity: ModuleEntity,
    entityToAdd: Library,
  ): LibraryEntity =
    builder.addLibraryEntity(
      name = entityToAdd.displayName,
      tableId = LibraryTableId.ModuleLibraryTableId(ModuleId(parentModuleEntity.name)),
      listOf(toLibraryRoot(entityToAdd)),
      emptyList(),
      workspaceModelDetails.projectConfigSource
    )

  private fun toLibraryRoot(entityToAdd: Library): LibraryRoot =
    LibraryRoot(
      url = workspaceModelDetails.virtualFileManager.fromUrl(entityToAdd.jar),
      type = LibraryRootTypeId.SOURCES,
    )
}

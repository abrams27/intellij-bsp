package org.jetbrains.magicmetamodel.impl.workspacemodel.impl.updaters

import com.intellij.workspaceModel.ide.WorkspaceModel
import com.intellij.workspaceModel.storage.EntitySource
import com.intellij.workspaceModel.storage.bridgeEntities.ModuleEntity
import com.intellij.workspaceModel.storage.url.VirtualFileUrlManager


internal abstract class WorkspaceModelEntity

internal data class WorkspaceModelDetails(
  val workspaceModel: WorkspaceModel,
  val virtualFileManager: VirtualFileUrlManager,
  val projectConfigSource: EntitySource,
)

internal interface WorkspaceModelEntityUpdater<R : WorkspaceModelEntity> {

  fun addEntries(entriesToAdd: Collection<R>, parentModuleEntity: ModuleEntity) =
    entriesToAdd.forEach { addEntity(it, parentModuleEntity) }

  fun addEntity(entityToAdd: R, parentModuleEntity: ModuleEntity)
}

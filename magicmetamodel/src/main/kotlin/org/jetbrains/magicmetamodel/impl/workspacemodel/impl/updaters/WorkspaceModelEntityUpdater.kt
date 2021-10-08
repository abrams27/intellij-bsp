package org.jetbrains.magicmetamodel.impl.workspacemodel.impl.updaters

import com.intellij.workspaceModel.ide.WorkspaceModel
import com.intellij.workspaceModel.storage.EntitySource
import com.intellij.workspaceModel.storage.bridgeEntities.ModuleEntity
import com.intellij.workspaceModel.storage.impl.WorkspaceEntityBase
import com.intellij.workspaceModel.storage.url.VirtualFileUrlManager


internal abstract class WorkspaceModelEntity

internal data class WorkspaceModelDetails(
  val workspaceModel: WorkspaceModel,
  val virtualFileManager: VirtualFileUrlManager,
  val projectConfigSource: EntitySource,
)

internal interface WorkspaceModelEntityUpdater<E : WorkspaceModelEntity, R : WorkspaceEntityBase> {

  fun addEntries(entriesToAdd: Collection<E>, parentModuleEntity: ModuleEntity): Collection<R> =
    entriesToAdd.map { addEntity(it, parentModuleEntity) }

  fun addEntity(entityToAdd: E, parentModuleEntity: ModuleEntity): R
}

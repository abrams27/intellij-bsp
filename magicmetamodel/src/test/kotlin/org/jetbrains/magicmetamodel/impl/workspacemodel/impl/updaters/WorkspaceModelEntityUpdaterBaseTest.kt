package org.jetbrains.magicmetamodel.impl.workspacemodel.impl.updaters

import com.intellij.openapi.command.WriteCommandAction
import com.intellij.workspaceModel.storage.WorkspaceEntity
import com.intellij.workspaceModel.storage.WorkspaceEntityStorageBuilder
import com.intellij.workspaceModel.storage.bridgeEntities.ContentRootEntity
import com.intellij.workspaceModel.storage.bridgeEntities.ModuleEntity
import com.intellij.workspaceModel.storage.bridgeEntities.addModuleEntity
import org.jetbrains.magicmetamodel.impl.workspacemodel.WorkspaceModelBaseTest
import org.junit.jupiter.api.BeforeEach

internal abstract class WorkspaceModelEntityUpdaterBaseTest : WorkspaceModelBaseTest() {

  protected lateinit var workspaceModelDetails: WorkspaceModelDetails
  protected lateinit var parentModuleEntity: ModuleEntity

  private val parentModuleName = "test-module-root"
  private val parentModuleType = "JAVA_MODULE"

  @BeforeEach
  override fun beforeEach() {
    super.beforeEach()

    workspaceModelDetails = WorkspaceModelDetails(workspaceModel, virtualFileUrlManager, projectConfigSource)
    addParentModuleEntity()
  }

  private fun addParentModuleEntity() {
    WriteCommandAction.runWriteCommandAction(project) {
      workspaceModel.updateProjectModel {
        parentModuleEntity = addParentModuleEntity(it)
      }
    }
  }

  private fun addParentModuleEntity(builder: WorkspaceEntityStorageBuilder): ModuleEntity =
    builder.addModuleEntity(
      name = parentModuleName,
      dependencies = emptyList(),
      source = projectConfigSource,
      type = parentModuleType
    )

  protected fun <E : WorkspaceEntity> workspaceModelLoadedEntries(entityClass: Class<E>): List<E> =
    workspaceModel.entityStorage.current.entities(entityClass).toList()
}
package org.jetbrains.magicmetamodel.impl.workspacemodel.impl.updaters

import com.intellij.workspaceModel.storage.bridgeEntities.ModuleDependencyItem
import com.intellij.workspaceModel.storage.bridgeEntities.ModuleEntity

internal data class JavaModule(
  val module: Module,
  val baseDirContentRoot: ContentRoot,
  val sourceRoots: List<JavaSourceRoot>,
  val resourceRoots: List<JavaResourceRoot>,
  val libraries: List<Library>,
) : WorkspaceModelEntity()

internal class JavaModuleWithSourcesUpdater(
  private val workspaceModelDetails: WorkspaceModelDetails,
) : WorkspaceModelEntityWithoutParentModuleUpdater<JavaModule, ModuleEntity> {

  override fun addEntity(entityToAdd: JavaModule): ModuleEntity {
    val moduleEntityUpdater = ModuleEntityUpdater(workspaceModelDetails, defaultDependencies)
    val moduleEntity = moduleEntityUpdater.addEntity(entityToAdd.module)

    val libraryEntityUpdater = LibraryEntityUpdater(workspaceModelDetails)
    libraryEntityUpdater.addEntries(entityToAdd.libraries, moduleEntity)

    val javaSourceEntityUpdater = JavaSourceEntityUpdater(workspaceModelDetails)
    javaSourceEntityUpdater.addEntries(entityToAdd.sourceRoots, moduleEntity)

    val javaResourceEntityUpdater = JavaResourceEntityUpdater(workspaceModelDetails)
    javaResourceEntityUpdater.addEntries(entityToAdd.resourceRoots, moduleEntity)

    return moduleEntity
  }

  private companion object {
    val defaultDependencies = listOf(
      ModuleDependencyItem.SdkDependency("11", "JavaSDK"),
      ModuleDependencyItem.ModuleSourceDependency,
    )
  }
}

internal class JavaModuleWithoutSourcesUpdater(
  private val workspaceModelDetails: WorkspaceModelDetails,
) : WorkspaceModelEntityWithoutParentModuleUpdater<JavaModule, ModuleEntity> {

  override fun addEntity(entityToAdd: JavaModule): ModuleEntity {
    val moduleEntityUpdater = ModuleEntityUpdater(workspaceModelDetails)
    val moduleEntity = moduleEntityUpdater.addEntity(entityToAdd.module)

    val contentRootEntityUpdater = ContentRootEntityUpdater(workspaceModelDetails)
    contentRootEntityUpdater.addEntity(entityToAdd.baseDirContentRoot, moduleEntity)

    return moduleEntity
  }
}

internal class JavaModuleUpdater(
  workspaceModelDetails: WorkspaceModelDetails,
) : WorkspaceModelEntityWithoutParentModuleUpdater<JavaModule, ModuleEntity> {

  private val javaModuleWithSourcesUpdater = JavaModuleWithSourcesUpdater(workspaceModelDetails)
  private val javaModuleWithoutSourcesUpdater = JavaModuleWithoutSourcesUpdater(workspaceModelDetails)

  override fun addEntity(entityToAdd: JavaModule): ModuleEntity =
    when (Pair(entityToAdd.sourceRoots.size, entityToAdd.resourceRoots.size)) {
      Pair(0, 0) -> javaModuleWithoutSourcesUpdater.addEntity(entityToAdd)
      else -> javaModuleWithSourcesUpdater.addEntity(entityToAdd)
    }
}

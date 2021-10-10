package org.jetbrains.magicmetamodel.impl.workspacemodel.impl.updaters

import com.intellij.workspaceModel.storage.WorkspaceEntityStorageBuilder
import com.intellij.workspaceModel.storage.bridgeEntities.*

internal data class ModuleDependency(
  val moduleName: String,
) : WorkspaceModelEntity()

internal data class LibraryDependency(
  val libraryName: String,
) : WorkspaceModelEntity()

internal data class Module(
  val name: String,
  val type: String,
  val modulesDependencies: List<ModuleDependency>,
  val librariesDependencies: List<LibraryDependency>,
) : WorkspaceModelEntity()

internal class ModuleEntityUpdater(
  private val workspaceModelDetails: WorkspaceModelDetails,
  private val defaultDependencies: List<ModuleDependencyItem> = emptyList(),
) : WorkspaceModelEntityWithoutParentModuleUpdater<Module, ModuleEntity> {

  override fun addEntity(entityToAdd: Module): ModuleEntity {
    return workspaceModelDetails.workspaceModel.updateProjectModel {
      addModuleEntity(it, entityToAdd)
    }
  }

  private fun addModuleEntity(
    builder: WorkspaceEntityStorageBuilder,
    entityToAdd: Module,
  ): ModuleEntity {
    val modulesDependencies = entityToAdd.modulesDependencies.map(this::toModuleDependencyItemModuleDependency)
    val librariesDependencies = entityToAdd.librariesDependencies.map { toModuleDependencyItemLibraryDependency(it, entityToAdd.name) }

    return builder.addModuleEntity(
      name = entityToAdd.name,
      dependencies = modulesDependencies + librariesDependencies + defaultDependencies,
      source = workspaceModelDetails.projectConfigSource,
      type = entityToAdd.type
    )
  }

  private fun toModuleDependencyItemModuleDependency(moduleDependency: ModuleDependency): ModuleDependencyItem.Exportable.ModuleDependency =
    ModuleDependencyItem.Exportable.ModuleDependency(
      module = ModuleId(moduleDependency.moduleName),
      exported = true,
      scope = ModuleDependencyItem.DependencyScope.COMPILE,
      productionOnTest = true,
    )

  private fun toModuleDependencyItemLibraryDependency(libraryDependency: LibraryDependency, moduleName: String): ModuleDependencyItem.Exportable.LibraryDependency =
    ModuleDependencyItem.Exportable.LibraryDependency(
      library = LibraryId(
        name = libraryDependency.libraryName,
        tableId = LibraryTableId.ModuleLibraryTableId(ModuleId(moduleName)),
      ),
      exported = false,
      scope = ModuleDependencyItem.DependencyScope.COMPILE,
    )
}

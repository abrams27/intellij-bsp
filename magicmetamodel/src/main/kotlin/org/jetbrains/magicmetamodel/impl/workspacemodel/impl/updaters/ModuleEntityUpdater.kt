package org.jetbrains.magicmetamodel.impl.workspacemodel.impl.updaters

import com.intellij.workspaceModel.storage.bridgeEntities.ModuleDependencyItem
import com.intellij.workspaceModel.storage.bridgeEntities.ModuleEntity

internal class Module(
  val name: String,
  val type: String,
  val dependencies: List<ModuleDependencyItem>,
) : WorkspaceModelEntity()

internal class JavaModuleEntityUpdater(
  private val workspaceModelDetails: WorkspaceModelDetails,
) : WorkspaceModelEntityWithoutParentModuleUpdater<Module, ModuleEntity> {
  override fun addEntity(entityToAdd: Module): ModuleEntity {
    TODO("Not yet implemented")
  }
}


//val module = it.addModuleEntity(
//        details.name,
//        listOf(
//          ModuleDependencyItem.SdkDependency("11", "JavaSDK"),
//          ModuleDependencyItem.ModuleSourceDependency,
//        ) + details.dependencies
//          .map {
//            ModuleDependencyItem.Exportable.ModuleDependency(
////              ModuleId("bsp-sample.${it.uri.drop(2).replace("/", ".").dropLastWhile { it != ':' }.dropLast(1)}"),
//              ModuleId(it.uri),
//              true,
//              ModuleDependencyItem.DependencyScope.COMPILE,
//              false
//            )
//          } + details.libraries.map { dep ->
//          ModuleDependencyItem.Exportable.LibraryDependency(
//            LibraryId(
//              dep.jar,
//              LibraryTableId.ModuleLibraryTableId(ModuleId(details.name)),
//            ), false, ModuleDependencyItem.DependencyScope.COMPILE
//          )
//
//        },
//        projectConfigSource,
//        "JAVA_MODULE"
//      )

//      val module = it.addModuleEntity(
//        "bsp-sample",
//        listOf(
//          ModuleDependencyItem.SdkDependency("11", "JavaSDK"),
//          ModuleDependencyItem.ModuleSourceDependency,
//        ),
//        projectConfigSource,
//        "JAVA_MODULE"
//      )
//
//      val contentRoot = it.addContentRootEntity(
//        projectLocation.baseDirectoryUrl,
//        emptyList(),
//        emptyList(),
//        module
//      )
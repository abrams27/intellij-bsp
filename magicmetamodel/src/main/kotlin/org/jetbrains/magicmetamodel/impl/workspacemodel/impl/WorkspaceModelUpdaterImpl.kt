package org.jetbrains.magicmetamodel.impl.workspacemodel.impl

import ch.epfl.scala.bsp4j.BuildTarget
import ch.epfl.scala.bsp4j.SourcesItem
import com.intellij.workspaceModel.ide.JpsFileEntitySource
import com.intellij.workspaceModel.ide.WorkspaceModel
import com.intellij.workspaceModel.storage.bridgeEntities.addContentRootEntity
import com.intellij.workspaceModel.storage.bridgeEntities.addJavaSourceRootEntity
import com.intellij.workspaceModel.storage.bridgeEntities.addSourceRootEntity
import com.intellij.workspaceModel.storage.url.VirtualFileUrlManager
import org.jetbrains.magicmetamodel.impl.workspacemodel.ModuleDetails
import org.jetbrains.magicmetamodel.impl.workspacemodel.WorkspaceModelUpdater
import java.nio.file.Path

//internal data class LibraryDetails(val jar: String, val prettyName: String)
//
//internal data class DependencyDetails(val uri: String)
//
//internal data class ResourcesDetails(val uri: String)
//
//internal data class SourceDetails(val source: ModuleSource, val packagePrefix: String)
//
//internal data class ModuleDetails(
//  val name: String,
//  val baseDirectory: String,
//  val sources: List<SourceDetails>,
//  val dependencies: List<DependencyDetails>,
//  val libraries: List<LibraryDetails>,
//  val resources: List<ResourcesDetails>,
//)

internal class WorkspaceModelUpdaterImpl(
  private val workspaceModel: WorkspaceModel,
  private val virtualFileUrlManager: VirtualFileUrlManager,
  private val projectBaseDirPath: Path,
) : WorkspaceModelUpdater {

  override fun loadModule(moduleDetails: ModuleDetails) {

  }
//
//    internal fun addJavaSources(
//      targets: List<BuildTarget>,
//      sources: List<SourcesItem>,
//      resources: List<ResourcesItem>,
//      dependencies: List<DependencySourcesItem>,
//  ) {
//    workspaceModel.updateProjectModel {
//      val projectConfigSource =
//        JpsFileEntitySource.FileInDirectory(projectLocation.baseDirectoryUrl.append(".idea/modules"), projectLocation)
//
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
//    }
//
//    targets.map { target ->
//      ModuleDetails(
//        name = target.displayName,
//        baseDirectory = target.baseDirectory,
//        sources = sources
//          .filter { it.target == target.id }
//          .flatMap {
//            it.sources.map {
//              SourceDetails(
//                ModuleSource(URI.create(it.uri).toPath().parent, it.generated),
//                it.uri.takeLastWhile { it != '/' })
//            }
//          }
//          .distinct(),
//        dependencies = target.dependencies
//          .filter { targets.map { it.id }.contains(it) }
//          .map { DependencyDetails(it.uri) },
//        libraries = dependencies
//          .filter { it.target == target.id }
//          .flatMap { it.sources.map { LibraryDetails(it, it) } },
//        resources = resources
//          .filter { it.target == target.id }
//          .flatMap { it.resources.map { ResourcesDetails(it) } }
//      )
//    }.forEach(this::addJavaSource)
//  }
//
//  private fun addJavaSource(
//    details: ModuleDetails,
//  ) {
//    workspaceModel.updateProjectModel {
//      val projectConfigSource =
//        JpsFileEntitySource.FileInDirectory(projectLocation.baseDirectoryUrl.append(".idea/modules"), projectLocation)
//
//      val module = it.addModuleEntity(
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
//
    //      details.libraries.forEach { dep ->
    //        val root = LibraryRoot(
    //          virtualFileUrlManager.fromUrl("jar${dep.jar.drop(4)}!/"),
    //          LibraryRootTypeId.COMPILED
    //        )
    //
    //        it.addLibraryEntity(
    //          dep.prettyName,
    //          LibraryTableId.ModuleLibraryTableId(ModuleId(details.name)),
    //          listOf(root),
    //          emptyList(),
    //          projectConfigSource
    //        )
    //      }
//
    //      if (details.sources.isNotEmpty()) {
    //        details.sources.forEach { source ->
    //          val xd = virtualFileUrlManager.fromPath(source.source.sourceDir.toString())
    //
    //          val contentRoot = it.addContentRootEntity(
    //            xd,
    //            emptyList(),
    //            emptyList(),
    //            module
    //          )
    //
    //          val sourceRoot = it.addSourceRootEntity(
    //            contentRoot,
    //            xd,
    //            "java-source",
    //            projectConfigSource
    //          )
    //
    //          it.addJavaSourceRootEntity(sourceRoot, source.source.generated, "bsp.${source.packagePrefix}")
    //        }
    //      } else {
    //        it.addContentRootEntity(
    //          virtualFileUrlManager.fromUrl(details.baseDirectory),
    //          emptyList(),
    //          emptyList(),
    //          module
    //        )
    //      }
    //      details.resources.forEach { resource ->
    //        val xd = virtualFileUrlManager.fromUrl(resource.uri)
    //
    //        val contentRoot = it.addContentRootEntity(
    //          xd,
    //          emptyList(),
    //          emptyList(),
    //          module
    //        )
    //
    //        val sourceRoot = it.addSourceRootEntity(
    //          contentRoot,
    //          xd,
    //          "java-resource",
    //          projectConfigSource
    //        )
    //
    //        it.addJavaResourceRootEntity(sourceRoot, false, "")
//      }
//      it.addJavaModuleSettingsEntity(
//        true,
//        true,
//        null,
//        null,
//        null,
//        module,
//        projectConfigSource
//      )
//    }
//  }
}

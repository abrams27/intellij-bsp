package org.jetbrains.magicmetamodel.impl.workspacemodel.impl

import com.intellij.openapi.command.WriteCommandAction
import com.intellij.project.stateStore
import com.intellij.workspaceModel.ide.JpsFileEntitySource
import com.intellij.workspaceModel.ide.JpsProjectConfigLocation
import com.intellij.workspaceModel.ide.WorkspaceModel
import com.intellij.workspaceModel.ide.getInstance
import com.intellij.workspaceModel.storage.bridgeEntities.*
import com.intellij.workspaceModel.storage.impl.url.toVirtualFileUrl
import com.intellij.workspaceModel.storage.url.VirtualFileUrlManager
import org.jetbrains.magicmetamodel.impl.workspacemodel.WorkspaceModelBaseTest
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test

@DisplayName("workspaceModelUpdaterImpl.loadRootModule() tests")
class WorkspaceModelUpdaterImplLoadRootModuleTest : WorkspaceModelBaseTest() {

//  @Test
//  fun `should
//  @Test
//  fun `should `() {
//
//
//    println(workspaceModel.entityStorage.current.entities(ModifiableModuleEntity::class.java).toList())
//    println(project.stateStore.projectBasePath)
//
//    WriteCommandAction.runWriteCommandAction(project) {
//      workspaceModel.updateProjectModel {
//        val loc = JpsProjectConfigLocation.DirectoryBased(
//          virtualFileUrlManager.fromPath(project.stateStore.projectBasePath.toString()),
//          project.stateStore.projectBasePath.toVirtualFileUrl(virtualFileUrlManager)
//        )
//
//        val projectConfigSource =
//          JpsFileEntitySource.FileInDirectory(
//            project.stateStore.projectBasePath.toVirtualFileUrl(virtualFileUrlManager),
//            loc
//          )
//
//        println(projectConfigSource.toString())
//        val module = it.addModuleEntity(
//          "bsp-sample",
//          listOf(
//            ModuleDependencyItem.SdkDependency("11", "JavaSDK"),
//            ModuleDependencyItem.ModuleSourceDependency,
//          ),
//          projectConfigSource,
//          "JAVA_MODULE"
//        )
//
//        val contentRoot = it.addContentRootEntity(
//          project.stateStore.projectBasePath.toVirtualFileUrl(virtualFileUrlManager),
//          emptyList(),
//          emptyList(),
//          module
//        )
//      }
//    }
//
//    println(
//      workspaceModel.entityStorage.current.entities(ModuleEntity::class.java).toList().first().contentRoots.toList()
//        .first().url.url
//    )
//    ModuleEntity()
//  }
}
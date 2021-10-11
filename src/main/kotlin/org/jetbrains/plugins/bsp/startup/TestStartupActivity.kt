package org.jetbrains.plugins.bsp.startup

import ch.epfl.scala.bsp4j.DependencySourcesItem
import ch.epfl.scala.bsp4j.ResourcesItem
import com.intellij.ide.impl.OpenProjectTask
import com.intellij.openapi.application.runWriteAction
import com.intellij.openapi.project.Project
import com.intellij.openapi.project.ex.ProjectManagerEx
import com.intellij.openapi.vfs.VirtualFile
import com.intellij.project.stateStore
import com.intellij.projectImport.ProjectOpenProcessor
import com.intellij.testFramework.UsefulTestCase
import com.intellij.workspaceModel.ide.WorkspaceModel
import com.intellij.workspaceModel.ide.getInstance
import com.intellij.workspaceModel.ide.getJpsProjectConfigLocation
import com.intellij.workspaceModel.storage.WorkspaceEntityStorageBuilder
import com.intellij.workspaceModel.storage.url.VirtualFileUrlManager
import org.jetbrains.magicmetamodel.MagicMetaModel
import org.jetbrains.magicmetamodel.impl.workspacemodel.ModuleDetails
import org.jetbrains.magicmetamodel.impl.workspacemodel.impl.WorkspaceModelUpdaterImpl
import java.nio.file.Paths

class TestStartupActivity : ProjectOpenProcessor() {

  override fun getName(): String {
    TODO("Not yet implemented")
  }

  override fun canOpenProject(file: VirtualFile): Boolean {
    return true
  }

  override fun doOpenProject(
    virtualFile: VirtualFile,
    projectToClose: Project?,
    forceOpenInNewFrame: Boolean
  ): Project? {

    val options = OpenProjectTask(isNewProject = true)
    val project = ProjectManagerEx.getInstanceEx().openProject(Paths.get(virtualFile.path), options)!!

    val workspaceModel = WorkspaceModel.getInstance(project)

    val virtualFileUrlManager = VirtualFileUrlManager.getInstance(project)
    val projectBaseDir = project.stateStore.projectBasePath

    val workspaceModelUpdaterImpl = WorkspaceModelUpdaterImpl(workspaceModel, virtualFileUrlManager, projectBaseDir)

    val details = listOf(
      ModuleDetails(
        target = SampleBSPProjectToImport.libATarget,
        allTargetsIds = SampleBSPProjectToImport.allTargetsIds,
        sources = listOf(SampleBSPProjectToImport.libASources),
        resources = listOf(),
        dependenciesSources = listOf(SampleBSPProjectToImport.libADependenciesSources),
      ),
      ModuleDetails(
        target = SampleBSPProjectToImport.libBBTarget,
        allTargetsIds = SampleBSPProjectToImport.allTargetsIds,
        sources = listOf(SampleBSPProjectToImport.libBBSources),
        resources = listOf(SampleBSPProjectToImport.libBBResources),
        dependenciesSources = listOf(SampleBSPProjectToImport.libBBDependenciesSources)
      ),
      ModuleDetails(
        target = SampleBSPProjectToImport.libBTarget,
        allTargetsIds = SampleBSPProjectToImport.allTargetsIds,
        sources = listOf(SampleBSPProjectToImport.libBSources),
        resources = listOf(),
        dependenciesSources = listOf(SampleBSPProjectToImport.libBDependenciesSources)
      ),
      ModuleDetails(
        target = SampleBSPProjectToImport.appTarget,
        allTargetsIds = SampleBSPProjectToImport.allTargetsIds,
        sources = listOf(SampleBSPProjectToImport.appSources),
        resources = listOf(SampleBSPProjectToImport.appResources),
        dependenciesSources = listOf(SampleBSPProjectToImport.appDependenciesSources)
      ),
    )

    runWriteAction {
      workspaceModelUpdaterImpl.loadModules(details)
    }

    return project
  }
}
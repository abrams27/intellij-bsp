package org.jetbrains.plugins.bsp.startup

import com.intellij.ide.impl.OpenProjectTask
import com.intellij.openapi.application.runWriteAction
import com.intellij.openapi.project.Project
import com.intellij.openapi.project.ex.ProjectManagerEx
import com.intellij.openapi.vfs.VirtualFile
import com.intellij.projectImport.ProjectOpenProcessor
import com.intellij.testFramework.UsefulTestCase
import com.intellij.workspaceModel.ide.WorkspaceModel
import com.intellij.workspaceModel.ide.getInstance
import com.intellij.workspaceModel.ide.getJpsProjectConfigLocation
import com.intellij.workspaceModel.storage.WorkspaceEntityStorageBuilder
import com.intellij.workspaceModel.storage.url.VirtualFileUrlManager
import org.jetbrains.magicmetamodel.MagicMetaModel
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
    val projectLocation = getJpsProjectConfigLocation(project)!!
    val virtualFileUrlManager = VirtualFileUrlManager.getInstance(project)

    val magicMetaModel = MagicMetaModel.create(
      workspaceModel,
      projectLocation,
      virtualFileUrlManager,
      SampleBSPProjectToImport.targets,
      SampleBSPProjectToImport.sources,
      SampleBSPProjectToImport.resources,
      SampleBSPProjectToImport.dependencies
    )
    runWriteAction {
      magicMetaModel.loadDefaultTargets()
    }

    return project
  }
}
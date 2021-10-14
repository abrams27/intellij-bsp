package org.jetbrains.plugins.bsp.services

import com.intellij.openapi.project.Project
import com.intellij.project.stateStore
import com.intellij.workspaceModel.ide.WorkspaceModel
import com.intellij.workspaceModel.ide.getInstance
import com.intellij.workspaceModel.storage.url.VirtualFileUrlManager
import org.jetbrains.magicmetamodel.MagicMetaModel
import org.jetbrains.magicmetamodel.MagicMetaModelProjectConfig
import org.jetbrains.magicmetamodel.ProjectDetails
import org.jetbrains.plugins.bsp.startup.SampleBSPProjectToImport

class MagicMetaModelService(project: Project) {

  val magicMetaModel: MagicMetaModel

  // TODO extract
  init {
    val workspaceModel = WorkspaceModel.getInstance(project)
    val virtualFileUrlManager = VirtualFileUrlManager.getInstance(project)
    val projectBaseDir = project.stateStore.projectBasePath

    val magicMetaModelProjectConfig = MagicMetaModelProjectConfig(workspaceModel, virtualFileUrlManager, projectBaseDir)

    val projectDetails = ProjectDetails(
      targetsId = SampleBSPProjectToImport.allTargetsIds,
      targets = listOf(
        SampleBSPProjectToImport.libATarget,
        SampleBSPProjectToImport.appTarget,
        SampleBSPProjectToImport.libBB2Target,
        SampleBSPProjectToImport.libBBTarget,
        SampleBSPProjectToImport.anotherAppTarget,
        SampleBSPProjectToImport.libBTarget,
      ),
      sources = listOf(
        SampleBSPProjectToImport.libBB2Sources,
        SampleBSPProjectToImport.appSources,
        SampleBSPProjectToImport.libASources,
        SampleBSPProjectToImport.libBBSources,
        SampleBSPProjectToImport.libBSources,
        SampleBSPProjectToImport.anotherAppSources,
      ),
      resources = listOf(
        SampleBSPProjectToImport.appResources,
        SampleBSPProjectToImport.libBBResources,
        SampleBSPProjectToImport.anotherAppResources,
      ),
      dependenciesSources = listOf(
        SampleBSPProjectToImport.appDependenciesSources,
        SampleBSPProjectToImport.libBBDependenciesSources,
        SampleBSPProjectToImport.libBB2DependenciesSources,
        SampleBSPProjectToImport.anotherAppDependenciesSources,
        SampleBSPProjectToImport.libADependenciesSources,
        SampleBSPProjectToImport.libBDependenciesSources,
      ),
    )

    magicMetaModel = MagicMetaModel.create(magicMetaModelProjectConfig, projectDetails)
  }

  companion object {
    fun getInstance(project: Project): MagicMetaModelService =
      project.getService(MagicMetaModelService::class.java)
  }
}

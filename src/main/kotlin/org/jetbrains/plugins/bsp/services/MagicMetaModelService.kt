package org.jetbrains.plugins.bsp.services

import com.intellij.openapi.project.Project
import com.intellij.project.stateStore
import com.intellij.workspaceModel.ide.WorkspaceModel
import com.intellij.workspaceModel.ide.getInstance
import com.intellij.workspaceModel.storage.url.VirtualFileUrlManager
import org.jetbrains.magicmetamodel.MagicMetaModel
import org.jetbrains.magicmetamodel.MagicMetaModelProjectConfig
import org.jetbrains.plugins.bsp.protocol.VeryTemporaryBspResolver

public class MagicMetaModelService(private val project: Project) {
  public val magicMetaModel: MagicMetaModel by lazy {
    initializeMagicModel()
  }

  private fun initializeMagicModel(): MagicMetaModel {
    val magicMetaModelProjectConfig = calculateProjectConfig()
    val bspResolver = VeryTemporaryBspResolver(magicMetaModelProjectConfig.projectBaseDir)
    val projectDetails = bspResolver.collectModel()

    return MagicMetaModel.create(magicMetaModelProjectConfig, projectDetails)
  }

  private fun calculateProjectConfig(): MagicMetaModelProjectConfig {
    val workspaceModel = WorkspaceModel.getInstance(project)
    val virtualFileUrlManager = VirtualFileUrlManager.getInstance(project)
    val projectBaseDir = project.stateStore.projectBasePath

    return MagicMetaModelProjectConfig(workspaceModel, virtualFileUrlManager, projectBaseDir)
  }

  public companion object {
    public fun getInstance(project: Project): MagicMetaModelService =
      project.getService(MagicMetaModelService::class.java)
  }
}

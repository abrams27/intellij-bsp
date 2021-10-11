package org.jetbrains.magicmetamodel.impl.workspacemodel.impl

import org.jetbrains.magicmetamodel.impl.workspacemodel.ModuleDetails
import org.jetbrains.magicmetamodel.impl.workspacemodel.WorkspaceModelUpdater
import org.jetbrains.magicmetamodel.impl.workspacemodel.impl.updaters.JavaModuleWithSourcesUpdater
import org.jetbrains.magicmetamodel.impl.workspacemodel.impl.updaters.WorkspaceModelDetails
import org.jetbrains.magicmetamodel.impl.workspacemodel.impl.updaters.transformers.ModuleDetailsToJavaModuleTransformer

internal class WorkspaceModelUpdaterImpl(
  private val workspaceModelDetails: WorkspaceModelDetails,
) : WorkspaceModelUpdater {

  override fun loadModule(moduleDetails: ModuleDetails) {
    val javaModule = ModuleDetailsToJavaModuleTransformer.transform(moduleDetails)

    val javaModuleWithSourcesUpdater = JavaModuleWithSourcesUpdater(workspaceModelDetails)
    javaModuleWithSourcesUpdater.addEntity(javaModule)
  }
}

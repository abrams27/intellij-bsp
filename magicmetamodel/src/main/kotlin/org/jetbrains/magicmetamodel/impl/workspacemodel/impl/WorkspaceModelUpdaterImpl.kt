package org.jetbrains.magicmetamodel.impl.workspacemodel.impl

import ch.epfl.scala.bsp4j.*
import org.jetbrains.magicmetamodel.impl.workspacemodel.ModuleDetails
import org.jetbrains.magicmetamodel.impl.workspacemodel.WorkspaceModelUpdater
import org.jetbrains.magicmetamodel.impl.workspacemodel.impl.updaters.ContentRoot
import org.jetbrains.magicmetamodel.impl.workspacemodel.impl.updaters.JavaModule
import org.jetbrains.magicmetamodel.impl.workspacemodel.impl.updaters.JavaModuleWithSourcesUpdater
import org.jetbrains.magicmetamodel.impl.workspacemodel.impl.updaters.WorkspaceModelDetails
import org.jetbrains.magicmetamodel.impl.workspacemodel.impl.updaters.transformers.*
import org.jetbrains.magicmetamodel.impl.workspacemodel.impl.updaters.transformers.BspModuleDetailsToModuleTransformer
import org.jetbrains.magicmetamodel.impl.workspacemodel.impl.updaters.transformers.DependencySourcesItemToLibraryTransformer
import org.jetbrains.magicmetamodel.impl.workspacemodel.impl.updaters.transformers.ResourcesItemToJavaResourceRootTransformer
import org.jetbrains.magicmetamodel.impl.workspacemodel.impl.updaters.transformers.SourcesItemToJavaSourceRootTransformer
import java.net.URI
import kotlin.io.path.toPath

internal class WorkspaceModelUpdaterImpl(
  private val workspaceModelDetails: WorkspaceModelDetails,
  private val allTargetsIds: List<BuildTargetIdentifier>,
) : WorkspaceModelUpdater {

  override fun loadModule(moduleDetails: ModuleDetails) {
    val sourceRoots = SourcesItemToJavaSourceRootTransformer.transform(moduleDetails.sources)
    val resourceRoots = ResourcesItemToJavaResourceRootTransformer.transform(moduleDetails.resources)
    val libraries = DependencySourcesItemToLibraryTransformer.transform(moduleDetails.dependenciesSources)

    val bspModuleDetails = BspModuleDetails(
      target = moduleDetails.target,
      allTargetsIds = allTargetsIds,
      dependencySources = moduleDetails.dependenciesSources,
      type = "JAVA_MODULE",
    )
    val module = BspModuleDetailsToModuleTransformer.transform(bspModuleDetails)

    val javaModule = JavaModule(
      module = module,
      baseDirContentRoot = ContentRoot(URI.create(moduleDetails.target.baseDirectory).toPath()),
      sourceRoots = sourceRoots,
      resourceRoots = resourceRoots,
      libraries = libraries,
    )
    val javaModuleWithSourcesUpdater = JavaModuleWithSourcesUpdater(workspaceModelDetails)
    javaModuleWithSourcesUpdater.addEntity(javaModule)
  }
}

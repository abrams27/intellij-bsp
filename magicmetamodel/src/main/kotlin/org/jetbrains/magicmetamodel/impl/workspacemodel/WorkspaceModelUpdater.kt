package org.jetbrains.magicmetamodel.impl.workspacemodel

import ch.epfl.scala.bsp4j.BuildTarget
import ch.epfl.scala.bsp4j.BuildTargetIdentifier
import ch.epfl.scala.bsp4j.DependencySourcesItem
import ch.epfl.scala.bsp4j.ResourcesItem
import ch.epfl.scala.bsp4j.SourcesItem

public data class ModuleDetails(
  val target: BuildTarget,
  val allTargetsIds: List<BuildTargetIdentifier>,
  val sources: List<SourcesItem>,
  val resources: List<ResourcesItem>,
  val dependenciesSources: List<DependencySourcesItem>,
)

internal interface WorkspaceModelUpdater {

//  fun loadRootModule()

  fun loadModules(modulesDetails: List<ModuleDetails>) =
    modulesDetails.forEach(this::loadModule)

  fun loadModule(moduleDetails: ModuleDetails)

//  fun removeModule(module: Any)
//
//  fun clear()
}
package org.jetbrains.magicmetamodel.impl.workspacemodel

import ch.epfl.scala.bsp4j.BuildTarget
import ch.epfl.scala.bsp4j.DependencySourcesItem
import ch.epfl.scala.bsp4j.ResourcesItem
import ch.epfl.scala.bsp4j.SourcesItem

internal data class ModuleDetails(
  val name: String,
  val target: BuildTarget,
  val sources: List<SourcesItem>,
  val resources: List<ResourcesItem>,
  val dependenciesSources: List<DependencySourcesItem>,
)

internal interface WorkspaceModelUpdater {

//  fun loadRootModule()

  fun loadModule(moduleDetails: ModuleDetails)

//  fun removeModule(module: Any)
//
//  fun clear()
}
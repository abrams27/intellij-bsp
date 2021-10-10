package org.jetbrains.magicmetamodel.impl.workspacemodel.impl.updaters.transformers

import ch.epfl.scala.bsp4j.DependencySourcesItem
import org.jetbrains.magicmetamodel.impl.workspacemodel.impl.updaters.Library

internal object DependencySourcesItemToLibraryTransformer :
  WorkspaceModelEntityPartitionTransformer<DependencySourcesItem, Library> {

  override fun transform(inputEntity: DependencySourcesItem): List<Library> {
    return inputEntity.sources.map(this::toLibrary)
  }

  private fun toLibrary(dependencyUri: String): Library =
    Library(
      // TODO: name and version should be extracted, maybe BSP prefix as well?
      displayName = dependencyUri,
      jar = "jar:${dependencyUri}!/"
    )
}

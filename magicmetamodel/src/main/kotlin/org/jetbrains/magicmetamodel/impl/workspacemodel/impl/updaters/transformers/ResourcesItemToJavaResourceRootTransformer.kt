package org.jetbrains.magicmetamodel.impl.workspacemodel.impl.updaters.transformers;

import ch.epfl.scala.bsp4j.ResourcesItem
import org.jetbrains.magicmetamodel.impl.workspacemodel.impl.updaters.JavaResourceRoot
import java.net.URI
import kotlin.io.path.toPath

internal object ResourcesItemToJavaResourceRootTransformer :
  WorkspaceModelEntityPartitionTransformer<ResourcesItem, JavaResourceRoot> {

  override fun transform(inputEntity: ResourcesItem): List<JavaResourceRoot> {
    return inputEntity.resources.map(this::toJavaResourceRoot)
  }

  private fun toJavaResourceRoot(resourcePath: String) =
    JavaResourceRoot(
      resourcePath = URI.create(resourcePath).toPath()
    )
}

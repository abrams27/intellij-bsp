package org.jetbrains.magicmetamodel.impl.workspacemodel.impl.updaters.transformers

import ch.epfl.scala.bsp4j.SourcesItem
import org.jetbrains.magicmetamodel.impl.workspacemodel.impl.updaters.WorkspaceModelEntity
import java.net.URI
import java.nio.file.Path

internal data class JavaSourceRoot(
  val sourceDir: Path,
  val generated: Boolean,
  val packagePrefix: String,
) : WorkspaceModelEntity()

internal object SourcesItemToJavaSourceRootTransformer :
  WorkspaceModelEntityPartitionTransformer<SourcesItem, JavaSourceRoot> {

  override fun transform(inputEntity: SourcesItem): Collection<JavaSourceRoot> {
    val sourceRoots = getSourceRootsAsURIs(inputEntity)

    return SourceItemToSourceRootTransformer
      .transform(inputEntity.sources)
      .map { calculateJavaSourceRoot(it, sourceRoots) }
  }

  private fun getSourceRootsAsURIs(sourcesItem: SourcesItem): List<URI> =
    sourcesItem.roots.map(URI::create)

  private fun calculateJavaSourceRoot(sourceRoot: SourceRoot, sourceRoots: List<URI>): JavaSourceRoot {
    val packagePrefix = calculatePackagePrefix(sourceRoot, sourceRoots)

    return JavaSourceRoot(
      sourceDir = sourceRoot.sourceDir,
      generated = sourceRoot.generated,
      packagePrefix = packagePrefix.packagePrefix
    )
  }

  private fun calculatePackagePrefix(sourceRoot: SourceRoot, sourceRoots: List<URI>): JavaSourceRootPackagePrefix {
    val packageDetails = JavaSourcePackageDetails(
      sourceDir = sourceRoot.sourceDir.toUri(),
      sourceRoots = sourceRoots
    )

    return JavaSourcePackageDetailsToJavaSourceRootPackagePrefixTransformer.transform(packageDetails)
  }
}

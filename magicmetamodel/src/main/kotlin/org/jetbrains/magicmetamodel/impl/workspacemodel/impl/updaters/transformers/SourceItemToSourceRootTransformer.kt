package org.jetbrains.magicmetamodel.impl.workspacemodel.impl.updaters.transformers

import ch.epfl.scala.bsp4j.SourceItem
import ch.epfl.scala.bsp4j.SourceItemKind
import org.jetbrains.magicmetamodel.impl.workspacemodel.impl.updaters.WorkspaceModelEntity
import java.net.URI
import java.nio.file.Path
import kotlin.io.path.toPath

internal data class SourceRoot(
  val sourceDir: Path,
  val generated: Boolean = false
) : WorkspaceModelEntity()

// TODO better generated handling & merge
internal object SourceItemToSourceRootTransformer :
  WorkspaceModelEntityTransformer<SourceItem, SourceRoot> {

  // TODO any good way of preventing this warning?
  override fun transform(inputEntity: SourceItem): SourceRoot = when (inputEntity.kind) {
    SourceItemKind.FILE -> mapSourceItemFileToModuleSource(inputEntity)
    SourceItemKind.DIRECTORY -> mapSourceItemDirToModuleSource(inputEntity)
  }

  private fun mapSourceItemFileToModuleSource(sourceItem: SourceItem): SourceRoot {
    val sourceDir = mapFileUriToDirectory(sourceItem.uri)

    return SourceRoot(sourceDir, sourceItem.generated)
  }

  private fun mapSourceItemDirToModuleSource(sourceItem: SourceItem): SourceRoot {
    val sourceDir = URI.create(sourceItem.uri).toPath()

    return SourceRoot(sourceDir, sourceItem.generated)
  }

  private fun mapFileUriToDirectory(fileRawUri: String): Path =
    URI.create(fileRawUri).toPath().parent
}

package org.jetbrains.magicmetamodel.impl.workspacemodel

import ch.epfl.scala.bsp4j.SourceItem
import ch.epfl.scala.bsp4j.SourceItemKind
import java.net.URI
import java.nio.file.Path
import kotlin.io.path.toPath

// TODO move it!
internal data class ModuleSource(val sourceDir: Path, val generated: Boolean = false)

// TODO better naming!
// TODO better generated handling & merge
internal object SourceDirs {

  internal fun calculate(sources: List<SourceItem>): List<ModuleSource> =
    sources.map(this::mapSourceItemToModuleSource).distinct()

  // TODO good way of preventing this warning?
  private fun mapSourceItemToModuleSource(sourceItem: SourceItem): ModuleSource = when(sourceItem.kind) {
    SourceItemKind.FILE -> mapSourceItemFileToModuleSource(sourceItem)
    SourceItemKind.DIRECTORY -> mapSourceItemDirToModuleSource(sourceItem)
  }

  private fun mapSourceItemFileToModuleSource(sourceItem: SourceItem): ModuleSource {
    val sourceDir = mapFileUriToDirectory(sourceItem.uri)

    return ModuleSource(sourceDir, sourceItem.generated)
  }

  private fun mapSourceItemDirToModuleSource(sourceItem: SourceItem): ModuleSource {
    val sourceDir = URI.create(sourceItem.uri).toPath()

    return ModuleSource(sourceDir, sourceItem.generated)
  }

  private fun mapFileUriToDirectory(fileRawUri: String): Path =
    URI.create(fileRawUri).toPath().parent
}
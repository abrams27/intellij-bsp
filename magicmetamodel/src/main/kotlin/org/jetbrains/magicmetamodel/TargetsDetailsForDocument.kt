package org.jetbrains.magicmetamodel

import ch.epfl.scala.bsp4j.BuildTargetIdentifier
import ch.epfl.scala.bsp4j.SourceItem
import ch.epfl.scala.bsp4j.SourcesItem
import ch.epfl.scala.bsp4j.TextDocumentIdentifier
import org.jetbrains.magicmetamodel.extensions.toAbsolutePath
import java.net.URI
import java.nio.file.Path
import kotlin.reflect.KProperty


internal class TargetsDetailsForDocumentProvider constructor(sources: List<SourcesItem>) {

  private val documentIdToTargetsIdsMap by DocumentIdToTargetsIdsMapDelegate(sources)

  internal fun getTargetsDetailsForDocument(documentId: TextDocumentIdentifier): List<BuildTargetIdentifier> =
    generateAllDocumentSubdirectoriesIncludingDocument(documentId)
      .flatMap { documentIdToTargetsIdsMap[it] ?: emptyList() }
      .toList()

  private fun generateAllDocumentSubdirectoriesIncludingDocument(documentId: TextDocumentIdentifier): Sequence<Path> {
    val documentAbsolutePath = mapDocumentIdToAbsolutePath(documentId)

    return generateSequence(documentAbsolutePath) { it.parent }
  }

  private fun mapDocumentIdToAbsolutePath(documentId: TextDocumentIdentifier): Path =
    URI.create(documentId.uri).toAbsolutePath()
}

private class DocumentIdToTargetsIdsMapDelegate(private val sources: List<SourcesItem>) {

  operator fun getValue(
    thisRef: Any?,
    property: KProperty<*>
  ): Map<Path, List<BuildTargetIdentifier>> =
    sources
      .flatMap(this::mapSourcesItemToPairsOfDocumentIdAndTargetId)
      .groupBy({ it.first }, { it.second })

  private fun mapSourcesItemToPairsOfDocumentIdAndTargetId(
    sourceItem: SourcesItem
  ): List<Pair<Path, BuildTargetIdentifier>> =
    sourceItem.sources
      .map(this::mapSourceItemToPath)
      .map { Pair(it, sourceItem.target) }

  private fun mapSourceItemToPath(sourceItem: SourceItem): Path =
    URI.create(sourceItem.uri).toAbsolutePath()
}

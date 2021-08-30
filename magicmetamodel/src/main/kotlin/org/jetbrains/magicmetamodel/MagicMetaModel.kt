package org.jetbrains.magicmetamodel

import ch.epfl.scala.bsp4j.BuildTargetIdentifier
import ch.epfl.scala.bsp4j.SourceItem
import ch.epfl.scala.bsp4j.SourcesItem
import ch.epfl.scala.bsp4j.TextDocumentIdentifier
import java.net.URI
import java.nio.file.Path
import kotlin.io.path.toPath
import kotlin.reflect.KProperty

public class MagicMetaModel public constructor(sources: List<SourcesItem>) {

  private val documentIdToTargetsIdsMap by DocumentIdToTargetsIdsMapDelegate(sources)

  public fun getTargetsForDocument(documentId: TextDocumentIdentifier): List<BuildTargetIdentifier> =
    generateAllDocumentSubdirectories(documentId)
      .flatMap { println(it); documentIdToTargetsIdsMap[it] ?: emptyList() }
      .toList()

  private fun generateAllDocumentSubdirectories(documentId: TextDocumentIdentifier): Sequence<Path> {
    val documentAbsolutePath = mapDocumentIdToAbsolutePath(documentId)

    return generateSequence(documentAbsolutePath) { it.parent }
  }

  private fun mapDocumentIdToAbsolutePath(documentId: TextDocumentIdentifier): Path =
    URI.create(documentId.uri)
      .normalize()
      .toPath()
      .toAbsolutePath()
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
    URI.create(sourceItem.uri)
      .normalize()
      .toPath()
      .toAbsolutePath()
}
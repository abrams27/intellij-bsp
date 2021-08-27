package org.jetbrains.magicmodel

import ch.epfl.scala.bsp4j.BuildTargetIdentifier
import ch.epfl.scala.bsp4j.SourceItem
import ch.epfl.scala.bsp4j.SourcesItem
import ch.epfl.scala.bsp4j.TextDocumentIdentifier
import kotlin.reflect.KProperty

public class MagicModel {

  public fun getTargetsForDocument(documentId: TextDocumentIdentifier): List<BuildTargetIdentifier> {
    return emptyList()
  }
}

internal class DocumentIdToTargetsIdsMapDelegate(private val sources: List<SourcesItem>) {

  internal operator fun getValue(
    thisRef: Any?,
    property: KProperty<*>
  ): Map<TextDocumentIdentifier, List<BuildTargetIdentifier>> =
    sources
      .flatMap(this::mapSourcesItemToPairsOfDocumentIdAndTargetId)
      .groupBy({ it.first }, { it.second })

  private fun mapSourcesItemToPairsOfDocumentIdAndTargetId(
    sourceItem: SourcesItem
  ): List<Pair<TextDocumentIdentifier, BuildTargetIdentifier>> =
    sourceItem.sources
      .map(this::mapSourcesItemToTextDocumentIdentifier)
      .map { Pair(it, sourceItem.target) }

  private fun mapSourcesItemToTextDocumentIdentifier(sourceItem: SourceItem): TextDocumentIdentifier =
    TextDocumentIdentifier(sourceItem.uri)
}
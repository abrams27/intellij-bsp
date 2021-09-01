package org.jetbrains.magicmetamodel

import ch.epfl.scala.bsp4j.BuildTarget
import ch.epfl.scala.bsp4j.BuildTargetIdentifier
import ch.epfl.scala.bsp4j.SourcesItem
import ch.epfl.scala.bsp4j.TextDocumentIdentifier
import kotlin.reflect.KProperty

public data class DocumentTargetsDetails(
  public val activeTargetId: BuildTargetIdentifier?,
  public val inactiveTargetsIds: List<BuildTargetIdentifier>
)

public class MagicMetaModel public constructor(
  public val targets: List<BuildTarget>,
  sources: List<SourcesItem>
) {

  private val targetsDetailsForDocumentProvider = TargetsDetailsForDocumentProvider(sources)

  public fun getTargetsDetailsForDocument(documentId: TextDocumentIdentifier): DocumentTargetsDetails {
    val allTargetsIds = targetsDetailsForDocumentProvider.getTargetsDetailsForDocument(documentId)

    return DocumentTargetsDetails(
      activeTargetId = null,
      inactiveTargetsIds = allTargetsIds
    )
  }
}

internal class OverlappingTargetsGraphDelegate(targetsDetailsForDocumentProvider: TargetsDetailsForDocumentProvider) {

  internal operator fun getValue(
    thisRef: Any?,
    property: KProperty<*>
  ): Map<BuildTargetIdentifier, Set<BuildTargetIdentifier>> {
    return emptyMap()
  }
}

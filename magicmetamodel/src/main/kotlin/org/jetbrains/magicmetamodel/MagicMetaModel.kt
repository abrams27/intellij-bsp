package org.jetbrains.magicmetamodel

import ch.epfl.scala.bsp4j.*
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
  private val overlappingTargetsGraph by OverlappingTargetsGraphDelegate(targetsDetailsForDocumentProvider)

  public fun getTargetsDetailsForDocument(documentId: TextDocumentIdentifier): DocumentTargetsDetails {
    val allTargetsIds = targetsDetailsForDocumentProvider.getTargetsDetailsForDocument(documentId)

    return DocumentTargetsDetails(
      activeTargetId = null,
      inactiveTargetsIds = allTargetsIds
    )
  }
}

private class OverlappingTargetsGraphDelegate(targetsDetailsForDocumentProvider: TargetsDetailsForDocumentProvider) {

  operator fun getValue(
    thisRef: Any?,
    property: KProperty<*>
  ): Map<BuildTargetIdentifier, Set<BuildTargetIdentifier>> {
    return emptyMap()
  }
}

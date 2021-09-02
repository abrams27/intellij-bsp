package org.jetbrains.magicmetamodel

import ch.epfl.scala.bsp4j.BuildTarget
import ch.epfl.scala.bsp4j.BuildTargetIdentifier
import ch.epfl.scala.bsp4j.TextDocumentIdentifier

public data class DocumentTargetsDetails(
  public val activeTargetId: BuildTargetIdentifier?,
  public val inactiveTargetsIds: List<BuildTargetIdentifier>
)

public interface MagicMetaModel {

  public val targets: List<BuildTarget>

  public fun loadDefaultTargets()

  public fun loadTarget(targetId: BuildTargetIdentifier)

  public fun getTargetsDetailsForDocument(documentId: TextDocumentIdentifier): DocumentTargetsDetails
}

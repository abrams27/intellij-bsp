package org.jetbrains.magicmetamodel

import ch.epfl.scala.bsp4j.BuildTarget
import ch.epfl.scala.bsp4j.BuildTargetIdentifier
import ch.epfl.scala.bsp4j.TextDocumentIdentifier

public data class DocumentTargetsDetails(
  public val loadedTargetId: BuildTargetIdentifier?,
  public val notLoadedTargetsIds: List<BuildTargetIdentifier>
)

public interface MagicMetaModel {

  public fun loadDefaultTargets()

  public fun loadTarget(targetId: BuildTargetIdentifier)

  public fun getTargetsDetailsForDocument(documentId: TextDocumentIdentifier): DocumentTargetsDetails

  public fun getAllLoadedTargets(): List<BuildTarget>

  public fun getAllNotLoadedTargets(): List<BuildTarget>
}

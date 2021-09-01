package org.jetbrains.magicmetamodel.impl

import ch.epfl.scala.bsp4j.BuildTarget
import ch.epfl.scala.bsp4j.SourcesItem
import ch.epfl.scala.bsp4j.TextDocumentIdentifier
import org.jetbrains.magicmetamodel.DocumentTargetsDetails
import org.jetbrains.magicmetamodel.MagicMetaModel

public class MagicMetaModelImpl public constructor(
  public override val targets: List<BuildTarget>,
  sources: List<SourcesItem>
) : MagicMetaModel {

  private val targetsDetailsForDocumentProvider = TargetsDetailsForDocumentProvider(sources)

  public override fun getTargetsDetailsForDocument(documentId: TextDocumentIdentifier): DocumentTargetsDetails {
    val allTargetsIds = targetsDetailsForDocumentProvider.getTargetsDetailsForDocument(documentId)

    return DocumentTargetsDetails(
      activeTargetId = null,
      inactiveTargetsIds = allTargetsIds
    )
  }
}
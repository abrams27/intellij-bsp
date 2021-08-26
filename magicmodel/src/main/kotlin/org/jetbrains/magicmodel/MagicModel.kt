package org.jetbrains.magicmodel

import ch.epfl.scala.bsp4j.BuildTargetIdentifier
import ch.epfl.scala.bsp4j.TextDocumentIdentifier

public class MagicModel {

    public fun getTargetsForDocument(documentId: TextDocumentIdentifier): List<BuildTargetIdentifier> {
        return emptyList()
    }
}
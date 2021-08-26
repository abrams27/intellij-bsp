package org.jetbrains.magicmodel

import ch.epfl.scala.bsp4j.TextDocumentIdentifier
import org.junit.jupiter.api.Test

class MagicModelTest {

    @Test
    fun `should return no targets for not existing document`() {
        val magicModel = MagicModel()
        val notExistingDocumentId = TextDocumentIdentifier("file:///not/existing/file")

        val targets = magicModel.getTargetsForDocument(notExistingDocumentId)

        assert(targets.isEmpty())
    }
}
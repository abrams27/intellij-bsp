package org.jetbrains.magicmodel

import ch.epfl.scala.bsp4j.TextDocumentIdentifier
import org.junit.jupiter.api.Test
import kotlin.test.assertTrue

class MagicModelTest {

  @Test
  fun `should return no targets for not existing document`() {
    // given
    val magicModel = MagicModel()
    val notExistingDocumentId = TextDocumentIdentifier("file:///not/existing/file")

    // when
    val targets = magicModel.getTargetsForDocument(notExistingDocumentId)

    // then
    assertTrue(targets.isEmpty())
  }
}
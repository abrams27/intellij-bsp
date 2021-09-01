package org.jetbrains.magicmetamodel

import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.Test

class OverlappingTargetsGraphTest {

  @Test
  fun `should return empty graph for no targets`() {
    // given
    val targetsDetailsForDocumentProvider = TargetsDetailsForDocumentProvider(emptyList())

    // when
    val overlappingTargetsGraph by OverlappingTargetsGraphDelegate(targetsDetailsForDocumentProvider)

    // then
    overlappingTargetsGraph shouldBe emptyMap()
  }
}
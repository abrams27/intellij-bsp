package org.jetbrains.magicmodel

import org.junit.jupiter.api.BeforeEach


class MagicModelTest {

  private lateinit var magicModel: MagicModel

  @BeforeEach
  fun beforeEach() {
    magicModel = MagicModel(emptyList())
  }

}
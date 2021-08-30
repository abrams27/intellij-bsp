package org.jetbrains.magicmetamodel

import org.junit.jupiter.api.BeforeEach


class MagicMetaModelTest {

  private lateinit var magicMetaModel: MagicMetaModel

  @BeforeEach
  fun beforeEach() {
    magicMetaModel = MagicMetaModel(emptyList())
  }

}
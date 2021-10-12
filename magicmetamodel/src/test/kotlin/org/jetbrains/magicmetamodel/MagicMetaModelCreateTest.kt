package org.jetbrains.magicmetamodel

import io.kotest.matchers.shouldBe
import org.jetbrains.magicmetamodel.impl.MagicMetaModelImpl
import org.jetbrains.magicmetamodel.impl.workspacemodel.WorkspaceModelBaseTest
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test

@DisplayName("MagicMetaModel.create(workspaceModel, targets, sources) tests")
private class MagicMetaModelCreateTest : WorkspaceModelBaseTest() {

  @Test
  fun `should return MagicMetaModelImpl`() {
    // given
    val testMagicMetaModelProjectConfig =
      MagicMetaModelProjectConfig(workspaceModel, virtualFileUrlManager, projectBaseDirPath)

    val projectDetails = ProjectDetails(
      targetsId = emptyList(),
      targets = emptyList(),
      sources = emptyList(),
      resources = emptyList(),
      dependenciesSources = emptyList(),
    )

    // when
    val magicMetaModel = MagicMetaModel.create(testMagicMetaModelProjectConfig, projectDetails)

    // then
    magicMetaModel::class shouldBe MagicMetaModelImpl::class
  }
}

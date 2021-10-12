package org.jetbrains.magicmetamodel.impl.workspacemodel

import io.kotest.matchers.shouldBe
import org.jetbrains.magicmetamodel.impl.workspacemodel.impl.WorkspaceModelUpdaterImpl
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test

@DisplayName("WorkspaceModelUpdater.create(workspaceModelUpdaterConfig) tests")
private class WorkspaceModelUpdaterCreateTest : WorkspaceModelBaseTest() {

  @Test
  fun `should return WorkspaceModelUpdaterImpl`() {
    // given

    // when
    val workspaceModelUpdater = WorkspaceModelUpdater.create(workspaceModel, virtualFileUrlManager, projectBaseDirPath)

    // then
    workspaceModelUpdater::class shouldBe WorkspaceModelUpdaterImpl::class
  }
}

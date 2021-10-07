package org.jetbrains.magicmetamodel.impl.workspacemodel

import com.intellij.openapi.project.Project
import com.intellij.project.stateStore
import com.intellij.testFramework.fixtures.IdeaTestFixtureFactory
import com.intellij.workspaceModel.ide.WorkspaceModel
import com.intellij.workspaceModel.ide.getInstance
import com.intellij.workspaceModel.storage.url.VirtualFileUrlManager
import org.junit.jupiter.api.BeforeEach
import java.nio.file.Path

abstract class WorkspaceModelBaseTest {

  protected lateinit var project: Project
  protected lateinit var workspaceModel: WorkspaceModel
  protected lateinit var virtualFileUrlManager: VirtualFileUrlManager
  protected lateinit var projectBaseDirPath: Path

  @BeforeEach
  fun beforeEach() {
    project = emptyProjectTestMock()
    workspaceModel = WorkspaceModel.getInstance(project)
    virtualFileUrlManager = VirtualFileUrlManager.getInstance(project)
    projectBaseDirPath = project.stateStore.projectBasePath
  }

  private fun emptyProjectTestMock(): Project {
    val factory = IdeaTestFixtureFactory.getFixtureFactory()
    val fixtureBuilder = factory.createFixtureBuilder("test", true)
    val fixture = fixtureBuilder.fixture
    fixture.setUp()

    return fixture.project
  }
}
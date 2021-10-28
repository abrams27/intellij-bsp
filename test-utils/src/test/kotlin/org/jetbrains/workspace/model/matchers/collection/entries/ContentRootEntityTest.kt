package org.jetbrains.workspace.model.matchers.collection.entries

import com.intellij.workspaceModel.storage.bridgeEntities.ContentRootEntity
import com.intellij.workspaceModel.storage.bridgeEntities.addContentRootEntity
import io.kotest.assertions.throwables.shouldNotThrow
import io.kotest.assertions.throwables.shouldThrow
import org.jetbrains.workspace.model.test.framework.WorkspaceModelWithParentJavaModuleBaseTest
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test

@DisplayName("ContentRootEntity matchers tests")
class ContentRootEntityTest : WorkspaceModelWithParentJavaModuleBaseTest() {

  @Nested
  @DisplayName("contentRootEntity shouldBeEqual expectedContentRootEntity tests")
  inner class ContentRootEntityShouldBeEqualTest {

    @Test
    fun `should fail for different entries`() {
      // given
      val actualContentRootEntity = runTestWriteAction {
        workspaceModel.updateProjectModel {
          it.addContentRootEntity(
            url = virtualFileUrlManager.fromUrl("file:///path/to/content/root"),
            excludedUrls = listOf(virtualFileUrlManager.fromUrl("file:///path/to/content/root/excluded")),
            excludedPatterns = listOf("excluded/pattern"),
            module = parentModuleEntity,
          )
        }
      }
      val expectedContentRootEntity = ExpectedContentRootEntity(
        contentRootEntity = ContentRootEntity(
          url = virtualFileUrlManager.fromUrl("file:///different/path/to/content/root"),
          excludedUrls = emptyList(),
          excludedPatterns = listOf("excluded/pattern"),
        ),
        parentModuleEntity = parentModuleEntity,
      )

      // when & then
      shouldThrow<AssertionError> {
        actualContentRootEntity shouldBeEqual expectedContentRootEntity
      }
    }

    @Test
    fun `should pass for equal entries`() {
      // given
      val actualContentRootEntity = runTestWriteAction {
        workspaceModel.updateProjectModel {
          it.addContentRootEntity(
            url = virtualFileUrlManager.fromUrl("file:///path/to/content/root"),
            excludedUrls = listOf(virtualFileUrlManager.fromUrl("file:///path/to/content/root/excluded")),
            excludedPatterns = listOf("excluded/pattern1", "excluded/pattern2"),
            module = parentModuleEntity,
          )
        }
      }
      val expectedContentRootEntity = ExpectedContentRootEntity(
        contentRootEntity = ContentRootEntity(
          url = virtualFileUrlManager.fromUrl("file:///path/to/content/root"),
          excludedUrls = listOf(virtualFileUrlManager.fromUrl("file:///path/to/content/root/excluded")),
          excludedPatterns = listOf("excluded/pattern2", "excluded/pattern1"),
        ),
        parentModuleEntity = parentModuleEntity,
      )

      // when & then
      shouldNotThrow<AssertionError> {
        actualContentRootEntity shouldBeEqual expectedContentRootEntity
      }
    }
  }

  @Nested
  @DisplayName("contentRootEntries shouldContainExactlyInAnyOrder expectedContentRootEntries tests")
  inner class ContentRootEntityShouldContainExactlyInAnyOrderTest {

    @Test
    fun `should fail for different entries`() {
      // given
      val actualContentRootEntity1 = runTestWriteAction {
        workspaceModel.updateProjectModel {
          it.addContentRootEntity(
            url = virtualFileUrlManager.fromUrl("file:///path1/to/content/root"),
            excludedUrls = listOf(virtualFileUrlManager.fromUrl("file:///path1/to/content/root/excluded")),
            excludedPatterns = listOf("excluded/pattern1", "excluded/pattern2"),
            module = parentModuleEntity,
          )
        }
      }
      val actualContentRootEntity2 = runTestWriteAction {
        workspaceModel.updateProjectModel {
          it.addContentRootEntity(
            url = virtualFileUrlManager.fromUrl("file:///path2/to/content/root"),
            excludedUrls = listOf(virtualFileUrlManager.fromUrl("file:///path2/to/content/root/excluded")),
            excludedPatterns = emptyList(),
            module = parentModuleEntity,
          )
        }
      }
      val actualContentRootEntries = listOf(actualContentRootEntity1, actualContentRootEntity2)

      val expectedContentRootEntity1 = ExpectedContentRootEntity(
        contentRootEntity = ContentRootEntity(
          url = virtualFileUrlManager.fromUrl("file:///different/path1/to/content/root"),
          excludedUrls = emptyList(),
          excludedPatterns = emptyList(),
        ),
        parentModuleEntity = parentModuleEntity,
      )
      val expectedContentRootEntity2 = ExpectedContentRootEntity(
        contentRootEntity = ContentRootEntity(
          url = virtualFileUrlManager.fromUrl("file:///path2/to/content/root"),
          excludedUrls = listOf(virtualFileUrlManager.fromUrl("file:///path2/to/content/root/excluded")),
          excludedPatterns = emptyList(),
        ),
        parentModuleEntity = parentModuleEntity,
      )
      val expectedContentRootEntries = listOf(expectedContentRootEntity2, expectedContentRootEntity1)

      // when & then
      shouldThrow<AssertionError> {
        actualContentRootEntries shouldContainExactlyInAnyOrder expectedContentRootEntries
      }
    }

    @Test
    fun `should pass for equal entries`() {
      // given
      val actualContentRootEntity1 = runTestWriteAction {
        workspaceModel.updateProjectModel {
          it.addContentRootEntity(
            url = virtualFileUrlManager.fromUrl("file:///path1/to/content/root"),
            excludedUrls = listOf(virtualFileUrlManager.fromUrl("file:///path1/to/content/root/excluded")),
            excludedPatterns = listOf("excluded/pattern1", "excluded/pattern2"),
            module = parentModuleEntity,
          )
        }
      }
      val actualContentRootEntity2 = runTestWriteAction {
        workspaceModel.updateProjectModel {
          it.addContentRootEntity(
            url = virtualFileUrlManager.fromUrl("file:///path2/to/content/root"),
            excludedUrls = listOf(virtualFileUrlManager.fromUrl("file:///path2/to/content/root/excluded")),
            excludedPatterns = emptyList(),
            module = parentModuleEntity,
          )
        }
      }
      val actualContentRootEntries = listOf(actualContentRootEntity1, actualContentRootEntity2)

      val expectedContentRootEntity1 = ExpectedContentRootEntity(
        contentRootEntity = ContentRootEntity(
          url = virtualFileUrlManager.fromUrl("file:///path1/to/content/root"),
          excludedUrls = listOf(virtualFileUrlManager.fromUrl("file:///path1/to/content/root/excluded")),
          excludedPatterns = listOf("excluded/pattern2", "excluded/pattern1"),
        ),
        parentModuleEntity = parentModuleEntity,
      )
      val expectedContentRootEntity2 = ExpectedContentRootEntity(
        contentRootEntity = ContentRootEntity(
          url = virtualFileUrlManager.fromUrl("file:///path2/to/content/root"),
          excludedUrls = listOf(virtualFileUrlManager.fromUrl("file:///path2/to/content/root/excluded")),
          excludedPatterns = emptyList(),
        ),
        parentModuleEntity = parentModuleEntity,
      )
      val expectedContentRootEntries = listOf(expectedContentRootEntity2, expectedContentRootEntity1)

      // when & then
      shouldNotThrow<AssertionError> {
        actualContentRootEntries shouldContainExactlyInAnyOrder expectedContentRootEntries
      }
    }
  }
}

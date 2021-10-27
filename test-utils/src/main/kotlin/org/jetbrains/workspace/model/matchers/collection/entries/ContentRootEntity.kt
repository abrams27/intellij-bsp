package org.jetbrains.workspace.model.matchers.collection.entries

import com.intellij.workspaceModel.storage.bridgeEntities.ContentRootEntity
import com.intellij.workspaceModel.storage.bridgeEntities.ModuleEntity
import io.kotest.matchers.equality.shouldBeEqualToComparingFields
import io.kotest.matchers.shouldBe
import org.jetbrains.workspace.model.matchers.collection.shouldContainExactlyInAnyOrder

public data class ExpectedContentRootEntity(
  val contentRootEntity: ContentRootEntity,
  val parentModuleEntity: ModuleEntity,
)

public infix fun ContentRootEntity.shouldBeEqual(expected: ExpectedContentRootEntity): Unit =
  validateContentRootEntity(this, expected)

public infix fun Collection<ContentRootEntity>.shouldContainExactlyInAnyOrder(
  expectedValues: Collection<ExpectedContentRootEntity>,
): Unit =
  this.shouldContainExactlyInAnyOrder(::validateContentRootEntity, expectedValues)

private fun validateContentRootEntity(
  actual: ContentRootEntity,
  expected: ExpectedContentRootEntity
) {
  actual.url shouldBe expected.contentRootEntity.url
  actual.excludedUrls shouldBe expected.contentRootEntity.excludedUrls
  actual.excludedPatterns shouldBe expected.contentRootEntity.excludedPatterns

  val actualModuleEntity = actual.module
  actualModuleEntity shouldBe expected.parentModuleEntity
  actualModuleEntity shouldBeEqualToComparingFields expected.parentModuleEntity
}

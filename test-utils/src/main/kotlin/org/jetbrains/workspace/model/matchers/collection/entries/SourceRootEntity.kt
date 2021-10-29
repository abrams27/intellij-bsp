package org.jetbrains.workspace.model.matchers.collection.entries

import com.intellij.workspaceModel.storage.bridgeEntities.ContentRootEntity
import com.intellij.workspaceModel.storage.bridgeEntities.ModuleEntity
import com.intellij.workspaceModel.storage.bridgeEntities.SourceRootEntity
import io.kotest.matchers.shouldBe
import org.jetbrains.workspace.model.matchers.collection.shouldContainExactlyInAnyOrder

public data class ExpectedSourceRootEntity(
  val sourceRootEntity: SourceRootEntity,
  val contentRootEntity: ContentRootEntity,
  val parentModuleEntity: ModuleEntity,
)

public infix fun SourceRootEntity.shouldBeEqual(expected: ExpectedSourceRootEntity): Unit =
  validateSourceRootEntity(this, expected)

public infix fun Collection<SourceRootEntity>.shouldContainExactlyInAnyOrder(
  expectedValues: Collection<ExpectedSourceRootEntity>,
): Unit =
  this.shouldContainExactlyInAnyOrder(::validateSourceRootEntity, expectedValues)

private fun validateSourceRootEntity(
  actual: SourceRootEntity,
  expected: ExpectedSourceRootEntity
) {
  actual.url shouldBe expected.sourceRootEntity.url
  actual.rootType shouldBe expected.sourceRootEntity.rootType

  actual.contentRoot shouldBeEqual toExpectedContentRootEntity(expected)
}

private fun toExpectedContentRootEntity(expected: ExpectedSourceRootEntity): ExpectedContentRootEntity =
  ExpectedContentRootEntity(
    contentRootEntity = expected.contentRootEntity,
    parentModuleEntity = expected.parentModuleEntity,
  )

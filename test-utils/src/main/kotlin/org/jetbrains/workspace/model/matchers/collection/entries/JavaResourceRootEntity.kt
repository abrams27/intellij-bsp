package org.jetbrains.workspace.model.matchers.collection.entries

import com.intellij.workspaceModel.storage.bridgeEntities.ContentRootEntity
import com.intellij.workspaceModel.storage.bridgeEntities.JavaResourceRootEntity
import com.intellij.workspaceModel.storage.bridgeEntities.ModuleEntity
import com.intellij.workspaceModel.storage.bridgeEntities.SourceRootEntity
import io.kotest.matchers.shouldBe
import org.jetbrains.workspace.model.matchers.collection.shouldContainExactlyInAnyOrder

public data class ExpectedJavaResourceRootEntity(
  val javaResourceRootEntity: JavaResourceRootEntity,
  val sourceRootEntity: SourceRootEntity,
  val contentRootEntity: ContentRootEntity,
  val parentModuleEntity: ModuleEntity,
)

public infix fun JavaResourceRootEntity.shouldBeEqual(expected: ExpectedJavaResourceRootEntity): Unit =
  validateJavaResourceRootEntity(this, expected)

public infix fun Collection<JavaResourceRootEntity>.shouldContainExactlyInAnyOrder(
  expectedValues: Collection<ExpectedJavaResourceRootEntity>,
): Unit =
  this.shouldContainExactlyInAnyOrder(::validateJavaResourceRootEntity, expectedValues)

private fun validateJavaResourceRootEntity(
  actual: JavaResourceRootEntity,
  expected: ExpectedJavaResourceRootEntity
) {
  actual.generated shouldBe expected.javaResourceRootEntity.generated
  actual.relativeOutputPath shouldBe expected.javaResourceRootEntity.relativeOutputPath

  val actualSourceRoot = actual.sourceRoot
  actualSourceRoot.url shouldBe expected.sourceRootEntity.url
  actualSourceRoot.rootType shouldBe expected.sourceRootEntity.rootType

  val actualContentRoot = actualSourceRoot.contentRoot
  actualContentRoot shouldBeEqual toExpectedContentRootEntity(expected)

  val actualModuleEntity = actualContentRoot.module
  actualModuleEntity shouldBe expected.parentModuleEntity
}

private fun toExpectedContentRootEntity(expected: ExpectedJavaResourceRootEntity): ExpectedContentRootEntity =
  ExpectedContentRootEntity(
    contentRootEntity = expected.contentRootEntity,
    parentModuleEntity = expected.parentModuleEntity,
  )

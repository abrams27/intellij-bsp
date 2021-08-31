package org.jetbrains.magicmetamodel

import ch.epfl.scala.bsp4j.BuildTarget
import ch.epfl.scala.bsp4j.BuildTargetCapabilities
import ch.epfl.scala.bsp4j.BuildTargetIdentifier
import ch.epfl.scala.bsp4j.SourcesItem
import io.kotest.matchers.collections.shouldContainExactlyInAnyOrder
import org.junit.jupiter.api.Test

class MagicMetaModelTargetsTest {

  @Test
  fun `should return the same targets as in the constructor`() {
    // given
    val target1Dependencies = listOf(
      BuildTargetIdentifier("@maven//:dep1.1"),
      BuildTargetIdentifier("@maven//:dep1.2")
    )
    val target1 = BuildTarget(
      BuildTargetIdentifier("//target1"),
      emptyList(),
      listOf("java", "kotlin"),
      target1Dependencies,
      BuildTargetCapabilities()
    )

    val target2Dependencies = listOf(
      BuildTargetIdentifier("@maven//:dep2.1"),
      BuildTargetIdentifier("@maven//:dep2.2"),
      BuildTargetIdentifier("//target1"),
    )
    val target2 = BuildTarget(
      BuildTargetIdentifier("//target2"),
      emptyList(),
      listOf("kotlin"),
      target2Dependencies,
      BuildTargetCapabilities()
    )

    val targets = listOf(target1, target2)
    val sources = emptyList<SourcesItem>()

    // when
    val magicMetaModel = MagicMetaModel(targets, sources)

    val modelTargets = magicMetaModel.targets

    // then
    modelTargets shouldContainExactlyInAnyOrder targets
  }
}
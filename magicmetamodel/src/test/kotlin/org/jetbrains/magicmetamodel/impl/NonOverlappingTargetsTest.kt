@file:Suppress("MaxLineLength")

package org.jetbrains.magicmetamodel.impl

import ch.epfl.scala.bsp4j.BuildTargetIdentifier
import io.kotest.matchers.collections.shouldContainExactlyInAnyOrder
import io.kotest.matchers.shouldBe
import org.jetbrains.workspace.model.constructors.BuildTarget
import org.jetbrains.workspace.model.constructors.BuildTargetId
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test

@DisplayName("NonOverlappingTargetsDelegate(overlappingTargetsGraph) tests")
class NonOverlappingTargetsTest {

  @Test
  fun `should return empty set for no targets`() {
    // given
    val allTargets = emptyList<BuildTarget>()
    val overlappingTargetsGraph = mapOf<BuildTargetIdentifier, Set<BuildTargetIdentifier>>()

    // when
    val nonOverlappingTargets by NonOverlappingTargetsDelegate(allTargets, overlappingTargetsGraph)

    // then
    nonOverlappingTargets shouldBe emptySet()
  }

  @Test
  fun `should return set with all targets for non overlapping targets`() {
    // given
    val target1 = BuildTarget(
      id = BuildTargetId("//target1"),
    )
    val target2 = BuildTarget(
      id = BuildTargetId("//target2"),
      dependencies = listOf(
        BuildTargetId("//target1"),
        BuildTargetId("@maven://random/external/dep")
      ),
    )
    val target3 = BuildTarget(
      id = BuildTargetId("//target3"),
    )
    val target4 = BuildTarget(
      id = BuildTargetId("//target4"),
      dependencies = listOf(BuildTargetId("//target1"), BuildTargetId("//target3")),
    )

    val allTargets = listOf(target1, target2, target3, target4)
    val overlappingTargetsGraph = mapOf<BuildTargetIdentifier, Set<BuildTargetIdentifier>>(
      BuildTargetId("//target1") to emptySet(),
      BuildTargetId("//target2") to emptySet(),
      BuildTargetId("//target3") to emptySet(),
    )

    // when
    val nonOverlappingTargets by NonOverlappingTargetsDelegate(allTargets, overlappingTargetsGraph)

    // then
    val expectedTargets = setOf(
      BuildTargetId("//target1"),
      BuildTargetId("//target2"),
      BuildTargetId("//target3"),
      BuildTargetId("//target4"),
    )
    nonOverlappingTargets shouldContainExactlyInAnyOrder expectedTargets
  }

  @Test
  fun `should return set with non overlapping targets for overlapping targets and one target without sources`() {
    // given
    val target1 = BuildTarget(
      id = BuildTargetId("//target1"),
    )
    val target2 = BuildTarget(
      id = BuildTargetId("//target2"),
      dependencies = listOf(BuildTargetId("@maven://random/external/dep")),
    )
    val target3 = BuildTarget(
      id = BuildTargetId("//target3"),
      dependencies = listOf(BuildTargetId("//target1"))
    )
    val target4 = BuildTarget(
      id = BuildTargetId("//target4"),
      dependencies = listOf(BuildTargetId("//target1"), BuildTargetId("@maven://random/external/dep")),
    )
    val target5 = BuildTarget(
      id = BuildTargetId("//target5"),
    )
    val target6 = BuildTarget(
      id = BuildTargetId("//target6"),
      dependencies = listOf(
        BuildTargetId("//target1"),
        BuildTargetId("//target2"),
        BuildTargetId("@maven://random/external/dep")
      ),
    )

    val allTargets = listOf(target1, target2, target3, target4, target5, target6)
    val overlappingTargetsGraph = mapOf<BuildTargetIdentifier, Set<BuildTargetIdentifier>>(
      BuildTargetId("//target1") to setOf(BuildTargetId("//target2")),
      BuildTargetId("//target2") to setOf(BuildTargetId("//target1")),
      BuildTargetId("//target3") to setOf(BuildTargetId("//target4"), BuildTargetId("//target5")),
      BuildTargetId("//target4") to setOf(BuildTargetId("//target3"), BuildTargetId("//target5")),
      BuildTargetId("//target5") to setOf(BuildTargetId("//target3"), BuildTargetId("//target4")),
    )

    // when
    val nonOverlappingTargets by NonOverlappingTargetsDelegate(allTargets, overlappingTargetsGraph)

    // then
    val expectedTargets = setOf(
      BuildTargetId("//target1"),
      BuildTargetId("//target3"),
      BuildTargetId("//target6")
    )
    nonOverlappingTargets shouldContainExactlyInAnyOrder expectedTargets
  }

  @Test
  fun `should return set with non overlapping targets for overlapping targets and non overlapping targets and one target without sources`() {
    // given
    val target1 = BuildTarget(id = BuildTargetId("//target1"))
    val target2 = BuildTarget(id = BuildTargetId("//target2"))
    val target3 = BuildTarget(id = BuildTargetId("//target3"))
    val target4 = BuildTarget(id = BuildTargetId("//target4"))
    val target5 = BuildTarget(id = BuildTargetId("//target5"))
    val target6 = BuildTarget(id = BuildTargetId("//target6"))

    val allTargets = listOf(target1, target2, target3, target4, target5, target6)
    val overlappingTargetsGraph = mapOf<BuildTargetIdentifier, Set<BuildTargetIdentifier>>(
      BuildTargetId("//target1") to setOf(BuildTargetId("//target2")),
      BuildTargetId("//target2") to setOf(
        BuildTargetId("//target1"),
        BuildTargetId("//target3")
      ),
      BuildTargetId("//target3") to setOf(BuildTargetId("//target2")),
      BuildTargetId("//target4") to emptySet(),
      BuildTargetId("//target5") to emptySet(),
    )

    // when
    val nonOverlappingTargets by NonOverlappingTargetsDelegate(allTargets, overlappingTargetsGraph)

    // then
    val expectedTargets = setOf(
      BuildTargetId("//target1"),
      BuildTargetId("//target3"),
      BuildTargetId("//target4"),
      BuildTargetId("//target5"),
      BuildTargetId("//target6")
    )
    nonOverlappingTargets shouldContainExactlyInAnyOrder expectedTargets
  }
}

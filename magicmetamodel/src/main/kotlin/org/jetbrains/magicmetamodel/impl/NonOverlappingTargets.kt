package org.jetbrains.magicmetamodel.impl

import ch.epfl.scala.bsp4j.BuildTargetIdentifier
import kotlin.reflect.KProperty

internal class NonOverlappingTargetsDelegate(
  private val overlappingTargetsGraph: Map<BuildTargetIdentifier, Set<BuildTargetIdentifier>>
) {

  operator fun getValue(
    thisRef: Any?,
    property: KProperty<*>
  ): Set<BuildTargetIdentifier> {
    val allTargets = overlappingTargetsGraph.keys

    return allTargets.fold(emptySet(), this::addTargetToSetIfNoneOfItsNeighborsIsAdded)
  }

  private fun addTargetToSetIfNoneOfItsNeighborsIsAdded(
    nonOverlappingTargetsAcc: Set<BuildTargetIdentifier>,
    target: BuildTargetIdentifier
  ): Set<BuildTargetIdentifier> {
    val shouldNotTargetBeAddedToSet = isAnyOfNeighborsAddedToSet(nonOverlappingTargetsAcc, target)

    return if (shouldNotTargetBeAddedToSet) nonOverlappingTargetsAcc else nonOverlappingTargetsAcc + target
  }

  private fun isAnyOfNeighborsAddedToSet(
    nonOverlappingTargetsAcc: Set<BuildTargetIdentifier>,
    target: BuildTargetIdentifier,
  ): Boolean {
    val neighbors = overlappingTargetsGraph[target] ?: emptySet()

    return isAnyTargetAddedToSet(nonOverlappingTargetsAcc, neighbors)
  }

  private fun isAnyTargetAddedToSet(
    nonOverlappingTargetsAcc: Set<BuildTargetIdentifier>,
    targets: Collection<BuildTargetIdentifier>,
  ): Boolean = targets.any { it in nonOverlappingTargetsAcc }
}

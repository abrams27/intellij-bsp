package org.jetbrains.magicmetamodel.impl

import ch.epfl.scala.bsp4j.BuildTargetIdentifier
import org.jetbrains.magicmetamodel.extensions.reduceSets
import kotlin.reflect.KProperty

internal class OverlappingTargetsGraphDelegate(
  private val targetsDetailsForDocumentProvider: TargetsDetailsForDocumentProvider
) {

  operator fun getValue(
    thisRef: Any?,
    property: KProperty<*>
  ): Map<BuildTargetIdentifier, Set<BuildTargetIdentifier>> =
    targetsDetailsForDocumentProvider.getAllDocuments()
      .map(targetsDetailsForDocumentProvider::getTargetsDetailsForDocument)
      .flatMap(this::generateEdgesForOverlappingTargetsForAllTargets)
      .groupBy({ it.first }, { it.second })
      .mapValues { it.value.reduceSets() }

  private fun generateEdgesForOverlappingTargetsForAllTargets(
    overlappingTargets: List<BuildTargetIdentifier>
  ): List<Pair<BuildTargetIdentifier, Set<BuildTargetIdentifier>>> =
    overlappingTargets.map { generateEdgesForOverlappingTargetsForOneTarget(it, overlappingTargets) }

  private fun generateEdgesForOverlappingTargetsForOneTarget(
    target: BuildTargetIdentifier,
    overlappingTargets: List<BuildTargetIdentifier>
  ): Pair<BuildTargetIdentifier, Set<BuildTargetIdentifier>> {
    val targetEdges = filterGivenTargetFromOverlappingTargetsAndMapToSet(target, overlappingTargets)

    return Pair(target, targetEdges)
  }

  private fun filterGivenTargetFromOverlappingTargetsAndMapToSet(
    target: BuildTargetIdentifier,
    overlappingTargets: List<BuildTargetIdentifier>
  ): Set<BuildTargetIdentifier> =
    overlappingTargets
      .filter { it != target }
      .toSet()
}
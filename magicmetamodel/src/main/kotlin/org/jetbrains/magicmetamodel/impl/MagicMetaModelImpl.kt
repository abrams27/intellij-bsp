package org.jetbrains.magicmetamodel.impl

import ch.epfl.scala.bsp4j.BuildTarget
import ch.epfl.scala.bsp4j.BuildTargetIdentifier
import ch.epfl.scala.bsp4j.SourcesItem
import ch.epfl.scala.bsp4j.TextDocumentIdentifier
import com.intellij.workspaceModel.ide.WorkspaceModel
import org.jetbrains.magicmetamodel.DocumentTargetsDetails
import org.jetbrains.magicmetamodel.MagicMetaModel

public class MagicMetaModelImpl public constructor(
  private val workspaceModel: WorkspaceModel,
  private val targets: List<BuildTarget>,
  sources: List<SourcesItem>
) : MagicMetaModel {

  private val targetsDetailsForDocumentProvider = TargetsDetailsForDocumentProvider(sources)
  private val overlappingTargetsGraph by OverlappingTargetsGraphDelegate(targetsDetailsForDocumentProvider)

  private val loadedTargets = mutableSetOf<BuildTargetIdentifier>()

  public override fun loadDefaultTargets() {
    val nonOverlappingTargetsToLoad by NonOverlappingTargetsDelegate(overlappingTargetsGraph)

    // TODO: add mapping to the workspace model
    loadedTargets.clear()
    loadedTargets.addAll(nonOverlappingTargetsToLoad)
  }

  public override fun loadTarget(targetId: BuildTargetIdentifier) {
    throwIllegalArgumentExceptionIfTargetIsNotIncludedInTheModel(targetId)

    if(!loadedTargets.contains(targetId)) {
      // TODO: add mapping to the workspace model
      loadTargetAndRemoveOverlappingLoadedTargets(targetId)
    }
  }

  private fun throwIllegalArgumentExceptionIfTargetIsNotIncludedInTheModel(targetId: BuildTargetIdentifier) {
    if (!isTargetIncludedInTheModel(targetId)) {
      throw IllegalArgumentException("Target $targetId is not included in the model.")
    }
  }

  private fun isTargetIncludedInTheModel(targetId: BuildTargetIdentifier): Boolean =
    targets.any { it.id == targetId }

  private fun loadTargetAndRemoveOverlappingLoadedTargets(targetIdToLoad: BuildTargetIdentifier) {
    val targetsToRemove = overlappingTargetsGraph[targetIdToLoad] ?: emptySet()

    loadedTargets.removeAll(targetsToRemove)
    loadedTargets.add(targetIdToLoad)
  }

  public override fun getTargetsDetailsForDocument(documentId: TextDocumentIdentifier): DocumentTargetsDetails {
    val allTargetsIds = targetsDetailsForDocumentProvider.getTargetsDetailsForDocument(documentId)

    return DocumentTargetsDetails(
      activeTargetId = null,
      inactiveTargetsIds = allTargetsIds
    )
  }

  override fun getAllLoadedTargets(): List<BuildTarget> =
    targets.filter(this::isTargetLoaded)

  override fun getAllNotLoadedTargets(): List<BuildTarget> =
    targets.filterNot(this::isTargetLoaded)

  private fun isTargetLoaded(target: BuildTarget): Boolean =
    loadedTargets.contains(target.id)
}
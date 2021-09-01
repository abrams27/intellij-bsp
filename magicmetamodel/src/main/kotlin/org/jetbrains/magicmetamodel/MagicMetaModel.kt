package org.jetbrains.magicmetamodel

import ch.epfl.scala.bsp4j.BuildTarget
import ch.epfl.scala.bsp4j.BuildTargetIdentifier
import ch.epfl.scala.bsp4j.TextDocumentIdentifier
import com.intellij.workspaceModel.ide.WorkspaceModel

public data class DocumentTargetsDetails(
  public val activeTargetId: BuildTargetIdentifier?,
  public val inactiveTargetsIds: List<BuildTargetIdentifier>
)

public interface MagicMetaModelDiff {

  public fun applyToWorkspaceModel(workspaceModel: WorkspaceModel)
}

public interface MagicMetaModel {

  public val targets: List<BuildTarget>

  public fun getTargetsDetailsForDocument(documentId: TextDocumentIdentifier): DocumentTargetsDetails

  public fun initializeProject(): MagicMetaModelDiff

  public fun loadTarget(targetId: BuildTargetIdentifier): MagicMetaModelDiff
}

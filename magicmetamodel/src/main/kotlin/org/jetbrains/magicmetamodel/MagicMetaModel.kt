package org.jetbrains.magicmetamodel

import ch.epfl.scala.bsp4j.BuildTarget
import ch.epfl.scala.bsp4j.BuildTargetIdentifier
import ch.epfl.scala.bsp4j.SourcesItem
import ch.epfl.scala.bsp4j.TextDocumentIdentifier
import com.intellij.workspaceModel.ide.WorkspaceModel
import org.jetbrains.magicmetamodel.impl.MagicMetaModelImpl

public data class DocumentTargetsDetails(
  public val loadedTargetId: BuildTargetIdentifier?,
  public val notLoadedTargetsIds: List<BuildTargetIdentifier>
)

public interface MagicMetaModel {

  public fun loadDefaultTargets()

  public fun loadTarget(targetId: BuildTargetIdentifier)

  public fun getTargetsDetailsForDocument(documentId: TextDocumentIdentifier): DocumentTargetsDetails

  public fun getAllLoadedTargets(): List<BuildTarget>

  public fun getAllNotLoadedTargets(): List<BuildTarget>

  public companion object {
    public fun create(
      workspaceModel: WorkspaceModel,
      targets: List<BuildTarget>,
      sources: List<SourcesItem>
    ): MagicMetaModel = MagicMetaModelImpl(workspaceModel, targets, sources)
  }
}

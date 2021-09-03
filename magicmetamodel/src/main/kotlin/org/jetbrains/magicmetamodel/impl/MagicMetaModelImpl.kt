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
  public override val targets: List<BuildTarget>,
  sources: List<SourcesItem>
) : MagicMetaModel {

  private val targetsDetailsForDocumentProvider = TargetsDetailsForDocumentProvider(sources)

  public override fun loadDefaultTargets() {
    TODO("Not yet implemented")
  }

  public override fun loadTarget(targetId: BuildTargetIdentifier) {
    TODO("Not yet implemented")
  }

  public override fun getTargetsDetailsForDocument(documentId: TextDocumentIdentifier): DocumentTargetsDetails {
    val allTargetsIds = targetsDetailsForDocumentProvider.getTargetsDetailsForDocument(documentId)

    return DocumentTargetsDetails(
      activeTargetId = null,
      inactiveTargetsIds = allTargetsIds
    )
  }

  override fun getAllLoadedTargets(): List<BuildTarget> {
    TODO("Not yet implemented")
  }

  override fun getAllNotLoadedTargets(): List<BuildTarget> {
    TODO("Not yet implemented")
  }
}
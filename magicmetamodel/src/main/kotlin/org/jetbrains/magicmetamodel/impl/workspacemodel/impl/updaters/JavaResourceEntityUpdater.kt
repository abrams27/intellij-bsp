package org.jetbrains.magicmetamodel.impl.workspacemodel.impl.updaters

import com.intellij.workspaceModel.storage.WorkspaceEntityStorageBuilder
import com.intellij.workspaceModel.storage.bridgeEntities.ContentRootEntity
import com.intellij.workspaceModel.storage.bridgeEntities.ModuleEntity
import com.intellij.workspaceModel.storage.bridgeEntities.SourceRootEntity
import com.intellij.workspaceModel.storage.bridgeEntities.addContentRootEntity
import com.intellij.workspaceModel.storage.bridgeEntities.addJavaResourceRootEntity
import com.intellij.workspaceModel.storage.bridgeEntities.addSourceRootEntity
import com.intellij.workspaceModel.storage.impl.url.toVirtualFileUrl
import com.intellij.workspaceModel.storage.url.VirtualFileUrl
import java.nio.file.Path

internal data class JavaResourceRoot(
  val resourcePath: Path,
) : WorkspaceModelEntity()

internal class JavaResourceEntityUpdater(
  private val workspaceModelDetails: WorkspaceModelDetails,
) : WorkspaceModelEntityUpdater<JavaResourceRoot> {

  private val defaultExcludedUrls = emptyList<VirtualFileUrl>()
  private val defaultExcludedPatterns = emptyList<String>()
  private val defaultGenerated = false
  private val defaultRelativeOutputPath = ""

  private val javaResourceContentRootType = "java-resource"

  override fun addEntity(entityToAdd: JavaResourceRoot, parentModuleEntity: ModuleEntity) {
    val virtualResourcePath = entityToAdd.resourcePath.toVirtualFileUrl(workspaceModelDetails.virtualFileManager)

    workspaceModelDetails.workspaceModel.updateProjectModel {
      val contentRootEntity = addContentRootEntity(it, parentModuleEntity, virtualResourcePath)
      val sourceRoot = addSourceRootEntity(it, contentRootEntity, virtualResourcePath)
      addJavaResourceRootEntity(it, sourceRoot)
    }
  }

  private fun addContentRootEntity(
    builder: WorkspaceEntityStorageBuilder,
    moduleEntity: ModuleEntity,
    virtualResourceUrl: VirtualFileUrl,
  ): ContentRootEntity = builder.addContentRootEntity(
    url = virtualResourceUrl,
    excludedUrls = defaultExcludedUrls,
    excludedPatterns = defaultExcludedPatterns,
    module = moduleEntity,
  )

  private fun addSourceRootEntity(
    builder: WorkspaceEntityStorageBuilder,
    contentRootEntity: ContentRootEntity,
    virtualResourceUrl: VirtualFileUrl,
  ): SourceRootEntity = builder.addSourceRootEntity(
    contentRoot = contentRootEntity,
    url = virtualResourceUrl,
    rootType = javaResourceContentRootType,
    source = workspaceModelDetails.projectConfigSource,
  )

  private fun addJavaResourceRootEntity(
    builder: WorkspaceEntityStorageBuilder,
    sourceRoot: SourceRootEntity,
  ) = builder.addJavaResourceRootEntity(
    sourceRoot = sourceRoot,
    generated = defaultGenerated,
    relativeOutputPath = defaultRelativeOutputPath,
  )
}

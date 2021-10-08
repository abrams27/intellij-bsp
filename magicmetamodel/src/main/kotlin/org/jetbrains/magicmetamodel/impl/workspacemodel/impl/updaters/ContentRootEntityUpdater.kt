package org.jetbrains.magicmetamodel.impl.workspacemodel.impl.updaters

import com.intellij.workspaceModel.storage.WorkspaceEntityStorageBuilder
import com.intellij.workspaceModel.storage.bridgeEntities.ContentRootEntity
import com.intellij.workspaceModel.storage.bridgeEntities.ModuleEntity
import com.intellij.workspaceModel.storage.bridgeEntities.addContentRootEntity
import com.intellij.workspaceModel.storage.impl.url.toVirtualFileUrl
import com.intellij.workspaceModel.storage.url.VirtualFileUrl
import java.nio.file.Path

internal data class ContentRoot(
  val url: Path
) : WorkspaceModelEntity()

internal class ContentRootEntityUpdater(
  private val workspaceModelDetails: WorkspaceModelDetails,
) : WorkspaceModelEntityUpdater<ContentRoot, ContentRootEntity> {

  override fun addEntity(entityToAdd: ContentRoot, parentModuleEntity: ModuleEntity): ContentRootEntity {
    val virtualContentUrl = entityToAdd.url.toVirtualFileUrl(workspaceModelDetails.virtualFileManager)

    return workspaceModelDetails.workspaceModel.updateProjectModel {
      addContentRootEntity(it, parentModuleEntity, virtualContentUrl)
    }
  }

  private fun addContentRootEntity(
    builder: WorkspaceEntityStorageBuilder,
    moduleEntity: ModuleEntity,
    virtualContentUrl: VirtualFileUrl,
  ): ContentRootEntity = builder.addContentRootEntity(
    url = virtualContentUrl,
    excludedUrls = DEFAULT_EXCLUDED_URLS,
    excludedPatterns = DEFAULT_EXCLUDED_PATTERNS,
    module = moduleEntity,
  )

  private companion object {
    private val DEFAULT_EXCLUDED_URLS = emptyList<VirtualFileUrl>()
    private val DEFAULT_EXCLUDED_PATTERNS = emptyList<String>()
  }
}

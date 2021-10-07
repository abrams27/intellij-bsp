package org.jetbrains.magicmetamodel.impl.workspacemodel.impl.updaters

import com.intellij.workspaceModel.storage.WorkspaceEntityStorageBuilder
import com.intellij.workspaceModel.storage.bridgeEntities.ContentRootEntity
import com.intellij.workspaceModel.storage.bridgeEntities.ModuleEntity
import com.intellij.workspaceModel.storage.bridgeEntities.SourceRootEntity
import com.intellij.workspaceModel.storage.bridgeEntities.addContentRootEntity
import com.intellij.workspaceModel.storage.bridgeEntities.addJavaSourceRootEntity
import com.intellij.workspaceModel.storage.bridgeEntities.addSourceRootEntity
import com.intellij.workspaceModel.storage.impl.url.toVirtualFileUrl
import com.intellij.workspaceModel.storage.url.VirtualFileUrl
import java.nio.file.Path

internal data class SourceRoot(
  val sourceDir: Path,
  val generated: Boolean
) : WorkspaceModelEntity()

internal data class JavaSourceRoot(
  val sourceDir: Path,
  val generated: Boolean,
  val packagePrefix: String,
) : WorkspaceModelEntity()

internal class JavaSourceEntityUpdater(
  private val workspaceModelDetails: WorkspaceModelDetails,
) : WorkspaceModelEntityUpdater<JavaSourceRoot> {

  private val javaSourceContentRootType = "java-source"

  override fun addEntity(entityToAdd: JavaSourceRoot, parentModuleEntity: ModuleEntity) {
    val virtualSourceRootDir = entityToAdd.sourceDir.toVirtualFileUrl(workspaceModelDetails.virtualFileManager)

    workspaceModelDetails.workspaceModel.updateProjectModel {
      val contentRootEntity = addContentRootEntity(it, parentModuleEntity, virtualSourceRootDir)
      val sourceRoot = addSourceRootEntity(it, contentRootEntity, virtualSourceRootDir)
      addJavaSourceRootEntity(it, sourceRoot, entityToAdd)
    }
  }

  private fun addContentRootEntity(
    builder: WorkspaceEntityStorageBuilder,
    moduleEntity: ModuleEntity,
    virtualSourceRootDir: VirtualFileUrl,
  ): ContentRootEntity = builder.addContentRootEntity(
    url = virtualSourceRootDir,
    excludedUrls = emptyList(),
    excludedPatterns = emptyList(),
    module = moduleEntity
  )

  private fun addSourceRootEntity(
    builder: WorkspaceEntityStorageBuilder,
    contentRootEntity: ContentRootEntity,
    virtualSourceRootDir: VirtualFileUrl,
  ): SourceRootEntity = builder.addSourceRootEntity(
    contentRoot = contentRootEntity,
    url = virtualSourceRootDir,
    rootType = javaSourceContentRootType,
    source = workspaceModelDetails.projectConfigSource
  )

  private fun addJavaSourceRootEntity(
    builder: WorkspaceEntityStorageBuilder,
    sourceRoot: SourceRootEntity,
    entityToAdd: JavaSourceRoot,
  ) = builder.addJavaSourceRootEntity(
    sourceRoot = sourceRoot,
    generated = entityToAdd.generated,
    packagePrefix = entityToAdd.packagePrefix
  )
}

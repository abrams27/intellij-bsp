package org.jetbrains.plugins.bsp.import

import com.intellij.openapi.application.runWriteAction
import com.intellij.openapi.diagnostic.logger
import com.intellij.openapi.project.Project
import com.intellij.openapi.startup.StartupManager
import com.intellij.openapi.util.io.FileUtil
import com.intellij.openapi.vfs.VirtualFile
import com.intellij.platform.PlatformProjectOpenProcessor
import com.intellij.projectImport.ProjectOpenProcessor
import org.jetbrains.plugins.bsp.config.BspPluginBundle
import org.jetbrains.plugins.bsp.config.BspPluginIcons
import org.jetbrains.plugins.bsp.services.MagicMetaModelService
import org.jetbrains.plugins.bsp.util.BspConstants
import javax.swing.Icon

public class BspProjectOpenProcessor : ProjectOpenProcessor() {

  override fun getName(): String = BspPluginBundle.message("plugin.name")

  override fun getIcon(): Icon = BspPluginIcons.bsp

  /// Check if the directory is the .bsp directory itself or contains one
  private fun getBspDir(file: VirtualFile): VirtualFile? =
    if (!file.isDirectory) null else {
      if (FileUtil.namesEqual(file.name, BspConstants.BSP_DIR)) file else file.findChild(BspConstants.BSP_DIR)
    }

  override fun canOpenProject(file: VirtualFile): Boolean {
    val bspDir = getBspDir(file) ?: return false
    return bspDir.children.any { it.extension == BspConstants.SERVER_CONFIG_EXTENSION }
  }

  override fun doOpenProject(
    virtualFile: VirtualFile,
    projectToClose: Project?,
    forceOpenInNewFrame: Boolean
  ): Project? {
    val bspDir = getBspDir(virtualFile) ?: return null
    val baseDir = bspDir.parent ?: return null

    return PlatformProjectOpenProcessor.getInstance().doOpenProject(baseDir, projectToClose, forceOpenInNewFrame)
  }
}

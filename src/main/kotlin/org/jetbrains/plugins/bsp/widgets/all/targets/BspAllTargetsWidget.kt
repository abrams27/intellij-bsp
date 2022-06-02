package org.jetbrains.plugins.bsp.widgets.all.targets

import ch.epfl.scala.bsp4j.BuildTarget
import ch.epfl.scala.bsp4j.BuildTargetIdentifier
import com.intellij.codeInsight.hints.presentation.MouseButton
import com.intellij.codeInsight.hints.presentation.mouseButton
import com.intellij.ide.DataManager
import com.intellij.openapi.actionSystem.ActionGroup
import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.actionSystem.DefaultActionGroup
import com.intellij.openapi.project.Project
import com.intellij.openapi.ui.popup.JBPopupFactory
import com.intellij.openapi.wm.ToolWindow
import com.intellij.openapi.wm.ToolWindowFactory
import com.intellij.ui.awt.RelativePoint
import com.intellij.ui.components.JBLabel
import com.intellij.ui.components.JBList
import com.intellij.ui.content.Content
import com.intellij.ui.content.impl.ContentImpl
import org.jetbrains.magicmetamodel.MagicMetaModel
import org.jetbrains.plugins.bsp.config.BspPluginIcons
import org.jetbrains.plugins.bsp.services.MagicMetaModelService
import java.awt.event.MouseEvent
import java.awt.event.MouseListener
import javax.swing.DefaultListModel
import javax.swing.ListSelectionModel
import javax.swing.SwingConstants

private class LoadTargetAction(
  message: String,
  private val target: BuildTargetIdentifier,
  private val magicMetaModel: MagicMetaModel
) : AnAction(message) {

  override fun actionPerformed(e: AnActionEvent) {
    magicMetaModel.loadTarget(target)
  }
}

private class TargetsListMouseListener(
  private val jbList: JBList<BuildTarget>,
  private val magicMetaModel: MagicMetaModel,
  private val xd: DefaultListModel<BuildTarget>,
) : MouseListener {

  override fun mouseClicked(e: MouseEvent?) = mouseClickedNotNull(e!!)

  private fun mouseClickedNotNull(mouseEvent: MouseEvent) {
    updateSelectedIndex(mouseEvent)

    showPopupIfRightButtonClicked(mouseEvent)
  }

  private fun updateSelectedIndex(mouseEvent: MouseEvent) {
    jbList.selectedIndex = jbList.locationToIndex(mouseEvent.point)
  }

  private fun showPopupIfRightButtonClicked(mouseEvent: MouseEvent) {
    if (mouseEvent.mouseButton == MouseButton.Right) {
      showPopup(mouseEvent)

      xd.remove(jbList.selectedIndex)
    }
  }

  private fun showPopup(mouseEvent: MouseEvent) {
    val actionGroup = calculatePopupGroup()
    val context = DataManager.getInstance().getDataContext(mouseEvent.component)
    val mnemonics = JBPopupFactory.ActionSelectionAid.MNEMONICS

    JBPopupFactory.getInstance()
      .createActionGroupPopup("dadas", actionGroup, context, mnemonics, true)
      .showInBestPositionFor(context)
  }

  private fun calculatePopupGroup(): ActionGroup {
    val group = DefaultActionGroup()

    val target = jbList.selectedValue.id
    group.addAction(LoadTargetAction("loade taf", target, magicMetaModel))

    return group
  }

  override fun mousePressed(e: MouseEvent?) {}

  override fun mouseReleased(e: MouseEvent?) {}

  override fun mouseEntered(e: MouseEvent?) {}

  override fun mouseExited(e: MouseEvent?) {}
}

class BspAllTargetsWidgetFactory : ToolWindowFactory {

  override fun shouldBeAvailable(project: Project): Boolean =
    // TODO: true if BSP project
    true

  override fun createToolWindowContent(project: Project, toolWindow: ToolWindow) {
    val magicMetaModel = MagicMetaModelService.getInstance(project).magicMetaModel

    toolWindow.title = BspAllTargetsWidgetBundle.message("widget.title")

    val loadedTargetsTab = createTargetsTab(
      magicMetaModel.getAllLoadedTargets(),
      BspAllTargetsWidgetBundle.message("widget.loaded.targets.tab.name"),
      magicMetaModel
    )
    toolWindow.contentManager.addContent(loadedTargetsTab)

    val notLoadedTargetsTab = createTargetsTab(
      magicMetaModel.getAllNotLoadedTargets(),
      BspAllTargetsWidgetBundle.message("widget.not.loaded.targets.tab.name"),
      magicMetaModel
    )
    toolWindow.contentManager.addContent(notLoadedTargetsTab)
  }

  private fun createTargetsTab(targets: List<BuildTarget>, tabName: String, magicMetaModel: MagicMetaModel): Content {
    val a = DefaultListModel<BuildTarget>()
    a.addAll(targets)
    val jbList = JBList(a)
    jbList.selectionMode = ListSelectionModel.SINGLE_SELECTION
    jbList.installCellRenderer { JBLabel(it.displayName ?: it.id.uri, BspPluginIcons.bsp, SwingConstants.LEFT) }
    jbList.addMouseListener(TargetsListMouseListener(jbList, magicMetaModel, a))
    return ContentImpl(jbList, tabName, true)
  }
}

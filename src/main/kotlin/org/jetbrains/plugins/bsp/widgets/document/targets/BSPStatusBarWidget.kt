package org.jetbrains.plugins.bsp.widgets.document.targets

import com.intellij.openapi.actionSystem.ActionGroup
import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.actionSystem.DataContext
import com.intellij.openapi.actionSystem.DefaultActionGroup
import com.intellij.openapi.project.Project
import com.intellij.openapi.ui.popup.JBPopupFactory
import com.intellij.openapi.ui.popup.ListPopup
import com.intellij.openapi.util.Disposer
import com.intellij.openapi.util.IconLoader
import com.intellij.openapi.vfs.VirtualFile
import com.intellij.openapi.wm.StatusBar
import com.intellij.openapi.wm.StatusBarWidget
import com.intellij.openapi.wm.StatusBarWidgetFactory
import com.intellij.openapi.wm.impl.status.EditorBasedStatusBarPopup

// move it
private const val ID = "BSPTargets"

private class BSPDocumentTargetsWidget(project: Project) : EditorBasedStatusBarPopup(project, false) {

  override fun ID(): String = ID

  override fun getWidgetState(file: VirtualFile?): WidgetState =
    if (file == null) getInactiveWidgetState() else getActiveWidgetState(file)

  private fun getInactiveWidgetState(): WidgetState {
    val state = WidgetState(BspDocumentTargetsWidgetBundle.message("widget.tooltip.text.inactive"), "", false)
    state.icon = IconLoader.getIcon("icons/buildServerProtocol.svg")

    return state
  }

  private fun getActiveWidgetState(file: VirtualFile): WidgetState {
    println(file)
    val state = WidgetState(BspDocumentTargetsWidgetBundle.message("widget.tooltip.text.active"), "", true)
    state.icon = IconLoader.getIcon("icons/buildServerProtocol.svg")

    return state
  }

  override fun createPopup(context: DataContext): ListPopup {
    val group = calculatePopupGroup()
    val mnemonics = JBPopupFactory.ActionSelectionAid.MNEMONICS
    val title = BspDocumentTargetsWidgetBundle.message("widget.title")

    return JBPopupFactory.getInstance().createActionGroupPopup(title, group, context, mnemonics, true)
  }

  private fun calculatePopupGroup(): ActionGroup {
    val group = DefaultActionGroup()
    group.addSeparator(BspDocumentTargetsWidgetBundle.message("widget.loaded.target.separator.title"))
    group.addAction(Action("Active target"))
    group.addSeparator(BspDocumentTargetsWidgetBundle.message("widget.available.targets.to.load"))
    group.addAll(Action("Target1"), Action("Target2"))

    return group
  }

  override fun createInstance(project: Project): StatusBarWidget =
    BSPDocumentTargetsWidget(project)

  private class Action(val name: String) : AnAction(name) {
    override fun actionPerformed(e: AnActionEvent) {
      // TODO
    }
  }
}

class BSPStatusBarWidgetFactory : StatusBarWidgetFactory {

  override fun getId(): String = ID

  override fun getDisplayName(): String =
    BspDocumentTargetsWidgetBundle.message("widget.factory.display.name")

  // TODO
  override fun isAvailable(project: Project): Boolean =
    true

  override fun createWidget(project: Project): StatusBarWidget =
    BSPDocumentTargetsWidget(project)

  override fun disposeWidget(widget: StatusBarWidget) =
    Disposer.dispose(widget)

  // TODO
  override fun canBeEnabledOn(statusBar: StatusBar): Boolean =
    true
}

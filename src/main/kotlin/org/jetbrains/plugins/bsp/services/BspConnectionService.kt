package org.jetbrains.plugins.bsp.services

import ch.epfl.scala.bsp4j.BspConnectionDetails
import ch.epfl.scala.bsp4j.BuildClient
import ch.epfl.scala.bsp4j.BuildClientCapabilities
import ch.epfl.scala.bsp4j.BuildServer
import ch.epfl.scala.bsp4j.BuildTarget
import ch.epfl.scala.bsp4j.DependencySourcesParams
import ch.epfl.scala.bsp4j.DidChangeBuildTarget
import ch.epfl.scala.bsp4j.InitializeBuildParams
import ch.epfl.scala.bsp4j.LogMessageParams
import ch.epfl.scala.bsp4j.PublishDiagnosticsParams
import ch.epfl.scala.bsp4j.ResourcesParams
import ch.epfl.scala.bsp4j.ShowMessageParams
import ch.epfl.scala.bsp4j.SourcesParams
import ch.epfl.scala.bsp4j.TaskFinishParams
import ch.epfl.scala.bsp4j.TaskProgressParams
import ch.epfl.scala.bsp4j.TaskStartParams
import com.google.gson.JsonArray
import com.google.gson.JsonObject
import com.intellij.build.AbstractViewManager
import com.intellij.build.DefaultBuildDescriptor
import com.intellij.build.SyncViewManager
import com.intellij.build.events.MessageEvent
import com.intellij.build.events.impl.BuildIssueEventImpl
import com.intellij.build.events.impl.FinishBuildEventImpl
import com.intellij.build.events.impl.MessageEventImpl
import com.intellij.build.events.impl.OutputBuildEventImpl
import com.intellij.build.events.impl.ProgressBuildEventImpl
import com.intellij.build.events.impl.StartBuildEventImpl
import com.intellij.build.events.impl.SuccessResultImpl
import com.intellij.build.issue.BuildIssue
import com.intellij.build.issue.BuildIssueQuickFix
import com.intellij.icons.AllIcons
import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.project.Project
import com.intellij.pom.Navigatable
import com.intellij.project.stateStore
import com.intellij.util.concurrency.AppExecutorUtil
import org.eclipse.lsp4j.jsonrpc.Launcher
import org.jetbrains.magicmetamodel.ProjectDetails
import org.jetbrains.protocol.connection.LocatedBspConnectionDetails
import java.nio.file.Path

public interface BspServer : BuildServer

public class BspConnectionService(private val project: Project) {

  public var server: BspServer? = null

  public fun connect(connectionFile: LocatedBspConnectionDetails) {
    val process = createAndStartProcess(connectionFile.bspConnectionDetails)
    val buildView = project.getService(SyncViewManager::class.java)

    val client = BspClient(buildView, "xd2", "xd")

    val launcher = createLauncher(process, client)

    launcher.startListening()
    server = launcher.remoteProxy
    client.onConnectWithServer(server)

  }

  private fun createAndStartProcess(bspConnectionDetails: BspConnectionDetails): Process =
    ProcessBuilder(bspConnectionDetails.argv)
      .directory(project.stateStore.projectBasePath.toFile())
      .start()

  private fun createLauncher(process: Process, client: BuildClient): Launcher<BspServer> =
    Launcher.Builder<BspServer>()
      .setRemoteInterface(BspServer::class.java)
      .setExecutorService(AppExecutorUtil.getAppExecutorService())
      .setInput(process.inputStream)
      .setOutput(process.outputStream)
      .setLocalService(client)
      .create()

  public fun disconnect() {
  }

  public companion object {
    public fun getInstance(project: Project): BspConnectionService =
      project.getService(BspConnectionService::class.java)
  }
}


public class VeryTemporaryBspResolver(
  private val projectBaseDir: Path,
  private val server: BspServer,
  private val project: Project,
  private val buildViewManager: AbstractViewManager
) {

  public fun collectModel(): ProjectDetails {
    val buildId = "xd"
    val title = "Title 2"
    val basePath = project.basePath!!
    val restartAction: AnAction = object : AnAction() {
      override fun update(e: AnActionEvent) {
        e.presentation.isEnabled = true
        e.presentation.icon = AllIcons.Actions.Refresh
      }

      override fun actionPerformed(e: AnActionEvent) {
      }
    }

    val buildDescriptor = DefaultBuildDescriptor(buildId, title, basePath, System.currentTimeMillis())
      .withRestartActions(restartAction)

    val startEvent = StartBuildEventImpl(buildDescriptor, "message")
    buildViewManager.onEvent(buildId, startEvent)

    val progressEvent = ProgressBuildEventImpl(
      "xd2", buildId, System.currentTimeMillis(), "Tmport", -1,
      -1, "kłykcie"
    )
    buildViewManager.onEvent(buildId, progressEvent)

    println("buildInitialize")
    server.buildInitialize(createInitializeBuildParams()).get()

    println("onBuildInitialized")
    server.onBuildInitialized()

    println("workspaceBuildTargets")
    val workspaceBuildTargetsResult = server.workspaceBuildTargets().get()
    val allTargetsIds = workspaceBuildTargetsResult!!.targets.map(BuildTarget::getId)

    println("buildTargetSources")
    val sourcesResult = server.buildTargetSources(SourcesParams(allTargetsIds)).get()

    println("buildTargetResources")
    val resourcesResult = server.buildTargetResources(ResourcesParams(allTargetsIds)).get()

    println("buildTargetDependencySources")
    val dependencySourcesResult = server.buildTargetDependencySources(DependencySourcesParams(allTargetsIds)).get()

    println("done!")
    val xd = FinishBuildEventImpl(
      "xd2", null, System.currentTimeMillis(), "Tmport 2", SuccessResultImpl()
    )
    buildViewManager.onEvent(buildId, xd)
    println("done!")
    val xd2 = FinishBuildEventImpl(
      buildId, null, System.currentTimeMillis(), "Tmport 233", SuccessResultImpl()
    )
    buildViewManager.onEvent(buildId, xd2)


    println("done done!")
    return ProjectDetails(
      targetsId = allTargetsIds!!,
      targets = workspaceBuildTargetsResult!!.targets.toSet(),
      sources = sourcesResult!!.items,
      resources = resourcesResult!!.items,
      dependenciesSources = dependencySourcesResult!!.items,
    )
  }

  private fun createInitializeBuildParams(): InitializeBuildParams {
    val params = InitializeBuildParams(
      "IntelliJ-BSP",
      "1.0.0",
      "2.0.0",
      projectBaseDir.toString(),
      BuildClientCapabilities(listOf("java"))
    )
    val dataJson = JsonObject()
    dataJson.addProperty("clientClassesRootDir", "$projectBaseDir/out")
    dataJson.add("supportedScalaVersions", JsonArray())
    params.data = dataJson

    return params
  }
}

private class BspClient(
  private val buildViewManager: AbstractViewManager,
  private val randomId: String,
  private val buildId: String
) : BuildClient {
  override fun onBuildShowMessage(params: ShowMessageParams?) {
    println("onBuildShowMessage")
    println(params)

//    val event = MessageEventImpl(randomId, MessageEvent.Kind.SIMPLE, null, "", params?.message)
//    val event = OutputBuildEventImpl(randomId, "XDXD", true)
    val event1 = OutputBuildEventImpl(buildId, "XDXD", true)
//    buildViewManager.onEvent(buildId, event)
    buildViewManager.onEvent(buildId, event1)
    val x = MessageEventImpl(buildId, MessageEvent.Kind.WARNING, "xd", "LOL",
      "LOLOLO")
    buildViewManager.onEvent(buildId, x)
  }

  override fun onBuildLogMessage(params: LogMessageParams?) {
    println("onBuildLogMessage")
    println(params)

//    val event = MessageEventImpl(randomId, MessageEvent.Kind.SIMPLE, null, "", params?.message)
    val event = OutputBuildEventImpl(randomId, "XDXD22", true)
    val event1 = OutputBuildEventImpl(buildId, params?.message!!, true)
//    buildViewManager.onEvent(buildId, event)
    buildViewManager.onEvent(buildId, event1)
//    val x = MessageEventImpl(buildId, MessageEvent.Kind.WARNING, "xd", "LOL",
//      "LOLOLO")
//    buildViewManager.onEvent(buildId, x)
//    val xx = FileMessageEventImpl(buildId, MessageEvent.Kind.ERROR, "XD", "RRSARE", "DASDASDASADS", FilePosition(File("~/dev/test/bazel-bsp/WORKSPACE"), 12, 2))
//    buildViewManager.onEvent(buildId, xx)

    val buildIssue = object : BuildIssue {
      override val description: String
        get() = "XDX"
      override val quickFixes: List<BuildIssueQuickFix>
        get() = emptyList()
      override val title: String
        get() = "RR"

      override fun getNavigatable(project: Project): Navigatable? {
       return null
      }
    }
    buildViewManager.onEvent(buildId, BuildIssueEventImpl(buildId, buildIssue, MessageEvent.Kind.ERROR))
  }

  override fun onBuildTaskStart(params: TaskStartParams?) {
    println("onBuildTaskStart")
    println(params)
  }

  override fun onBuildTaskProgress(params: TaskProgressParams?) {
    println("onBuildTaskProgress")
    println(params)
  }

  override fun onBuildTaskFinish(params: TaskFinishParams?) {
    println("onBuildTaskFinish")
    println(params)
  }

  override fun onBuildPublishDiagnostics(params: PublishDiagnosticsParams?) {
    println("onBuildPublishDiagnostics")
    println(params)
  }

  override fun onBuildTargetDidChange(params: DidChangeBuildTarget?) {
    println("onBuildTargetDidChange")
    println(params)
  }
}

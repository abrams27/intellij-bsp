package org.jetbrains.plugins.bsp.services

import ch.epfl.scala.bsp4j.BspConnectionDetails
import ch.epfl.scala.bsp4j.BuildClient
import ch.epfl.scala.bsp4j.BuildServer
import ch.epfl.scala.bsp4j.DidChangeBuildTarget
import ch.epfl.scala.bsp4j.LogMessageParams
import ch.epfl.scala.bsp4j.PublishDiagnosticsParams
import ch.epfl.scala.bsp4j.ShowMessageParams
import ch.epfl.scala.bsp4j.TaskFinishParams
import ch.epfl.scala.bsp4j.TaskProgressParams
import ch.epfl.scala.bsp4j.TaskStartParams
import com.intellij.openapi.project.Project
import com.intellij.project.stateStore
import com.intellij.util.concurrency.AppExecutorUtil
import org.eclipse.lsp4j.jsonrpc.Launcher
import org.jetbrains.protocol.connection.LocatedBspConnectionDetails

public interface BspServer : BuildServer

public class BspConnectionService(private val project: Project) {

  public var server: BspServer? = null

  public fun connect(connectionFile: LocatedBspConnectionDetails) {
    val process = createAndStartProcess(connectionFile.bspConnectionDetails)
    // TODO
    val client = BspClient()

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

private class BspClient : BuildClient {
  override fun onBuildShowMessage(params: ShowMessageParams?) {
    println("onBuildShowMessage")
    println(params)
  }

  override fun onBuildLogMessage(params: LogMessageParams?) {
    println("onBuildLogMessage")
    println(params)
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

package org.jetbrains.plugins.bsp.services

import com.intellij.build.BuildViewManager
import com.intellij.build.DefaultBuildDescriptor
import com.intellij.build.events.impl.ProgressBuildEventImpl
import com.intellij.build.events.impl.StartBuildEventImpl
import com.intellij.openapi.application.ModalityState
import com.intellij.openapi.application.invokeLater
import com.intellij.openapi.application.runInEdt
import com.intellij.openapi.progress.ProgressIndicator
import com.intellij.openapi.progress.ProgressIndicatorProvider
import com.intellij.openapi.progress.ProgressManager
import com.intellij.openapi.progress.Task
import com.intellij.openapi.project.Project
import kotlinx.coroutines.runBlocking
import org.jetbrains.concurrency.runAsync


public class BuildWindowSomething(project: Project) {
  init {
    val buildView = project.getService(BuildViewManager::class.java)
    val buildId = "buildId"
    val title = "title"
    val basePath = project.basePath!!
    val buildDescriptor = DefaultBuildDescriptor(buildId, title, basePath, System.currentTimeMillis())
    val startEvent = StartBuildEventImpl(buildDescriptor, "message")
    buildView.onEvent(buildId, startEvent)

    val task = object : Task.Backgroundable(project, "Loading changes", false) {
      override fun run(indicator: ProgressIndicator) {
        indicator.text = "Loading changes";
        indicator.isIndeterminate = false;
        indicator.fraction = 0.0;
        for (i in 1..10) {
          Thread.sleep(10000)
          indicator.fraction = i / 10.0
          val progressEvent = ProgressBuildEventImpl(
            "nowe id", buildId, System.currentTimeMillis(), "message$i", 10,
            i.toLong(), "kłykcie"
          )

          buildView.onEvent(buildId, progressEvent)
        }
        indicator.fraction = 1.0;
      }
    }

    ProgressManager.getInstance().run(task)
  }
}
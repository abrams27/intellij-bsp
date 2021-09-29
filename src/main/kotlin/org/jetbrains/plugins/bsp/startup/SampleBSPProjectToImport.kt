package org.jetbrains.plugins.bsp.startup

import ch.epfl.scala.bsp4j.BuildTarget
import ch.epfl.scala.bsp4j.BuildTargetCapabilities
import ch.epfl.scala.bsp4j.BuildTargetIdentifier
import ch.epfl.scala.bsp4j.JvmBuildTarget
import ch.epfl.scala.bsp4j.SourceItem
import ch.epfl.scala.bsp4j.SourceItemKind
import ch.epfl.scala.bsp4j.SourcesItem

object SampleBSPProjectToImport {

  val appId = BuildTargetIdentifier("//app/src/bsp/app:App")
  val appDisplayName = "//app/src/bsp/app:App"
  val appBaseDirectory = "file:///Users/marcin.abramowicz/Projects/bsp-sample/app/src/bsp/app/"
  val appTags = listOf("application")
  val appLanguageIds = listOf("java")
  val appDependencies = listOf(
    BuildTargetIdentifier("@maven//:com_google_guava_guava"),
    BuildTargetIdentifier("//libA/src/bsp/libA:libA"),
    BuildTargetIdentifier("//libB:libB"),
  )
  val appCapabilities = BuildTargetCapabilities(true, false, true, false)
  val appDataKind = "jvm"
  val appData = JvmBuildTarget(
    "file:///private/var/tmp/_bazel_marcin.abramowicz/a2afba1d0e52eedc0574abc5f4ddea23/external/local_jdk/",
    "8"
  )

  val appTarget = BuildTarget(appId, appTags, appLanguageIds, appDependencies, appCapabilities)

  init {
    appTarget.displayName = appDisplayName
    appTarget.baseDirectory = appBaseDirectory
    appTarget.dataKind = appDataKind
    appTarget.data = appData
  }


  val libBId = BuildTargetIdentifier("//libB:libB")
  val libBDisplayName = "//libB:libB"
  val libBBaseDirectory = "file:///Users/marcin.abramowicz/Projects/bsp-sample/libB/"
  val libBTags = listOf<String>()
  val libBLanguageIds = listOf<String>()
  val libBDependencies = listOf(
    BuildTargetIdentifier("//libB/src/bsp/libB:libB")
  )
  val libBCapabilities = BuildTargetCapabilities(true, false, false, false)

  val libBTarget = BuildTarget(libBId, libBTags, libBLanguageIds, libBDependencies, libBCapabilities)

  init {
    libBTarget.displayName = libBDisplayName
    libBTarget.baseDirectory = libBBaseDirectory
  }


  val libAid = BuildTargetIdentifier("//libA/src/bsp/libA:libA")
  val libADisplayName = "//libA/src/bsp/libA:libA"
  val libABaseDirectory = "file:///Users/marcin.abramowicz/Projects/bsp-sample/libA/src/bsp/libA/"
  val libATags = listOf("library")
  val libALanguageIds = listOf("java")
  val libADependencies = listOf(
    BuildTargetIdentifier("@maven//:com_google_guava_guava"),
    BuildTargetIdentifier("@maven//:io_vavr_vavr"),
    BuildTargetIdentifier("//libB:libB"),
  )
  val libACapabilities = BuildTargetCapabilities(true, false, false, false)
  val libADataKind = "jvm"
  val libAData = JvmBuildTarget(
    "file:///private/var/tmp/_bazel_marcin.abramowicz/a2afba1d0e52eedc0574abc5f4ddea23/external/local_jdk/",
    "8"
  )

  val libATarget = BuildTarget(libAid, libATags, libBLanguageIds, libADependencies, libACapabilities)

  init {
    libATarget.displayName = libADisplayName
    libATarget.baseDirectory = libABaseDirectory
    libATarget.dataKind = libADataKind
    libATarget.data = libAData
  }


  val libBBId = BuildTargetIdentifier("//libB/src/bsp/libB:libB")
  val libBBDisplayName = "//libB/src/bsp/libB:libB"
  val libBBBaseDirectory = "file:///Users/marcin.abramowicz/Projects/bsp-sample/libB/src/bsp/libB/"
  val libBBTags = listOf("library")
  val libBBLanguageIds = listOf("java")
  val libBBDependencies = listOf(
    BuildTargetIdentifier("@maven//:com_google_guava_guava"),
  )
  val libBBCapabilities = BuildTargetCapabilities(true, false, false, false)
  val libBBDataKind = "jvm"
  val libBBData = JvmBuildTarget(
    "file:///private/var/tmp/_bazel_marcin.abramowicz/a2afba1d0e52eedc0574abc5f4ddea23/external/local_jdk/",
    "8"
  )

  val libBBTarget = BuildTarget(libBBId, libBBTags, libBBLanguageIds, libBBDependencies, libBBCapabilities)

  init {
    libBBTarget.displayName = libBBDisplayName
    libBBTarget.baseDirectory = libBBBaseDirectory
    libBBTarget.dataKind = libBBDataKind
    libBBTarget.data = libBBData
  }


  val appSourcesList = listOf(
    SourceItem(
      "file:///Users/marcin.abramowicz/Projects/bsp-sample/app/src/bsp/app/App.java",
      SourceItemKind.FILE,
      false
    )
  )
  val appRoots = listOf("file:///Users/marcin.abramowicz/Projects/bsp-sample/app/src/")

  val appSources = SourcesItem(appId, appSourcesList)

  init {
    appSources.roots = appRoots
  }


  val libBSourcesList = listOf<SourceItem>()
  val libBRoots = listOf("file:///Users/marcin.abramowicz/Projects/bsp-sample/")

  val libBSources = SourcesItem(libBId, libBSourcesList)

  init {
    libBSources.roots = libBRoots
  }


  val libASourcesList = listOf(
    SourceItem(
      "file:///Users/marcin.abramowicz/Projects/bsp-sample/libA/src/bsp/libA/LibA1.java",
      SourceItemKind.FILE,
      false
    )
  )
  val libARoots = listOf("file:///Users/marcin.abramowicz/Projects/bsp-sample/libA/src/")

  val libASources = SourcesItem(libAid, libASourcesList)

  init {
    libASources.roots = libARoots
  }


  val libBBSourcesList = listOf(
    SourceItem(
      "file:///Users/marcin.abramowicz/Projects/bsp-sample/libB/src/bsp/libB/LibB1.java",
      SourceItemKind.FILE,
      false
    ),
    SourceItem(
      "file:///Users/marcin.abramowicz/Projects/bsp-sample/libB/src/bsp/libB/LibB2.java",
      SourceItemKind.FILE, false
    )
  )
  val libBBRoots = listOf("file:///Users/marcin.abramowicz/Projects/bsp-sample/libB/src/")

  val libBBSources = SourcesItem(libBBId, libBBSourcesList)

  init {
    libBBSources.roots = libBBRoots
  }


  val targets = listOf(appTarget, libBBTarget, libBTarget, libATarget)
  val sources = listOf(libBBSources, libASources, appSources, libBSources)
}
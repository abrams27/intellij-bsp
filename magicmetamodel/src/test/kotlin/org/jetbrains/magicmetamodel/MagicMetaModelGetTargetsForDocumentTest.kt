package org.jetbrains.magicmetamodel

import ch.epfl.scala.bsp4j.*
import io.kotest.matchers.collections.shouldContainExactlyInAnyOrder
import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.Test

class MagicMetaModelGetTargetsForDocumentTest {

  @Test
  fun `should return empty map for empty sources`() {
    // given
    val sources = emptyList<SourcesItem>()

    val notExistingDocumentUri = "file:///not/existing/file"

    // when
    val magicMetaModel = MagicMetaModel(sources)

    val notExistingDocumentId = TextDocumentIdentifier(notExistingDocumentUri)

    val notExistingDocumentTargets = magicMetaModel.getTargetsForDocument(notExistingDocumentId)

    // then
    notExistingDocumentTargets shouldBe emptyList()
  }

  @Test
  fun `should map multiple files to single target`() {
    // given
    val singleTargetDocument1Uri = "file:///file1/with/single/target"
    val singleTargetDocument2Uri = "file:///file2/with/single/target"

    val target = BuildTargetIdentifier("//target")

    val singleTargetFile1 = SourceItem(singleTargetDocument1Uri, SourceItemKind.FILE, false)
    val singleTargetFile2 = SourceItem(singleTargetDocument2Uri, SourceItemKind.FILE, false)
    val targetSourceItem = SourcesItem(target, listOf(singleTargetFile1, singleTargetFile2))

    val sources = listOf(targetSourceItem)

    // when
    val magicMetaModel = MagicMetaModel(sources)

    val singleTargetDocument1Id = TextDocumentIdentifier(singleTargetDocument1Uri)
    val singleTargetDocument2Id = TextDocumentIdentifier(singleTargetDocument2Uri)

    val singleTargetDocument1Targets = magicMetaModel.getTargetsForDocument(singleTargetDocument1Id)
    val singleTargetDocument2Targets = magicMetaModel.getTargetsForDocument(singleTargetDocument2Id)

    // then
    val expectedTargets = listOf(target)

    singleTargetDocument1Targets shouldBe expectedTargets
    singleTargetDocument2Targets shouldBe expectedTargets
  }

  @Test
  fun `should map one file to multiple targets`() {
    // given
    val multipleTargetsDocumentUri = "file:///file/with/multiple/targets"

    val target1 = BuildTargetIdentifier("//target1")
    val target2 = BuildTargetIdentifier("//target2")

    val multipleTargetsFile = SourceItem(multipleTargetsDocumentUri, SourceItemKind.FILE, false)
    val target1SourceItem = SourcesItem(target1, listOf(multipleTargetsFile))
    val target2SourceItem = SourcesItem(target2, listOf(multipleTargetsFile))

    val sources = listOf(target1SourceItem, target2SourceItem)

    // when
    val magicMetaModel = MagicMetaModel(sources)

    val multipleTargetsDocumentId = TextDocumentIdentifier(multipleTargetsDocumentUri)

    val multipleTargetsDocumentTargets = magicMetaModel.getTargetsForDocument(multipleTargetsDocumentId)

    // then
    val expectedTargets = listOf(target1, target2)

    multipleTargetsDocumentTargets shouldContainExactlyInAnyOrder expectedTargets
  }

  @Test
  fun `should map multiple files to multiple targets`() {
    // given
    val multipleTargetsDocument1Uri = "file:///file1/with/multiple/targets"
    val multipleTargetsDocument2Uri = "file:///file2/with/multiple/targets"

    val target1 = BuildTargetIdentifier("//target1")
    val target2 = BuildTargetIdentifier("//target2")
    val target3 = BuildTargetIdentifier("//target3")

    val multipleTargetsFile1 = SourceItem(multipleTargetsDocument1Uri, SourceItemKind.FILE, false)
    val multipleTargetsFile2 = SourceItem(multipleTargetsDocument2Uri, SourceItemKind.FILE, false)
    val target1SourceItem = SourcesItem(target1, listOf(multipleTargetsFile1, multipleTargetsFile2))
    val target2SourceItem = SourcesItem(target2, listOf(multipleTargetsFile1))
    val target3SourceItem = SourcesItem(target3, listOf(multipleTargetsFile2))

    val sources = listOf(target1SourceItem, target2SourceItem, target3SourceItem)

    // when
    val magicMetaModel = MagicMetaModel(sources)

    val multipleTargetsDocument1Id = TextDocumentIdentifier(multipleTargetsDocument1Uri)
    val multipleTargetsDocument2Id = TextDocumentIdentifier(multipleTargetsDocument2Uri)

    val document1Targets = magicMetaModel.getTargetsForDocument(multipleTargetsDocument1Id)
    val document2Targets = magicMetaModel.getTargetsForDocument(multipleTargetsDocument2Id)

    // then
    val expectedDocument1Targets = listOf(target1, target2)
    val expectedDocument2Targets = listOf(target1, target3)

    document1Targets shouldContainExactlyInAnyOrder expectedDocument1Targets
    document2Targets shouldContainExactlyInAnyOrder expectedDocument2Targets
  }

  @Test
  fun `should map multiple files in single directory to single target`() {
    // given
    val commonDirectoryUri = "file:///common/directory"
    val commonDirectoryDocument1Uri = "$commonDirectoryUri/file1/with/single/target"
    val commonDirectoryDocument2Uri = "$commonDirectoryUri/file2/with/single/target"

    val target = BuildTargetIdentifier("//target")

    val targetSourceDirectory = SourceItem(commonDirectoryUri, SourceItemKind.DIRECTORY, false)
    val targetSourceItem = SourcesItem(target, listOf(targetSourceDirectory))

    val sources = listOf(targetSourceItem)

    // when
    val magicMetaModel = MagicMetaModel(sources)

    val commonDirectoryDocument1Id = TextDocumentIdentifier(commonDirectoryDocument1Uri)
    val commonDirectoryDocument2Id = TextDocumentIdentifier(commonDirectoryDocument2Uri)

    val commonDirectoryDocument1Targets = magicMetaModel.getTargetsForDocument(commonDirectoryDocument1Id)
    val commonDirectoryDocument2Targets = magicMetaModel.getTargetsForDocument(commonDirectoryDocument2Id)

    // then
    val expectedTargets = listOf(target)

    commonDirectoryDocument1Targets shouldBe expectedTargets
    commonDirectoryDocument2Targets shouldBe expectedTargets
  }

  @Test
  fun `should map multiple files in multiple directories to multiple targets`() {
    // given
    val commonDirectoryUri = "file:///common/directory"
    val commonDirectoryChildUri = "file:///common/directory/child"
    val commonDirectoryFileInTarget1Uri = "$commonDirectoryUri/file1/in/target1"
    val commonDirectoryChildFileInTarget1AndTarget2Uri = "$commonDirectoryChildUri/file2/in/target1/and/target2"

    val target1 = BuildTargetIdentifier("//target1")
    val target2 = BuildTargetIdentifier("//target2")

    val commonDirectorySourceDirectory = SourceItem(commonDirectoryUri, SourceItemKind.DIRECTORY, false)
    val commonDirectoryChildSourceDirectory = SourceItem(commonDirectoryChildUri, SourceItemKind.DIRECTORY, false)
    val target1SourceItem = SourcesItem(target1, listOf(commonDirectorySourceDirectory))
    val target2SourceItem = SourcesItem(target2, listOf(commonDirectoryChildSourceDirectory))

    val sources = listOf(target1SourceItem, target2SourceItem)

    // when
    val magicMetaModel = MagicMetaModel(sources)

    val commonDirectoryFileInTarget1Id = TextDocumentIdentifier(commonDirectoryFileInTarget1Uri)
    val commonDirectoryChildFileInTarget1AndTarget2Id =
      TextDocumentIdentifier(commonDirectoryChildFileInTarget1AndTarget2Uri)

    val commonDirectoryFileInTarget1Targets = magicMetaModel.getTargetsForDocument(commonDirectoryFileInTarget1Id)
    val commonDirectoryChildFileInTarget1AndTarget2Targets =
      magicMetaModel.getTargetsForDocument(commonDirectoryChildFileInTarget1AndTarget2Id)

    // then
    val expectedCommonDirectoryFileInTarget1Targets = listOf(target1)
    val expectedCommonDirectoryChildFileInTarget1AndTarget2Targets = listOf(target1, target2)

    commonDirectoryFileInTarget1Targets shouldContainExactlyInAnyOrder expectedCommonDirectoryFileInTarget1Targets
    commonDirectoryChildFileInTarget1AndTarget2Targets shouldContainExactlyInAnyOrder expectedCommonDirectoryChildFileInTarget1AndTarget2Targets
  }

  @Test
  fun `should map multiple files in multiple directories to multiple targets including file based targets`() {
    // given
    val commonDirectoryUri = "file:///common/directory"
    val commonDirectoryChildUri = "file:///common/directory/child"
    val commonDirectoryFileInTarget1Uri = "$commonDirectoryUri/file1/in/target1"
    val commonDirectoryChildFileInTarget1Target2Target3Uri = "$commonDirectoryChildUri/file2/in/target1/target2/target3"

    val target1 = BuildTargetIdentifier("//target1")
    val target2 = BuildTargetIdentifier("//target2")
    val target3 = BuildTargetIdentifier("//target3")

    val commonDirectorySourceDirectory = SourceItem(commonDirectoryUri, SourceItemKind.DIRECTORY, false)
    val commonDirectoryChildSourceDirectory = SourceItem(commonDirectoryChildUri, SourceItemKind.DIRECTORY, false)
    val commonDirectoryChildFileInTarget1Target2Target3Source =
      SourceItem(commonDirectoryChildFileInTarget1Target2Target3Uri, SourceItemKind.FILE, false)
    val target1SourceItem = SourcesItem(target1, listOf(commonDirectorySourceDirectory))
    val target2SourceItem = SourcesItem(target2, listOf(commonDirectoryChildSourceDirectory))
    val target3SourceItem = SourcesItem(target3, listOf(commonDirectoryChildFileInTarget1Target2Target3Source))

    val sources = listOf(target1SourceItem, target2SourceItem, target3SourceItem)

    // when
    val magicMetaModel = MagicMetaModel(sources)

    val commonDirectoryFileInTarget1Id = TextDocumentIdentifier(commonDirectoryFileInTarget1Uri)
    val commonDirectoryChildFileInTarget1Target2Target3Id =
      TextDocumentIdentifier(commonDirectoryChildFileInTarget1Target2Target3Uri)

    val commonDirectoryFileInTarget1Targets = magicMetaModel.getTargetsForDocument(commonDirectoryFileInTarget1Id)
    val commonDirectoryChildFileInTarget1AndTarget2Targets =
      magicMetaModel.getTargetsForDocument(commonDirectoryChildFileInTarget1Target2Target3Id)

    // then
    val expectedCommonDirectoryFileInTarget1Targets = listOf(target1)
    val expectedCommonDirectoryChildFileInTarget1AndTarget2Targets = listOf(target1, target2, target3)

    commonDirectoryFileInTarget1Targets shouldContainExactlyInAnyOrder expectedCommonDirectoryFileInTarget1Targets
    commonDirectoryChildFileInTarget1AndTarget2Targets shouldContainExactlyInAnyOrder expectedCommonDirectoryChildFileInTarget1AndTarget2Targets
  }
}
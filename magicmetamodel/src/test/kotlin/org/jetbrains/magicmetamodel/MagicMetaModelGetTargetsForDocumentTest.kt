package org.jetbrains.magicmetamodel

import ch.epfl.scala.bsp4j.*
import io.kotest.matchers.collections.shouldContainExactlyInAnyOrder
import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.Nested
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

  @Nested
  inner class FileBasedSourcesTests {

    @Test
    fun `should map multiple files to single target`() {
      // given
      val file1InTarget1Uri = "file:///file1/in/target1"
      val file2InTarget1Uri = "file:///file2/in/target1"

      val target1 = BuildTargetIdentifier("//target1")

      val file1InTarget1Source = SourceItem(file1InTarget1Uri, SourceItemKind.FILE, false)
      val file2InTarget1Source = SourceItem(file2InTarget1Uri, SourceItemKind.FILE, false)

      val target1Sources = SourcesItem(target1, listOf(file1InTarget1Source, file2InTarget1Source))

      val sources = listOf(target1Sources)

      // when
      val magicMetaModel = MagicMetaModel(sources)

      val file1InTarget1Id = TextDocumentIdentifier(file1InTarget1Uri)
      val file2InTarget1Id = TextDocumentIdentifier(file2InTarget1Uri)

      val file1InTarget1Targets = magicMetaModel.getTargetsForDocument(file1InTarget1Id)
      val file2InTarget1Targets = magicMetaModel.getTargetsForDocument(file2InTarget1Id)

      // then
      file1InTarget1Targets shouldBe listOf(target1)
      file2InTarget1Targets shouldBe listOf(target1)
    }

    @Test
    fun `should map one file to multiple targets`() {
      // given
      val fileInTarget1Target2Uri = "file:///file/in/target1/target2"

      val target1 = BuildTargetIdentifier("//target1")
      val target2 = BuildTargetIdentifier("//target2")

      val fileInTarget1Target2Source = SourceItem(fileInTarget1Target2Uri, SourceItemKind.FILE, false)

      val target1Sources = SourcesItem(target1, listOf(fileInTarget1Target2Source))
      val target2Sources = SourcesItem(target2, listOf(fileInTarget1Target2Source))

      val sources = listOf(target1Sources, target2Sources)

      // when
      val magicMetaModel = MagicMetaModel(sources)

      val fileInTarget1Target2Id = TextDocumentIdentifier(fileInTarget1Target2Uri)

      val fileInTarget1Target2Targets = magicMetaModel.getTargetsForDocument(fileInTarget1Target2Id)

      // then
      fileInTarget1Target2Targets shouldContainExactlyInAnyOrder listOf(target1, target2)
    }

    @Test
    fun `should map multiple files to multiple targets`() {
      // given
      val fileInTarget1Target2Uri = "file:///file/in/target1/target2"
      val fileInTarget1Target3Uri = "file:///file/in/target1/target3"

      val target1 = BuildTargetIdentifier("//target1")
      val target2 = BuildTargetIdentifier("//target2")
      val target3 = BuildTargetIdentifier("//target3")

      val fileInTarget1Target2Source = SourceItem(fileInTarget1Target2Uri, SourceItemKind.FILE, false)
      val fileInTarget1Target3Source = SourceItem(fileInTarget1Target3Uri, SourceItemKind.FILE, false)

      val target1Sources = SourcesItem(target1, listOf(fileInTarget1Target2Source, fileInTarget1Target3Source))
      val target2Sources = SourcesItem(target2, listOf(fileInTarget1Target2Source))
      val target3Sources = SourcesItem(target3, listOf(fileInTarget1Target3Source))

      val sources = listOf(target1Sources, target2Sources, target3Sources)

      // when
      val magicMetaModel = MagicMetaModel(sources)

      val fileInTarget1Target2Id = TextDocumentIdentifier(fileInTarget1Target2Uri)
      val fileInTarget1Target3Id = TextDocumentIdentifier(fileInTarget1Target3Uri)

      val fileInTarget1Target2Targets = magicMetaModel.getTargetsForDocument(fileInTarget1Target2Id)
      val fileInTarget1Target3Targets = magicMetaModel.getTargetsForDocument(fileInTarget1Target3Id)

      // then
      fileInTarget1Target2Targets shouldContainExactlyInAnyOrder listOf(target1, target2)
      fileInTarget1Target3Targets shouldContainExactlyInAnyOrder listOf(target1, target3)
    }
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
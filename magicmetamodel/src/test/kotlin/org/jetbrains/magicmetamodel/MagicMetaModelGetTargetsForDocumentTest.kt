package org.jetbrains.magicmetamodel

import ch.epfl.scala.bsp4j.*
import io.kotest.matchers.collections.shouldContainExactlyInAnyOrder
import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test

class MagicMetaModelGetTargetsForDocumentTest {

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

      val targets = generateMockBuildTargetsForTargetsId(listOf(target1))
      val sources = listOf(target1Sources)

      // when
      val magicMetaModel = MagicMetaModel(targets, sources)

      val file1InTarget1Id = TextDocumentIdentifier(file1InTarget1Uri)
      val file2InTarget1Id = TextDocumentIdentifier(file2InTarget1Uri)

      val file1InTarget1TargetsDetails = magicMetaModel.getTargetsDetailsForDocument(file1InTarget1Id)
      val file2InTarget1TargetsDetails = magicMetaModel.getTargetsDetailsForDocument(file2InTarget1Id)

      // then
      file1InTarget1TargetsDetails.inactiveTargetsIds shouldBe listOf(target1)
      file2InTarget1TargetsDetails.inactiveTargetsIds shouldBe listOf(target1)
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
      val targets = generateMockBuildTargetsForTargetsId(listOf(target1, target2))
      val magicMetaModel = MagicMetaModel(targets, sources)

      val fileInTarget1Target2Id = TextDocumentIdentifier(fileInTarget1Target2Uri)

      val fileInTarget1Target2TargetsDetails = magicMetaModel.getTargetsDetailsForDocument(fileInTarget1Target2Id)

      // then
      fileInTarget1Target2TargetsDetails.inactiveTargetsIds shouldContainExactlyInAnyOrder listOf(target1, target2)
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

      val targets = generateMockBuildTargetsForTargetsId(listOf(target1, target2, target3))
      val sources = listOf(target1Sources, target2Sources, target3Sources)

      // when
      val magicMetaModel = MagicMetaModel(targets, sources)

      val fileInTarget1Target2Id = TextDocumentIdentifier(fileInTarget1Target2Uri)
      val fileInTarget1Target3Id = TextDocumentIdentifier(fileInTarget1Target3Uri)

      val fileInTarget1Target2TargetsDetails = magicMetaModel.getTargetsDetailsForDocument(fileInTarget1Target2Id)
      val fileInTarget1Target3TargetsDetails = magicMetaModel.getTargetsDetailsForDocument(fileInTarget1Target3Id)

      // then
      fileInTarget1Target2TargetsDetails.inactiveTargetsIds shouldContainExactlyInAnyOrder listOf(target1, target2)
      fileInTarget1Target3TargetsDetails.inactiveTargetsIds shouldContainExactlyInAnyOrder listOf(target1, target3)
    }
  }

  @Nested
  inner class DirectoryBasedSourcesTests {

    @Test
    fun `should map multiple files in single directory to single target`() {
      // given
      val commonDirectoryUri = "file:///common/directory"

      val commonDirectoryFile1InTarget1Uri = "$commonDirectoryUri/file1/in/target1"
      val commonDirectoryFile2InTarget1Uri = "$commonDirectoryUri/file2/in/target1"

      val target1 = BuildTargetIdentifier("//target1")

      val commonDirectorySource = SourceItem(commonDirectoryUri, SourceItemKind.DIRECTORY, false)

      val target1Sources = SourcesItem(target1, listOf(commonDirectorySource))

      val targets = generateMockBuildTargetsForTargetsId(listOf(target1))
      val sources = listOf(target1Sources)

      // when
      val magicMetaModel = MagicMetaModel(targets, sources)

      val commonDirectoryFile1InTarget1Id = TextDocumentIdentifier(commonDirectoryFile1InTarget1Uri)
      val commonDirectoryFile2InTarget1Id = TextDocumentIdentifier(commonDirectoryFile2InTarget1Uri)

      val commonDirectoryFile1InTarget1TargetsDetails = magicMetaModel.getTargetsDetailsForDocument(commonDirectoryFile1InTarget1Id)
      val commonDirectoryFile2InTarget1TargetsDetails = magicMetaModel.getTargetsDetailsForDocument(commonDirectoryFile2InTarget1Id)

      // then
      commonDirectoryFile1InTarget1TargetsDetails.inactiveTargetsIds shouldBe listOf(target1)
      commonDirectoryFile2InTarget1TargetsDetails.inactiveTargetsIds shouldBe listOf(target1)
    }

    @Test
    fun `should map multiple files in nested directories to multiple targets`() {
      // given
      val commonDirectoryUri = "file:///common/directory"
      val commonDirectoryChildUri = "file:///common/directory/child"

      val commonDirectoryFileInTarget1Uri = "$commonDirectoryUri/file/in/target1"
      val commonDirectoryChildFileInTarget1Target2Uri = "$commonDirectoryChildUri/file/in/target1/target2"

      val target1 = BuildTargetIdentifier("//target1")
      val target2 = BuildTargetIdentifier("//target2")

      val commonDirectorySource = SourceItem(commonDirectoryUri, SourceItemKind.DIRECTORY, false)
      val commonDirectoryChildSource = SourceItem(commonDirectoryChildUri, SourceItemKind.DIRECTORY, false)

      val target1Sources = SourcesItem(target1, listOf(commonDirectorySource))
      val target2Sources = SourcesItem(target2, listOf(commonDirectoryChildSource))

      val targets = generateMockBuildTargetsForTargetsId(listOf(target1, target2))
      val sources = listOf(target1Sources, target2Sources)

      // when
      val magicMetaModel = MagicMetaModel(targets, sources)

      val commonDirectoryFileInTarget1Id = TextDocumentIdentifier(commonDirectoryFileInTarget1Uri)
      val commonDirectoryChildFileInTarget1Target2Id =
        TextDocumentIdentifier(commonDirectoryChildFileInTarget1Target2Uri)

      val commonDirectoryFileInTarget1TargetsDetails = magicMetaModel.getTargetsDetailsForDocument(commonDirectoryFileInTarget1Id)
      val commonDirectoryChildFileInTarget1Target2TargetsDetails =
        magicMetaModel.getTargetsDetailsForDocument(commonDirectoryChildFileInTarget1Target2Id)

      // then
      commonDirectoryFileInTarget1TargetsDetails.inactiveTargetsIds shouldContainExactlyInAnyOrder listOf(target1)
      commonDirectoryChildFileInTarget1Target2TargetsDetails.inactiveTargetsIds shouldContainExactlyInAnyOrder listOf(target1, target2)
    }
  }

  @Nested
  inner class FileAndDirectoryBasedSourcesTests {

    @Test
    fun `should map document to no targets for no sources`() {
      // given

      val fileInNoTargetUri = "file:///file/in/no/target"

      val targets = emptyList<BuildTarget>()
      val sources = emptyList<SourcesItem>()

      // when
      val magicMetaModel = MagicMetaModel(targets, sources)

      val fileInNoTargetId = TextDocumentIdentifier(fileInNoTargetUri)

      val fileInNoTargetTargetsDetails = magicMetaModel.getTargetsDetailsForDocument(fileInNoTargetId)

      // then
      fileInNoTargetTargetsDetails.inactiveTargetsIds shouldBe emptyList()
    }

    @Test
    fun `should map file based source inside nested directories to multiple targets`() {
      // given
      val commonDirectoryUri = "file:///common/directory"
      val commonDirectoryChildUri = "file:///common/directory/child"

      val fileInNoTargetUri = "file:///file/in/no/target"
      val commonDirectoryFileInTarget1Uri = "$commonDirectoryUri/file1/in/target1"
      val commonDirectoryChildFileInTarget1Target2Target3Uri =
        "$commonDirectoryChildUri/file/in/target1/target2/target3"

      val target1 = BuildTargetIdentifier("//target1")
      val target2 = BuildTargetIdentifier("//target2")
      val target3 = BuildTargetIdentifier("//target3")

      val commonDirectorySource = SourceItem(commonDirectoryUri, SourceItemKind.DIRECTORY, false)
      val commonDirectoryChildSource = SourceItem(commonDirectoryChildUri, SourceItemKind.DIRECTORY, false)
      val commonDirectoryChildFileInTarget1Target2Target3Source =
        SourceItem(commonDirectoryChildFileInTarget1Target2Target3Uri, SourceItemKind.FILE, false)

      val target1Sources = SourcesItem(target1, listOf(commonDirectorySource))
      val target2Sources = SourcesItem(target2, listOf(commonDirectoryChildSource))
      val target3Sources = SourcesItem(target3, listOf(commonDirectoryChildFileInTarget1Target2Target3Source))

      val targets = generateMockBuildTargetsForTargetsId(listOf(target1, target2, target3))
      val sources = listOf(target1Sources, target2Sources, target3Sources)

      // when
      val magicMetaModel = MagicMetaModel(targets, sources)

      val fileInNoTargetId = TextDocumentIdentifier(fileInNoTargetUri)
      val commonDirectoryFileInTarget1Id = TextDocumentIdentifier(commonDirectoryFileInTarget1Uri)
      val commonDirectoryChildFileInTarget1Target2Target3Id =
        TextDocumentIdentifier(commonDirectoryChildFileInTarget1Target2Target3Uri)

      val fileInNoTargetTargetsDetails = magicMetaModel.getTargetsDetailsForDocument(fileInNoTargetId)
      val commonDirectoryFileInTarget1TargetsDetails = magicMetaModel.getTargetsDetailsForDocument(commonDirectoryFileInTarget1Id)
      val commonDirectoryChildFileInTarget1Target2Target3Targets =
        magicMetaModel.getTargetsDetailsForDocument(commonDirectoryChildFileInTarget1Target2Target3Id)

      // then
      fileInNoTargetTargetsDetails.inactiveTargetsIds shouldBe emptyList()
      commonDirectoryFileInTarget1TargetsDetails.inactiveTargetsIds shouldContainExactlyInAnyOrder listOf(target1)
      commonDirectoryChildFileInTarget1Target2Target3Targets.inactiveTargetsIds shouldContainExactlyInAnyOrder listOf(
        target1,
        target2,
        target3
      )
    }
  }

  private fun generateMockBuildTargetsForTargetsId(targetsId: List<BuildTargetIdentifier>): List<BuildTarget> =
    targetsId.map { generateMockBuildTargetForTargetId(it) }

  private fun generateMockBuildTargetForTargetId(targetId: BuildTargetIdentifier): BuildTarget =
    BuildTarget(
      targetId,
      emptyList(),
      listOf("kotlin"),
      emptyList(),
      BuildTargetCapabilities()
    )
}
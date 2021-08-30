package org.jetbrains.magicmodel

import ch.epfl.scala.bsp4j.*
import io.kotest.matchers.collections.shouldContainExactlyInAnyOrder
import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.Test

class MagicModelGetTargetsForDocumentTest {

  @Test
  fun `should return empty map for empty sources`() {
    // given
    val sources = emptyList<SourcesItem>()

    val notExistingDocumentUri = "file:///not/existing/file"

    // when
    val magicModel = MagicModel(sources)

    val notExistingDocumentId = TextDocumentIdentifier(notExistingDocumentUri)

    val notExistingDocumentTargets = magicModel.getTargetsForDocument(notExistingDocumentId)

    // then
    notExistingDocumentTargets shouldBe emptyList()
  }

  @Test
  fun `should map multiple documents to single target`() {
    // given
    val singleTargetDocument1Uri = "file:///file1/with/single/target"
    val singleTargetDocument2Uri = "file:///file2/with/single/target"

    val target = BuildTargetIdentifier("//target1")

    val singleTargetFile = SourceItem(singleTargetDocument1Uri, SourceItemKind.FILE, false)
    val anotherFile = SourceItem(singleTargetDocument2Uri, SourceItemKind.FILE, false)
    val targetSourceItem = SourcesItem(target, listOf(singleTargetFile, anotherFile))

    val sources = listOf(targetSourceItem)

    // when
    val magicModel = MagicModel(sources)

    val singleTargetDocument1Id = TextDocumentIdentifier(singleTargetDocument1Uri)
    val singleTargetDocument2Id = TextDocumentIdentifier(singleTargetDocument2Uri)

    val singleTargetDocument1Targets = magicModel.getTargetsForDocument(singleTargetDocument1Id)
    val singleTargetDocument2Targets = magicModel.getTargetsForDocument(singleTargetDocument2Id)

    // then
    val expectedTargets = listOf(target)

    singleTargetDocument1Targets shouldBe expectedTargets
    singleTargetDocument2Targets shouldBe expectedTargets
  }

  @Test
  fun `should map one document to multiple targets`() {
    // given
    val multipleTargetsDocumentUri = "file:///file/with/multiple/targets"

    val target1 = BuildTargetIdentifier("//target1")
    val target2 = BuildTargetIdentifier("//target2")

    val multipleTargetsFile = SourceItem(multipleTargetsDocumentUri, SourceItemKind.FILE, false)
    val target1SourceItem = SourcesItem(target1, listOf(multipleTargetsFile))
    val target2SourceItem = SourcesItem(target2, listOf(multipleTargetsFile))

    val sources = listOf(target1SourceItem, target2SourceItem)

    // when
    val magicModel = MagicModel(sources)

    val multipleTargetsDocumentId = TextDocumentIdentifier(multipleTargetsDocumentUri)

    val multipleTargetsDocumentTargets = magicModel.getTargetsForDocument(multipleTargetsDocumentId)

    // then
    val expectedTargets = listOf(target1, target2)

    multipleTargetsDocumentTargets shouldContainExactlyInAnyOrder expectedTargets
  }

  @Test
  fun `should map multiple documents to multiple targets`() {
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
    val magicModel = MagicModel(sources)

    val multipleTargetsDocument1Id = TextDocumentIdentifier(multipleTargetsDocument1Uri)
    val multipleTargetsDocument2Id = TextDocumentIdentifier(multipleTargetsDocument2Uri)

    val document1Targets = magicModel.getTargetsForDocument(multipleTargetsDocument1Id)
    val document2Targets = magicModel.getTargetsForDocument(multipleTargetsDocument2Id)

    // then
    val expectedDocument1Targets = listOf(target1, target2)
    val expectedDocument2Targets = listOf(target1, target3)

    document1Targets shouldContainExactlyInAnyOrder expectedDocument1Targets
    document2Targets shouldContainExactlyInAnyOrder expectedDocument2Targets
  }
}
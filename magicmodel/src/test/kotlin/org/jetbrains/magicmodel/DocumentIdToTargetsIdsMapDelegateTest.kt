package org.jetbrains.magicmodel

import ch.epfl.scala.bsp4j.*
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class DocumentIdToTargetsIdsMapDelegateTest {

  @Test
  fun `should return empty map for empty sources`() {
    // given
    val sources = emptyList<SourcesItem>()

    // when
    val documentIdToTargetsIdsMap by DocumentIdToTargetsIdsMapDelegate(sources)

    // then
    assertTrue(documentIdToTargetsIdsMap.isEmpty())
  }

  @Test
  fun `should map multiple documents to single target`() {
    // given
    val singleTargetFile1Uri = "file:///file1/with/single/target"
    val singleTargetFile2Uri = "file:///file2/with/single/target"
    val target = BuildTargetIdentifier("//target1")

    val singleTargetFile = SourceItem(singleTargetFile1Uri, SourceItemKind.FILE, false)
    val anotherFile = SourceItem(singleTargetFile2Uri, SourceItemKind.FILE, false)
    val targetSourceItem = SourcesItem(target, listOf(singleTargetFile, anotherFile))

    val sources = listOf(targetSourceItem)

    // when
    val documentIdToTargetsIdsMap by DocumentIdToTargetsIdsMapDelegate(sources)

    // then
    val expectedMap = mapOf(
      TextDocumentIdentifier(singleTargetFile1Uri) to listOf(target),
      TextDocumentIdentifier(singleTargetFile2Uri) to listOf(target)
    )

    assertEquals(expectedMap, documentIdToTargetsIdsMap)
  }

  @Test
  fun `should map one document to multiple targets`() {
    // given
    val multipleTargetsFileUri = "file:///file/with/multiple/targets"
    val target1 = BuildTargetIdentifier("//target1")
    val target2 = BuildTargetIdentifier("//target2")

    val multipleTargetsFile = SourceItem(multipleTargetsFileUri, SourceItemKind.FILE, false)
    val target1SourceItem = SourcesItem(target1, listOf(multipleTargetsFile))
    val target2SourceItem = SourcesItem(target2, listOf(multipleTargetsFile))

    val sources = listOf(target1SourceItem, target2SourceItem)

    // when
    val documentIdToTargetsIdsMap by DocumentIdToTargetsIdsMapDelegate(sources)
    val documentIdToTargetsIdsMapWithSetOfValues = documentIdToTargetsIdsMap.mapValues { it.value.toSet() }

    // then
    val expectedMapWithSetOfValues = mapOf(
      TextDocumentIdentifier(multipleTargetsFileUri) to setOf(target1, target2)
    )

    assertEquals(expectedMapWithSetOfValues, documentIdToTargetsIdsMapWithSetOfValues)
  }

  @Test
  fun `should map multiple documents to multiple targets`() {
    // given
    val multipleTargetsFile1Uri = "file:///file1/with/multiple/targets"
    val multipleTargetsFile2Uri = "file:///file2/with/multiple/targets"
    val target1 = BuildTargetIdentifier("//target1")
    val target2 = BuildTargetIdentifier("//target2")
    val target3 = BuildTargetIdentifier("//target3")

    val multipleTargetsFile1 = SourceItem(multipleTargetsFile1Uri, SourceItemKind.FILE, false)
    val multipleTargetsFile2 = SourceItem(multipleTargetsFile2Uri, SourceItemKind.FILE, false)
    val target1SourceItem = SourcesItem(target1, listOf(multipleTargetsFile1, multipleTargetsFile2))
    val target2SourceItem = SourcesItem(target2, listOf(multipleTargetsFile1))
    val target3SourceItem = SourcesItem(target3, listOf(multipleTargetsFile2))

    val sources = listOf(target1SourceItem, target2SourceItem, target3SourceItem)

    // when
    val documentIdToTargetsIdsMap by DocumentIdToTargetsIdsMapDelegate(sources)
    val documentIdToTargetsIdsMapWithSetOfValues = documentIdToTargetsIdsMap.mapValues { it.value.toSet() }

    // then
    val expectedMapWithSetOfValues = mapOf(
      TextDocumentIdentifier(multipleTargetsFile1Uri) to setOf(target1, target2),
      TextDocumentIdentifier(multipleTargetsFile2Uri) to setOf(target1, target3),
    )

    assertEquals(expectedMapWithSetOfValues, documentIdToTargetsIdsMapWithSetOfValues)
  }
}
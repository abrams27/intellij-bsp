package org.jetbrains.magicmetamodel.impl.workspacemodel.impl.updaters.transformers

import ch.epfl.scala.bsp4j.BuildTargetIdentifier
import ch.epfl.scala.bsp4j.ResourcesItem
import io.kotest.matchers.collections.shouldContainExactlyInAnyOrder
import io.kotest.matchers.shouldBe
import org.jetbrains.magicmetamodel.impl.workspacemodel.impl.updaters.JavaResourceRoot
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import java.net.URI
import kotlin.io.path.toPath

@DisplayName("ResourcesItemToJavaResourceRootTransformer.transform(resourcesItem) tests")
class ResourcesItemToJavaResourceRootTransformerTest {

  @Test
  fun `should return no resources roots for no resources items`() {
    // given
    val emptyResources = listOf<ResourcesItem>()

    // when
    val javaResources = ResourcesItemToJavaResourceRootTransformer.transform(emptyResources)

    // then
    javaResources shouldBe emptyList()
  }

  @Test
  fun `should return single resource root for resources item with one path`() {
    // given
    val resourceRawUri1 = "file:///root/dir/example/resource/File.txt"

    val resourcesItem = ResourcesItem(
      BuildTargetIdentifier("//target"),
      listOf(resourceRawUri1)
    )

    // when
    val javaResources = ResourcesItemToJavaResourceRootTransformer.transform(resourcesItem)

    // then
    val expectedJavaResource = JavaResourceRoot(
      resourcePath = URI.create(resourceRawUri1).toPath()
    )

    javaResources shouldContainExactlyInAnyOrder listOf(expectedJavaResource)
  }

  @Test
  fun `should return multiple resource roots for resources item with multiple paths`() {
    // given
    val resourceRawUri1 = "file:///root/dir/example/resource/File1.txt"
    val resourceRawUri2 = "file:///root/dir/example/resource/File2.txt"
    val resourceRawUri3 = "file:///root/dir/example/resource/File3.txt"

    val resourcesItem = ResourcesItem(
      BuildTargetIdentifier("//target"),
      listOf(resourceRawUri1, resourceRawUri2, resourceRawUri3)
    )

    // when
    val javaResources = ResourcesItemToJavaResourceRootTransformer.transform(resourcesItem)

    // then
    val expectedJavaResource1 = JavaResourceRoot(
      resourcePath = URI.create(resourceRawUri1).toPath()
    )
    val expectedJavaResource2 = JavaResourceRoot(
      resourcePath = URI.create(resourceRawUri2).toPath()
    )
    val expectedJavaResource3 = JavaResourceRoot(
      resourcePath = URI.create(resourceRawUri3).toPath()
    )

    javaResources shouldContainExactlyInAnyOrder listOf(
      expectedJavaResource1,
      expectedJavaResource2,
      expectedJavaResource3
    )
  }

  @Test
  fun `should return multiple resource roots for multiple resources items`() {
    // given
    val resourceRawUri1 = "file:///root/dir/example/resource/File1.txt"
    val resourceRawUri2 = "file:///root/dir/example/resource/File2.txt"
    val resourceRawUri3 = "file:///root/dir/example/resource/File3.txt"

    val resourcesItem1 = ResourcesItem(
      BuildTargetIdentifier("//target"),
      listOf(resourceRawUri1, resourceRawUri2)
    )
    val resourcesItem2 = ResourcesItem(
      BuildTargetIdentifier("//target"),
      listOf(resourceRawUri2, resourceRawUri3)
    )

    val resourcesItems = listOf(resourcesItem1, resourcesItem2)

    // when
    val javaResources = ResourcesItemToJavaResourceRootTransformer.transform(resourcesItems)

    // then
    val expectedJavaResource1 = JavaResourceRoot(
      resourcePath = URI.create(resourceRawUri1).toPath()
    )
    val expectedJavaResource2 = JavaResourceRoot(
      resourcePath = URI.create(resourceRawUri2).toPath()
    )
    val expectedJavaResource3 = JavaResourceRoot(
      resourcePath = URI.create(resourceRawUri3).toPath()
    )

    javaResources shouldContainExactlyInAnyOrder listOf(
      expectedJavaResource1,
      expectedJavaResource2,
      expectedJavaResource3
    )
  }
}

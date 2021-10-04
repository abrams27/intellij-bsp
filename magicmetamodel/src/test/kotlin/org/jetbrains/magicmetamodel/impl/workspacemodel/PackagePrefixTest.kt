package org.jetbrains.magicmetamodel.impl.workspacemodel

import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import java.net.URI

@DisplayName("PackagePrefix.calculate(source, sourceRoots) tests")
class PackagePrefixTest {

  @Test
  fun `should return no package for equal source dir and single root`() {
    // given
    val sourceDir = URI.create("file:///example/package/root/")
    val roots = listOf(URI.create("file:///example/package/root/"))

    // when
    val packagePrefix = PackagePrefix.calculate(sourceDir, roots)

    // then
    packagePrefix shouldBe ""
  }

  @Test
  fun `should return no package for equal source dir and one root from all roots`() {
    // given
    val sourceDir = URI.create("file:///example/package/root/")
    val roots = listOf(
      URI.create("file:///another/example/package/root/"),
      URI.create("file:///example/package/root/"),
      URI.create("file:///another/another/example/package/root/"),
    )

    // when
    val packagePrefix = PackagePrefix.calculate(sourceDir, roots)

    // then
    packagePrefix shouldBe ""
  }

  @Test
  fun `should return package for source dir and single root`() {
    // given
    val sourceDir = URI.create("file:///root/dir/example/package/")
    val roots = listOf(URI.create("file:///root/dir/"))

    // when
    val packagePrefix = PackagePrefix.calculate(sourceDir, roots)

    // then
    packagePrefix shouldBe "example.package"
  }

  @Test
  fun `should return package for source dir and multiple roots`() {
    // given
    val sourceDir = URI.create("file:///root/dir/example/package/")
    val roots = listOf(
      URI.create("file:///another/example/package/root/"),
      URI.create("file:///root/dir/"),
      URI.create("file:///another/another/example/package/root/"),
    )

    // when
    val packagePrefix = PackagePrefix.calculate(sourceDir, roots)

    // then
    packagePrefix shouldBe "example.package"
  }

  @Test
  fun `should return full path as package for source dir being parent for roots`() {
    // given
    val sourceDir = URI.create("file:///example/package/")
    val roots = listOf(
      URI.create("file:///another/example/package/root/"),
      URI.create("file:///example/package/root/"),
      URI.create("file:///another/another/example/package/root/"),
    )

    // when
    val packagePrefix = PackagePrefix.calculate(sourceDir, roots)

    // then
    packagePrefix shouldBe "example.package"
  }

  @Test
  fun `should return full path as package for no matching roots`() {
    // given
    val sourceDir = URI.create("file:///root/dir/example/package/")
    val roots = listOf(
      URI.create("file:///another/root/dir/example/package/"),
      URI.create("file:///not/matching/root/dir/"),
      URI.create("file:///another/another/root/dir/example/package/"),
    )

    // when
    val packagePrefix = PackagePrefix.calculate(sourceDir, roots)

    // then
    packagePrefix shouldBe "root.dir.example.package"
  }
}

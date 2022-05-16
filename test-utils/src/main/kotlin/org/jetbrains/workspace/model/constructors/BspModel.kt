package org.jetbrains.workspace.model.constructors

import ch.epfl.scala.bsp4j.BuildTargetCapabilities
import ch.epfl.scala.bsp4j.BuildTargetIdentifier

public class BuildTargetId(
  uri: String
) : BuildTargetIdentifier(uri)


public class BuildTarget(
  id: BuildTargetIdentifier,
  displayName: String? = null,
  baseDirectory: String? = null,
  tags: List<String> = emptyList(),
  languageIds: List<String> = emptyList(),
  dependencies: List<BuildTargetIdentifier> = emptyList(),
  capabilities: BuildTargetCapabilities = BuildTargetCapabilities(),
  dataKind: String? = null,
  data: Any? = null,
) : ch.epfl.scala.bsp4j.BuildTarget(id, tags, languageIds, dependencies, capabilities) {
  init {
    super.setDisplayName(displayName)
    super.setBaseDirectory(baseDirectory)
    super.setDataKind(dataKind)
    super.setData(data)
  }
}

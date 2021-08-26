package org.jetbrains.plugins.bsp.services

import org.jetbrains.plugins.bsp.MyBundle
import com.intellij.openapi.project.Project

class MyProjectService(project: Project) {

    init {
        println(MyBundle.message("projectService", project.name))
    }
}

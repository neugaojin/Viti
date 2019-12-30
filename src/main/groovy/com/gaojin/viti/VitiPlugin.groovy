package com.gaojin.viti

import com.android.build.gradle.AppExtension
import org.gradle.api.Plugin
import org.gradle.api.Project

class VitiPlugin implements Plugin<Project> {
    @Override
    void apply(Project project) {
        println("This is viti plugin")
        AppExtension appExtension = project.extensions.getByType(AppExtension)
        appExtension.registerTransform(new VitiTransform(project))
    }
}
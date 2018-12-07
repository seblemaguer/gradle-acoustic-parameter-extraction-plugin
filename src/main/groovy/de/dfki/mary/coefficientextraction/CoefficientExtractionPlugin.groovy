package de.dfki.mary.coefficientextraction

import org.gradle.api.JavaVersion
import org.gradle.api.Plugin
import org.gradle.api.Project

import de.dfki.mary.coefficientextraction.export.*
import de.dfki.mary.coefficientextraction.process.*

class CoefficientExtractionPlugin implements Plugin<Project> {
    @Override
    void apply(Project project) {
        // project.plugins.apply JavaPlugin
        // project.plugins.apply MavenPlugin

        project.sourceCompatibility = JavaVersion.VERSION_1_8

        project.ext {
            basename = project.name
        }

        project.status = project.version.endsWith('SNAPSHOT') ? 'integration' : 'release'


        project.task("configurationExtraction") {
            dependsOn "configuration"

            ext.nb_proc = project.configuration.hasProperty("nb_proc") ? project.configuration.nb_proc : 1
            ext.user_configuration = project.configuration.hasProperty("user_configuration") ? project.configuration.user_configuration : null
        }


        def kinds = [
            "straight":        new STRAIGHTProcess(),
            "straightwavelet": new STRAIGHTWaveletProcess(),
            "straightdnn" :    new STRAIGHTDNNProcess(),
            "world":           new WorldProcess(),
        ];


        if (project.configurationExtraction.user_configuration != null) {
            def ext = project.configurationExtraction.user_configuration.settings.extraction.kind;
            if (! kinds.containsKey(ext)) {
                throw new Exception("${ext} is not a valid extraction process")
            }

            kinds[ext].addTasks(project)
        }
    }
}

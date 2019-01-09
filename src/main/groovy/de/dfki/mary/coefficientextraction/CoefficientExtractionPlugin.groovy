package de.dfki.mary.coefficientextraction

import org.gradle.api.JavaVersion
import org.gradle.api.Plugin
import org.gradle.api.Project

import de.dfki.mary.coefficientextraction.export.*
import de.dfki.mary.coefficientextraction.process.*

class CoefficientExtractionPlugin implements Plugin<Project> {
    @Override
    void apply(Project project) {
        project.sourceCompatibility = JavaVersion.VERSION_1_8

        project.ext {
            basename = project.name
        }

        project.status = project.version.endsWith('SNAPSHOT') ? 'integration' : 'release'


        def kinds = [
            "straight":        new STRAIGHTProcess(),
            "straightwavelet": new STRAIGHTWaveletProcess(),
            "straightdnn" :    new STRAIGHTDNNProcess(),
            "world":           new WorldProcess(),
        ];


        def ext_method = project.vb_configuration.settings.extraction.kind;
        if (! kinds.containsKey(ext_method)) {
            throw new Exception("${ext_method} is not a valid extraction process")
        }
        kinds[ext_method].addTasks(project)
    }
}

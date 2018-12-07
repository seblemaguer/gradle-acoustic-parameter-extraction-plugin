package de.dfki.mary.coefficientextraction.process

/* Gradle imports */
import org.gradle.api.JavaVersion
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.logging.LogLevel
import org.gradle.api.plugins.JavaPlugin
import org.gradle.api.plugins.MavenPlugin
import org.gradle.api.tasks.Copy
import org.gradle.api.tasks.Exec
import org.gradle.api.tasks.JavaExec
import org.gradle.api.tasks.bundling.Zip

/* Helpers import */
import de.dfki.mary.coefficientextraction.process.task.ExtractSTRAIGHTTask;
import de.dfki.mary.coefficientextraction.process.task.ExtractWaveletTask;
import de.dfki.mary.coefficientextraction.process.task.ExtractVUVTask;


class STRAIGHTWaveletProcess extends STRAIGHTProcess
{
    // FIXME: where filename is defined !
    public void addTasks(Project project)
    {
        addGenericTasks(project);

        /**
         *  Task which extract the voice/unvoice mask from the lf0
         *
         */
        project.task('extractVUV', type:ExtractVUVTask){
            description "Task which extract the voice/unvoice mask from the lf0"

            // Define directories
            f0_dir = project.extractSTRAIGHT.f0_dir
            vuv_dir = new File("$project.buildDir/vuv/")

            // Define list_basenames
            list_basenames = project.configuration.list_basenames
        }

        /**
         *  This task generate the log f0 file from the f0 file.
         *
         */
        project.task('extractCWT', type: ExtractWaveletTask) {
            dependsOn.add("configurationExtraction")
            description "Task which converts f0 to cwt file"

            // Define directories
            f0_dir = project.extractSTRAIGHT.f0_dir
            cwt_dir = new File("$project.buildDir/cwt/")

            // Define list_basenames
            list_basenames = project.configuration.list_basenames
        }

        /**
         * extraction generic task
         */
        project.task('extract') {
            dependsOn.add("extractMGC")
            dependsOn.add("extractVUV")
            dependsOn.add("extractCWT")
            dependsOn.add("extractBAP")
        }
    }
}

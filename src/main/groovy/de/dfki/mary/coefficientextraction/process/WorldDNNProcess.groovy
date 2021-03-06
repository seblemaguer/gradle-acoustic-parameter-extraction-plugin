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
import de.dfki.mary.coefficientextraction.process.task.ExtractVUVTask;
import de.dfki.mary.coefficientextraction.process.task.ExtractLF0Task;
import de.dfki.mary.coefficientextraction.process.task.ExtractInterpolatedF0Task;

class WorldDNNProcess extends WorldProcess
{
    // FIXME: where filename is defined !
    @Override
    public void addTasks(Project project)
    {
        addGenericTasks(project);


        /**
         *  This task generate the log f0 file from the f0 file.
         *
         */
        project.task('extractLF0', type: ExtractLF0Task) {
            description "Task which converts f0 to lf0 file"

            // Define directories
            f0_dir = project.extractWorld.f0_dir
            lf0_dir = new File("$project.buildDir/lf0/")
        }


        /**
         *  Task which extract the voice/unvoice mask from the lf0
         *
         */
        project.task('extractVUV', type:ExtractVUVTask){
            description "Task which extract the voice/unvoice mask from the lf0"

            // Define directories
            f0_dir = project.extractWorld.f0_dir
            vuv_dir = new File("$project.buildDir/vuv/")

        }

        /**
         *  Task which generates an interpolated F0 from the log f0
         *
         */
        project.task('extractInterpolatedF0', type: ExtractInterpolatedF0Task) {
            description "Task which interpolate the f0"

            // Define directories
            lf0_dir = project.extractLF0.lf0_dir
            interpolated_lf0_dir = new File("$project.buildDir/interpolated_lf0/")
        }

        /**
         * extraction generic task
         */
        project.task('extract') {
            dependsOn.add("extractMGC")
            dependsOn.add("extractLF0")
            dependsOn.add("extractVUV")
            dependsOn.add("extractInterpolatedF0")
            dependsOn.add("extractBAP")
        }
    }
}

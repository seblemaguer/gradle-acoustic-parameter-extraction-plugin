package de.dfki.mary.coefficientextraction.process.task

// Inject
import javax.inject.Inject;

// Worker import
import org.gradle.workers.*;

// Gradle task related
import org.gradle.api.Action;
import org.gradle.api.DefaultTask
import org.gradle.api.file.DirectoryProperty
import org.gradle.api.file.RegularFileProperty
import org.gradle.api.tasks.*

// Extraction helper class
import de.dfki.mary.coefficientextraction.extraction.ExtractVUV

/**
 *  Definition of the task type to extract voice/unvoice pattern
 *
 */
public class ExtractVUVTask extends DefaultTask {
    /** The worker */
    private final WorkerExecutor workerExecutor;

    /** The directory containing f0 files */
    @InputDirectory
    final DirectoryProperty f0_dir = newInputDirectory()

    /** The directory containing the voice/unvoice pattern files */
    @OutputDirectory
    final DirectoryProperty vuv_dir = newOutputDirectory()

    /**
     *  The constructor which defines which worker executor is going to achieve the conversion job
     *
     *  @param workerExecutor the worker executor
     */
    @Inject
    public ExtractVUVTask(WorkerExecutor workerExecutor) {
        super();
        this.workerExecutor = workerExecutor;
    }

    /**
     *  The actual extraction method
     *
     */
    @TaskAction
    public void extract() {
        for (File f0_file: project.fileTree(f0_dir.get()).include('*.f0').collect()) {
            File vuv_file = new File(vuv_dir.getAsFile().get(), f0_file.getName() - ".f0" + ".vuv");

            // Submit the execution
            workerExecutor.submit(ExtractVUVWorker.class,
                                  new Action<WorkerConfiguration>() {
                    @Override
                    public void execute(WorkerConfiguration config) {
                        config.setIsolationMode(IsolationMode.NONE);
                        config.params(f0_file, vuv_file, project.gradle.vb_configuration);
                    }
                });
        }
    }
}


/**
 *  Worker class which extract the voice/unvoice pattern from the f0 file
 *
 */
class ExtractVUVWorker implements Runnable {
    /** The input F0 file */
    private final File input_file;

    /** The generated VUV file */
    private final File output_file;

    /** The configuration object */
    private final Object configuration;

    /**
     *  The contructor which initialize the worker
     *
     *  @param input_file the input F0 file
     *  @param output_file the output VUV file
     *  @param configuration the configuration object
     */
    @Inject
    public ExtractVUVWorker(File input_file, File output_file, Object configuration) {
	this.input_file = input_file;
	this.output_file = output_file;
        this.configuration = configuration
    }

    /**
     *  Run method which achieve the extraction/conversion
     *
     */
    @Override
    public void run() {

        // Define extractor
        def extractor = new ExtractVUV();

        // Define output files
        def extToFile = new Hashtable<String, String>()
        extToFile.put("vuv".toString(), output_file)
        extractor.setOutputFiles(extToFile)

        // Run extraction
        extractor.extract(input_file)
    }
}

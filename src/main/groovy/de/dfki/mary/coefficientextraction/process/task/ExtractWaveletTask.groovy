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
import de.dfki.mary.coefficientextraction.extraction.ExtractWavelet

/**
 *  Definition of the task type to convert f0 to wavelet
 *
 */
public class ExtractWaveletTask extends DefaultTask {
    /** The worker */
    private final WorkerExecutor workerExecutor;

    /** The directory containing f0 files */
    @InputDirectory
    final DirectoryProperty f0_dir = newInputDirectory()

    /** The directory containing the wavelet files */
    @OutputDirectory
    final DirectoryProperty cwt_dir = newOutputDirectory()

    /**
     *  The constructor which defines which worker executor is going to achieve the conversion job
     *
     *  @param workerExecutor the worker executor
     */
    @Inject
    public ExtractWaveletTask(WorkerExecutor workerExecutor) {
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
            File cwt_file = new File(cwt_dir.getAsFile().get(), f0_file.getName() - ".f0" + ".cwt");

            // Submit the execution
            workerExecutor.submit(ExtractWaveletWorker.class,
                                  new Action<WorkerConfiguration>() {
                    @Override
                    public void execute(WorkerConfiguration config) {
                        config.setIsolationMode(IsolationMode.NONE);
                        config.params(f0_file, cwt_file, project.gradle.vb_configuration);
                    }
                });
        }
    }
}


/**
 *  Worker class which achieve the f0 conversion to wavelet
 *
 */
class ExtractWaveletWorker implements Runnable {
    /** The input F0 file */
    private final File input_file;

    /** The generated wavelet file */
    private final File output_file;

    /** The configuration object */
    private final Object configuration;

    /**
     *  The contructor which initialize the worker
     *
     *  @param input_file the input F0 file
     *  @param output_file the output wavelet file
     *  @param configuration the configuration object
     */
    @Inject
    public ExtractWaveletWorker(File input_file, File output_file, Object configuration) {
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
        def extractor = new ExtractWavelet();

        // // Prepare extractor configuration (FIXME: nothing for now, but the configuration file maybe?)
        // configuration.models.cmp.streams.each { stream ->
        //     if (stream.kind ==  "cwt") {
        //     }
        // }

        // Define output files
        def extToFile = new Hashtable<String, String>()
        extToFile.put("cwt".toString(), output_file)
        extractor.setOutputFiles(extToFile)

        // Run extraction
        extractor.extract(input_file)
    }
}

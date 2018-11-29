package de.dfki.mary.coefficientextraction.extraction;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;

/**
 * Coefficients extraction based on STRAIGHT
 *
 * @author <a href="mailto:slemaguer@coli.uni-saarland.de">Sébastien Le Maguer</a>
 */
public class ExtractWavelet extends ExtractBase
{
    protected static final String EXT = "cwt";
    protected static final String WAVELET_CMD = "cwt_analysis_synthesis";
    private File configuration_file;

    public ExtractWavelet()
    {
        setConfigurationFile(null);
    }

    public void extract(File input_file) throws Exception
    {
        // Check output_file
        if (!extToFile.containsKey(EXT))
            throw new Exception("extToFile does not contain\"" + EXT + "\" associated output file path");

        // 2. Prepare command
        String command = WAVELET_CMD + " ";
        if (getConfigurationFile() != null)
            command += "-c " + getConfigurationFile().toString() + " ";
        command += input_file.toString() + " " + extToFile.get(EXT);

        // 3. Execute command
        run(command);
    }

    public File getConfigurationFile() {
        return configuration_file;
    }

    public void setConfigurationFile(File configuration_file) {
        this.configuration_file = configuration_file;
    }
}

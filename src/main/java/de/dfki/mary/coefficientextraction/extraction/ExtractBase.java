package de.dfki.mary.coefficientextraction.extraction;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.util.Hashtable;

/**
 * Interface to provide an coefficient extraction method
 *
 * @author <a href="mailto:slemaguer@coli.uni-saarland.de">slemaguer</a>
 */
public abstract class ExtractBase implements ExtractInterface
{
    protected Hashtable<String, File> extToFile;

    public void setOutputFiles(Hashtable<String, File> extToFile)
    {
        this.extToFile = extToFile;
    }

    public abstract void extract(File input_file) throws Exception;

    public void run(String command) throws Exception {
        // Run extraction
        String[] cmd = {"bash", "-c", command};
        Process p = Runtime.getRuntime().exec(cmd);
        p.waitFor();

        if (p.exitValue() != 0) {
            throw new Exception("Command failed: " + command);
        }
    }
}

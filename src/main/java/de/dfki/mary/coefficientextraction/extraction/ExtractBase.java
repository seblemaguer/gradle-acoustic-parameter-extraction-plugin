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

        BufferedReader reader = new BufferedReader(new InputStreamReader(p.getInputStream()));
        String line;
        // while ((line = reader.readLine())!= null) {
        //         System.out.println(line);
        // }

        StringBuilder sb = new StringBuilder();
        reader = new BufferedReader(new InputStreamReader(p.getErrorStream()));
        while ((line = reader.readLine())!= null) {
            sb.append(line + "\n");
        }
        // if (!sb.toString().isEmpty())
        // {
        //     throw new Exception(sb.toString());
        // }
    }
}

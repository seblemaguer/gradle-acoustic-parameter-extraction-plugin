package de.dfki.mary.coefficientextraction.extraction;

/* */
import java.io.File;
import java.net.URL;
import java.nio.file.StandardCopyOption;
import java.util.Hashtable;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import org.apache.commons.io.FileUtils;

/* testng part */
import org.testng.Assert;
import org.testng.annotations.*;

/**
 *
 *
 * @author <a href="mailto:slemaguer@coli.uni-saarland.de">Sébastien Le Maguer</a>
 */
public class ExtractWaveletTest
{
    private ExtractWavelet ex_wavelet;
    private Path temp_dir;

    public ExtractWaveletTest() throws Exception
    {
        ex_wavelet = new ExtractWavelet();
        temp_dir = Files.createTempDirectory(null);
    }

    @BeforeTest
    public void extractWavFile() throws Exception {
        URL url = ExtractWaveletTest.class.getResource("vaiueo2d.wav");
        Files.copy(url.openStream(),
                   Paths.get(temp_dir.toString(), "vaiueo2d.wav"),
                   StandardCopyOption.REPLACE_EXISTING);
    }

    @AfterTest
    public void clean() throws Exception {
        FileUtils.deleteDirectory(temp_dir.toFile());
    }

    // @Test
    // public void checkRunning() throws Exception
    // {
    //     File test_wav = new File(temp_dir.toFile(), "vaiueo2d.wav");
    //     Hashtable<String, File> extToFile = new Hashtable<String, File>();
    //     extToFile.put(ExtractWavelet.EXT, new File("/tmp/test_wav.cwt"));

    //     ex_wavelet = new ExtractWavelet();
    //     ex_wavelet.setOutputFiles(extToFile);
    //     ex_wavelet.extract(test_wav);

    //     Assert.assertTrue(true);
    // }
}


/* ExtractWaveletTest.java ends here */

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
public class ExtractWorldTest
{
    private ExtractWorld ex_wavelet;
    private Path temp_dir;

    public ExtractWorldTest() throws Exception
    {
        ex_wavelet = new ExtractWorld();
        temp_dir = Files.createTempDirectory(null);
    }

    @BeforeTest
    public void extractWavFile() throws Exception {
        URL url = ExtractWorldTest.class.getResource("vaiueo2d.wav");
        Files.copy(url.openStream(),
                   Paths.get(temp_dir.toString(), "vaiueo2d.wav"),
                   StandardCopyOption.REPLACE_EXISTING);
    }

    @AfterTest
    public void clean() throws Exception {
        FileUtils.deleteDirectory(temp_dir.toFile());
    }

    @Test
    public void checkRunning() throws Exception
    {
        File test_wav = new File(temp_dir.toFile(), "vaiueo2d.wav");
        Hashtable<String, File> extToFile = new Hashtable<String, File>();
        extToFile.put("f0", new File("/tmp/test_wav.f0"));
        extToFile.put("ap", new File("/tmp/test_wav.ap"));
        extToFile.put("sp", new File("/tmp/test_wav.sp"));

        ex_wavelet = new ExtractWorld();
        ex_wavelet.setOutputFiles(extToFile);
        ex_wavelet.extract(test_wav);

        Assert.assertTrue(true);
    }
}


/* ExtractWorldTest.java ends here */

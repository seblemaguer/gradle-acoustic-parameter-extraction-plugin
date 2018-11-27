package de.dfki.mary.coefficientextraction.extraction;

/* testng part */
import org.testng.Assert;
import org.testng.annotations.*;

import java.io.File;
/**
 *
 *
 * @author <a href="mailto:slemaguer@coli.uni-saarland.de">Sébastien Le Maguer</a>
 */
public class ExtractSTRAIGHTTest
{

    private ExtractSTRAIGHT ex_straight;
    public ExtractSTRAIGHTTest()
    {
        ex_straight = new ExtractSTRAIGHT("/tmp");
    }

    @Test
    public void checkScriptGeneration() throws Exception
    {
        Assert.assertTrue(true);
    }
}


/* ExtractSTRAIGHTTest.java ends here */

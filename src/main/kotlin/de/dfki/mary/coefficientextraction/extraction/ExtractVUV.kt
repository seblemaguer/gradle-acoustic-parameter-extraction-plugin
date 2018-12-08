package de.dfki.mary.coefficientextraction.extraction;

import java.io.File;
import java.io.IOException;
import java.io.BufferedReader;
import java.io.PrintWriter;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.file.Files;
import java.io.InputStreamReader;
import java.util.Hashtable;

/**
 * Coefficients extraction based on STRAIGHT
 *
 * @author <a href="mailto:slemaguer@coli.uni-saarland.de">Sébastien Le Maguer</a>
 */
public class ExtractVUV extends ExtractBase
{
    public ExtractVUV()
    {
    }

    public void extract(File input_file) throws Exception
    {
        // Load byte array of data
        byte[] data_bytes = Files.readAllBytes(input_file.toPath());
        ByteBuffer buffer = ByteBuffer.wrap(data_bytes);
        buffer.order(ByteOrder.LITTLE_ENDIAN);

        // Compute size
        int T = data_bytes.length / Float.BYTES;

        // Generate vector C
        float[] data = new float[T];
        for (int i=0; i<T; i++)
        {
            float f = buffer.getFloat();
            if (Float.isNaN(f))
            {
                throw new Exception(input_file.toString() + " contains nan values! ");
            }
            if (f == 0.0)
                data[i] = 0;
            else
                data[i] = 1.0f;
        }

        // Saving VUV mask
        ByteBuffer output_buffer = ByteBuffer.allocate(data_bytes.length);
        output_buffer.asFloatBuffer().put(data);
        output_buffer.order(ByteOrder.LITTLE_ENDIAN);
        byte[] output_data_bytes = output_buffer.array();
        Files.write(extToFile.get("vuv").toPath(), output_data_bytes);
    }
}

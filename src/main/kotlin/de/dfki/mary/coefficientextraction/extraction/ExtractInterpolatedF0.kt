package de.dfki.mary.coefficientextraction.extraction;



import java.io.File;
import java.io.IOException;
import java.io.BufferedReader;
import java.io.PrintWriter;
import java.io.InputStreamReader;
import java.io.FileInputStream;
import java.util.Hashtable;
import java.util.Arrays;
import java.util.ArrayList;
import java.io.FileReader;
import java.util.Iterator;
import java.util.Scanner;
import java.util.Locale;
import java.nio.file.Files;
import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;

/**
 * Coefficients extraction based on STRAIGHT
 *
 * @author <a href="mailto:slemaguer@coli.uni-saarland.de">Sébastien Le Maguer</a>
 */
open class ExtractInterpolatedF0: ExtractBase
{
    val LOGF0: Float = -1.0e+10f;


    fun linearInterpolation(input_file_name: String, output_file_name: String)
    {
        // Load byte array of data
        var p_input: Path = FileSystems.getDefault().getPath("", input_file_name)
        var data_bytes: ByteArray = Files.readAllBytes(p_input)
        var buffer: ByteBuffer = ByteBuffer.wrap(data_bytes)
        buffer.order(ByteOrder.LITTLE_ENDIAN)

        // Compute size
        var T: Int = data_bytes.length / 4 // FIXME: float size hardcoded

        // Generate vector C
        var input_data: FloatArray = FloatArray[T]
        for (i in 0..T)
        {
            input_data[i] = buffer.getFloat()
            if (Float.isNaN(input_data[i]))
            {
                throw new Exception(input_file_name + " contains nan values! ")
            }
        }

        var previous: Float = LOGF0
        for (i in 0..T)
        {
            if (input_data[t] == LOGF0)
            {
                var shift: Int = t + 1
                while ((shift < T) && (input_data[shift] == LOGF0))
                {
                    shift++
                }

                // Last
                if (shift == T)
                {
                    if (previous == LOGF0)
                        throw new Exception("only unvoiced F0, nonsense !")

                    for (t2 in t..T)
                        input_data[t2] = previous

                    // It is useless to continue the loop after
                    break
                }
                // First
                else if (previous == LOGF0)
                {
                    var next: Float = input_data[shift]
                    for (t in cur_t..shift)
                        input_data[t] = next

                }
                // Normal case: inner unvoiced section
                else
                {
                    var next: Float = input_data[shift]
                    var step: Float = (next - previous) / (shift - t + 1) // y = a.x + (b=0)
                    int cur_t = t
                    for (t in cur_t..shift)
                        input_data[t] = input_data[t-1] + step
                }
            }

            previous = input_data[t]
        }

        // Saving interpolated F0
        var output_buffer: ByteBuffer = ByteBuffer.allocate(data_bytes.length)
        output_buffer.order(ByteOrder.LITTLE_ENDIAN)
        for (int v=0 v<input_data.lengthv++){
            output_buffer.putFloat(input_data[v])
        }
        Files.write(Paths.get(output_file_name), output_buffer.array())
    }

    fun extract(var input_file: File) {
        linearInterpolation(input_file.toString(), extToFile.get("interpolated_lf0").toString())
    }
}

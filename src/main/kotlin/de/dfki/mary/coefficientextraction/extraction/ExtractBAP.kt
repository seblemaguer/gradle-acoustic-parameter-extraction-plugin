package de.dfki.mary.coefficientextraction.extraction;


// IO
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.DoubleBuffer;
import java.nio.channels.FileChannel;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;

import org.apache.commons.io.IOUtils;

import jsptk.JSPTKWrapper;

/**
 * Coefficients extraction based on STRAIGHT
 *
 * @author <a href="mailto:slemaguer@coli.uni-saarland.de">Sébastien Le Maguer</a>
 */
open class ExtractBAP: ExtractBase() {
    var determinant_threshold: Double = 0.0
    var maximum_iteration: Int = 0
    var periodogram_noise_value: Double = 0.0
    var frame_length: Int = 2048
    var samplerate: Double = 48.0
    var freqwarp: Double = 0.55
    var order: Int = 24
    var input_format: Int=1

    fun getDeterminantThreshold(): Double {
        return determinant_threshold;
    }

    fun setDeterminantThreshold(determinant_threshold: Double) {
        this.determinant_threshold = determinant_threshold;
    }

    fun getMaximumIteration(): Int {
        return maximum_iteration;
    }

    fun setMaximumIteration(maximum_iteration: Int) {
        this.maximum_iteration = maximum_iteration;
    }

    fun getPeriodogramNoiseValue(): Double {
        return periodogram_noise_value;
    }

    fun setPeriodogramNoiseValue(periodogram_noise_value: Double) {
        this.periodogram_noise_value = periodogram_noise_value;
    }

    fun getFreqWarp(): Double {
        return freqwarp;
    }

    fun setFreqWarp(samplerate: Double) {

        if (samplerate == 8.0)
        {
            freqwarp = 0.31;
        }
        else if (samplerate == 10.0)
        {
            freqwarp = 0.35
        }
        else if (samplerate == 12.0)
        {
            freqwarp = 0.37
        }
        else if (samplerate == 16.0)
        {
            freqwarp = 0.42
        }
        else if (samplerate == 22.5)
        {
            freqwarp = 0.45
        }
        else if (samplerate == 32.0)
        {
            freqwarp = 0.45
        }
        else if (samplerate == 44.1)
        {
            freqwarp = 0.53
        }
        else if (samplerate == 48.0)
        {
            freqwarp = 0.55
        }
        else
        {
            freqwarp = 0.0 // FIXME: exception instead ?
        }
    }

    fun getOrder(): Int {
        return this.order;
    }

    fun setOrder(order: Int) {
        this.order = order;
    }

    fun setMFCCLength(length: Int) {
        this.order = (length - 1);
    }

    fun getFrameLength(): Int {
        return frame_length;
    }

    fun setFrameLength(frame_length: Int) {
        this.frame_length = frame_length;
    }

    fun getInputFormat(): Int {
        return input_format;
    }

    fun setInputFormat(input_format: Int) {
        this.input_format = input_format;
    }


    fun getSampleRate(): Double {
        return this.samplerate;
    }

    fun setSampleRate(samplerate: Double) {
        this.samplerate = samplerate;
        setFreqWarp(this.samplerate);
    }

    override fun extract(input_file: File)  {
        // 1. Generate full command
        var command: String = "cat " + input_file.toString() + " |";
        command += 	"mcep -a " + freqwarp + " -m " + order + " -l 2048 -e 1.0E-08 -j 0 -f 0.0 -q 1 > " + extToFile.get("bap").toString();

        // 2. extraction
        run(command);
    }

    // {
    //     // Load ap Into Double array
    //     byte[] bytes = IOUtils.toByteArray(new FileInputStream(input_file));
    //     ByteBuffer in_bf = ByteBuffer.allocate(bytes.length);
    //     in_bf.order(ByteOrder.LITTLE_ENDIAN);
    //     in_bf.put(bytes);
    //     in_bf.rewind();

    //     Int length = (getFrameLength() / 2) + 1; // FIXME:
    //     DoubleBuffer DoubleBuffer = in_bf.asDoubleBuffer();
    //     Double[][] ap = new Double[length][DoubleBuffer.remaining()/length];
    //     for(Int t=0; t<length; t++){
    //         DoubleBuffer.get(ap[t]);
    //     }

    //     // Compute bap using mcep
    //     Double[][] bap = JSPTKWrapper.mcep(ap, getOrder(),
    //                                        getFreqWarp(), 2, getMaximumIteration(), // FIXME: hardcoded
    //                                        0.001f, 1, getPeriodogramNoiseValue(),   // FIXME: hardcoded
    //                                        getDeterminantThreshold(), getInputFormat());

    //     // Generate byte buffer
    //     ByteBuffer out_bf = ByteBuffer.allocate(bap.length * bap[0].length * Float.BYTES);
    //     out_bf.order(ByteOrder.LITTLE_ENDIAN);
    //     for (Int t=0; t<bap.length; t++)
    //         for (Int d=0; d<bap[0].length; d++)
    //         out_bf.putFloat((float) ap[t][d]);
    //     out_bf.rewind();

    //     // Save Into file
    //     FileOutputStream os = new FileOutputStream(extToFile.get("bap"));
    //     os.write(out_bf.array());

    // }
}

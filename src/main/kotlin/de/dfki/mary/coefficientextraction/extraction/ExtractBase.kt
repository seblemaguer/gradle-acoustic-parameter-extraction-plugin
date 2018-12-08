package de.dfki.mary.coefficientextraction.extraction;

// Logging
import org.apache.log4j.Logger

// File
import java.io.File

import kotlin.collections.*

/**
 * Interface to provide an coefficient extraction method
 *
 * @author <a href="mailto:slemaguer@coli.uni-saarland.de">slemaguer</a>
 */
abstract class ExtractBase: ExtractInterface
{
    @JvmField protected val logger: Logger = Logger.getLogger(ExtractBase::class.java.name)
    @JvmField protected var extToFile: Map<String, File> = emptyMap<String, File>();

    override fun setOutputFiles(extToFile: Map<String, File>) {
        this.extToFile = extToFile;
    }


    // override abstract fun extract(input_file: File);

    fun run(command: String) {
        var process = ProcessBuilder(listOf("sh", "-c", command))
            .directory(File(System.getProperty("user.dir")))
            .redirectErrorStream(true)
            .start()

        process.inputStream.reader().forEachLine { out -> logger.info(out) }
        process.waitFor();

        if (process.exitValue() != 0) {
            throw Exception("hts command failed: " + command)
        }
    }
}

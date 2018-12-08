package de.dfki.mary.coefficientextraction.extraction;

// File
import java.io.File

/**
 * Interface to provide an coefficient extraction method
 *
 * @author <a href="mailto:slemaguer@coli.uni-saarland.de">slemaguer</a>
 */
interface ExtractInterface
{
  fun setOutputFiles(extToFile: Map<String, File>)

  @Throws(Exception::class)
  fun extract(input_file: File)
}

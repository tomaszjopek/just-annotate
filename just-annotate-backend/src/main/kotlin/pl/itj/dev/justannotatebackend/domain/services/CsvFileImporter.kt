package pl.itj.dev.justannotatebackend.domain.services

import mu.KLogging
import java.io.InputStream

class CsvFileImporter {

    companion object : KLogging()

    fun import(inputStream: InputStream): Sequence<String> {
        logger.info { "Importing file" }

        val reader = inputStream.bufferedReader()
        val header = reader.readLine()

        logger.info { "Header $header" }

        return reader.readLines()
                .asSequence()
    }

}
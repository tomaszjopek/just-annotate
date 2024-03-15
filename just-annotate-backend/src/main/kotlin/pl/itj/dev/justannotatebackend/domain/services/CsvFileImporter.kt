package pl.itj.dev.justannotatebackend.domain.services

import java.io.InputStream

class CsvFileImporter {

    fun import(inputStream: InputStream): Sequence<String> {
        val reader = inputStream.bufferedReader()
        val header = reader.readLine()

        return reader.readLines()
                .asSequence()
    }

}
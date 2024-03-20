package pl.itj.dev.justannotatebackend.domain.services

import mu.KLogging
import org.springframework.core.io.buffer.DataBuffer
import org.springframework.core.io.buffer.DataBufferUtils
import reactor.core.publisher.Flux
import reactor.core.scheduler.Schedulers
import java.io.IOException
import java.io.InputStream
import java.io.PipedInputStream
import java.io.PipedOutputStream


class CsvFileImporter {

    companion object : KLogging()

    fun import(data: Flux<DataBuffer>): Sequence<String> {
        logger.info { "Reading data buffers" }

        val inputStream = getInputStreamFromFluxDataBuffer(data)
        val reader = inputStream.bufferedReader()
        reader.readLine()

        return reader.readLines()
                .asSequence()
    }

    @Throws(IOException::class)
    fun getInputStreamFromFluxDataBuffer(data: Flux<DataBuffer>): InputStream {
        val osPipe = PipedOutputStream()
        val isPipe = PipedInputStream(osPipe)

        DataBufferUtils.write(data, osPipe)
                .subscribeOn(Schedulers.boundedElastic())
                .doOnComplete {
                    try {
                        osPipe.close()
                    } catch (ignored: IOException) {
                        logger.error { "Error during file import" }
                    }
                }
                .subscribe(DataBufferUtils.releaseConsumer())
        return isPipe
    }

}
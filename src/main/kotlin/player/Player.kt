package ua.pp.lumivoid.player

import javafx.scene.media.Media
import javafx.scene.media.MediaPlayer
import org.slf4j.LoggerFactory
import ua.pp.lumivoid.data.StreamsData

object Player {
    private val logger = LoggerFactory.getLogger(javaClass)

    private var player: MediaPlayer? = null
    private var streams: StreamsData? = null
    private var volume: Float = 1f
//    private var isHd = false

    fun setStation(streams: StreamsData) {
        this.streams = streams

        if (!isStopped()) {
            stop()
            play()
        }
    }

//    fun setHd(hd: Boolean) {
//        isHd = hd
//        if (!isStopped()) {
//            stop()
//            play()
//        }
//    }

    fun play() {
        if (streams == null) {
            logger.error("Station is null!")
            return
        }

        val station = streams!!.hd["android"]
//        if (isHd) streams!!.hd["android"]
//        else streams!!.regular["android"]

        logger.info("Playing: $station")

        player = MediaPlayer(Media(station!!))
        player!!.isAutoPlay = true

        player!!.setOnError {
            logger.error(player!!.error.message)
            logger.error(player!!.error.type.toString())
        }
    }

    fun stop() {
        logger.info("Stopping player...")
        player?.stop()
        player = null
    }

    fun isStopped(): Boolean = player == null

//    fun isHd(): Boolean = isHd

    fun setVolume(volume: Float) {
        logger.info("Volume: $volume")
        this.volume = volume
    }

//    fun getVolume(): Double = player!!.volume

}
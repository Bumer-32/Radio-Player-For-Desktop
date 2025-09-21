package ua.pp.lumivoid.player

import org.slf4j.LoggerFactory
import ua.pp.lumivoid.data.StreamsData
import uk.co.caprica.vlcj.factory.MediaPlayerFactory
import uk.co.caprica.vlcj.player.base.MediaPlayer

object Player {
    private val logger = LoggerFactory.getLogger(javaClass)

    private val mediaFactory = MediaPlayerFactory()
    private var mediaPlayer: MediaPlayer? = null
    private var station: String? = null
    private var volume: Float = 1f
    private var isHd = false

    fun setStation(streams: StreamsData) {
        station = if (isHd) streams.hd["android"]
        else streams.regular["android"]

        if (!isStopped()) {
            stop()
            play()
        }
    }

    fun setHd(hd: Boolean) {
        isHd = hd
        if (!isStopped()) {
            stop()
            play()
        }
    }

    fun play() {
        if (station == null) {
            logger.error("Station is null!")
            return
        }

        logger.info("Station: $station")

        mediaPlayer = mediaFactory.mediaPlayers().newMediaPlayer()
        mediaPlayer!!.media().play(station!!)
    }

    fun stop() {
        logger.info("Stopping player...")
        mediaPlayer?.controls()!!.stop()
        mediaPlayer?.release()
        mediaPlayer = null
    }

    fun isStopped(): Boolean = mediaPlayer == null

    fun isHd(): Boolean = isHd

    fun setVolume(volume: Float) {
        logger.info("Volume: $volume")
        this.volume = volume
    }

//    fun getVolume(): Double = player!!.volume

}
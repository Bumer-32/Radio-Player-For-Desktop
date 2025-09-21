package ua.pp.lumivoid.player

import io.github.selemba1000.JMTC
import io.github.selemba1000.JMTCButtonCallback
import io.github.selemba1000.JMTCCallbacks
import io.github.selemba1000.JMTCEnabledButtons
import io.github.selemba1000.JMTCMediaType
import io.github.selemba1000.JMTCMusicProperties
import io.github.selemba1000.JMTCPlayingState
import io.github.selemba1000.JMTCSettings
import io.github.selemba1000.JMTCTimelineProperties
import javafx.application.Platform
import javafx.scene.media.Media
import javafx.scene.media.MediaPlayer
import org.slf4j.LoggerFactory
import ua.pp.lumivoid.data.StreamsData
import ua.pp.lumivoid.ui.Controls

object Player {
    private val logger = LoggerFactory.getLogger(javaClass)

    private val jmtc = JMTC.getInstance(JMTCSettings("RadioPlayer","RadioPlayer"))

    private var player: MediaPlayer? = null
    private var streams: StreamsData? = null
    private var volume: Float = 1f

    init {
        val callbacks = JMTCCallbacks()

        callbacks.onPlay = JMTCButtonCallback {
            play()
            Controls.update()
        }
        callbacks.onStop = JMTCButtonCallback {
            stop()
            Controls.update()
        }

        jmtc.mediaType = JMTCMediaType.Music
        jmtc.enabledButtons = JMTCEnabledButtons(
            true,
            false,
            true,
            false,
            false,
        )
        jmtc.setCallbacks(callbacks)
        jmtc.enabled = true
    }

    fun setStation(streams: StreamsData) {
        this.streams = streams

        if (!isStopped()) {
            stop()
            play()
        }
    }

    fun play() = Platform.runLater {
        if (streams == null) {
            logger.error("Station is null!")
            return@runLater
        }

        val station = streams!!.hd["android"]

        logger.info("Playing: $station")

        player = MediaPlayer(Media(station!!))
        player!!.isAutoPlay = true

        player!!.setOnError {
            logger.error(player!!.error.message)
            logger.error(player!!.error.type.toString())
        }

        jmtc.playingState = JMTCPlayingState.PLAYING
        jmtc.mediaProperties = JMTCMusicProperties(
            "TestTitle",
            "TestArtist",
            "test",
            "tset",
            emptyArray(),
            0,
            1,
            null
        )

        jmtc.setTimelineProperties(
            JMTCTimelineProperties(
                0L,
                100000L,
                0L,
                100000L
            )
        )
        jmtc.updateDisplay()

        Controls.update()
    }

    fun stop() {
        logger.info("Stopping player...")
        player?.stop()
        player?.dispose()
        player = null

        Controls.update()
        jmtc.playingState = JMTCPlayingState.STOPPED
    }

    fun isStopped(): Boolean = player == null

    fun setVolume(volume: Float) {
        logger.info("Volume: $volume")
        this.volume = volume
    }

//    fun getVolume(): Double = player!!.volume

}
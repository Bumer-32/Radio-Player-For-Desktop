package ua.pp.lumivoid.ui

import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import javafx.application.Platform
import javafx.event.EventHandler
import javafx.geometry.Pos
import javafx.scene.control.Button
import javafx.scene.control.Label
import javafx.scene.control.Separator
import javafx.scene.image.Image
import javafx.scene.image.ImageView
import javafx.scene.layout.HBox
import javafx.scene.layout.VBox
import kotlinx.coroutines.runBlocking
import kotlinx.serialization.json.Json
import org.slf4j.LoggerFactory
import ua.pp.lumivoid.data.SongData
import ua.pp.lumivoid.httpClient
import ua.pp.lumivoid.player.Player
import ua.pp.lumivoid.player.Stations

object Controls: VBox() {
    private val logger = LoggerFactory.getLogger(javaClass)
    private val json = Json { ignoreUnknownKeys = true }

    private val playImage = Image("/icons/play.png")
    private val pauseImage = Image("/icons/pause.png")

    private val playButtonImageView: ImageView
    private val stationText: Label
    private val tagText: Label
    private val trackText: Label
    private val authorText: Label

    init {
        prefWidth = 400.0 // size same as logo
        alignment = Pos.CENTER_LEFT

        stationText = Label("Radio Name").apply { styleClass.addAll("stationText", "station") }
        tagText = Label("Tag").apply { styleClass.addAll("stationText", "tag") }
        trackText = Label("Track name").apply { styleClass.addAll("stationText", "track") }
        authorText = Label("Author").apply { styleClass.addAll("stationText", "author") }
        val text = VBox(stationText, tagText, Separator(), trackText, authorText)

        val playButton = Button().apply {
            playButtonImageView = ImageView(playImage)
            graphic = playButtonImageView

            onAction = EventHandler {
                if (Player.isStopped()) {
                    Player.play()
                } else {
                    Player.stop()
                }
                update()
            }
        }
        val buttons = HBox(playButton).apply { alignment = Pos.CENTER_LEFT }


        children.addAll(text, buttons)

        val thread = Thread {
            while (true) {
                runBlocking { updateTrack() }
                Thread.sleep(10000)
            }
        }
        thread.name = "Current song updater"
        thread.isDaemon = true
        thread.start()
    }

    fun update() {
        playButtonImageView.image = if (Player.isStopped()) {
            playImage
        } else {
            pauseImage
        }

        stationText.text = Stations.current()?.names["uk"]
        tagText.text = Stations.current()?.tags["uk"]
    }

    suspend fun updateTrack() {
        logger.trace("Updating track")
        val currentSongUrl = Stations.current()?.currentSongUrl ?: return

        if (currentSongUrl.isEmpty()) return

        val response = httpClient.get(currentSongUrl)

        if (!response.status.isSuccess()) {
            logger.error("Failed to parse current song...")
            return
        }

         val data = json.decodeFromString<List<SongData>>(response.bodyAsText()).first()

        Platform.runLater {
            trackText.text = data.song
            authorText.text = data.singer
        }
    }
}
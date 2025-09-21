package ua.pp.lumivoid.ui

import javafx.event.EventHandler
import javafx.geometry.Pos
import javafx.scene.control.Button
import javafx.scene.control.Label
import javafx.scene.control.Separator
import javafx.scene.effect.ColorAdjust
import javafx.scene.image.Image
import javafx.scene.image.ImageView
import javafx.scene.layout.HBox
import javafx.scene.layout.VBox
import ua.pp.lumivoid.player.Player

object Controls: VBox() {
    init {
        prefWidth = 400.0 // size same as logo
        alignment = Pos.CENTER_LEFT

        val station = Label("Radio Name").apply { styleClass.addAll("stationText", "station") }
        val tag = Label("Tag").apply { styleClass.addAll("stationText", "tag") }
        val track = Label("Track name").apply { styleClass.addAll("stationText", "track") }
        val author = Label("Author").apply { styleClass.addAll("stationText", "author") }
        val text = VBox(station, tag, Separator(), track, author)

        val hdButton = Button().apply {
            val imageView = ImageView(Image("/icons/hd.png")).apply {
                fitWidth = 50.0
                fitHeight = 50.0
                isPreserveRatio = true
                effect = ColorAdjust().apply {
                    brightness = -0.3
                }
            }
            graphic = imageView

            onAction = EventHandler {
                Player.setHd(!Player.isHd())

                if (Player.isHd()) {
                    imageView.effect = null
                } else {
                    imageView.effect = ColorAdjust().apply {
                        brightness = -0.3
                    }
                }
            }
        }
        val playButton = Button().apply {
            val playImage = Image("/icons/play.png")
            val pauseImage = Image("/icons/pause.png")
            val imageView = ImageView(playImage)
            graphic = imageView

            onAction = EventHandler {
                if (Player.isStopped()) {
                    Player.play()
                    imageView.image = pauseImage
                } else {
                    Player.stop()
                    imageView.image = playImage
                }
            }
        }
        val buttons = HBox(hdButton, playButton).apply { alignment = Pos.CENTER_LEFT }


        children.addAll(text, buttons)
    }
}
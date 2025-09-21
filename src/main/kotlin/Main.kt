package ua.pp.lumivoid

import javafx.application.Application
import javafx.application.Platform
import javafx.scene.Scene
import javafx.scene.image.Image
import javafx.scene.layout.BorderPane
import javafx.stage.Stage
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.slf4j.LoggerFactory
import org.slf4j.bridge.SLF4JBridgeHandler
import ua.pp.lumivoid.player.Player
import ua.pp.lumivoid.player.Stations
import ua.pp.lumivoid.ui.Ui

fun main() {
    SLF4JBridgeHandler.removeHandlersForRootLogger()
    SLF4JBridgeHandler.install()
    Main().main()
}

class Main: Application() {
    private val logger = LoggerFactory.getLogger(javaClass)

    override fun start(stage: Stage) {
        val javaVersion = System.getProperty("java.version")
        val javafxVersion = System.getProperty("javafx.version")

        logger.info("java version: $javaVersion")
        logger.info("javafx version: $javafxVersion")

        val root = BorderPane()
        root.id = "mainRoot"
        val scene = Scene(root)
        scene.stylesheets.add(javaClass.getResource("/styles/style.css")!!.toExternalForm()) // css will be created from scss on compilation

        stage.icons.add(Image(javaClass.getResourceAsStream("/icons/rp_logo.png")))
        stage.title = "Radio Player"
        stage.scene = scene
        stage.minWidth = 960.0
        stage.minHeight = 640.0

        stage.show()

        CoroutineScope(Dispatchers.IO).launch {
            @Suppress("UnusedExpression")
            Player // yearly init for quick loading
        }


        CoroutineScope(Dispatchers.IO).launch { Stations.parse() }.invokeOnCompletion {
            Platform.runLater {
                Ui.add(root)
            }
        }
    }

    fun main() {
        logger.info("Starting!")
        launch()
    }
}
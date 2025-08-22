package ua.pp.lumivoid

import javafx.application.Application
import javafx.scene.Scene
import javafx.scene.control.Label
import javafx.scene.image.Image
import javafx.scene.layout.StackPane
import javafx.stage.Stage
import org.slf4j.LoggerFactory
import org.slf4j.bridge.SLF4JBridgeHandler
import kotlin.jvm.javaClass

fun main() {
    SLF4JBridgeHandler.removeHandlersForRootLogger()
    SLF4JBridgeHandler.install()
    Main().main()
}

class Main: Application() {
    private val logger = LoggerFactory.getLogger(this.javaClass)

    override fun start(stage: Stage) {
        val javaVersion = System.getProperty("java.version")
        val javafxVersion = System.getProperty("javafx.version")

        logger.info("java version: $javaVersion")
        logger.info("javafx version: $javafxVersion")

        stage.icons.add(Image(javaClass.getResourceAsStream("rp_logo.png")))

        val label = Label("JavaFX Version: $javafxVersion")
        val scene = Scene(StackPane(label), 800.0, 600.0)
        stage.scene = scene
        stage.show()
    }

    fun main() {
        logger.info("Starting!")
        launch()
    }
}
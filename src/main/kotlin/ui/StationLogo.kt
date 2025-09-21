package ua.pp.lumivoid.ui

import javafx.scene.image.Image
import javafx.scene.image.ImageView
import javafx.scene.shape.Rectangle
import ua.pp.lumivoid.player.Stations

object StationLogo: ImageView() {

    init {
        fitWidth = 400.0
        fitHeight = 400.0
        isPreserveRatio = true

        clip = Rectangle(fitWidth, fitHeight).apply {
            arcWidth = 50.0
            arcHeight = 50.0
        }

        updateLogo()
    }

    fun updateLogo() {
        image = Image(Stations.current()?.logoUrl, true)
        // TODO: Cache
    }
}
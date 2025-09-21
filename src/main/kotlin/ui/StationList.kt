package ua.pp.lumivoid.ui

import javafx.event.EventHandler
import javafx.geometry.Pos
import javafx.scene.control.ScrollPane
import javafx.scene.image.Image
import javafx.scene.image.ImageView
import javafx.scene.layout.HBox
import ua.pp.lumivoid.player.Stations

object StationList: ScrollPane() {

    init {
        isPannable = true
        isFitToWidth = true
        hbarPolicy = ScrollBarPolicy.NEVER
        vbarPolicy = ScrollBarPolicy.NEVER
        styleClass.add("stationList")

        val content = HBox(20.0).apply {
            alignment = Pos.CENTER
            prefHeight = 130.0
        }

        this.content = content

        val stations = Stations.getStations().stations.sortedBy { it.sortingId }
        stations.forEach { station ->
            if (station.enabled) {
                val button =
                    ImageView(Image(station.logoUrl, true)).apply {
                         // TODO: Cache
                        fitWidth = 120.0
                        fitHeight = 120.0
                        isPreserveRatio = true

                        var dragging = false

                        onMousePressed = EventHandler {
                            dragging = false
                        }
                        onDragDetected = EventHandler {
                            dragging = true
                        }
                        onMouseClicked = EventHandler {
                            if (!dragging) Stations.setStation(station.id)
                        }
                    }
                content.children.add(button)
            }
        }

        onScroll = EventHandler { event ->
            hvalue += event.deltaY * 0.0002
        }
    }
}
package ua.pp.lumivoid.ui

import javafx.geometry.Pos
import javafx.scene.layout.BorderPane
import javafx.scene.layout.HBox
import javafx.scene.layout.Pane

object Ui {
    fun add(root: BorderPane) {
        root.top = StationList
        root.center = HBox(120.0, StationLogo, Controls).apply {
            alignment = Pos.CENTER
        }
    }
}
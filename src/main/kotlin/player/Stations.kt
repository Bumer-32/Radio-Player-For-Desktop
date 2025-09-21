package ua.pp.lumivoid.player

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.engine.cio.CIO
import io.ktor.client.request.get
import io.ktor.http.isSuccess
import javafx.scene.image.Image
import kotlinx.serialization.json.Json
import org.slf4j.LoggerFactory
import ua.pp.lumivoid.Constants
import ua.pp.lumivoid.data.RPData
import ua.pp.lumivoid.data.StationData
import ua.pp.lumivoid.ui.StationLogo

object Stations {
    private val logger = LoggerFactory.getLogger(javaClass)
    private val json = Json { ignoreUnknownKeys = true }
    private val httpClient = HttpClient(CIO)
    private var stations: RPData? = null
    private var currentStation: StationData? = null

    suspend fun parse() {
        logger.info("Parsing stations...")
        val response = httpClient.get(Constants.API_URL)

        if (!response.status.isSuccess()) {
            logger.error("Failed to parse stations.")
            return
        }

        stations = json.decodeFromString<RPData>(response.body())

        if (currentStation == null) {
            val station = stations!!.stations.filter { it.enabled }.minBy { it.sortingId }
            logger.info("Default station is: ${station.names["en"]} (${station.id})")
            setStation(station.id)
        }
    }


    fun setStation(id: Int) {
        currentStation = stations!!.stations.find { it.id == id }
        StationLogo.updateLogo()
        Player.setStation(currentStation!!.streams)
        logger.info("Station: ${ currentStation?.names["en"] } ($id)")
    }

    fun getStations() = stations

    fun current() = currentStation
}
package ua.pp.lumivoid.data

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

//Not all data here, because it seems useless and I don't want to manually add it
// https://api.radioplayer.ua/config

@Serializable
data class RPData(
    val stations: List<StationData>,
)

@Serializable
data class StationData(
    val id: Int,
    val enabled: Boolean,
    @SerialName("sorting_id") val sortingId: Int,
    val url: String,
//    @SerialName("filtering_tags") val filteringTags: List<String>,
    val names: Map<String, String>,
    val tags: Map<String, String>,
    @SerialName("logo") val logoUrl: String,
//    @SerialName("playlist") val playlistUrl: String,
    @SerialName("current_song") val currentSongUrl: String,
    val streams: StreamsData,
)

@Serializable
data class StreamsData(
    val regular: Map<String, String>,
    val hd: Map<String, String>,
)
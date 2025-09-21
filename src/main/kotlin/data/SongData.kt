package ua.pp.lumivoid.data

import kotlinx.serialization.Serializable

@Serializable
data class SongData(val stime: String, val time: String, val singer: String, val song: String, val cover: String,)

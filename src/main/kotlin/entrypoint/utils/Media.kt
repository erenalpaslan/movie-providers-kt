package entrypoint.utils

import common.MediaTypes

sealed class CommonMedia(
    open val title: String,
    open val releaseYear: Int,
    open val imdbId: String,
    open val tmdbId: String,
    val type: MediaTypes
) {
    data class ShowMedia(
        override val title: String,
        override val releaseYear: Int,
        override val imdbId: String,
        override val tmdbId: String,
        val episode: PartInfo,
        val season: PartInfo
    ): CommonMedia(title, releaseYear, imdbId, tmdbId, MediaTypes.SHOW)

    data class MovieMedia (
        override val title: String,
        override val releaseYear: Int,
        override val imdbId: String,
        override val tmdbId: String
    ): CommonMedia(title, releaseYear, imdbId, tmdbId, MediaTypes.MOVIE)
}



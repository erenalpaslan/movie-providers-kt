package providers

import entrypoint.utils.CommonMedia
import providers.sources.EmbedSources

abstract class SourceCreator {
    abstract suspend fun getMovie(media: CommonMedia.MovieMedia): EmbedSources
    abstract suspend fun getShow(media: CommonMedia.ShowMedia): EmbedSources
}

data class Source(
    val embedId: String,
    val url: String
)
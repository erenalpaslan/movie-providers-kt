package providers

import entrypoint.utils.CommonMedia
import providers.sources.EmbedSources

abstract class SourceCreator {
    abstract suspend fun getShow(media: CommonMedia): EmbedSources
    abstract suspend fun getMovie(media: CommonMedia): EmbedSources
}

data class Source(
    val embedId: String,
    val url: String
)
package providers

import entrypoint.utils.CommonMedia

abstract class SourceCreator {
    abstract suspend fun getShow(media: CommonMedia): Source
    abstract suspend fun getMovie(media: CommonMedia): Source
}

data class Source(
    val embedId: String,
    val url: String
)
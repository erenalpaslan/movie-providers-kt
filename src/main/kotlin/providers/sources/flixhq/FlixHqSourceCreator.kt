package providers.sources.flixhq

import common.SourceTypes
import entrypoint.utils.CommonMedia
import io.ktor.client.request.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import network.ScrapeClient
import providers.Source
import providers.SourceCreator
import utils.errors.NotFoundError

class FlixHqSourceCreator: SourceCreator() {
    
    override suspend fun getMovie(media: CommonMedia): Source = withContext(Dispatchers.IO) {
        val id = FlixHqSearch.getFlixHqId(media)
        if (id.isNullOrEmpty())
            throw NotFoundError("No search results matched")


        Source("123123", "121231")
    }

    override suspend fun getShow(media: CommonMedia) = withContext(Dispatchers.IO) {
        val id = FlixHqSearch.getFlixHqId(media)
        if (id.isNullOrEmpty())
            throw NotFoundError("No search results matched")

        Source("123123", "121231")
    }
}
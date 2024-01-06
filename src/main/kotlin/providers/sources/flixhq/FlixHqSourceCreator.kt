package providers.sources.flixhq

import common.ProviderConstants.UPLOADSCRAPER_ID
import entrypoint.utils.CommonMedia
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import providers.SourceCreator
import providers.sources.Embed
import providers.sources.EmbedSources
import providers.sources.flixhq.FlixHqScraper.getFlixhqMovieSources
import providers.sources.flixhq.FlixHqScraper.getFlixhqShowSources
import providers.sources.flixhq.FlixHqScraper.getFlixhqSourceDetails
import utils.errors.NotFoundError

class FlixHqSourceCreator : SourceCreator() {

    override suspend fun getMovie(media: CommonMedia.MovieMedia): EmbedSources = withContext(Dispatchers.IO) {
        val id = FlixHqSearch.getFlixHqId(media)
        if (id.isNullOrEmpty())
            throw NotFoundError("No search results matched")

        val sources = getFlixhqMovieSources(media, id)
        val upcloudStream = sources.filter { it.embed.lowercase() == "upcloud" }

        return@withContext if (upcloudStream.isEmpty())
            throw NotFoundError("upcloud stream not found for flixhq")
        else
            EmbedSources(
                embeds = upcloudStream.map {
                    Embed(
                        embedId = UPLOADSCRAPER_ID,
                        url = getFlixhqSourceDetails(it.episodeId)
                    )
                }
            )
    }

    override suspend fun getShow(media: CommonMedia.ShowMedia) = withContext(Dispatchers.IO) {
        val id = FlixHqSearch.getFlixHqId(media)
        if (id.isNullOrEmpty())
            throw NotFoundError("No search results matched")

        val sources = getFlixhqShowSources(media, id)
        val upcloudStream = sources.filter { it.embed.lowercase() == "server upcloud" }

        return@withContext if (upcloudStream.isEmpty())
            throw NotFoundError("upcloud stream not found for flixhq")
        else
            EmbedSources(
                embeds = upcloudStream.map {
                    Embed(
                        embedId = UPLOADSCRAPER_ID,
                        url = getFlixhqSourceDetails(it.episodeId)
                    )
                }
            )
    }
}
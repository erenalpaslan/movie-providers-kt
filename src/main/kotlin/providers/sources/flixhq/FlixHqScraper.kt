package providers.sources.flixhq

import common.SourceTypes
import entrypoint.utils.CommonMedia
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import providers.SourceCreator
import providers.embeds.ScraperTypes
import providers.embeds.UpcloudScraper
import providers.sources.Embed
import providers.sources.EmbedSources
import utils.errors.NotFoundError

class FlixHqScraper : SourceCreator(SourceTypes.FLIXHQ) {

    override suspend fun getMovie(media: CommonMedia.MovieMedia): EmbedSources = withContext(Dispatchers.IO) {
        val id = FlixHqSearch.getFlixHqId(media)
        if (id.isNullOrEmpty())
            throw NotFoundError("No search results matched")

        val sources = getFlixhqMovieSources(media, id)
        val upcloudStream = sources.filter { it.embed.lowercase() == ScraperTypes.Upcloud.id }

        return@withContext if (upcloudStream.isEmpty())
            throw NotFoundError("upcloud stream not found for flixhq")
        else
            EmbedSources(
                embeds = upcloudStream.map {
                    Embed(
                        embedId = UpcloudScraper.type.id,
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
        val upcloudStream = sources.filter { it.embed.lowercase() == "server ${ScraperTypes.Upcloud.id}" }

        return@withContext if (upcloudStream.isEmpty())
            throw NotFoundError("upcloud stream not found for flixhq")
        else
            EmbedSources(
                embeds = upcloudStream.map {
                    Embed(
                        embedId = UpcloudScraper.type.id,
                        url = getFlixhqSourceDetails(it.episodeId)
                    )
                }
            )
    }
}
package providers.sources.zoechip

import common.ProviderConstants
import entrypoint.utils.CommonMedia
import io.ktor.client.call.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import network.ScrapeClient
import org.jsoup.Jsoup
import providers.scraper.MixdropScraper
import providers.scraper.UpcloudScraper
import providers.scraper.UpstreamScraper
import providers.sources.Embed
import providers.sources.EmbedSources
import providers.sources.SourceLink
import providers.sources.zoechip.ZoeChipSearch.getZoeChipSearchResults
import providers.utils.CompareUtil
import java.net.URL

suspend fun ZoeChipScraper.getZoeChipMovieID(media: CommonMedia.MovieMedia): String? {
    val results = getZoeChipSearchResults(media)

    val matchingItem = results.firstOrNull {
        it.type == "Movie" && CompareUtil.compareMedia(media, it.title, it.year)
    }

    return matchingItem?.id
}

suspend fun ZoeChipScraper.createZoeChipStreamData(media: CommonMedia, id: String): EmbedSources {
    val sources = getZoeChipSources(media, id)
    val embeds = arrayListOf<Embed>()
    sources.forEach {
        val formattedSource = formatSource(it)
        if (formattedSource != null) {
            embeds.add(formattedSource)
        }
    }

    return EmbedSources(embeds)
}

suspend fun ZoeChipScraper.getZoeChipSources(media: CommonMedia, id: String): List<SourceLink> {

    // Movies use /ajax/episode/list/ID
    // Shows use /ajax/episode/servers/ID
    val endpoint = when(media) {
        is CommonMedia.MovieMedia -> "list"
        is CommonMedia.ShowMedia -> "servers"
    }
    val response = ScrapeClient.get {
        url("${type.baseUrl}/ajax/episode/${endpoint}/${id}")
    }.bodyAsText()

    val doc = Jsoup.parse(response)
    val sourcelinks = arrayListOf<SourceLink>()
    doc.select(".nav-item a")
        .map {element ->
            // Movies use data-linkid
            // Shows use data-id

            val idAttribute = when(media) {
                is CommonMedia.MovieMedia -> "data-linkid"
                is CommonMedia.ShowMedia -> "data-id"
            }
            val embedTitle = element.attr("title")
            val linkId = element.attr(idAttribute)
            if (!embedTitle.isNullOrEmpty() && !linkId.isNullOrEmpty()) {
                sourcelinks.add(
                    SourceLink(
                        embed = embedTitle,
                        episodeId = linkId
                    )
                )
            }
        }
    return sourcelinks
}

suspend fun ZoeChipScraper.formatSource(source: SourceLink): Embed? {
    val link = getZoeChipSourceURL(sourceID = source.episodeId)
    val parsedURL = URL(link)

    val embedId = when(parsedURL.host) {
        "rabbitstream.net" -> UpcloudScraper.type.id
        "upstream.to" -> UpstreamScraper.type.id
        "mixdrop.co" -> MixdropScraper.type.id
        else -> null
    }

    if (embedId.isNullOrEmpty())
        return null

    return Embed(
        embedId,
        link
    )
}

suspend fun ZoeChipScraper.getZoeChipSourceURL(sourceID: String): String? {
    val details = ScrapeClient.get {
        url("${type.baseUrl}/ajax/sources/$sourceID")
    }.body<ZoeChipDetailResponse?>()

    if (details?.type != "iframe")
        return null

    return details.link
}
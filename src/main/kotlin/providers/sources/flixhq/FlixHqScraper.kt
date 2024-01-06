package providers.sources.flixhq

import common.ProviderConstants
import entrypoint.utils.CommonMedia
import io.ktor.client.call.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import network.ScrapeClient
import org.jsoup.Jsoup
import providers.sources.SourceLink

object FlixHqScraper {

    suspend fun getFlixhqMovieSources(media: CommonMedia, id: String): List<SourceLink> {
        val episodeParts = id.split("-")
        val episodeId = episodeParts[episodeParts.lastIndex]

        val response = ScrapeClient.get {
            url("${ProviderConstants.FLIX_BASE}/ajax/movie/episodes/${episodeId}")
        }.bodyAsText()

        val doc = Jsoup.parse(response)

        val sourceLinks = doc.select(".nav-item > a").map { query ->
            val embedTitle = query.attr("title")
            val linkId = query.attr("data-linkid")
            if (embedTitle.isNullOrEmpty() && linkId.isNullOrEmpty())
                throw Exception("Invalid sources")

            SourceLink(
                embed = embedTitle,
                episodeId = linkId
            )
        }

        return sourceLinks
    }

    suspend fun getFlixhqSourceDetails(sourceId: String): String {
        return ScrapeClient.get {
            url("${ProviderConstants.FLIX_BASE}/ajax/sources/$sourceId")
        }.body<FlixhqSourceDetail>().link
    }
}
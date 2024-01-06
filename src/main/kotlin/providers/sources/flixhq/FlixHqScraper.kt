package providers.sources.flixhq

import common.ProviderConstants
import entrypoint.utils.CommonMedia
import io.ktor.client.call.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import network.ScrapeClient
import org.jsoup.Jsoup
import org.jsoup.select.NodeFilter
import providers.sources.Episode
import providers.sources.SourceLink
import utils.errors.NotFoundError

object FlixHqScraper {

    suspend fun getFlixhqMovieSources(media: CommonMedia.MovieMedia, id: String): List<SourceLink> {
        val episodeParts = id.split("-")
        val episodeId = episodeParts.last()

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

    suspend fun getFlixhqShowSources(media: CommonMedia.ShowMedia, id: String): List<SourceLink> {
        val episodeParts = id.split("-")
        val episodeId = episodeParts.last()

        val response = ScrapeClient.get {
            url("${ProviderConstants.FLIX_BASE}/ajax/season/list/$episodeId")
        }.bodyAsText()

        val seasonDoc = Jsoup.parse(response)
        val season = seasonDoc.select(".dropdown-item")
            .find {
                it.text() == "Season ${media.season.number}" && it.hasAttr("data-id")
            }?.attr("data-id") ?: throw NotFoundError("season not found")

        val episodesResponse = ScrapeClient.get {
            url("${ProviderConstants.FLIX_BASE}/ajax/season/episodes/$season")
        }.bodyAsText()

        val episodeDoc = Jsoup.parse(episodesResponse)
        val episode = episodeDoc.select(".nav-item > a")
            .map {
                Episode(
                    id = it.attr("data-id"),
                    title = it.attr("title")
                )
            }.find {
                it.title?.startsWith("Eps ${media.episode.number}") == true
            }?.id
        if (episode.isNullOrEmpty()) throw NotFoundError("episode not found")

        val sourceResponse = ScrapeClient.get {
            url("${ProviderConstants.FLIX_BASE}/ajax/episode/servers/$episode")
        }.bodyAsText()

        val sourceDoc = Jsoup.parse(sourceResponse)
        val sourceLinks = arrayListOf<SourceLink>()
        sourceDoc.select(".nav-item > a")
            .map { query ->
                val embedTitle = query.attr("title")
                val linkId = query.attr("data-id")
                if (!embedTitle.isNullOrEmpty() && !linkId.isNullOrEmpty()) {
                    sourceLinks.add(
                        SourceLink(
                            embed = embedTitle,
                            episodeId = linkId
                        )
                    )
                }
            }

        return sourceLinks
    }
}
package providers.sources.zoechip

import entrypoint.utils.CommonMedia
import io.ktor.client.request.*
import io.ktor.client.statement.*
import network.ScrapeClient
import org.jsoup.Jsoup

object ZoeChipSearch {

    suspend fun ZoeChipScraper.getZoeChipSearchResults(media: CommonMedia): List<ZoeChipSearchResult> {
        val titleCleaned = media.title.lowercase().replace(" ", "-")
        val searchResponse = ScrapeClient.get {
            url("${type.baseUrl}/search/$titleCleaned")
        }.bodyAsText()

        val searchDoc = Jsoup.parse(searchResponse)
        val searchResult = arrayListOf<ZoeChipSearchResult>()
        searchDoc.select(".film_list-wrap > .flw-item > .film-detail")
            .map { movie ->
                val anchor = movie.select(".film-name a")
                val info = movie.select(".fd-infor")
                val title = anchor.attr("title")
                val href = anchor.attr("href")
                val type = info.select(".fdi-type").html()
                var year = try {
                    info.select(".fdi-item").firstOrNull()?.html()?.toInt() ?: 0
                }catch (e: Exception) {
                    0
                }
                val id = href.split("-").lastOrNull()

                var isYearValid = true
                try {
                    if (year.toDouble().isNaN()) {
                        if (type == "TV") {
                            year = 0
                        } else {
                            isYearValid = false
                        }
                    }
                } catch (e: Exception) {
                    isYearValid = false
                }
                if (
                    !title.isNullOrEmpty() &&
                    !href.isNullOrEmpty() &&
                    !type.isNullOrEmpty() &&
                    isYearValid &&
                    !id.isNullOrEmpty()
                ) {
                    searchResult.add(
                        ZoeChipSearchResult(
                            title = title,
                            href = href,
                            year = year,
                            id = id,
                            type = type
                        )
                    )
                }
            }
        return searchResult
    }

}
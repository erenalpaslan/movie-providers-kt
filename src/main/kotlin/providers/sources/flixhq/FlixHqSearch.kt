package providers.sources.flixhq

import common.MediaTypes
import common.ProviderConstants
import entrypoint.utils.CommonMedia
import io.ktor.client.request.*
import io.ktor.client.statement.*
import network.ScrapeClient
import org.jsoup.Jsoup
import providers.sources.SourceItem
import providers.utils.CompareUtil

object FlixHqSearch {

    suspend fun getFlixHqId(media: CommonMedia): String? {
        val response = ScrapeClient.get {
            url("${ProviderConstants.FLIX_BASE}/search/${media.title.replace(" ", "-")}")
        }.bodyAsText()

        val doc = Jsoup.parse(response)
        val items = doc.getElementsByClass("flw-item").map { flwItems ->
            val id = flwItems.select("a").attr("href")
            val title = flwItems.select("div.film-detail > h2 > a").attr("title")
            var year = flwItems.getElementsByClass("film-detail").select("div.fd-infor > span:nth-child(1)").text()
            val season = if (year.contains("SS")) {
                val count = try {
                    year.split(" ")[1]
                }catch (e: Exception) {
                    "0"
                }
                year = "2024"
                count
            } else "0"


            SourceItem(id, title, year.toInt(), season.toInt())
        }

        var matchedItem: SourceItem? = null
        for (item in items) {
            val isMatched = when(media) {
                is CommonMedia.MovieMedia -> CompareUtil.compareMedia(media, item.title, item.year)
                is CommonMedia.ShowMedia -> CompareUtil.compareTitle(media.title, item.title) && media.season.number < item.seasons + 1
            }

            if (isMatched) {
                matchedItem = item
                break
            }
        }

        println("Matched ITEM => $matchedItem")
        return matchedItem?.id
    }

}
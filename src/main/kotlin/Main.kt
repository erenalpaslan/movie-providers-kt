import entrypoint.utils.CommonMedia
import io.ktor.client.*
import io.ktor.client.engine.java.*
import io.ktor.client.plugins.logging.*
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.encodeToJsonElement
import providers.embeds.ScraperTypes
import providers.embeds.UpcloudScraper
import providers.embeds.UpcloudSources
import providers.sources.Embed
import providers.sources.flixhq.FlixHqScraper
import providers.sources.zoechip.ZoeChipScraper

suspend fun main(args: Array<String>) {
    println("Hello World!")
    /*val embeds = FlixHqScraper()
        .getMovie(CommonMedia.MovieMedia(
            "Dave Chappelle: The Dreamer",
            2023,
            "12323r2r3",
            "1r23r23r"
        ))*/
    val embedSource = ZoeChipScraper()
        .getMovie(CommonMedia.MovieMedia(
            "Pastacolypse",
            2023,
            "12323r2r3",
            "1r23r23r"
        ))

    println(embedSource)
    embedSource.embeds.firstOrNull()?.let { embed ->
        val streamData = ScraperTypes.scraperById(embed.embedId).scrape(embed)
        println(streamData)
    }
    /*val streamData = UpcloudScraper.scrape(Embed(embedId="upcloud", url="https://rabbitstream.net/embed-4/2TgyvuAn4hik?z="))
    println(streamData)*/

}
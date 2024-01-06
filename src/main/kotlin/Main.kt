import entrypoint.utils.CommonMedia
import io.ktor.client.*
import io.ktor.client.engine.java.*
import io.ktor.client.plugins.logging.*
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
    val embeds = ZoeChipScraper()
        .getMovie(CommonMedia.MovieMedia(
            "Andy Somebody",
            2023,
            "12323r2r3",
            "1r23r23r"
        ))

    println(embeds)
}
import entrypoint.utils.CommonMedia
import entrypoint.utils.PartInfo
import io.ktor.client.*
import io.ktor.client.engine.java.*
import io.ktor.client.plugins.logging.*
import providers.sources.flixhq.FlixHqSearch

suspend fun main(args: Array<String>) {
    println("Hello World!")
    FlixHqSearch.getFlixHqId(
        CommonMedia.ShowMedia(
            "Mad Dogs",
            2019,
            "12323r2r3",
            "1r23r23r",
            season = PartInfo(0, ""),
            episode = PartInfo(0, "")
        )
    )
}
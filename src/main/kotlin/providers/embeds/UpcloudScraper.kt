package providers.embeds

import common.TargetFlags
import io.ktor.client.call.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import kotlinx.serialization.Serializable
import network.ScrapeClient
import providers.Captions
import providers.sources.Embed
import utils.CryptographyUtils
import utils.extensions.isJSON
import java.net.URL
import java.util.Date

object UpcloudScraper : BaseScraper(ScraperTypes.Upcloud) {
    override suspend fun scrape(embed: Embed): EmbedStreams {

        val parsedUrl = URL(embed.url?.replace("embed-5", "embed-4"))
        val dataPath = parsedUrl.path.split("/")
        val dataId = dataPath.last()
        val streamResponse = ScrapeClient.get {
            url("https://${parsedUrl.host}/ajax/embed-4/getSources?id=$dataId")
            headers {
                append("Referer", parsedUrl.toString())
                append("X-Requested-With", "XMLHttpRequest")
            }
        }.body<UpcloudSources>()

        val source = UpcloudSource()
        if (!streamResponse.sources.isJSON()) {
            val scriptJs = ScrapeClient.get {
                url("https://rabbitstream.net/js/player/prod/e4-player.min.js")
                parameters {
                    append("v", Date().time.toString())
                }
            }.bodyAsText()
            val decriptionKey = extractKey(scriptJs)
            if (decriptionKey.isEmpty()) throw Exception("Key extraction failed")

            var extractedKey: String = ""
            var strippedSources = streamResponse.sources
            var totalledOffset = 0
            decriptionKey.forEach { pair ->
                val start = pair.first + totalledOffset
                val end = start + pair.second
                extractedKey += streamResponse.sources?.slice(start..end)
                strippedSources = strippedSources?.replace(streamResponse.sources?.substring(start, end) ?: "", "")
                totalledOffset += pair.second
            }

            val decryptedStream = CryptographyUtils.decryptWithAES(extractedKey, strippedSources ?: "")
            println("DECRYPTED => $decryptedStream")
            //TODO: Implement parse
            /*
            const parsedStream = JSON.parse(decryptedStream)[0];
            if (!parsedStream) throw new Error('No stream found');
            sources = parsedStream;
             */
        }

        if (streamResponse.sources.isNullOrEmpty()) throw Exception("upcloud source not found")
        val captions = arrayListOf<EmbedCaption>()
        for (track in streamResponse.tracks) {
            if (track.kind != "captions") continue
            val type = Captions.getCaptionTypeFromUrl(track.file) ?: continue
            val language = Captions.getLanguageCode(track.label) ?: continue

            captions.add(
                EmbedCaption(
                    id = track.file,
                    language = language,
                    hasCorsRestrictions = false,
                    type = type,
                    url = track.file
                )
            )
        }

        return EmbedStreams(
            stream = listOf(
                Stream(
                    id = "primary",
                    type = "hls",
                    playlist = source.file,
                    flags = listOf(TargetFlags.CORS_ALLOWED),
                    captions = captions
                )
            )
        )
    }

    private fun extractKey(script: String): List<Pair<Int, Int>> {
        val startOfSwitch = script.lastIndexOf("switch")
        val endOfCases = script.indexOf("partKeyStartPosition")
        val switchBody = script.slice(startOfSwitch..endOfCases)
        val nums = arrayListOf<Pair<Int, Int>>()
        ":[a-zA-Z0-9]+=([a-zA-Z0-9]+),[a-zA-Z0-9]+=([a-zA-Z0-9]+);".toRegex().findAll(switchBody)
            .forEach { res ->
                res.groupValues.forEach {group ->
                    val innerNumbers = arrayListOf<Int>()
                    try {
                        println(group)
                        listOf(group[1], group[2]).forEach {
                            val regex = Regex("($it=0x)([a-zA-Z0-9]+)")
                            val varMatches = regex.find(script)
                            val lastMatch = varMatches?.groupValues?.last()
                            if (lastMatch != null) {
                                innerNumbers.add(lastMatch.toInt(16))
                            }
                        }
                        nums.add(innerNumbers[0] to innerNumbers[1])
                    } catch (e: Exception) {
                    }
                }
            }

        println(nums)
        return nums
    }

}

@Serializable
data class UpcloudSources(
    val sources: String?,
    val tracks: List<UpcloudTracks>,
    val encrypted: Boolean,
    val server: Int
)

@Serializable
data class UpcloudTracks(
    val file: String?,
    val label: String?,
    val kind: String?,
    val default: Boolean?
)

data class UpcloudSource(
    val file: String? = null,
    val type: String? = null
)



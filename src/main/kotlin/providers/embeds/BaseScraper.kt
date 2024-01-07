package providers.embeds

import providers.sources.Embed
import providers.sources.EmbedSources

abstract class BaseScraper(val type: ScraperTypes) {
    abstract suspend fun scrape(embed: Embed): EmbedStreams
}
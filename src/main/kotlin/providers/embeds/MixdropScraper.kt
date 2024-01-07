package providers.embeds

import providers.sources.Embed

object MixdropScraper: BaseScraper(ScraperTypes.Mixdrop) {
    override suspend fun scrape(embed: Embed): EmbedStreams {
        return EmbedStreams(emptyList())
    }
}
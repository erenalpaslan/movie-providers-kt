package providers.embeds

import providers.sources.Embed

object UpstreamScraper: BaseScraper(ScraperTypes.Upstream) {
    override suspend fun scrape(embed: Embed): EmbedStreams {
        return EmbedStreams(emptyList())
    }
}
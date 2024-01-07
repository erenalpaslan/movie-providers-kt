package providers.embeds

enum class ScraperTypes(val id: String) {
    Upcloud("upcloud"),
    Upstream("upstream"),
    Mixdrop("mixdrop");

    companion object {
        fun scraperById(id: String): BaseScraper {
            return when(entries.firstOrNull { it.id == id}) {
                Upcloud -> UpcloudScraper
                Upstream -> UpstreamScraper
                Mixdrop -> MixdropScraper
                else -> throw Exception("Unknown scraper")
            }
        }
    }
}
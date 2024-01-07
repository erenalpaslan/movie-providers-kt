package providers.scraper

abstract class BaseScraper(val type: ScraperTypes) {
    abstract suspend fun scrape()
}
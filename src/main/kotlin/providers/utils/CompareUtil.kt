package providers.utils

import entrypoint.utils.CommonMedia
import providers.sources.SourceItem

object CompareUtil {

    private fun normalizeTitle(title: String): String {
        return title.trim()
            .lowercase()
            .replace("/['\":]/g".toRegex(), "")
            .replace("/[^a-zA-Z0-9]+/g".toRegex(), "_")
    }

    fun compareTitle(a: String, b: String): Boolean {
        return normalizeTitle(a) == normalizeTitle(b)
    }

    fun compareMedia(media: CommonMedia, title: String, year: Int): Boolean {
        val isSameYear = media.releaseYear == year
        return compareTitle(media.title, title) && isSameYear
    }
}
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

    fun compareMedia(media: CommonMedia, item: SourceItem): Boolean {
        val isSameYear = media.releaseYear == item.year
        return compareTitle(media.title, item.title) && isSameYear
    }
}
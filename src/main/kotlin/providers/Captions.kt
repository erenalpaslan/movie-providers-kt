package providers

import common.CaptionTypes
import java.util.Locale

object Captions {
    fun getCaptionTypeFromUrl(file: String?): CaptionTypes? {
        return CaptionTypes.entries.firstOrNull { file?.endsWith(".${it}", true) == true }
    }

    fun getLanguageCode(label: String?): String? {
        return try {
            label?.let {
                Locale(it.split(" - ")[0]).language
            }
        }catch (e: Exception) {
            null
        }
    }
}
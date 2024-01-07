package providers.embeds

import common.CaptionTypes
import kotlinx.serialization.Serializable

@Serializable
data class EmbedCaption(
    val id: String?,
    val language: String?,
    val hasCorsRestrictions: Boolean = false,
    val type: CaptionTypes?,
    val url: String?
)

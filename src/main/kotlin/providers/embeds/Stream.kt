package providers.embeds

import common.TargetFlags
import kotlinx.serialization.Serializable

@Serializable
data class EmbedStreams(
    val stream: List<Stream>
)

@Serializable
data class Stream(
    val id: String,
    val type: String,
    val playlist: String?,
    val flags: List<TargetFlags>,
    val captions: List<EmbedCaption>
)

package providers.sources.zoechip

import kotlinx.serialization.Serializable

@Serializable
data class ZoeChipDetailResponse(
    val link: String?,
    val type: String?,
    val sources: List<String>,
    val tracks: List<String>,
    val title: String
)
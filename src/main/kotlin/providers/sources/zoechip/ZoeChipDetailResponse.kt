package providers.sources.zoechip

import kotlinx.serialization.Serializable

@Serializable
data class ZoeChipDetailResponse(
    val link: String?,
    val type: String?
)

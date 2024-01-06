package providers.sources.zoechip

data class ZoeChipSearchResult(
    val title: String,
    val year: Int,
    val id: String?,
    val type: String,
    val href: String
)
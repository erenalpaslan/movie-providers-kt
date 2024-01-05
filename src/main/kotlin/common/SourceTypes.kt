package common

enum class SourceTypes(
    val id: String,
    val sourceName: String,
    val baseUrl: String
) {
    FLIXHQ(
        id = "flixhq",
        sourceName = "FlixHQ",
        baseUrl = ProviderConstants.FLIX_BASE
    ),
    GOMOVIES(
        id = "gomovies",
        sourceName = "GOmovies",
        baseUrl = ProviderConstants.GOMOVIES_BASE
    ),
    LOOKMOOVIE(
        id = "lookmovie",
        sourceName = "LookMovie",
        baseUrl = ProviderConstants.LOOKMOVIE_BASE
    ),
    SHOWBOX(
        id = "showbox",
        sourceName = "Showbox",
        baseUrl = ProviderConstants.SHOWBOX_BASE
    ),
    SMASHYSTREAM(
        id = "smashystream",
        sourceName = "SmashyStream",
        baseUrl = ProviderConstants.SMASHYSTREAM_BASE
    ),
    ZOECHIP(
        id = "zoechip",
        sourceName = "ZoeChip",
        baseUrl = ProviderConstants.ZOECHIP_BASE
    )
}
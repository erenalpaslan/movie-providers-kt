package common

enum class CaptionTypes(val type: String) {
    SRT("srt"),
    VTT("vtt");

    companion object {
        fun find(type: String) = entries.firstOrNull { it.type == type }
    }
}
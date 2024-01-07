package utils.extensions

import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.jsonObject
import java.lang.Exception

fun String?.isJSON(): Boolean {
    return try {
        this?.let { Json.parseToJsonElement(it).jsonObject.isNotEmpty() } ?: false
    }catch (e: Exception) {
        false
    }
}
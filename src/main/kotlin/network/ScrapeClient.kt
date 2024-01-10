package network

import io.ktor.client.*
import io.ktor.client.engine.java.*
import io.ktor.client.plugins.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.plugins.logging.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.*
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.json.Json

object ScrapeClient {
    @OptIn(ExperimentalSerializationApi::class)
    private val scrapeClient = HttpClient(Java) {
        install(Logging) {
            this.level = LogLevel.INFO
            this.logger = object : Logger {
                override fun log(message: String) {
                    println("==============================================")
                    println(message)
                    println("==============================================")
                }
            }
        }
        install(ContentNegotiation) {
            json(Json {
                prettyPrint = true
                isLenient = true
                explicitNulls = false
                ignoreUnknownKeys = true
            })
        }
    }

    suspend fun get(builder: HttpRequestBuilder.() -> Unit): HttpResponse {
        return scrapeClient.get {
            this.builder()
        }
    }

    suspend fun post(builder: HttpRequestBuilder.() -> Unit): HttpResponse {
        return scrapeClient.post {
            this.builder()
        }
    }

    suspend fun delete(builder: HttpRequestBuilder.() -> Unit): HttpResponse {
        return scrapeClient.delete {
            this.builder()
        }
    }

    suspend fun patch(builder: HttpRequestBuilder.() -> Unit): HttpResponse {
        return scrapeClient.patch {
            this.builder()
        }
    }
}
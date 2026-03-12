package infrastructure.charging_station

import application.charging_station.ChargingStationSearchQuery
import application.charging_station.QueryParsingPort
import domain.InternalErrorException
import infrastructure.Config
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.engine.cio.CIO
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.request.*
import io.ktor.client.statement.HttpResponse
import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json

class HuggingFaceQueryParsingAdapter : QueryParsingPort {

    private companion object {
        val HF_SECRET = Config.hfSecret ?: throw InternalErrorException("HF secret is required")
        const val HF_URL = "https://router.huggingface.co/v1/chat/completions"
        const val HF_MODEL = "Qwen/Qwen2.5-7B-Instruct:together"
        const val PROMPT = """
            You are a charging stations query parser. You have to return ONLY a valid JSON following this schema:
            { "intent": "NEAR" or "CLOSEST", "address": string, "filters"?: { "minPowerKw"?: int } }
            Meanings: NEAR = searching multiple charging stations, CLOSEST = searching the closest charging station
            """
        const val NUM_ATTEMPTS = 2
        const val EXTERNAL_SERVICE_ERROR_MESSAGE = "Can't contact external service"
        const val INVALID_LLM_RESPONSE = "Invalid LLM response"
    }

    @Serializable
    data class LlmRequest(val model: String, val temperature: Double, val max_tokens: Int,
                          val messages: List<LlmMessage>)
    @Serializable
    data class LlmResponse(val choices: List<LlmChoice>)
    @Serializable
    data class LlmChoice(val message: LlmMessage)
    @Serializable
    data class LlmMessage(val role: String, val content: String)

    private val client = HttpClient(CIO) {
        install(ContentNegotiation) {
            json(Json {
                prettyPrint = true
                isLenient = true
                ignoreUnknownKeys = true
            })
        }
    }
    private val json = Json { ignoreUnknownKeys = true }

    override suspend fun parse(query: String): ChargingStationSearchQuery {
        println("Query: $query")
        for (i in 0..NUM_ATTEMPTS) {
            try {
                val rawLLMResponse = client
                    .post(HF_URL) {
                        bearerAuth(HF_SECRET)
                        contentType(ContentType.Application.Json)
                        setBody(
                            LlmRequest(
                                model = HF_MODEL,
                                temperature = 0.0,
                                max_tokens = 300,
                                messages = listOf(LlmMessage("system", PROMPT), LlmMessage("user", query))
                            )
                        )
                    }.checkStatus()
                    .body<LlmResponse>()
                    .choices.first().message.content
                println("rawLLMResponse: $rawLLMResponse")
                return json.decodeFromString<ChargingStationQueryDTO>(rawLLMResponse).toInput()
            } catch (e: Exception) {
                println("LLM error: ${e.message}")
                if (i == NUM_ATTEMPTS) {
                    throw InternalErrorException(INVALID_LLM_RESPONSE)
                }
            }
        }
        throw InternalErrorException()
    }

    private fun HttpResponse.checkStatus(): HttpResponse =
        this.also {
            if (!status.isSuccess()) {
                println("Can't contact '$HF_URL': ${status.description}")
                throw InternalErrorException(EXTERNAL_SERVICE_ERROR_MESSAGE)
            }
        }
}
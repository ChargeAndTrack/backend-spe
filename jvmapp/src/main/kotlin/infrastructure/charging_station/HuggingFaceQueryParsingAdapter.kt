package infrastructure.charging_station

import application.charging_station.ChargingStationSearchQuery
import application.charging_station.QueryParsingPort
import domain.InternalErrorException
import infrastructure.AbstractExternalServiceAdapter
import infrastructure.Config
import io.ktor.client.call.body
import io.ktor.client.request.*
import io.ktor.http.*
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json

class HuggingFaceQueryParsingAdapter : AbstractExternalServiceAdapter(), QueryParsingPort {

    private companion object {
        val HF_SECRET = Config.hfSecret ?: throw InternalErrorException("HF secret is required")
        const val HF_URL = "https://router.huggingface.co/v1/chat/completions"
        const val HF_MODEL = "Qwen/Qwen2.5-7B-Instruct:together"
        const val PROMPT = """
            You are a charging stations query parser. You have to return ONLY a valid JSON following this schema:
            { "intent": "NEAR" or "CLOSEST", "address": string, "filters"?: { "minPowerKw"?: int } }
            Meanings: NEAR = searching multiple charging stations, CLOSEST = searching the closest charging station
            """
        const val NUM_ATTEMPTS = 3
        const val DEFAULT_TEMPERATURE = 0.0
        const val DEFAULT_MAX_TOKENS = 300

        const val INVALID_LLM_RESPONSE_MESSAGE = "Invalid LLM response"
    }

    @Serializable
    data class LlmRequest(
        val model: String,
        val temperature: Double,
        val max_tokens: Int,
        val messages: List<LlmMessage>
    )

    @Serializable
    data class LlmResponse(val choices: List<LlmChoice>)

    @Serializable
    data class LlmChoice(val message: LlmMessage)

    @Serializable
    data class LlmMessage(val role: String, val content: String)

    private val jsonParser = Json { ignoreUnknownKeys = true }

    override suspend fun parse(query: String): ChargingStationSearchQuery {
        println("Query: $query")
        for (i in 1..NUM_ATTEMPTS) {
            try {
                return attemptParse(query)
            } catch (e: Exception) {
                println("LLM error: ${e.message}")
                if (i == NUM_ATTEMPTS) {
                    throw InternalErrorException(INVALID_LLM_RESPONSE_MESSAGE)
                }
            }
        }
        throw InternalErrorException()
    }

    private suspend fun attemptParse(query: String): ChargingStationSearchQuery {
        val rawLLMResponse = client
            .post(HF_URL) {
                bearerAuth(HF_SECRET)
                contentType(ContentType.Application.Json)
                setBody(LlmRequest(
                    model = HF_MODEL,
                    temperature = DEFAULT_TEMPERATURE,
                    max_tokens = DEFAULT_MAX_TOKENS,
                    messages = listOf(LlmMessage("system", PROMPT), LlmMessage("user", query))
                ))
            }.checkStatus()
            .body<LlmResponse>()
            .choices.first().message.content
        println("rawLLMResponse: $rawLLMResponse")
        return jsonParser.decodeFromString<ChargingStationQueryDTO>(rawLLMResponse).toDomainEntity()
    }
}
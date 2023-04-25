package com.example.akirabot.model
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import com.squareup.moshi.Moshi
import okhttp3.*
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.IOException

@JsonClass(generateAdapter = true)
data class ChatGPTRequest(
    @Json(name = "model") val model: String,
    @Json(name = "messages") val messages: List<ChatGPTMessage>
)

@JsonClass(generateAdapter = true)
data class ChatGPTMessage(
    @Json(name = "role") val role: String,
    @Json(name = "content") val content: String
)

@JsonClass(generateAdapter = true)
data class ChatGPTResponse(
    @Json(name = "id") val id: String,
    @Json(name = "object") val `object`: String,
    @Json(name = "created") val created: Long,
    @Json(name = "choices") val choices: List<ChatGPTChoice>,
    @Json(name = "usage") val usage: ChatGPTUsage
)

@JsonClass(generateAdapter = true)
data class ChatGPTChoice(
    @Json(name = "index") val index: Int,
    @Json(name = "message") val message: ChatGPTMessage,
    @Json(name = "finish_reason") val finishReason: String
)

@JsonClass(generateAdapter = true)
data class ChatGPTUsage(
    @Json(name = "prompt_tokens") val promptTokens: Int,
    @Json(name = "completion_tokens") val completionTokens: Int,
    @Json(name = "total_tokens") val totalTokens: Int
)

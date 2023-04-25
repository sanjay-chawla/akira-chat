package com.example.akirabot.api

import android.os.AsyncTask
import android.util.Log
import com.example.akirabot.model.ChatGPTRequest
import com.example.akirabot.model.ChatGPTMessage
import com.example.akirabot.model.ChatGPTResponse
import com.squareup.moshi.Moshi
import okhttp3.*
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.IOException

class AkiraAsyncTask(
    private val client: OkHttpClient,
    val message: String,
    val apiKey: String,
    private val callback: AkiraAsyncTaskCallback
) :
    AsyncTask<String, Void?, String>() {

    private val jsonAdapter = Moshi.Builder().build().adapter(ChatGPTRequest::class.java)

    fun sendMessage(message: String, callback: (Response) -> Unit) {

    }

    override fun doInBackground(vararg params: String?): String {
        val request = Request.Builder()
            .url("https://api.openai.com/v1/chat/completions")
            .post(
                jsonAdapter.toJson(
                    ChatGPTRequest(
                        model = "gpt-3.5-turbo",
                        messages = listOf(ChatGPTMessage(role = "user", content = message))
                    )
                ).toRequestBody("application/json".toMediaType())
            )
            .addHeader("Authorization", "Bearer $apiKey")
            .build()

        Log.wtf("Akira Tag", "Sending API Request: $request")

        try {
            client.newCall(request).execute().use { response ->
                if (!response.isSuccessful) throw IOException("Unexpected code $response")
                val responseBody =
                    response.body?.string() ?: throw IOException("Empty response body")
                val chatResponse =
                    Moshi.Builder().build().adapter(ChatGPTResponse::class.java)
                        .fromJson(responseBody)
                        ?: throw IOException("Invalid response body")

                val choice = chatResponse.choices.firstOrNull()
                    ?: throw IOException("No response choices")
                return choice.message.content.trim()
            }
        } catch (e: Exception) {
            return "Error: ${e.message}"
            // message.messageText = getString(R.string.api_error_message)
        }
    }

    override fun onPostExecute(result: String) {
        super.onPostExecute(result)
        if (result.isNotBlank()) {
            callback.onSendMessageResponse(result = result)
        } else {
            callback.onSendMessageError()
        }
    }
}

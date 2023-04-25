package com.example.akirabot

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.widget.EditText
import android.widget.ImageView
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.akirabot.adapter.ChatAdapter
import com.example.akirabot.api.API_KEY.api_key_value
import com.example.akirabot.api.AkiraAsyncTask
import com.example.akirabot.api.AkiraAsyncTaskCallback
import com.example.akirabot.model.ChatGPTResponse
import com.example.akirabot.model.ChatMessage
import com.squareup.moshi.Moshi
import okhttp3.OkHttpClient
import okhttp3.Response
import java.io.IOException

import java.util.concurrent.TimeUnit

class MainActivity : AppCompatActivity(), AkiraAsyncTaskCallback {

    private lateinit var chatAdapter: ChatAdapter
    private lateinit var chatRecyclerView: RecyclerView
    private lateinit var chatEditText: EditText
    private lateinit var sendButton: ImageView
    private lateinit var client: OkHttpClient

    private val chatMessages = mutableListOf<ChatMessage>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chat)
        client = OkHttpClient.Builder()
            .readTimeout(60, TimeUnit.SECONDS)
            .callTimeout(60, TimeUnit.SECONDS)
            .build();
        chatRecyclerView = findViewById(R.id.chat_recycler_view)
        chatEditText = findViewById(R.id.chat_input_field)
        sendButton = findViewById(R.id.send_button)

        chatAdapter = ChatAdapter(chatMessages)
        chatRecyclerView.adapter = chatAdapter
        chatRecyclerView.layoutManager = LinearLayoutManager(this)

        sendButton.setOnClickListener {
            val messageText = chatEditText.text.toString().trim()
            if (messageText.isNotEmpty()) {
                var message = ChatMessage(messageText, true)

                chatAdapter.addMessage(message)
                chatRecyclerView.smoothScrollToPosition(chatAdapter.itemCount)
                chatEditText.setText("")

                // Hide typing animation and show Akira animation
                chatAdapter.setIsTyping(false)
                chatAdapter.setIsLoading(false)
                message = ChatMessage("Akira is typing...", false)
                chatAdapter.addMessage(message)
                chatRecyclerView.smoothScrollToPosition(chatAdapter.itemCount)

                val akiraAsyncTask = AkiraAsyncTask(client, messageText, api_key_value, this)
                akiraAsyncTask.execute()

            }
        }
    }

    override fun onSendMessageResponse(result: String) {
        chatAdapter.setIsLoading(false)
        chatAdapter.setIsApiCalled(true)
        chatAdapter.removeLastMessage()
        val message = ChatMessage(result, false)
        chatAdapter.addMessage(message)
        chatRecyclerView.smoothScrollToPosition(chatAdapter.itemCount)
    }

    override fun onSendMessageError() {
        chatAdapter.setIsLoading(false)
        chatAdapter.setIsApiCalled(true)
        chatAdapter.removeLastMessage()
        val message = ChatMessage("Sorry, I am unable to respond", false)
        chatAdapter.addMessage(message)
        chatRecyclerView.smoothScrollToPosition(chatAdapter.itemCount)
    }
}


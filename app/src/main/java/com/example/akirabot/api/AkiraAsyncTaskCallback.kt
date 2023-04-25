package com.example.akirabot.api

interface AkiraAsyncTaskCallback {
    fun onSendMessageResponse(result: String)
    fun onSendMessageError()
}

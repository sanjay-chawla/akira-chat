package com.example.akirabot.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.ProgressBar
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.airbnb.lottie.LottieAnimationView
import com.example.akirabot.R
import com.example.akirabot.model.ChatMessage

class ChatAdapter(private val messages: MutableList<ChatMessage>) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private var lastPosition = -1
    var isTyping = false
    private var isLoading = false
    private var isApiCalled = false

    companion object {
        const val USER_MESSAGE = 0
        const val AKIRA_MESSAGE = 1
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            USER_MESSAGE -> {
                val view = LayoutInflater.from(parent.context)
                    .inflate(R.layout.user_message_layout, parent, false)
                UserMessageViewHolder(view)
            }

            AKIRA_MESSAGE -> {
                val view = LayoutInflater.from(parent.context)
                    .inflate(R.layout.akira_message_layout, parent, false)
                AkiraMessageViewHolder(view)
            }

            else -> throw IllegalArgumentException("Invalid view type")
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val message = messages[position]

        when (holder.itemViewType) {
            USER_MESSAGE -> {
                val userHolder = holder as UserMessageViewHolder
                userHolder.bind(message, isTyping)
            }

            AKIRA_MESSAGE -> {
                val akiraHolder = holder as AkiraMessageViewHolder
                akiraHolder.bind(message, isLoading, isApiCalled)
            }
        }

        if (holder.bindingAdapterPosition > lastPosition) {
            val anim: Animation = AnimationUtils.loadAnimation(
                holder.itemView.context,
                if (holder.itemViewType == USER_MESSAGE) R.anim.slide_from_right else R.anim.slide_from_left
            )
            holder.itemView.startAnimation(anim)
            lastPosition = holder.bindingAdapterPosition
        }
    }

    override fun getItemViewType(position: Int): Int {
        return if (messages[position].isSentByUser) {
            USER_MESSAGE
        } else {
            AKIRA_MESSAGE
        }
    }

    override fun getItemCount(): Int {
        return messages.size
    }

    fun setIsTyping(isTyping: Boolean) {
        this.isTyping = isTyping
        notifyDataSetChanged()
    }

    fun setIsLoading(isLoading: Boolean) {
        this.isLoading = isLoading
        notifyDataSetChanged()
    }

    fun setIsApiCalled(isApiCalled: Boolean) {
        this.isApiCalled = isApiCalled
        notifyDataSetChanged()
    }

    fun addMessage(message: ChatMessage) {
        messages.add(message)
        notifyItemInserted(messages.size - 1)
    }

    fun removeLastMessage() {
        messages.removeLast()
        notifyItemRemoved(messages.size - 1)
    }

    class UserMessageViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        fun bind(message: ChatMessage, isTyping: Boolean) {
            val textMessage = itemView.findViewById<TextView>(R.id.user_message_text)
            val animationView =
                itemView.findViewById<LottieAnimationView>(R.id.user_message_animation)

            if (isTyping) {
                textMessage.visibility = View.GONE
                animationView.visibility = View.VISIBLE
                animationView.playAnimation()
            } else {
                textMessage.text = message.messageText
                textMessage.visibility = View.VISIBLE
                animationView.visibility = View.GONE
                animationView.cancelAnimation()
            }
        }
    }

    class AkiraMessageViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        fun bind(message: ChatMessage, isLoading: Boolean, isApiCalled: Boolean) {
            val textMessage = itemView.findViewById<TextView>(R.id.akira_message_text)
            val animationView =
                itemView.findViewById<LottieAnimationView>(R.id.akira_message_animation)
            // val progressBar = itemView.findViewById<ProgressBar>(R.id.akira_message_progress)

            if (isLoading) {
                textMessage.visibility = View.VISIBLE
                animationView.visibility = View.VISIBLE
                //progressBar.visibility = View.VISIBLE
            } else {
                if (isApiCalled) {
                    textMessage.text = message.messageText
                    textMessage.visibility = View.VISIBLE
                    animationView.visibility = View.GONE
                    //progressBar.visibility = View.GONE
                    animationView.cancelAnimation()
                } else {
                    textMessage.visibility = View.VISIBLE
                    animationView.visibility = View.VISIBLE
                    //progressBar.visibility = View.GONE
                    animationView.playAnimation()
                }
            }
        }
    }
}


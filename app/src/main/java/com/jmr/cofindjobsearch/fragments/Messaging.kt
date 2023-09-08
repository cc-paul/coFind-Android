package com.jmr.cofindjobsearch.fragments

import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.jmr.cofindjobsearch.R
import com.jmr.cofindjobsearch.helper.SharedHelper
import com.jmr.cofindjobsearch.recycleview.JobAdapter
import com.jmr.cofindjobsearch.recycleview.JobData
import com.jmr.cofindjobsearch.recycleview.MessageAdapter
import com.jmr.cofindjobsearch.recycleview.MessageData
import com.jmr.cofindjobsearch.services.RestAPIServices
import com.jmr.cofindjobsearch.services.Utils
import com.jmr.data.JobSender
import com.jmr.data.MessageDataMe
import com.jmr.data.MessageSender
import java.lang.Exception


class Messaging : Fragment() {
    private lateinit var viewMessaging: View
    private lateinit var lnBack: LinearLayout
    private lateinit var imgProfile: ImageView
    private lateinit var tvFullName: TextView
    private lateinit var etMessage: EditText
    private lateinit var imgSend: ImageView
    private lateinit var rvMessage: RecyclerView

    private var fullName: String? = null
    private var imageLink: String? = null
    private var receiverID: Int? = 0
    private var currentMessage: String? = null
    private val handler = Handler(Looper.getMainLooper())
    private lateinit var runnable: Runnable
    private val seconds:Long = 2000

    private val Utils = Utils()
    private val apiService: RestAPIServices by lazy {
        RestAPIServices()
    }

    private var messageAdapter: MessageAdapter? = null
    private val messageList  = ArrayList<MessageData>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            fullName = it.getString(FULL_NAME)
            imageLink = it.getString(IMAGE_LINK)
            receiverID = it.getInt(RECEIVER_ID)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        viewMessaging = inflater.inflate(R.layout.fragment_messaging, container, false)

        viewMessaging.apply {
            lnBack = findViewById(R.id.lnBack)
            imgProfile = findViewById(R.id.imgProfile)
            tvFullName = findViewById(R.id.tvFullName)
            etMessage = findViewById(R.id.etMessage)
            imgSend = findViewById(R.id.imgSend)
            rvMessage = findViewById(R.id.rvMessage)
        }

        imgSend.apply {
            isEnabled = false
            alpha = 0.50f
        }

        tvFullName.text = fullName

        if (imageLink != "-") {
            Glide.with(requireActivity())
                .load(Uri.parse(imageLink))
                .into(imgProfile)
        }

        etMessage.addTextChangedListener(object: TextWatcher {
            override fun afterTextChanged(s: Editable?) { }
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) { }
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                imgSend.apply {
                    isEnabled = s.toString().trim().isNotEmpty()
                    alpha = if (s.toString().trim().isEmpty()) {
                        0.50f
                    } else {
                        1.0f
                    }
                }
            }
        })

        lnBack.setOnClickListener {
            requireActivity().onBackPressedDispatcher.onBackPressed()
        }

        imgSend.setOnClickListener {
            currentMessage = etMessage.text.toString()
            etMessage.setText("")

            val messageData = MessageSender(
                senderId = SharedHelper.getInt("user_id"),
                receiverId = receiverID!!,
                messageText = currentMessage.toString()
            )

            handler.removeCallbacks(runnable)

            apiService.sendMessage(messageData) { it ->
                if (it!!.success) {
                    messageList.add(MessageData(
                        currentMessage.toString(),
                       0
                    ))

                    Log.e("Message Array",messageList.toString())

                    messageAdapter = MessageAdapter(messageList)
                    rvMessage.layoutManager = LinearLayoutManager(requireContext())
                    rvMessage.adapter = messageAdapter
                    handler.postDelayed(runnable, seconds)
                } else {
                    Utils.showToastMessage(requireContext(),it?.messages?.get(0).toString())
                }
            }
        }

        loadMessage(1)

        return viewMessaging
    }

    override fun onStart() {
        super.onStart()

        // Start the repeating task
        handler.postDelayed(runnable, seconds)
    }

    override fun onStop() {
        super.onStop()

        // Stop the repeating task
        handler.removeCallbacks(runnable)
    }


    override fun onDestroy() {
        super.onDestroy()

        // Release the handler
        handler.removeCallbacks(runnable)
    }


    private fun loadMessage(isAll:Int) {
        if (Utils.hasInternet(this.requireContext())) {
            messageList.clear()
            messageAdapter?.notifyDataSetChanged()

            Log.e("Load Message","$receiverID - ${SharedHelper.getInt("user_id")}")

            apiService.loadMessage(
                receiverID!!,
                SharedHelper.getInt("user_id"),
                isAll
            ) {response ->
                if (response!!.success) {
                    val data = response.data

                    if (data.rows_returned != 0) {
                        val message = data.message

                        message.forEach { mess ->
                            messageList.add(MessageData(
                                mess.messageText,
                                mess.isMine
                            ))
                        }

                        messageAdapter = MessageAdapter(messageList)
                        rvMessage.layoutManager = LinearLayoutManager(requireContext())
                        rvMessage.adapter = messageAdapter

                        val itemCount = messageAdapter?.itemCount ?: 0

                        if (itemCount > 0) {
                            rvMessage.smoothScrollToPosition(itemCount - 1)
                        }
                    }
                }
            }

            runnable = Runnable {
                // Call your function here
                loadMessageOne()

                // Schedule the task to run again after X seconds
                handler.postDelayed(runnable, seconds)
            }
        }
    }

    private fun loadMessageOne() {
        if (Utils.hasInternet(this.requireContext())) {
            try {
                val layoutManager = rvMessage.layoutManager as LinearLayoutManager
                val lastVisibleItemPosition = layoutManager.findLastVisibleItemPosition()
                val isAtBottom = lastVisibleItemPosition == messageAdapter?.itemCount?.minus(1)

                apiService.loadMessage(
                    receiverID!!,
                    SharedHelper.getInt("user_id"),
                    0
                ) { response ->
                    if (response!!.success) {
                        val data = response.data

                        if (data.rows_returned != 0) {
                            val message = data.message

                            message.forEach { mess ->
                                if (mess.isRead != 1 && mess.isMine == 1) {
                                    messageList.add(
                                        MessageData(
                                            mess.messageText,
                                            1
                                        )
                                    )

                                    messageAdapter?.notifyDataSetChanged()

                                    // Scroll to the bottom only if already at the bottom
                                    if (isAtBottom) {
                                        rvMessage.scrollToPosition(
                                            messageAdapter?.itemCount?.minus(1) ?: 0
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            } catch (e:Exception) {}
        }
    }

    companion object {
        private const val FULL_NAME = "full_name"
        private const val IMAGE_LINK = "image_link"
        private const val RECEIVER_ID = "receiver_id"

        @JvmStatic
        fun newInstance(
            receiverID: Int,
            fullName: String,
            imageLink: String
        ) =
            Messaging().apply {
                arguments = Bundle().apply {
                    putInt(RECEIVER_ID, receiverID)
                    putString(FULL_NAME, fullName)
                    putString(IMAGE_LINK, imageLink)
                }
            }
    }
}
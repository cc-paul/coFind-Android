package com.jmr.cofindjobsearch.fragments

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.jmr.cofindjobsearch.R
import com.jmr.cofindjobsearch.helper.SharedHelper
import com.jmr.cofindjobsearch.recycleview.ChatAdapter
import com.jmr.cofindjobsearch.recycleview.ChatData
import com.jmr.cofindjobsearch.recycleview.JobAdapter
import com.jmr.cofindjobsearch.recycleview.JobData
import com.jmr.cofindjobsearch.services.RestAPIServices
import com.jmr.cofindjobsearch.services.Utils
import com.jmr.data.JobSender
import java.util.Currency

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [Inbox.newInstance] factory method to
 * create an instance of this fragment.
 */
class Inbox : Fragment() {
    private lateinit var inboxView: View
    private lateinit var etSearch: EditText
    private lateinit var rvMessage: RecyclerView

    private val Utils = Utils()
    private val apiService: RestAPIServices by lazy {
        RestAPIServices()
    }

    private var chatAdapter: ChatAdapter? = null
    private val chatList  = ArrayList<ChatData>()
    val addedName = HashSet<String>()


    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        inboxView = inflater.inflate(R.layout.fragment_inbox, container, false)

        inboxView.apply {
            etSearch = findViewById(R.id.etSearch)
            rvMessage = findViewById(R.id.rvMessage)
        }

        etSearch.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
            override fun afterTextChanged(s: Editable?) {
                loadChat(s.toString())
            }
        })



        return inboxView
    }

    override fun onResume() {
        super.onResume()

        Handler(Looper.getMainLooper()).postDelayed(
            {
                loadChat("")
            },
            1000 // value in milliseconds
        )
    }

    private fun loadChat(search:String) {
        var currentSearch = "~"

        if (search.trim().isNotEmpty()) {
            currentSearch = search
        }

        currentSearch = currentSearch.replace("'","")

        try {
            chatList.clear()
            chatAdapter?.notifyDataSetChanged()
            addedName.clear()

            apiService.loadChat(SharedHelper.getInt("user_id"),currentSearch) {response ->
                if (response!!.success) {
                    val data = response.data

                    if (data.rows_returned != 0) {
                        val chat = data.message

                        chat.forEach { chat ->
                            if (!addedName.contains(chat.contact_name)) {
                                chatList.add(
                                    ChatData(
                                        chat.contact_name,
                                        chat.last_message,
                                        chat.imageLink,
                                        chat.receiver_id,
                                        chat.sender_id
                                    )
                                )
                                addedName.add(chat.contact_name)
                            }
                        }

                        chatAdapter = ChatAdapter(chatList)
                        rvMessage.layoutManager = LinearLayoutManager(requireContext())
                        rvMessage.adapter = chatAdapter
                    }
                }
            }
        } catch (e:Exception) {
            Log.e("Get Error",e.message.toString())
        }
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment Inbox_Fragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            Inbox().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}
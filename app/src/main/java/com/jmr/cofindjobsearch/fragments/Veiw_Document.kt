package com.jmr.cofindjobsearch.fragments

import android.annotation.SuppressLint
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebView
import android.widget.LinearLayout
import androidx.fragment.app.Fragment
import com.jmr.cofindjobsearch.R


class Veiw_Document : Fragment() {
    private lateinit var viewDocument:View
    private lateinit var wvDocument: WebView
    private lateinit var lnBack: LinearLayout
    private var resumeLink: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            resumeLink = it.getString(RESUME_LINK).toString()
        }

    }

    @SuppressLint("SetJavaScriptEnabled")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        viewDocument = inflater.inflate(R.layout.fragment_veiw_document, container, false)

        viewDocument.apply {
            wvDocument = findViewById(R.id.wvDocument)
            lnBack = findViewById(R.id.lnBack)
        }

        lnBack.setOnClickListener {
            requireActivity().onBackPressedDispatcher.onBackPressed()
        }

//        wvDocument.settings.setJavaScriptEnabled(true)
//        wvDocument.webViewClient = object : WebViewClient() {
//            override fun shouldOverrideUrlLoading(view: WebView?, url: String?): Boolean {
//                view?.loadUrl(url.toString())
//                return true
//            }
//        }
//        wvDocument.loadUrl(resumeLink)

//        wvDocument.settings.javaScriptEnabled = true
//        val pdf = resumeLink
//        wvDocument.loadUrl("http://docs.google.com/gview?embedded=true&url=$pdf")





        return viewDocument
    }


    companion object {
        private const val RESUME_LINK = "resume_link"

        @JvmStatic
        fun newInstance(resumeLink: String) =
            Veiw_Document().apply {
                arguments = Bundle().apply {
                    putString(RESUME_LINK, resumeLink)
                }
            }
    }
}
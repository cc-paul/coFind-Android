package com.jmr.cofindjobsearch.fragments

import android.os.Build
import android.os.Bundle
import android.os.StrictMode
import android.text.TextUtils.replace
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.LinearLayout
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import com.jmr.cofindjobsearch.R
import com.jmr.cofindjobsearch.helper.SharedHelper
import com.jmr.cofindjobsearch.services.RestAPIServices
import com.jmr.cofindjobsearch.services.Utils
import java.util.Properties
import javax.mail.Authenticator
import javax.mail.Message
import javax.mail.MessagingException
import javax.mail.PasswordAuthentication
import javax.mail.Session
import javax.mail.Transport
import javax.mail.internet.AddressException
import javax.mail.internet.InternetAddress
import javax.mail.internet.MimeMessage


class Dialog_Forgot : DialogFragment() {

    lateinit var etEmail: EditText
    lateinit var lnPasswordReset : LinearLayout
    lateinit var lnForgotPass : LinearLayout
    lateinit var lnCancel : LinearLayout
    lateinit var forgotPasswordView: View
    private lateinit var otpFragMan: FragmentManager
    private lateinit var otpFragTrans: FragmentTransaction

    val Utils = Utils()

    private val apiService: RestAPIServices by lazy {
        RestAPIServices()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(STYLE_NO_TITLE, android.R.style.Theme_Holo_Light_Dialog_NoActionBar_MinWidth);
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        forgotPasswordView = inflater.inflate(R.layout.fragment_dialog_forgotpass, container, false)

        forgotPasswordView.apply {
            etEmail = findViewById(R.id.etEmail)
            lnPasswordReset = findViewById(R.id.lnPasswordReset)
            lnCancel = findViewById(R.id.lnCancel)
            lnForgotPass = findViewById(R.id.lnForgotPass)
        }

        otpFragMan = requireActivity().supportFragmentManager
        otpFragTrans = otpFragMan.beginTransaction()

        lnPasswordReset.setOnClickListener {
            if (!Utils.hasInternet(this.requireContext())) {
                Utils.showToastMessage(forgotPasswordView.context, "Please check your internet connection")
                return@setOnClickListener
            }

            if (!Utils.isEmailValid(etEmail.text.toString())) {
                Utils.showToastMessage(forgotPasswordView.context, "Please provide proper email address")
                return@setOnClickListener
            }

            Utils.showProgress(this.requireContext())

            lnCancel.performClick()

            apiService.getOTP(etEmail.text.toString().trim()) {it ->
                if (it!!.success) {
                    sendOTPByEmail(
                        email = etEmail.text.toString(),
                        otp = it.data[1],
                        confirmationMessage = it?.messages?.get(0).toString()
                    )
                }
            }
        }

        lnCancel.setOnClickListener {
            dialog!!.dismiss()
        }

        return forgotPasswordView
    }

    @Throws(AddressException::class)
    private fun sendOTPByEmail(email: String, otp: String,confirmationMessage: String) {
        try {
            val SDK_INT = Build.VERSION.SDK_INT
            if (SDK_INT > 8) {
                val policy = StrictMode.ThreadPolicy.Builder().permitAll().build()
                StrictMode.setThreadPolicy(policy)
                val senderEmail = "cofindrecovery@gmail.com"
                val password = "xfsvuqmulddsdnma"
                val props = Properties()

                props["mail.smtp.auth"] = "true"
                props["mail.smtp.starttls.enable"] = "true"
                props["mail.smtp.host"] = "smtp.gmail.com"
                props["mail.smtp.port"] = "587"
                val session = Session.getInstance(props, object : Authenticator() {
                    override fun getPasswordAuthentication(): PasswordAuthentication {
                        return PasswordAuthentication(senderEmail, password)
                    }
                })
                Thread.currentThread().contextClassLoader = javaClass.classLoader

                val emailBody = """
                    Dear User,
                    
                    We have received a request to reset your account password. To complete the process, please use the following One-Time Password (OTP):

                    OTP: $otp
                    
                    If you didn't request this password reset, please ignore this email.
                    
                    For security reasons, please do not share this OTP with anyone.
        
                    Best regards,
                    COFind Job Search
                """.trimIndent()

                try {
                    val message = MimeMessage(session)
                    message.setFrom(InternetAddress(senderEmail))
                    message.addRecipient(Message.RecipientType.TO, InternetAddress(email))
                    message.subject = "Password Reset OTP"
                    message.setText(emailBody)
                    Transport.send(message)
                    Utils.closeProgress()

                    otpFragTrans.apply {
                        replace(R.id.frLogin,  Change_Password.newInstance(otp,etEmail.text.toString()))
                        addToBackStack("otp")
                        commit()
                    }
                } catch (e: MessagingException) {
                    throw RuntimeException(e)
                    Log.e("Error Message",e.message.toString())
                    Utils.showToastMessage(forgotPasswordView.context, "Unable to send OTP")
                }
            }
        } catch (e: java.lang.Exception) {
            Utils.closeProgress()
            Utils.showToastMessage(forgotPasswordView.context, "Unable to send OTP")
            Log.e("Error Message",e.message.toString())
        }
    }

    override fun onResume() {
        super.onResume()
        dialog?.window?.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
    }
}
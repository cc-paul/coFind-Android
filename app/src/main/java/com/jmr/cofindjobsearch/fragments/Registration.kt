package com.jmr.cofindjobsearch.fragments

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.os.StrictMode
import android.os.StrictMode.ThreadPolicy
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import com.google.android.material.textfield.TextInputLayout
import com.jmr.cofindjobsearch.R
import com.jmr.cofindjobsearch.helper.SharedHelper
import com.jmr.cofindjobsearch.services.RestAPIServices
import com.jmr.cofindjobsearch.services.Utils
import com.jmr.data.RegistrationSender
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

class Registration : Fragment() {
    private lateinit var lnBack: LinearLayout
    private lateinit var etFirstName: EditText
    private lateinit var etMiddleName: EditText
    private lateinit var etLastName: EditText
    private lateinit var etUsername: EditText
    private lateinit var etEmailAddress: EditText
    private lateinit var etMobileNumber: EditText
    private lateinit var etAddress: EditText
    private lateinit var etPassword: EditText
    private lateinit var etConfirmPassword: EditText
    private lateinit var tvPassword: TextView
    private lateinit var tvConfirmPassword: TextView
    private lateinit var txtPassword: TextInputLayout
    private lateinit var txtConfirmPassword: TextInputLayout
    private lateinit var etOTP: EditText
    private lateinit var tvCreateAccount: TextView
    private lateinit var tvUsername: TextView
    private lateinit var lnRegister: LinearLayout
    private lateinit var lnSendEmail: LinearLayout
    private lateinit var lnRegistration: LinearLayout
    private lateinit var viewRegistration: View
    private lateinit var registrationFragMan: FragmentManager
    private lateinit var registrationFragTrans: FragmentTransaction

    private val Utils = Utils()
    private val apiService: RestAPIServices by lazy {
        RestAPIServices()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        viewRegistration = inflater.inflate(R.layout.fragment_registration, container, false)

        registrationFragMan = requireActivity().supportFragmentManager
        registrationFragTrans = registrationFragMan.beginTransaction()

        viewRegistration.apply {
            lnBack = findViewById(R.id.lnBack)
            etFirstName = findViewById(R.id.etFirstName)
            etMiddleName = findViewById(R.id.etMiddleName)
            etLastName = findViewById(R.id.etLastName)
            etUsername = findViewById(R.id.etUsername)
            etEmailAddress = findViewById(R.id.etEmailAddress)
            etMobileNumber = findViewById(R.id.etMobileNumber)
            etAddress = findViewById(R.id.etAddress)
            etPassword = findViewById(R.id.etPassword)
            etConfirmPassword = findViewById(R.id.etConfirmPassword)
            etOTP = findViewById(R.id.etOTP)
            tvCreateAccount = findViewById(R.id.tvCreateAccount)
            lnRegister = findViewById(R.id.lnRegister)
            lnSendEmail = findViewById(R.id.lnSendEmail)
            lnRegistration = findViewById(R.id.lnRegistration)
            tvPassword = findViewById(R.id.tvPassword)
            tvConfirmPassword = findViewById(R.id.tvConfirmPassword)
            txtPassword = findViewById(R.id.txtPassword)
            txtConfirmPassword = findViewById(R.id.txtConfirmPassword)
            tvUsername = findViewById(R.id.tvUsername)
        }

        SharedHelper.apply {
            etEmailAddress.setText(getString("gMail"))
            etFirstName.setText(getString("firstName"))
            etLastName.setText(getString("lastName").replace("null",""))

            if (getInt("isGoogleSignIn",0) == 1) {
                etEmailAddress.isEnabled = false
                etUsername.visibility = View.GONE
                tvUsername.visibility = View.GONE
                txtPassword.visibility = View.GONE
                txtConfirmPassword.visibility = View.GONE
                tvPassword.visibility = View.GONE
                tvConfirmPassword.visibility = View.GONE
            }
        }

        lnBack.setOnClickListener {
            requireActivity().onBackPressedDispatcher.onBackPressed()
        }

        tvCreateAccount.setOnLongClickListener {
            etFirstName.setText("Juan")
            etMiddleName.setText("Dela")
            etLastName.setText("Cruz")
            etUsername.setText("juandelacruz")
            etEmailAddress.setText("paulfelco0@gmail.com")
            etMobileNumber.setText("09495869201")
            etAddress.setText("Quezon City")
            etPassword.setText("BnS28X^1")
            etConfirmPassword.setText("BnS28X^1")
            true
        }

        lnRegister.setOnClickListener {
            if (!Utils.hasInternet(this.requireContext())) {
                Utils.showSnackMessage(lnRegistration,"Please check your internet connection")
                return@setOnClickListener
            }

            Utils.showProgress(requireContext())

            apiService.accountRegistration(RegistrationSender(
                firstName = etFirstName.text.toString(),
                middleName = etMiddleName.text.toString(),
                lastName = etLastName.text.toString(),
                username =  etUsername.text.toString(),
                emailAddress =  etEmailAddress.text.toString(),
                mobileNumber = etMobileNumber.text.toString(),
                address = etAddress.text.toString(),
                password = etPassword.text.toString(),
                rPassword = etConfirmPassword.text.toString(),
                isGoogleSignIn = SharedHelper.getInt("isGoogleSignIn",0).toString()
            )) { it ->
                if (!it!!.success) {
                    var errorMessage = if (it.data != null) {
                        it.data.joinToString("\n")
                    } else {
                        it?.messages?.get(0).toString()
                    }

                    Utils.showSnackMessage(lnRegistration,errorMessage)
                    Utils.closeProgress()
                } else {
                    apiService.getOTP(etEmailAddress.text.toString().trim()) {it ->
                        if (it!!.success) {
                            sendOTPByEmail(
                                email = etEmailAddress.text.toString(),
                                otp = it.data[1],
                                confirmationMessage = it?.messages?.get(0).toString()
                            )
                        }
                    }
                }
            }
        }

        return viewRegistration
    }

    @Throws(AddressException::class)
    private fun sendOTPByEmail(email: String, otp: String,confirmationMessage: String) {
        try {
            val SDK_INT = Build.VERSION.SDK_INT
            if (SDK_INT > 8) {
                val policy = ThreadPolicy.Builder().permitAll().build()
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
                    
                    Thank you for using our services. Please find below the One-Time Password (OTP) that you can use for account registration:
        
                    OTP: $otp
        
                    This OTP is valid for a single use and will expire in a short period for security purposes. Please do not share this OTP with anyone.
        
                    If you did not request this OTP, please ignore this email.
        
                    Best regards,
                    COFind Job Search
                """.trimIndent()

                try {
                    val message = MimeMessage(session)
                    message.setFrom(InternetAddress(senderEmail))
                    message.addRecipient(Message.RecipientType.TO, InternetAddress(email))
                    message.subject = "Your One-Time Password (OTP)"
                    message.setText(emailBody)
                    Transport.send(message)
                    Utils.closeProgress()

                    SharedHelper.putString("otp",otp)
                    SharedHelper.putString("emailAddress",etEmailAddress.text.toString())

                    registrationFragMan.popBackStackImmediate()

                    registrationFragTrans.apply {
                        replace(R.id.frLogin,OTP())
                        addToBackStack("otp")
                        commit()
                    }
                } catch (e: MessagingException) {
                    throw RuntimeException(e)
                    Utils.showSnackMessage(lnRegistration,"Unable to send OTP")
                }
            }
        } catch (e: java.lang.Exception) {
            Utils.closeProgress()
            Utils.showSnackMessage(lnRegistration,"Unable to send OTP")
        }
    }
}
package com.jmr.cofindjobsearch.fragments

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.jmr.cofindjobsearch.MainActivity
import com.jmr.cofindjobsearch.R
import com.jmr.cofindjobsearch.helper.SharedHelper
import com.jmr.cofindjobsearch.services.RestAPIServices
import com.jmr.cofindjobsearch.services.Utils
import com.jmr.data.LoginSender

class Login : Fragment() {
    private lateinit var viewLogin: View
    private lateinit var lnGoogleSignIn: LinearLayout
    private lateinit var lnLogin: LinearLayout
    private lateinit var etEmailUser: EditText
    private lateinit var etPassword: EditText
    private lateinit var tvSignUp: TextView
    private lateinit var clLogin: ConstraintLayout
    private lateinit var tvForgotPassword: TextView
    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var loginFragMan: FragmentManager
    private lateinit var loginFragTrans: FragmentTransaction

    val RC_SIGN_IN: Int = 1
    lateinit var mGoogleSignInClient: GoogleSignInClient
    lateinit var mGoogleSignInOptions: GoogleSignInOptions

    private val Utils = Utils()
    private val apiService: RestAPIServices by lazy {
        RestAPIServices()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        firebaseAuth = FirebaseAuth.getInstance()
        configureGoogleSignIn()
        signOut()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        viewLogin = inflater.inflate(R.layout.fragment_login, container, false)
        val apiService = RestAPIServices()

        viewLogin.apply {
            lnLogin = findViewById(R.id.lnLogin)
            lnGoogleSignIn = findViewById(R.id.lnGoogleSignIn)
            etEmailUser = findViewById(R.id.etEmailUser)
            etPassword = findViewById(R.id.etPassword)
            clLogin = findViewById(R.id.clLogin)
            tvSignUp = findViewById(R.id.tvSignUp)
            tvForgotPassword = findViewById(R.id.tvForgotPassword)
        }

        loginFragMan = requireActivity().supportFragmentManager
        loginFragTrans = loginFragMan.beginTransaction()

        lnLogin.setOnLongClickListener {
            etEmailUser.setText("paulfelco0@gmail.com")
            etPassword.setText("Cofind082993$")
            lnLogin.performClick()
            true
        }

        lnLogin.setOnClickListener {
            if (etEmailUser.text.toString().trim().isEmpty() || etPassword.text.toString().trim().isEmpty()) {
                Utils.showSnackMessage(clLogin,"Please provide Email/Username and Password")
                return@setOnClickListener
            }

            if (!Utils.hasInternet(this.requireContext())) {
                Utils.showSnackMessage(clLogin,"Please check your internet connection")
                return@setOnClickListener
            }

            Utils.showProgress(requireContext())

            val loginInfo = LoginSender(
                username = etEmailUser.text.toString(),
                password = etPassword.text.toString()
            )

            apiService.loginAccount(loginInfo) { it ->
                Utils.closeProgress()
                Utils.showToastMessage(requireContext(),it?.messages?.get(0).toString())

                if (it!!.success) {
                    gotoMainActivity(it.data.user_id)
                }
            }
        }

        tvForgotPassword.setOnClickListener {
            val fragMan : FragmentManager = requireActivity().supportFragmentManager
            val forgotPasswordDialog = Dialog_Forgot()
            forgotPasswordDialog.show(fragMan,"Forgot Password")
        }

        lnGoogleSignIn.setOnClickListener {
            signIn()
        }

        tvSignUp.setOnClickListener {
            SharedHelper.putInt("isGoogleSignIn",0)

            loginFragTrans.apply {
                SharedHelper.apply {
                    putInt("isGoogleSignIn",0)
                    putString("gMail","")
                    putString("firstName","")
                    putString("lastName","")
                }

                replace(R.id.frLogin,Registration())
                addToBackStack("registration")
                commit()
            }
        }

        return viewLogin
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == RC_SIGN_IN) {
            val task: Task<GoogleSignInAccount> = GoogleSignIn.getSignedInAccountFromIntent(data)
            try {
                val account = task.getResult(ApiException::class.java)
                firebaseAuthWithGoogle(account)
            } catch (e: ApiException) {
                Utils.showSnackMessage(clLogin,"Google sign in failed")
            }
        }
    }

    private fun firebaseAuthWithGoogle(acct: GoogleSignInAccount) {
        val credential = GoogleAuthProvider.getCredential(acct.idToken, null)
        firebaseAuth.signInWithCredential(credential).addOnCompleteListener {
            if (it.isSuccessful) {
                val user = firebaseAuth.currentUser
                val email = user?.email
                val displayName = user?.displayName

                val firstName: String?
                val lastName: String?

                if (displayName != null) {
                    val names = displayName.split(" ")
                    if (names.size >= 2) {
                        firstName = names[0]
                        lastName = names[1]
                    } else {
                        firstName = displayName
                        lastName = null
                    }
                } else {
                    firstName = null
                    lastName = null
                }

                Utils.showProgress(requireContext())
                checkGmailAccount(
                    email = email.toString(),
                    firstName = firstName.toString(),
                    lastName = lastName.toString()
                )
            } else {
                Utils.showSnackMessage(clLogin,"Google sign in failed")
            }
        }
    }

    private fun checkGmailAccount(email:String,firstName:String,lastName:String) {
        signOut()
        apiService.checkGmail(email) { it ->
            Utils.closeProgress()

            if (it!!.success) {
                when(it?.messages?.get(0).toString()) {
                    "PROCEED_TO_LOGIN" -> {
                        gotoMainActivity(it.data.user_id)
                    }
                    "PROCEED_TO_REGISTRATION" -> {
                        SharedHelper.apply {
                            putInt("isGoogleSignIn",1)
                            putString("gMail",email)
                            putString("firstName",firstName)
                            putString("lastName",lastName)
                        }

                        loginFragTrans.apply {
                            replace(R.id.frLogin,Registration())
                            addToBackStack("registration")
                            commit()
                        }
                    }
                    "PROCEED_TO_LOGIN_PASSWORD" -> {
                        Utils.showSnackMessage(lnLogin,"This account has been registered in-app. Please login by providing password")
                    }
                }
            }
        }
    }

    private fun gotoMainActivity(user_id: Int) {
        Log.e("User ID",user_id.toString())

        SharedHelper.apply {
            putInt("user_id",user_id)
        }

        activity?.let{
            val intent = Intent (it, MainActivity::class.java)
            it.startActivity(intent)
        }
        activity?.finish()
    }

    private fun configureGoogleSignIn() {
        mGoogleSignInOptions = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()
        mGoogleSignInClient = GoogleSignIn.getClient(requireActivity(), mGoogleSignInOptions)
    }

    private fun signIn() {
        val signInIntent: Intent = mGoogleSignInClient.signInIntent
        startActivityForResult(signInIntent, RC_SIGN_IN)
    }

    private fun signOut() {
        FirebaseAuth.getInstance().signOut()
        mGoogleSignInClient.signOut().addOnCompleteListener {}
    }
}
package com.jmr.cofindjobsearch

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import com.jmr.cofindjobsearch.R
import com.jmr.cofindjobsearch.fragments.Login

class LoginActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        val loginFragMan : FragmentManager = supportFragmentManager
        val loginFragTrans : FragmentTransaction = loginFragMan.beginTransaction()
        val loginFrag = Login()

        loginFragTrans.replace(R.id.frLogin,loginFrag)
        loginFragTrans.commit()
    }
}
package com.example.cs.myapplication.home.activity

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import com.example.cs.myapplication.home.presenter.HomePresenter
import com.example.cs.myapplication.R
import com.example.cs.myapplication.signlanguageinterpreter.SignLanguageInterpreterActivity
import kotlinx.android.synthetic.main.activity_home.*
import javax.inject.Inject

class HomeActivity : AppCompatActivity(), HomePresenter.Display {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)
        onNextButtonClicked()
    }
    @Inject
    override fun onNextButtonClicked() {
        fb.setOnClickListener {
            startActivity(Intent(this, SignLanguageInterpreterActivity::class.java))
        }
    }
}

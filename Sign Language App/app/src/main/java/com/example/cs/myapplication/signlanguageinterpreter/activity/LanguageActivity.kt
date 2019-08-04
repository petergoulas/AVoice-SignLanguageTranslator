package com.example.cs.myapplication.signlanguageinterpreter

import android.os.Bundle
import android.speech.tts.TextToSpeech
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.View
import com.example.cs.myapplication.R
import com.example.cs.myapplication.signlanguageinterpreter.presenter.LanguagePresenter
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.activity_sign_language.*
import java.util.*

class SignLanguageInterpreterActivity : AppCompatActivity(), LanguagePresenter.Display, TextToSpeech.OnInitListener{

    private lateinit var textToSpeech: TextToSpeech
    private var text : String = ""

    private lateinit var database: DatabaseReference
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_language)

        textToSpeech = TextToSpeech(this, this)

        database = FirebaseDatabase.getInstance().reference

        val rootReference = database.child("sign")
        rootReference.addValueEventListener( object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val condition = dataSnapshot.value.toString() != "1" && dataSnapshot.value.toString() != "2" && dataSnapshot.value.toString() != "3"

                if(text == "" && dataSnapshot.value.toString() != "FULLSTOP" && condition) {
                    text = dataSnapshot.value.toString()
                    signLanguageText.text = ""
                }

                else if (dataSnapshot.value.toString() != "FULLSTOP" && condition) {
                    text += " " + dataSnapshot.value.toString()
                }

                signLanguageText.text = text

                if(dataSnapshot.value.toString() == "FULLSTOP" && text != "") {
                    speakOut()
                    text = ""
                }

                val result = textToSpeech!!.setLanguage(Locale.ENGLISH)
                if (result == TextToSpeech.LANG_MISSING_DATA || result == TextToSpeech.LANG_NOT_SUPPORTED) {
                    Log.e("TTS", "The Language specified is not supported!")
                } else {
                    convertTextToSpeech()
                }
            }

            override fun onCancelled(databaseError: DatabaseError) {
                Log.w("Failed to read value.", databaseError.toException())
            }
        })
    }

    public override fun onDestroy() {
        if (textToSpeech != null) {
            textToSpeech.stop()
            textToSpeech.shutdown()
        }
        super.onDestroy()
        speakButton.visibility = View.GONE
    }

    override fun convertTextToSpeech() {
        speakButton.setOnClickListener {
            if(text != "") {
                speakOut()
            }
        }
    }

    override fun onInit(status: Int) {
        if (status == TextToSpeech.SUCCESS) {
            speakButton.visibility = View.VISIBLE
            signLanguageText.visibility = View.VISIBLE
            val result = textToSpeech.setLanguage(Locale.ENGLISH)

            if (result == TextToSpeech.LANG_MISSING_DATA || result == TextToSpeech.LANG_NOT_SUPPORTED) {
                Log.e("TTS", "The Language specified is not supported!")
            } else {
                convertTextToSpeech()
            }
        }
    }

    private fun speakOut() {
        textToSpeech.speak(signLanguageText!!.text.toString(), TextToSpeech.QUEUE_FLUSH, null, "")
    }
}
package com.example.ywna.quiz.ui.quiz

import android.content.Context
import androidx.lifecycle.ViewModel
import java.io.IOException
import java.nio.charset.Charset


class QuizViewModel : ViewModel() {

    fun loadJSONFromAsset(context: Context): String? {
        lateinit var json: String
        val charset: Charset = Charsets.UTF_8
        try {
            val `is` = context.assets.open("questions.json")
            val size = `is`.available()
            val buffer = ByteArray(size)
            `is`.read(buffer)
            `is`.close()
            json = String(buffer, charset)
        } catch (ex: IOException) {
            ex.printStackTrace()
            return null
        }
        return json
    }
}
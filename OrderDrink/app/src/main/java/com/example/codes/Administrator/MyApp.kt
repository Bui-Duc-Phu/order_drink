package com.example.codes.Administrator

import android.app.Application
import android.content.Context
import android.content.res.Configuration
import androidx.appcompat.app.AppCompatDelegate
import com.example.codes.Models.ModeTheme
import com.example.sqlite.DBHelper
import java.util.Locale

// Tạo một class mới kế thừa từ Application


class MyApp : Application() {


    lateinit var mode: String
    lateinit var lang: String
    lateinit var data: DBHelper
    override fun onCreate() {
        super.onCreate()
        mode = ModeTheme.light.toString() // Gán giá trị mặc định cho mode
        lang = "vi"

        data = DBHelper(this, null)

        val modeList = data.getModeList()

        if (modeList.isEmpty()) {
            createValue()
        } else {
            mode = modeList[0]
            lang = modeList[1]
        }
        if (mode.equals(ModeTheme.dark.toString())) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
        } else {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        }

        changeLang(applicationContext,lang)
    }


    fun createValue() {
        val data: DBHelper = DBHelper(this, null)
        data.addName("1", "light")
        data.addName("2", "vi")
    }


    fun changeLang(context: Context, lang: String) {
        val myLocale = Locale(lang)
        Locale.setDefault(myLocale)
        val configuration = Configuration(context.resources.configuration)
        configuration.setLocale(myLocale)
        context.resources.updateConfiguration(configuration, context.resources.displayMetrics)
    }
}







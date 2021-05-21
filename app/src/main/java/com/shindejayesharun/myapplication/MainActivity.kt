package com.shindejayesharun.myapplication

import DataPrefrences
import android.content.res.Configuration
import android.content.res.Resources
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.widget.ArrayAdapter
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.launch
import java.util.*


class MainActivity : AppCompatActivity() {
    var languages = listOf<String>("en", "hi", "mr")
    lateinit var appPreferences: DataPrefrences
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        appPreferences = DataPrefrences(this)
        GlobalScope.launch(Dispatchers.Main) {
            var locale = appPreferences.lastAppLanguage
            setApplicationLocale(locale.first())
        }
        buildLanguagesView()
    }

    private fun buildLanguagesView() {
        lv_languages.adapter = ArrayAdapter<String>(
            this,
            android.R.layout.simple_list_item_1,
            android.R.id.text1,
            languages
        )
        lv_languages.setOnItemClickListener { parent, view, position, id ->
            setApplicationLocale(
                languages.get(
                    position
                )
            )
        }
    }

    private fun setApplicationLocale(locale: String) {
        Log.d("lan", locale)
        val resources: Resources = resources
        val config: Configuration = resources.getConfiguration()
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            config.setLocale(Locale(locale.toLowerCase()))
        } else {
            config.locale = Locale(locale.toLowerCase())
        }
        onConfigurationChanged(config)

        //store app language code on data prefrence (shared prefrence)
        GlobalScope.launch(Dispatchers.Main) {
            appPreferences.saveAppLanguage(locale)
        }

    }

    override fun onConfigurationChanged(newConfig: Configuration) {
        getBaseContext().getResources()
            .updateConfiguration(newConfig, getBaseContext().getResources().getDisplayMetrics())
        tv_hello_world.setText(R.string.hello_world)
        buildLanguagesView()
        super.onConfigurationChanged(newConfig)
    }
}
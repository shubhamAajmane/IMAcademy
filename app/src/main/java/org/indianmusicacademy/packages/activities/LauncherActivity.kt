package org.indianmusicacademy.packages.activities

import android.app.ActivityOptions
import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.widget.ImageView
import androidx.annotation.RequiresApi
import org.indianmusicacademy.packages.R

class LauncherActivity : AppCompatActivity() {

    lateinit var logo:ImageView

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_launcher)

        logo = findViewById(R.id.ivLogo)

        Handler().postDelayed(
            {
                val intent = Intent(this, LoginActivity::class.java)
                val options = ActivityOptions.makeSceneTransitionAnimation(this, logo,getString(R.string.logo_transition))
                startActivity(intent,options.toBundle())
                finish()
            }, 1000
        )
    }
}
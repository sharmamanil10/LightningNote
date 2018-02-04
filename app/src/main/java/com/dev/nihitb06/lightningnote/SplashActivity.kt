package com.dev.nihitb06.lightningnote

import android.content.Intent
import android.os.Bundle
import com.dev.nihitb06.lightningnote.themeutils.ThemeActivity

class SplashActivity : ThemeActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        startActivity(Intent(this, MainActivity::class.java))
        finish()
    }
}

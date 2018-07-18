package com.dev.nihitb06.lightningnote.aboutapp

import android.app.Dialog
import android.content.ActivityNotFoundException
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.support.constraint.ConstraintLayout
import android.support.design.widget.Snackbar
import com.dev.nihitb06.lightningnote.BuildConfig
import com.dev.nihitb06.lightningnote.R
import com.dev.nihitb06.lightningnote.themeutils.ThemeActivity
import com.google.android.gms.oss.licenses.OssLicensesMenuActivity
import kotlinx.android.synthetic.main.activity_about_app.*
import java.net.MalformedURLException

class AboutAppActivity : ThemeActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setNoActionBarTheme()
        setContentView(R.layout.activity_about_app)

        setToolbar()

        val versionName = BuildConfig.VERSION_NAME
        appVersion.text = getString(R.string.version_placeholder, versionName)

        openSourceLicenses.setOnClickListener {
            startActivity(Intent(this@AboutAppActivity, OssLicensesMenuActivity::class.java))
        }
        assetsUsed.setOnClickListener {
            val dialog = Dialog(this@AboutAppActivity)

            dialog.setContentView(R.layout.layout_image_assets_used)

            dialog.findViewById<ConstraintLayout>(R.id.assetItem).setOnClickListener {
                try {
                    startActivity(Intent(Intent.ACTION_VIEW).setData(Uri.parse(getString(R.string.asset_link))))
                } catch (e: MalformedURLException) {
                    e.printStackTrace()
                    Snackbar.make(root, "Something Went Wrong", Snackbar.LENGTH_SHORT).show()
                } catch (e: ActivityNotFoundException) {
                    Snackbar.make(root, "No Activity can Handle the Action", Snackbar.LENGTH_SHORT).show()
                }
            }

            dialog.show()
        }
        privacyPolicy.setOnClickListener {
            try {
                startActivity(Intent(Intent.ACTION_VIEW).setData(Uri.parse(getString(R.string.policy_link))))
            } catch (e: MalformedURLException) {
                e.printStackTrace()
                Snackbar.make(root, "Something Went Wrong", Snackbar.LENGTH_SHORT).show()
            } catch (e: ActivityNotFoundException) {
                Snackbar.make(root, "No Activity can Handle the Action", Snackbar.LENGTH_SHORT).show()
            }
        }
    }

    private fun setToolbar() {
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayShowHomeEnabled(true)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        toolbar.setNavigationOnClickListener { onBackPressed() }
    }
}

package com.dev.nihitb06.lightningnote

import android.app.Fragment
import android.os.Bundle
import android.os.Handler
import android.support.design.widget.NavigationView
import android.support.v4.view.GravityCompat
import android.support.v7.app.ActionBarDrawerToggle
import android.view.MenuItem
import android.view.View
import com.dev.nihitb06.lightningnote.appsettings.SettingsFragment
import com.dev.nihitb06.lightningnote.notes.NotesFragment
import com.dev.nihitb06.lightningnote.themeutils.ThemeActivity
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : ThemeActivity(), NavigationView.OnNavigationItemSelectedListener {

    private var isNotesFragmentShowing = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setNoActionBarTheme()
        setContentView(R.layout.activity_main)

        setupToolbarAndNavigation()

        onNavigationItemSelected(navBarContainer.menu.findItem(R.id.notes))
    }

    private fun setupToolbarAndNavigation() {
        setSupportActionBar(toolbar)

        val drawerToggle = object: ActionBarDrawerToggle(
                this,
                drawerLayout,
                toolbar,
                R.string.drawer_open,
                R.string.drawer_close
        ) {
            override fun onDrawerOpened(drawerView: View) {
                //super.onDrawerOpened(drawerView)
                super.onDrawerSlide(drawerView, 0f)
            }

            override fun onDrawerSlide(drawerView: View, slideOffset: Float) {
                super.onDrawerSlide(drawerView, 0f)
            }
        }

        drawerLayout.addDrawerListener(drawerToggle)
        drawerToggle.syncState()

        navBarContainer.setNavigationItemSelectedListener(this)
    }

    private fun switchFragment(fragment: Fragment, notesFragment: Boolean) {
        fragmentManager.beginTransaction().replace(R.id.fragmentContainer, fragment).commit()

        isNotesFragmentShowing = notesFragment
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when(item.itemId) {
            R.id.notes -> switchFragment(NotesFragment.newInstance(this, false), true)

            R.id.starred -> switchFragment(NotesFragment.newInstance(this, true), false)

            R.id.settings -> switchFragment(SettingsFragment.newInstance(), false)

            else -> return false
        }

        setCheck(item)
        closeDrawer()
        return true
    }

    private fun setCheck(item: MenuItem) {
        Handler().post({
            for(index in 0..(navBarContainer.menu.size()-1)) {
                navBarContainer.menu.getItem(index).isChecked = false
            }

            item.isChecked = true
        })
    }

    private fun closeDrawer() { drawerLayout.closeDrawer(GravityCompat.START) }

    override fun onBackPressed() {
        when {
            drawerLayout.isDrawerOpen(GravityCompat.START) -> closeDrawer()

            !isNotesFragmentShowing -> onNavigationItemSelected(navBarContainer.menu.findItem(R.id.notes))

            else -> super.onBackPressed()
        }
    }
}

/*
 *
 *  *
 *  *  * Copyright (c) 2019 Pascual Montesinos http://pasmodev.com
 *  *  *
 *  *  * Licensed to the Apache Software Foundation (ASF) under one
 *  *  * or more contributor license agreements.  See the NOTICE file
 *  *  * distributed with this work for additional information
 *  *  * regarding copyright ownership.  The ASF licenses this file
 *  *  * to you under the Apache License, Version 2.0 (the
 *  *  * "License"); you may not use this file except in compliance
 *  *  * with the License.  You may obtain a copy of the License at
 *  *  *
 *  *  *   http://www.apache.org/licenses/LICENSE-2.0
 *  *  *
 *  *  * Unless required by applicable law or agreed to in writing,
 *  *  * software distributed under the License is distributed on an
 *  *  * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 *  *  * KIND, either express or implied.  See the License for the
 *  *  * specific language governing permissions and limitations
 *  *  * under the License.
 *  *
 *
 */

package com.pasmodev.template.ui

import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import com.google.android.material.navigation.NavigationView
import com.google.android.material.snackbar.Snackbar
import com.pasmodev.template.App
import com.pasmodev.template.R
import com.pasmodev.template.R.string.navigation_drawer_close
import com.pasmodev.template.R.string.navigation_drawer_open
import com.pasmodev.template.di.components.DaggerMainComponent
import com.pasmodev.template.di.modules.MainModule
import com.pasmodev.presentation.dto.UserDto
import com.pasmodev.presentation.presenter.MainPresenter
import com.pasmodev.presentation.view.MainView
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.app_bar_main.*
import kotlinx.android.synthetic.main.nav_header_main.view.*
import javax.inject.Inject


class MainActivity : AppCompatActivity(), MainView, NavigationView.OnNavigationItemSelectedListener {

    val component by lazy {
        DaggerMainComponent.builder()
                .appComponent((application as App).component)
                .mainModule(MainModule(this))
                .build()
    }

    @Inject
    lateinit var presenter: MainPresenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        component.inject(this)

        setSupportActionBar(toolbar)

        fab?.setOnClickListener { view ->
            Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                    .setAction("Action", null)
                    .show()
        }

        val toggle = ActionBarDrawerToggle(
                this,
                drawerLayout,
                toolbar,
                navigation_drawer_open,
                navigation_drawer_close
        )
        drawerLayout?.addDrawerListener(toggle)
        toggle.syncState()

        navigationView?.setNavigationItemSelectedListener(this)
    }

    override fun onBackPressed() {
        if (drawerLayout != null) {
            if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
                drawerLayout.closeDrawer(GravityCompat.START)
            } else {
                super.onBackPressed()
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.main, menu)
        return true
    }


    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return if (item.itemId == R.id.action_settings) true else super.onOptionsItemSelected(item)
    }

    override fun onStart() {
        super.onStart()
        presenter.onStart()
    }

    override fun onStop() {
        presenter.onStop()
        super.onStop()
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.nav_account -> {
                drawerLayout.closeDrawer(GravityCompat.START)
                return true
            }
            R.id.nav_info -> {
                drawerLayout.closeDrawer(GravityCompat.START)
                return true
            }
            R.id.nav_help -> {
                drawerLayout.closeDrawer(GravityCompat.START)
                return true
            }
            R.id.nav_logout -> {
                presenter.onLogoutAction()
                drawerLayout.closeDrawer(GravityCompat.START)
                return true
            }
            else -> return false
        }
    }

    override fun close() {
        finish()
    }

    override fun showError(error: String) {
        runOnUiThread {
            val snackbar = Snackbar.make(mainLayout, error, Snackbar.LENGTH_INDEFINITE);
            snackbar.setAction(android.R.string.ok) { snackbar.dismiss() }
                    .setActionTextColor(Color.RED)
                    .show()
        }
    }

    override fun showUser(user: UserDto) {
        Log.d(MainActivity::class.java.simpleName, "Show User $user")

        val header = navigationView.getHeaderView(0)

        runOnUiThread {
            header.navUserName.text = user.name
            header.navUserEmail.text = user.email
        }
    }

}

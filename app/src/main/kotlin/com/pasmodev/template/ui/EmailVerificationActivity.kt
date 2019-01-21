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

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import com.google.android.material.snackbar.Snackbar
import androidx.appcompat.app.AppCompatActivity
import android.view.View
import com.pasmodev.template.App
import com.pasmodev.template.R
import com.pasmodev.template.di.components.DaggerEmailVerificationComponent
import com.pasmodev.template.di.modules.EmailVerificationModule
import com.pasmodev.presentation.presenter.EmailVerificationPresenter
import com.pasmodev.presentation.view.EmailVerificationView
import kotlinx.android.synthetic.main.activity_email_verification.*
import javax.inject.Inject

class EmailVerificationActivity : EmailVerificationView, AppCompatActivity() {

    val component by lazy {
        DaggerEmailVerificationComponent.builder()
                .appComponent((application as App).component)
                .emailVerificationModule(EmailVerificationModule(this))
                .build()
    }

    @Inject lateinit var presenter: EmailVerificationPresenter

    override fun showIndeterminateProgress(show: Boolean) {
        runOnUiThread {
            progressBar.visibility = if (show) View.VISIBLE else View.GONE
        }
    }

    override fun navigateToMain() {
        val i = Intent(this, MainActivity::class.java)
        finish()
        startActivity(i)
    }

    override fun showError(error: String) {
        runOnUiThread {
            val snackbar = Snackbar.make(emailVerificationLayout, error, Snackbar.LENGTH_INDEFINITE);
            snackbar.setAction(android.R.string.ok) { snackbar.dismiss() }
                    .setActionTextColor(Color.RED)
                    .show()
        }    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_email_verification)

        component.inject(this)
    }

    override fun onStart() {
        super.onStart()
        presenter.onStart()
    }

    override fun onStop() {
        presenter.onStop()
        super.onStop()
    }

    private fun showMessage(message: String){
        runOnUiThread {
            Snackbar.make(emailVerificationLayout, message, Snackbar.LENGTH_LONG).show()
        }
    }
}

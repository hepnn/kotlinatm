package com.martins.homework

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.martins.homework.databinding.ActivityMainBinding
import com.martins.homework.utils.*

class MainActivity : AppCompatActivity() {

    private var sharedPreference: SharedPreference? = null

        private lateinit var binding: ActivityMainBinding
        private lateinit var pinInput: EditText
        private lateinit var loginButton: Button
        private lateinit var resetButton: Button

        override fun onCreate(savedInstanceState: Bundle?) {
            super.onCreate(savedInstanceState)
            binding = ActivityMainBinding.inflate(layoutInflater)
            setContentView(binding.root)

            sharedPreference = SharedPreference(this)

            pinInput = findViewById(R.id.pinInput)
            loginButton = findViewById(R.id.loginButton)
            resetButton = findViewById(R.id.resetButton)


            resetButton.setOnClickListener {
                sharedPreference!!.clearSharedPreference()
                Toast.makeText(this, PIN_RESET, Toast.LENGTH_SHORT).show()
            }

            loginButton.setOnClickListener {
                val pinId = pinInput.text.toString()

                if (pinId.isEmpty()) {
                    Toast.makeText(this, EMPTY_FIELD, Toast.LENGTH_SHORT).show()
                } else {
                    val userPin = sharedPreference!!.getPreferenceString(PIN_CODE)

                    if (userPin.equals(pinId)) {
                        sharedPreference!!.saveString("login_status", "1")

                        val intent = Intent(this, LoginActivity::class.java)
                        startActivity(intent)
                        finish()
                    } else {
                        Toast.makeText(this, INVALID_PIN, Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }
    }

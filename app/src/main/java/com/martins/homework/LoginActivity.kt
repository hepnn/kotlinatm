package com.martins.homework

import android.app.Dialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.Window
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.core.view.isVisible
import com.martins.homework.databinding.ActivityLoginBinding
import com.martins.homework.utils.*

class LoginActivity : AppCompatActivity() {  //todo: use bindings in dialogs

    var sharedPreference: SharedPreference? = null

    private lateinit var binding: ActivityLoginBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        sharedPreference = SharedPreference(this)

        binding.btnbalance.setOnClickListener {
            binding.tvbalance.toggleVisibility()
        }
        binding.btnWithdraw.setOnClickListener {
            withdrawBalance()
        }
        binding.btnDeposit.setOnClickListener {
            depositDialog()
        }
        binding.btnChangepin.setOnClickListener {
            changePinDialog()
        }
        binding.btnLogout.setOnClickListener {
            logOut()
        }
    }

    private fun View.toggleVisibility() {
        if (this.isVisible) {
            this.visibility = View.INVISIBLE
        } else {
            this.visibility = View.VISIBLE
        }
    }

    private fun withdrawBalance() {
        val withdrawInput = binding.withdrawInput.text.toString().toInt()
        val balance = binding.tvbalance.text.toString().toInt()

        when {
            withdrawInput.equals("") ->
                Toast.makeText(this, EMPTY_FIELD, Toast.LENGTH_SHORT).show()
            withdrawInput <= balance ->   //todo: Fix App crash on button press if input field is empty
                withdrawDialog()
            else ->
                Toast.makeText(this, BALANCE_ERROR, Toast.LENGTH_SHORT).show()
        }
    }

    private fun logOut() {
        Toast.makeText(this, "Logged out.", Toast.LENGTH_SHORT).show()
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
        finish()
    }

    private fun withdrawDialog() {
        val dialog = Dialog(this)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setCancelable(true)
        dialog.setContentView(R.layout.withdraw_dialog)
        val cancelButton = dialog.findViewById<Button>(R.id.cancelButton)
        val withdrawButton = dialog.findViewById<Button>(R.id.withdrawButton)
        val pinInput = dialog.findViewById<TextView>(R.id.pinInput)

        cancelButton.setOnClickListener {
            dialog.dismiss()
        }
        withdrawButton.setOnClickListener {
            val userPin = sharedPreference!!.getPreferenceString(PIN_CODE)
            val balance = binding.tvbalance.text.toString().toInt()
            val withdrawInput = binding.withdrawInput.text.toString().toInt()
            val pinInput = pinInput.text.toString()

            if (pinInput == userPin) {
                binding.tvbalance.text = (balance - withdrawInput).toString()
                dialog.dismiss()
            } else {
                Toast.makeText(this, PIN_ERROR, Toast.LENGTH_SHORT).show()
            }
        }
        dialog.show()

    }

    private fun depositDialog() {
        val dialog = Dialog(this)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setCancelable(true)
        dialog.setContentView(R.layout.deposit_dialog)
        val cancelButton = dialog.findViewById<Button>(R.id.cancelButton)
        val depositButton = dialog.findViewById<Button>(R.id.depositButton)
        val pinInput = dialog.findViewById<TextView>(R.id.pinInput)

        cancelButton.setOnClickListener {
            dialog.dismiss()
        }
        depositButton.setOnClickListener {
            val userPin = sharedPreference!!.getPreferenceString(PIN_CODE)
            val balance = binding.tvbalance.text.toString().toInt()
            val depositAmount = binding.depositInput.text.toString().toInt()
            val pinInput = pinInput.text.toString()

            if (pinInput == userPin) {
                binding.tvbalance.text = (balance + depositAmount).toString() //Todo: Find a fix for this
                dialog.dismiss()
            } else {
                Toast.makeText(this, PIN_ERROR, Toast.LENGTH_SHORT).show()
            }
        }
        dialog.show()

    }

    private fun changePinDialog() {
        val dialog = Dialog(this)
        dialog.show()
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setCancelable(true)
        dialog.setContentView(R.layout.change_pin)

        val cancelButton = dialog.findViewById<Button>(R.id.cancelButton)
        val changeButton = dialog.findViewById<Button>(R.id.changeButton)
        val tv_confirm_pin = dialog.findViewById<TextView>(R.id.tv_confirm_pin)
        val tv_new_pin = dialog.findViewById<TextView>(R.id.tv_new_pin)
        val tv_enter_pin = dialog.findViewById<TextView>(R.id.tv_enter_pin)

        cancelButton.setOnClickListener {
            dialog.dismiss()
        }
        changeButton.setOnClickListener {
            val userPin = sharedPreference!!.getPreferenceString(PIN_CODE)
            val currentPin = tv_enter_pin.text.toString()
            val confirmNewPin = tv_confirm_pin.text.toString()
            val newPin = tv_new_pin.text.toString()

            when {
                currentPin == "" || newPin == "" || confirmNewPin == "" -> {
                    Toast.makeText(this, EMPTY_FIELD, Toast.LENGTH_SHORT).show()
                }
                !userPin.equals(currentPin) -> {
                    Toast.makeText(this, INVALID_PIN, Toast.LENGTH_SHORT).show()
                }
                newPin != confirmNewPin -> {
                    Toast.makeText(this, PIN_MATCH, Toast.LENGTH_SHORT).show()
                }
                else -> {
                    sharedPreference!!.saveString(PIN_CODE, confirmNewPin)
                    Toast.makeText(this, DATA_SAVED, Toast.LENGTH_SHORT).show()
                    dialog.dismiss()
                }
            }
        }
    }
}

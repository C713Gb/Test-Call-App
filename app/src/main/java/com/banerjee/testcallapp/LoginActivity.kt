package com.banerjee.testcallapp

import android.app.ProgressDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.widget.Toast
import com.banerjee.testcallapp.model.UserAuthDataClasses
import com.banerjee.testcallapp.network.ApiCallInterface
import com.banerjee.testcallapp.network.RetrofitClientInstance
import com.google.gson.Gson
import kotlinx.android.synthetic.main.activity_login.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.lang.Exception

class LoginActivity : AppCompatActivity() {

    var email = ""
    var pwd = ""
    private lateinit var apiCallInterface: ApiCallInterface
    private val TAG = "LoginActivity"
    private lateinit var progressDialog: ProgressDialog

    override fun onStart() {
        super.onStart()
        if (SharedPreference().getUserStatusCode(this@LoginActivity) == "1"){
            goToHomePage()
            finish()
        }
    }

    private fun goToHomePage() {
        val intent = Intent(this@LoginActivity, HomeActivity::class.java)
        startActivity(intent)
    }

    private fun goToSignupPage() {
        val intent = Intent(this@LoginActivity, SignupActivity::class.java)
        startActivity(intent)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        progressDialog = ProgressDialog(this@LoginActivity)
        progressDialog.setCanceledOnTouchOutside(false)
        progressDialog.setCancelable(false)

        apiCallInterface = RetrofitClientInstance()
            .getRetrofitInstance()?.create(ApiCallInterface::class.java)!!

        email_txt_2.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if (s != null) {
                    email = s.toString()
                }
            }

            override fun afterTextChanged(s: Editable?) {

            }

        })

        pwd_txt_2.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if (s != null) {
                    pwd = s.toString()
                }
            }

            override fun afterTextChanged(s: Editable?) {

            }

        })

        login_btn.setOnClickListener {
            if (email.length > 0 && pwd.length > 0)
                login()
            else
                Toast.makeText(this@LoginActivity, "Fill all the details", Toast.LENGTH_SHORT).show()
        }

        signup_txt.setOnClickListener { goToSignupPage() }
    }

    private fun login() {

        progressDialog.setMessage("Signing in...")
        progressDialog.show()

        try{
            apiCallInterface
                .logInUser(emailid = email, password = pwd, oneSignalId = "0")
                .enqueue(object : Callback<UserAuthDataClasses.SignUpResponse> {
                    override fun onResponse(
                        call: Call<UserAuthDataClasses.SignUpResponse>,
                        response: Response<UserAuthDataClasses.SignUpResponse>
                    ) {
                        progressDialog.dismiss()
                        if (!response.isSuccessful) {
                            Log.d(TAG, "onResponse: Failed")
                            return
                        }

                        Log.d(TAG, "onResponse: Success")

                        if (response.body() != null) {
                            val signUpResponse: UserAuthDataClasses.SignUpResponse =
                                response.body()!!

                            val data = Gson().toJson(signUpResponse)
                            Log.d(TAG, "onResponse: $data")

                            Toast.makeText(
                                this@LoginActivity,
                                "Login successful!",
                                Toast.LENGTH_SHORT
                            ).show()

                            SharedPreference().setUserStatusCode(this@LoginActivity, signUpResponse.StatusCode)

                            if (signUpResponse.StatusCode == "1") {
                                Handler().postDelayed({
                                    goToHomePage()
                                    finish()
                                }, 350)
                            }else {
                                Toast.makeText(this@LoginActivity,
                                    "Login unsuccessful!", Toast.LENGTH_SHORT).show()
                            }
                        }
                    }

                    override fun onFailure(
                        call: Call<UserAuthDataClasses.SignUpResponse>,
                        t: Throwable
                    ) {
                        progressDialog.dismiss()
                        Log.d(TAG, "onFailure: ${t.message}")
                        t.printStackTrace()
                    }

                })
        }
        catch (e: Exception){
            progressDialog.dismiss()
            e.printStackTrace()
        }

    }
}
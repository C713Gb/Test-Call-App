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
import kotlinx.android.synthetic.main.activity_signup.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.create

class SignupActivity : AppCompatActivity() {

    var name = ""
    var email = ""
    var pwd = ""
    var mobile = ""
    var countryCode = ""
    var isTerms = ""
    private lateinit var apiCallInterface: ApiCallInterface
    private val TAG = "SignupActivity"
    private lateinit var progressDialog: ProgressDialog

    override fun onStart() {
        super.onStart()
        if (SharedPreference().getUserStatusCode(this@SignupActivity) == "1"){
            goToHomePage()
            finish()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_signup)

        progressDialog = ProgressDialog(this@SignupActivity)
        progressDialog.setCanceledOnTouchOutside(false)
        progressDialog.setCancelable(false)

        apiCallInterface = RetrofitClientInstance()
            .getRetrofitInstance()?.create(ApiCallInterface::class.java)!!

        name_txt.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if (s != null) {
                    name = s.toString()
                }
            }

            override fun afterTextChanged(s: Editable?) {

            }

        })

        email_txt.addTextChangedListener(object : TextWatcher {
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

        pwd_txt.addTextChangedListener(object : TextWatcher {
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

        mobile_txt.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if (s != null) {
                    mobile = s.toString()
                }
            }

            override fun afterTextChanged(s: Editable?) {

            }

        })

        code_txt.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if (s != null) {
                    countryCode = s.toString()
                }
            }

            override fun afterTextChanged(s: Editable?) {

            }

        })

        signup_btn.setOnClickListener { signUp() }

        login_txt.setOnClickListener { goToLoginPage() }
    }

    private fun signUp() {

        if (name.isNotEmpty()) {
//            Toast.makeText(this@SignupActivity, "Not Empty", Toast.LENGTH_SHORT).show()
        } else {
            Toast.makeText(this@SignupActivity, "Empty", Toast.LENGTH_SHORT).show()
        }

        val signUpModel: UserAuthDataClasses.SignUpModel = UserAuthDataClasses.SignUpModel(
            Name = name,
            Mobile = mobile,
            Email = email,
            Password = pwd,
            CountryCode = countryCode,
            isTerms = isTerms
        )

        progressDialog.setMessage("Signing up...")
        progressDialog.show()

        try {
            apiCallInterface
                .signUpUser(signUpModel)
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
                                this@SignupActivity,
                                signUpResponse.Message,
                                Toast.LENGTH_SHORT
                            ).show()

                            SharedPreference().setUserStatusCode(this@SignupActivity, signUpResponse.StatusCode)

                            if (signUpResponse.StatusCode == "1") {
                                Handler().postDelayed({
                                    goToHomePage()
                                    finish()
                                }, 350)
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
        } catch (e: Exception) {
            progressDialog.dismiss()
            e.printStackTrace()
        }
    }

    private fun goToHomePage() {
        val intent = Intent(this@SignupActivity, HomeActivity::class.java)
        startActivity(intent)
    }

    private fun goToLoginPage() {
        val intent = Intent(this@SignupActivity, LoginActivity::class.java)
        startActivity(intent)
    }
}
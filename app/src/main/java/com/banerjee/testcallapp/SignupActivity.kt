package com.banerjee.testcallapp

import android.app.ProgressDialog
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.text.Editable
import android.text.TextUtils
import android.text.TextWatcher
import android.util.Log
import android.util.Patterns
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.banerjee.testcallapp.model.UserAuthDataClasses
import com.banerjee.testcallapp.network.ApiCallInterface
import com.banerjee.testcallapp.network.RetrofitClientInstance
import com.google.gson.Gson
import kotlinx.android.synthetic.main.activity_signup.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


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

        signup_btn.setOnClickListener {

            if (name.length > 0 && mobile.length > 0 && email.length > 0 && pwd.length > 0 && countryCode.length > 0
                && isTerms.length > 0){
                if (isTerms == "true"){
                    if (isValidEmail(email))
                        signUp()
                    else
                        Toast.makeText(this@SignupActivity, "Invalid email", Toast.LENGTH_SHORT).show()
                }
                else{
                    Toast.makeText(this@SignupActivity, "Accept Terms and Conditions", Toast.LENGTH_SHORT).show()
                }
            }
            else{
                Toast.makeText(this@SignupActivity, "Fill all details", Toast.LENGTH_SHORT).show()
            }
        }

        terms_conditions.setOnCheckedChangeListener { buttonView, isChecked ->
            isTerms = isChecked.toString()
        }

        login_txt.setOnClickListener { goToLoginPage() }
    }

    fun isValidEmail(target: CharSequence?): Boolean {
        return !TextUtils.isEmpty(target) && Patterns.EMAIL_ADDRESS.matcher(target).matches()
    }

    private fun signUp() {


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
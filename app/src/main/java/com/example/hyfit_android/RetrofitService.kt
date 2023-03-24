package com.example.hyfit_android

import android.util.Log
import com.example.hyfit_android.Join.JoinActivity3
import com.example.hyfit_android.Join.JoinReq
import com.example.hyfit_android.Join.JoinView
import com.example.hyfit_android.Login.LoginReq
import com.example.hyfit_android.Login.LoginView
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class RetrofitService {
    private lateinit var loginView: LoginView
    private lateinit var joinView: JoinView

    fun setLoginView(loginView: LoginView){
        this.loginView=loginView
    }
    fun setJoinView(joinView: JoinView){
        this.joinView = joinView
    }

    fun login(loginRequest: LoginReq){
        val userService = getRetrofit().create(RetrofitInterface::class.java)
        Log.d("email: ", loginRequest.email)
        Log.d("password: ", loginRequest.password)
        userService.login(loginRequest).enqueue(object : Callback<UserResponse>{
            override fun onResponse(call: Call<UserResponse>, response: Response<UserResponse>) {
                val resp: UserResponse = response.body()!!
                when(val code = resp.code){
                    1000 -> loginView.onLoginSuccess(code, resp.result)
                    else -> loginView.onLoginSuccess(code, resp.message)
                }
            }

            override fun onFailure(call: Call<UserResponse>, t: Throwable) {
                Log.d("Failure", t.message.toString())
            }
        })
    }

    fun join(joinRequest : JoinReq) {

        val userService = getRetrofit().create(RetrofitInterface::class.java)
        Log.d("email: ", joinRequest.email)
        Log.d("password: ", joinRequest.password)
        userService.join(joinRequest).enqueue(object : Callback<UserResponse> {
            override fun onResponse(call: Call<UserResponse>, response: Response<UserResponse>) {
                val resp: UserResponse = response.body()!!
                when (val code = resp.code) {
                    1000 -> joinView.onJoinSuccess()
                    else -> joinView.onJoinFailure()
                }
            }

            override fun onFailure(call: Call<UserResponse>, t: Throwable) {
                Log.d("JoinFailure", t.message.toString())
            }
        })
    }
}

package com.example.hyfit_android

import android.util.Log
import com.example.hyfit_android.Join.JoinEmailView
import com.example.hyfit_android.Join.JoinReq
import com.example.hyfit_android.Join.JoinView
import com.example.hyfit_android.Login.LoginReq
import com.example.hyfit_android.Login.LoginView
import com.example.hyfit_android.Login.LogoutView
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class UserRetrofitService {
    private lateinit var loginView: LoginView
    private lateinit var joinView: JoinView
    private lateinit var logoutView: LogoutView
    private lateinit var joinEmailView: JoinEmailView

    fun setLoginView(loginView: LoginView){
        this.loginView=loginView
    }
    fun setJoinView(joinView: JoinView){
        this.joinView = joinView
    }

    fun setLogoutView(logoutView: LogoutView){
        this.logoutView=logoutView
    }

    fun setJoinEmailView(joinEmailView: JoinEmailView){
        this.joinEmailView=joinEmailView
    }

    fun login(loginRequest: LoginReq){
        val userService = getRetrofit().create(UserRetrofitInterface::class.java)
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

        val userService = getRetrofit().create(UserRetrofitInterface::class.java)
        Log.d("email: ", joinRequest.email)
        Log.d("password: ", joinRequest.password)
        userService.join(joinRequest).enqueue(object : Callback<UserResponse> {
            override fun onResponse(call: Call<UserResponse>, response: Response<UserResponse>) {
                val resp: UserResponse = response.body()!!
                when (val code = resp.code) {
                    1000 -> joinView.onJoinSuccess(code, resp.result)
                    else -> joinView.onJoinFailure(code, resp.message,resp.result)
                }
            }

            override fun onFailure(call: Call<UserResponse>, t: Throwable) {
                Log.d("JoinFailure", t.message.toString())
            }
        })
    }

    fun logout(jwt:String?){
        val userService = getRetrofit().create(UserRetrofitInterface::class.java)
        if (jwt != null) {
            userService.logout(jwt).enqueue(object : Callback<UserResponse> {
                override fun onResponse(call: Call<UserResponse>, response: Response<UserResponse>) {
                    val resp: UserResponse = response.body()!!
                    when (val code = resp.code) {
                        1000 -> logoutView.onLogoutSuccess(code, resp.result)
                        else -> logoutView.onLogoutFailure(code, resp.message)
                    }
                }

                override fun onFailure(call: Call<UserResponse>, t: Throwable) {
                    Log.d("LogoutFailure", t.message.toString())
                }
            })
        }
    }

    fun confirm(email:String?){
        val userService = getRetrofit().create(UserRetrofitInterface::class.java)
        if (email != null) {
            userService.confirm(email).enqueue(object : Callback<UserResponse> {
                override fun onResponse(call: Call<UserResponse>, response: Response<UserResponse>) {
                    val resp: UserResponse = response.body()!!
                    Log.d("resbody", resp.toString())
                    when (val code = resp.code) {
                        1000 -> joinEmailView.onEmailSuccess(code, resp.result)
                        else -> joinEmailView.onEmailFailure(code, resp.message)
                    }
                }

                override fun onFailure(call: Call<UserResponse>, t: Throwable) {
                    Log.d("LogoutFailure", t.message.toString())
                }
            })
        }
    }

}

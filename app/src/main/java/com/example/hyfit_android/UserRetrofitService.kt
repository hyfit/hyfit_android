package com.example.hyfit_android

import android.util.Log
import com.example.hyfit_android.Join.JoinEmailView
import com.example.hyfit_android.Join.JoinReq
import com.example.hyfit_android.Join.JoinView
import com.example.hyfit_android.Login.*
import com.example.hyfit_android.UserInfo.PasswordCheckView
import com.example.hyfit_android.UserInfo.PasswordUpdateView
import com.example.hyfit_android.UserInfo.UpdatepassReq
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.http.Header
import retrofit2.http.Query

class UserRetrofitService {
    private lateinit var loginView: LoginView
    private lateinit var joinView: JoinView
    private lateinit var logoutView: LogoutView
    private lateinit var joinEmailView: JoinEmailView
    private lateinit var findPasswordView: FindPasswordView
    private lateinit var passwordCheckView: PasswordCheckView
    private lateinit var passwordUpdateView: PasswordUpdateView
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

    fun setFindPasswordView(findPasswordView: FindPasswordView){
        this.findPasswordView=findPasswordView
    }

    fun setpasswordCheckView(passwordCheckView: PasswordCheckView){
        this.passwordCheckView=passwordCheckView
    }

    fun setpasswordUpdateView(passwordUpdateView: PasswordUpdateView){
        this.passwordUpdateView=passwordUpdateView
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
                    else -> joinView.onJoinFailure(code, resp.message, resp.result)
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

    //비번 분실 시 찾는 거
    fun editpassword(findPasswordReq: FindPasswordReq){
        val userService= getRetrofit().create(UserRetrofitInterface::class.java)
        if(findPasswordReq.email!=null && findPasswordReq.password!=null){
            Log.d("thisisemail",findPasswordReq.email)
            Log.d("thisispwd",findPasswordReq.password)
            userService.editpassword(findPasswordReq).enqueue(object : Callback<UserResponse> {
                override fun onResponse(call: Call<UserResponse>, response: Response<UserResponse>) {
                    val resp: UserResponse = response.body()!!
                    when (val code = resp.code) {
                        1000 -> findPasswordView.onFindSuccess(code, resp.result)
                        else -> findPasswordView.onFindFailure(code, resp.message)
                    }
                }


                override fun onFailure(call: Call<UserResponse>, t: Throwable) {
                    Log.d("FindFailure", t.message.toString())
                }

            })
        }

    }

    fun passwordcheck(jwt:String?, password:String){
        val userService = getRetrofit().create(UserRetrofitInterface::class.java)
        if (jwt != null) {
            userService.passwordcheck(jwt, password).enqueue(object : Callback<UserResponse> {
                override fun onResponse(call: Call<UserResponse>, response: Response<UserResponse>) {
                    Log.d("passwordcheck", "goodgood")
                    val resp: UserResponse = response.body()!!
                    when (val code = resp.code) {
                        1000 -> passwordCheckView.onCheckSuccess(code, resp.result)
                        else -> passwordCheckView.onCheckFailure(code, resp.message)
                    }
                }

                override fun onFailure(call: Call<UserResponse>, t: Throwable) {
                    Log.d("CheckFailure", t.message.toString())
                }
            })
        }
    }

    fun passwordupdate(jwt:String?, updatepassReq: UpdatepassReq){
        val userService = getRetrofit().create(UserRetrofitInterface::class.java)
        if (jwt != null) {
            userService.passwordupdate(jwt, updatepassReq).enqueue(object : Callback<UserResponse> {
                override fun onResponse(call: Call<UserResponse>, response: Response<UserResponse>) {
                    Log.d("passwordupdate", "goodgood")
                    val resp: UserResponse = response.body()!!
                    when (val code = resp.code) {
                        1000 -> passwordUpdateView.onUpdateSuccess(code, resp.result)
                        else -> passwordUpdateView.onUpdateFailure(code, resp.message)
                    }
                }

                override fun onFailure(call: Call<UserResponse>, t: Throwable) {
                    Log.d("CheckFailure", t.message.toString())
                }
            })
        }
    }
}


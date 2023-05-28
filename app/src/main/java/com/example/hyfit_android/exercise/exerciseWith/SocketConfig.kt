package com.example.hyfit_android.exercise.exerciseWith

import android.util.Log
import com.gmail.bishoybasily.stomp.lib.Event
import com.gmail.bishoybasily.stomp.lib.Event.Type.*
import com.gmail.bishoybasily.stomp.lib.StompClient
import io.reactivex.disposables.Disposable
import okhttp3.*

class SocketConfig {
    lateinit var stompConnection: Disposable
    lateinit var topic: Disposable
    val intervalMillis = 1000L
    val client = OkHttpClient()
    val stomp = StompClient(client, intervalMillis)

    // CONNECT
    fun connect(){
        stomp.url = "ws/localhost:8081/ws"
        stompConnection = stomp.connect().subscribe {
            when (it.type) {
                OPENED -> {

                }
                CLOSED -> {

                }
                ERROR -> {

                }

                null -> Log.d("stompConnection","NOTING")
            }
        }
    }

    fun subscribe(){
        topic = stomp.join("/sub/channel/")
            .doOnError{error -> }
            .subscribe { Log.i("AA", it) }
    }
    fun unsubscribe(){
        topic.dispose()
    }

    fun send(){
        stomp.send("/app/[destination]", "[ALARM]").subscribe {
            if(it){

            }
        }
    }

    fun disconnect(){
        stompConnection.dispose()
    }


}

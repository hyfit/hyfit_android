package com.example.hyfit_android

import androidx.appcompat.app.AppCompatActivity
import retrofit2.Retrofit
import com.example.hyfit_android.BuildConfig.BASE_URL
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Response
import retrofit2.converter.gson.GsonConverterFactory
import java.io.IOException


//val okHttpClient = OkHttpClient.Builder()
//    .addInterceptor { chain: Interceptor.Chain ->
//        val original = chain.request()
//        chain.proceed(original.newBuilder().apply {
//            addHeader("X-Naver-Client-Id", Naver_Client_Id)
//            addHeader("X-Naver-Client-Secret", Naver_Client_Secret)
//        }.build())
//    }
//    .addInterceptor(HttpLoggingInterceptor().apply {
//        level = HttpLoggingInterceptor.Level.BODY
//    }).build()

fun getRetrofit(): Retrofit {

    val retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .client(OkHttpClient())
        .build()

    return retrofit
}

//fun okHttpClient(interceptor: AppInterceptor) : OkHttpClient.Builder {
//    return okHttpClient.Builder()
//        .addInterceptor(interceptor)
//        .addInterceptor(httpLoggingIn.apply){
//            level=HttpLogginInterceptor.Level.BODY
//        }).build()
//}



class AppInterceptor : Interceptor{
    @Throws(IOException::class)
    override fun intercept(chain: Interceptor.Chain): Response = with(chain) {
        val newRequest=request().newBuilder()
            .addHeader("(header key)", "(header value)")
            .build()
        proceed(newRequest)
    }
}
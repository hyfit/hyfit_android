package com.example.hyfit_android

import android.graphics.BitmapFactory
import android.util.Log
import com.example.hyfit_android.Login.LoginReq
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import com.example.hyfit_android.ReportFragment
import com.example.hyfit_android.databinding.FragmentReportBinding

class ReportRetrofitService {
    private lateinit var reportView:ReportView
    private lateinit var binding:FragmentReportBinding


    fun setReportView(reportView: ReportView){
        this.reportView=reportView
    }

    fun report(){
        val reportService = getreportRetrofit().create(ReportRetrofitInterface::class.java)
//        Log.d("email: ", loginRequest.email)
//        Log.d("password: ", loginRequest.password)
        reportService.report().enqueue(object : Callback<ResponseBody> {
            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                //val resp = response.body()?.bytes()
                val inputStream=response.body()?.bytes()
                Log.d("resbody", inputStream.toString())
                //여기까지 잘들어감, 밑에 bitmap부터 안됨.. 에러 다시 찾아보기
                val prior = inputStream
                    //Log.d("prior", prior.size.toString())
                    //if (prior.isNotEmpty()){

                var bitmap = BitmapFactory.decodeByteArray(prior, 0, prior!!.size)
                //Log.d("bitmapbitmapbitmap", bitmap.toString())
                //binding.puthere.setImageBitmap(bitmap)
                reportView.onReportSuccess(bitmap)

            }

            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {

                Log.d("Failurehhhhh", t.message.toString())
            }

        })
    }
}
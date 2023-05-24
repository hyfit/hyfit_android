package com.example.hyfit_android

import android.graphics.BitmapFactory
import android.util.Log
import com.example.hyfit_android.Join.JoinReq
import com.example.hyfit_android.Login.LoginReq
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import com.example.hyfit_android.ReportFragment
import com.example.hyfit_android.databinding.FragmentReportBinding
import com.example.hyfit_android.report.UserbodyReq
import com.example.hyfit_android.report.UserbodyView
import com.github.mikephil.charting.charts.BarChart

class ReportRetrofitService {
    private lateinit var reportView:ReportView
    private lateinit var userbodyView: UserbodyView
    private lateinit var binding:FragmentReportBinding



    fun setReportView(reportView: ReportView){
        this.reportView=reportView
    }
    fun setBodyView(userbodyView: UserbodyView){
        this.userbodyView=userbodyView
    }


    fun report(email:String?){
        val reportService = getreportRetrofit().create(ReportRetrofitInterface::class.java)
        reportService.report(email!!).enqueue(object : Callback<ReportResponse> {
            override fun onResponse(call: Call<ReportResponse>, response: Response<ReportResponse>) {
                if (response.isSuccessful) {
                    Log.d("bargraphget", "goodgood")
                    val resp: ReportResponse? = response.body()
                    if (resp != null) {
                        when (val code = resp.report.code) {
                            1000 -> reportView.onReportSuccess(resp.body.weight,resp.body.height,resp.body.bodydate,resp.body.goal_weight,resp.report.totaltime,resp.report.pace, resp.report.distance, resp.report.rate,
                                resp.report.gname, resp.rank.ranking, resp.rank.requestedRank, resp.rank.genderRanking, resp.rank.genderRequestRanking, resp.rank.ageRanking, resp.rank.ageRequestedRank, resp.rank.requestedDist)
                            else -> reportView.onReportFailure(code)
                        }
                    } else {
                        // 서버로부터 받은 응답이 null인 경우 처리
                        Log.d("GetuserFailure", "Response body is null.")
                    }
                }
            }

            override fun onFailure(call: Call<ReportResponse>, t: Throwable) {

                Log.d("BarChartFailure", t.message.toString())
            }

        })
    }

    fun bodydata(userbodyReq: UserbodyReq) {

        val reportService = getreportRetrofit().create(ReportRetrofitInterface::class.java)
        reportService.bodydata(userbodyReq).enqueue(object : Callback<UserResponse> {
            override fun onResponse(call: Call<UserResponse>, response: Response<UserResponse>) {
                val resp: UserResponse = response.body()!!
                when (val code = resp.code) {
                    1000 -> userbodyView.onBodySuccess(code, resp.result)
                    else -> userbodyView.onBodyFailure(code, resp.message)
                }
            }

            override fun onFailure(call: Call<UserResponse>, t: Throwable) {
                Log.d("BodyDataFailure", t.message.toString())
            }
        })
    }
}
package com.example.hyfit_android

import android.content.Intent
import android.graphics.Bitmap
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import androidx.fragment.app.Fragment
import com.example.hyfit_android.Login.FindPasswordReq
import com.example.hyfit_android.databinding.FragmentReportBinding

/**
 * A simple [Fragment] subclass.
 * Use the [ReportFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class ReportFragment : Fragment(), ReportView{
    lateinit var binding: FragmentReportBinding
    lateinit var progressBar: ProgressBar

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentReportBinding.inflate(inflater, container, false)
        progressBar = binding.progressBar
        binding.change.setOnClickListener {
            binding.layoutdashboard.visibility=View.GONE
            binding.layoutchange.visibility=View.VISIBLE
        }
        binding.ranking.setOnClickListener {
            binding.layoutdashboard.visibility=View.GONE
            binding.layoutranking.visibility=View.VISIBLE
        }
        binding.dashboard2.setOnClickListener {
            binding.layoutchange.visibility=View.GONE
            binding.layoutdashboard.visibility=View.VISIBLE
        }
        binding.ranking2.setOnClickListener {
            binding.layoutchange.visibility=View.GONE
            binding.layoutranking.visibility=View.VISIBLE
        }
        binding.dashboard3.setOnClickListener {
            binding.layoutranking.visibility=View.GONE
            binding.layoutdashboard.visibility=View.VISIBLE
        }
        binding.change3.setOnClickListener {
            binding.layoutranking.visibility=View.GONE
            binding.layoutchange.visibility=View.VISIBLE
        }
        binding.pushbutton.setOnClickListener {
            report()
            progressBar.visibility=ProgressBar.VISIBLE
        }


        return binding.root
    }

    private fun report(){
        val rpService = ReportRetrofitService()
        rpService.setReportView(this)
        rpService.report()
    }

    override fun onReportSuccess(code: Bitmap) {
        if(code!=null){
            progressBar.visibility=ProgressBar.GONE
            Log.d("GetreportSuccess", "cong")
            binding.puthere.setImageBitmap(code)
        }
    }

    override fun onReportFailure(code: Int) {
        Log.d("GetreportFail", code.toString())
    }

}
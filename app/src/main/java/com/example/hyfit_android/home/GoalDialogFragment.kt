package com.example.hyfit_android.home

import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import android.app.Dialog
import android.content.DialogInterface
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.hyfit_android.getRetrofit
import com.example.hyfit_android.goal.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class GoalDialogFragment : DialogFragment(), GetGoalView  {

    private lateinit var goalList : ArrayList<Goal>

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return activity?.let {
            // 선택된 goal
            var selectedGoal: String? = null

            val builder = AlertDialog.Builder(it)
            builder.setTitle("Select Your Goal")
//                .setSingleChoiceItems(goalList., -1) { dialog, which ->
//                    selectedGoal = goalList[which]
//                }
                .setPositiveButton("SELECT", DialogInterface.OnClickListener {
                    dialog, id->
                })
                .setNegativeButton("CANCEL", DialogInterface.OnClickListener {
                    dialog, id ->
                })
            builder.create()
        } ?: throw IllegalStateException("Activity cannot be null")
    }

    private fun getJwt(): String? {
        val spf = activity?.getSharedPreferences("auth", AppCompatActivity.MODE_PRIVATE)
        return spf!!.getString("jwt", "0")
    }

    private fun getGoalProgress() {
        val jwt = getJwt()
        val goalService = GoalService()
        goalService.setGetGoalView(this)
    }

    override fun onGetGoalSuccess(result: ArrayList<Goal>) {
        goalList = result
    }

    override fun onGetGoalFailure(code: Int, msg: String) {

    }
}
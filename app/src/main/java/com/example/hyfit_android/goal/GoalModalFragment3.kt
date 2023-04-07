package com.example.hyfit_android.goal

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.DialogFragment
import com.example.hyfit_android.R
import com.example.hyfit_android.databinding.FragmentGoalModal3Binding
import com.example.hyfit_android.databinding.FragmentGoalModalBinding
import com.google.gson.Gson

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [GoalModalFragment3.newInstance] factory method to
 * create an instance of this fragment.
 */
class GoalModalFragment3() : DialogFragment(),SaveGoalView {
    lateinit var binding: FragmentGoalModal3Binding
    private lateinit var type : String
    private lateinit var place : String
    var onChangeListener: OnGoalChangeListener? = null
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentGoalModal3Binding.inflate(inflater, container, false)
        dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        val bundle = arguments
        type = bundle?.getString("type").toString()
        place = bundle?.getString("place").toString()
        binding.searchClose.setOnClickListener{
            dismiss()
        }
        binding.searchSave.setOnClickListener{
            saveGoal()
        }
        return binding.root
    }

    private fun getJwt():String?{
        val spf = activity?.getSharedPreferences("auth", AppCompatActivity.MODE_PRIVATE)
        return spf!!.getString("jwt","0")
    }
   private fun saveGoal(){
       val jwt = getJwt()
       val description : String = binding.goalDescriptionContent.text.toString()
       val goalService = GoalService()
       val goalFragment = parentFragmentManager.fragments.firstOrNull { it is GoalFragment } as? GoalFragment
       goalFragment?.let { goalService.setSaveGoalView(it) }
//       goalService.setSaveGoalView(this)
       Log.d("type",type)
       Log.d("place",place)
       goalService.saveGoal(jwt!!,SaveGoalReq(place,type,description))
       dismiss()
   }


    override fun onSaveGoalSuccess(result: Goal) {
//        onChangeListener?.onItemChange()
        dismiss()
    }

    override fun onSaveGoalFailure(code: Int, msg: String) {

    }


}
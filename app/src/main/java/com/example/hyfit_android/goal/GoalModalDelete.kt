package com.example.hyfit_android.goal

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.DialogFragment
import com.example.hyfit_android.databinding.FragmentGoalModalDeleteBinding
import kotlin.properties.Delegates

/**
 * A simple [Fragment] subclass.
 * Use the [GoalModalDelete.newInstance] factory method to
 * create an instance of this fragment.
 */
class GoalModalDelete : DialogFragment() {
    lateinit var binding: FragmentGoalModalDeleteBinding
    private lateinit var goalTitle : String
    private lateinit var goalRate : String
    private var goalId by Delegates.notNull<Long>()
    var listener: OnGoalChangeListener? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding =  FragmentGoalModalDeleteBinding.inflate(inflater, container, false)
        dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        val bundle = arguments
        goalId = bundle?.getLong("goalId")!!
        goalTitle = bundle?.getString("goalTitle").toString()
        goalRate = bundle?.getString("goalRate").toString()
        binding.goalDeleteDescription.text = goalTitle + "( " + goalRate + "% )"
        binding.deleteCloseButton.setOnClickListener{
            dismiss()
        }
        binding.deleteYesButton.setOnClickListener{
            deleteGoal()
        }
        return binding.root
    }

    private fun getJwt():String?{
        val spf = activity?.getSharedPreferences("auth", AppCompatActivity.MODE_PRIVATE)
        return spf!!.getString("jwt","0")
    }

    private fun deleteGoal(){
        val jwt = getJwt()
        val goalService = GoalService()
        val goalFragment = parentFragmentManager.fragments.firstOrNull { it is GoalFragment } as? GoalFragment
        goalFragment?.let { goalService.setDeleteGoalView(it) }
//        goalService.setDeleteGoalView(this)
        goalService.deleteGoal(jwt!!,goalId)

    }



}
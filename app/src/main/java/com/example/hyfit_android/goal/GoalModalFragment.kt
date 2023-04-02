package com.example.hyfit_android.goal

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.Fragment
import com.example.hyfit_android.R
import com.example.hyfit_android.databinding.FragmentGoalModalBinding


// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [GoalModalFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class GoalModalFragment : DialogFragment() {
    lateinit var binding: FragmentGoalModalBinding
    val bundle = arguments
    val fragment2 = GoalModalFragment2()
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentGoalModalBinding.inflate(inflater, container, false)
        dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        binding.goalModalClose.setOnClickListener{
            dismiss()
        }
        binding.buildingLayout.setOnClickListener{
            val bundle = Bundle().apply {
                putString("type", "building")
            }

            fragment2.arguments = bundle
            fragment2.show(parentFragmentManager, "dialog2")
            dismiss()

        }
        binding.mountainLayout.setOnClickListener{
            val bundle = Bundle().apply {
                putString("type", "mountain")
            }
            fragment2.arguments = bundle
            fragment2.show(parentFragmentManager, "dialog2")
            dismiss()
        }
        return binding.root
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

}

}
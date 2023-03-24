package com.example.hyfit_android.goal

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.hyfit_android.R
import com.example.hyfit_android.databinding.FragmentGoalModal3Binding
import com.example.hyfit_android.databinding.FragmentGoalModalBinding

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [GoalModalFragment3.newInstance] factory method to
 * create an instance of this fragment.
 */
class GoalModalFragment3 : Fragment() {
    lateinit var binding: FragmentGoalModal3Binding
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentGoalModal3Binding.inflate(inflater, container, false)

        return binding.root
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

    }
}
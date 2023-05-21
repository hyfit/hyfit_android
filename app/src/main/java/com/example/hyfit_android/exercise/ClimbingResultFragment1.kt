package com.example.hyfit_android.exercise

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.hyfit_android.R
import com.example.hyfit_android.databinding.FragmentClimbingResult1Binding

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [ClimbingResultFragment1.newInstance] factory method to
 * create an instance of this fragment.
 */
class ClimbingResultFragment1 : Fragment() {
    private lateinit var binding: FragmentClimbingResult1Binding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentClimbingResult1Binding.inflate(inflater, container, false)
        return binding.root
    }

}
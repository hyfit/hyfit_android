package com.example.hyfit_android.home

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bumptech.glide.Glide
import com.example.hyfit_android.R
import com.example.hyfit_android.databinding.FragmentClimbingResult1Binding
import com.example.hyfit_android.databinding.FragmentGoalRecBinding

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [GoalRecFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class GoalRecFragment(private var src : String,private var name : String, private var placeId : Long) : Fragment() {

    private lateinit var binding: FragmentGoalRecBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentGoalRecBinding.inflate(inflater, container, false)
        Glide.with(this)
            .load(src)
            .placeholder(R.drawable.loading_img)
            .into(binding.goalRecImg)

        binding.goalTitle.text = name
        return binding.root
    }

}
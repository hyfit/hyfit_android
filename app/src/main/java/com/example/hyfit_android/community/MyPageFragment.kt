package com.example.hyfit_android.community

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.hyfit_android.databinding.FragmentMyPageBinding


class MyPageFragment: Fragment() {

    lateinit var binding: FragmentMyPageBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentMyPageBinding.inflate(inflater, container, false)

        binding.backBtn.setOnClickListener {
            (activity as com.example.hyfit_android.MainActivity).replaceFragment(CommunityFragment())

        }
        return binding.root
    }
}
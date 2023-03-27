package com.example.hyfit_android.community

import android.app.Activity
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.hyfit_android.R
import com.example.hyfit_android.databinding.FragmentCommunityBinding
import com.example.hyfit_android.goal.GoalDetailRVAdapter
import com.example.hyfit_android.MainActivity as MainActivity1

class CommunityFragment: Fragment() {
    lateinit var binding: FragmentCommunityBinding
    private lateinit var communityRVAdapter: CommunityRVAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentCommunityBinding.inflate(inflater, container, false)

        val followingButton = binding.followingBtn
        val allButton = binding.allBtn
        val followingIv = binding.followingIv
        val allIv = binding.allIv

        // 첫 화면-> 팔로잉 유저 게시물 목록 보여줌(getFollowingPosts)
        followingIv.visibility = View.VISIBLE
        allIv.visibility = View.INVISIBLE
        followingButton.setTextColor(Color.parseColor("#000000"))
        allButton.setTextColor(Color.parseColor("#949494"))


        // recyclerview에 following 유저 게시물 띄움
        followingButton.setOnClickListener{
            binding.followingIv.visibility = View.VISIBLE
            binding.allIv.visibility = View.INVISIBLE
            followingButton.setTextColor(Color.parseColor("#000000"))
            allButton.setTextColor(Color.parseColor("#949494"))


        }

        // recyclerview에 모든 유저 게시물 띄움
        allButton.setOnClickListener {
            binding.allIv.visibility = View.VISIBLE
            binding.followingIv.visibility = View.INVISIBLE
            followingButton.setTextColor(Color.parseColor("#949494"))
            allButton.setTextColor(Color.parseColor("#000000"))

        }



        binding.postLayout.setOnClickListener {
            (activity as com.example.hyfit_android.MainActivity).replaceFragment(PostFragment())
        }

        binding.profileImageview.setOnClickListener {
            (activity as com.example.hyfit_android.MainActivity).replaceFragment(MyPageFragment())
        }



        return binding.root
    }
    }

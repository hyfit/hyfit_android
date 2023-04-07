package com.example.hyfit_android.community

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.example.hyfit_android.R
import com.example.hyfit_android.databinding.FragmentMyPageBinding


class MyPageFragment: Fragment(), GetAllUserPostsView, GetFollowerView, GetFollowingView {

    lateinit var binding: FragmentMyPageBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentMyPageBinding.inflate(inflater, container, false)

        // user의 모든 게시물 가져옴
        getAllUserPosts()

        // user 정보(profileImage, nickName) 가져옴

        // post 개수

        // follower 수

        // following 수

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

    }

    override fun onResume() {
        super.onResume()

        binding.backBtn.setOnClickListener {
            parentFragmentManager.beginTransaction().apply {
                replace(R.id.fragment_container, CommunityFragment().apply {
                })
                commit()
            }
        }
    }

    private fun getJwt():String?{
        val spf = activity?.getSharedPreferences("auth", AppCompatActivity.MODE_PRIVATE)
        return spf!!.getString("jwt","0")
    }

    private fun getAllUserPosts() {
        val postService = PostService()
//        postService.getAllUserPosts(email)
    }


    override fun onGetAllUserPostsSuccess(result: ArrayList<Post>) {
        TODO("Not yet implemented")
    }

    override fun onGetAllUserPostsFailure(code: Int, msg: String) {
    }

    override fun onFollowerSuccess(result: HashMap<String, List<String>>) {
        TODO("Not yet implemented")
    }

    override fun onFollowerFailure(code: Int, msg: String) {
    }

    override fun onFollowingSuccess(result: HashMap<String, List<String>>) {
        TODO("Not yet implemented")
    }

    override fun onFollowingFailure(code: Int, msg: String) {
    }
}
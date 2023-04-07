package com.example.hyfit_android.community

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.example.hyfit_android.R
import com.example.hyfit_android.databinding.FragmentPostBinding

class PostFragment : Fragment(), AddFollowView, UnfollowView, GetOnePostView {

    lateinit var binding: FragmentPostBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentPostBinding.inflate(inflater, container, false)

        // 선택된 게시물 띄움
        getOnePost()

        // 게시물 유저 정보 띄움(profileImage, nickName)


        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

    }

    override fun onResume() {
        super.onResume()

        binding.followBtn.setOnClickListener {

        }

        binding.backBtn.setOnClickListener {
//            (activity as com.example.hyfit_android.MainActivity).replaceFragment(CommunityFragment())
            parentFragmentManager.beginTransaction().apply {
                replace(R.id.fragment_container, CommunityFragment().apply {
//                        arguments = Bundle().apply {
//                        }
                })
                commit()
            }
        }

    }

    private fun getJwt():String?{
        val spf = activity?.getSharedPreferences("auth", AppCompatActivity.MODE_PRIVATE)
        return spf!!.getString("jwt","0")
    }

    private fun addFollow() {
        val jwt = getJwt()
        val followService = FollowService()
//        followService.addFollow(jwt, email)
    }

    private fun getOnePost() {
        val jwt = getJwt()
        val postService = PostService()
//        postService.getOnePost(jwt, postId, email)
    }

    override fun onAddFollowSuccess(result: String) {
        TODO("Not yet implemented")
    }

    override fun onAddFollowFailure(code: Int, msg: String) {
    }

    override fun onGetOnePostSuccess(result: Post) {
        TODO("Not yet implemented")
    }

    override fun onGetOnePostFailure(code: Int, msg: String) {
    }

    override fun onUnfollowSuccess(result: String) {
        TODO("Not yet implemented")
    }

    override fun onUnfollowFailure(code: Int, msg: String) {
    }

}
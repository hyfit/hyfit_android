package com.example.hyfit_android.community

import android.content.Context
import android.os.Bundle
import android.os.Parcelable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.hyfit_android.MainActivity
import com.example.hyfit_android.R
import com.example.hyfit_android.databinding.FragmentMyPageBinding


class MyPageFragment: Fragment(), GetAllPostsOfUserView, GetFollowerView, GetFollowingView, GetCommunityProfileView, OnMyPostClickListener {

    lateinit var binding: FragmentMyPageBinding

    private lateinit var myPageRVAdapter: MyPageRVAdapter
//    private lateinit var recyclerViewState: Parcelable
    private lateinit var bundle: Bundle
    private lateinit var userEmail: String

    private var myPostList: List<PostImgInfo>? = null
    private var isLoginUser: Boolean = false


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentMyPageBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // 현재 유저 페이지가 로그인 한 유저의 페이지인지 아닌지 확인
        userEmail = arguments?.getString("email")!!
        val myEmail = getMyEmail()
        isLoginUser = if(myEmail.equals(userEmail)) true else false

        getCommunityProfile()

        // user의 모든 게시물 가져옴
//        getAllUserPosts()

    }

    override fun onResume() {
        super.onResume()

        // 뒤로가기시 커뮤니티 메인 프레그먼트로 이동
        // 앗 프레그먼트 백스택,,,
        binding.backBtn.setOnClickListener {
            parentFragmentManager.beginTransaction().apply {
                replace(R.id.fragment_container, CommunityFragment().apply {
                })
                commit()
            }
        }

        // 팔로워 클릭시 팔로워 유저 리스트 띄움
        binding.followerLayout.setOnClickListener {

        }

        // 팔로잉 클릭시 팔로잉 유저 리스트 띄움
        binding.followingNumTv.setOnClickListener {

        }

    }

    private fun getJwt():String?{
        val spf = activity?.getSharedPreferences("auth", AppCompatActivity.MODE_PRIVATE)
        return spf!!.getString("jwt","0")
    }

    private fun getMyEmail():String?{
        val sharedPreferences = requireActivity().getSharedPreferences("auth", Context.MODE_PRIVATE)
        return sharedPreferences.getString("email", "")
    }

    private fun getAllUserPosts() {
        val postService = PostService()
        val myemail = getMyEmail()
        postService.getAllPostsOfUser(myemail!!)
        binding.mprogressbar.visibility = View.VISIBLE
    }

    private fun getCommunityProfile() {
        val postService = PostService()
        val sharedPreferences = requireActivity().getSharedPreferences("auth", Context.MODE_PRIVATE)
        val myemail = sharedPreferences.getString("email", "")

        postService.setGetCommunityProfileView(this)
        postService.getCommunityProfile(myemail!!)

    }

    private fun initAdapter(postInfoList: List<PostImgInfo>) {
//        recyclerViewState = binding.mypostListRv.layoutManager?.onSaveInstanceState()!!
        myPageRVAdapter = MyPageRVAdapter(requireContext(), postInfoList, this)

        binding.mypostListRv.apply {
            setHasFixedSize(true)
            setItemViewCacheSize(10)
            adapter = myPageRVAdapter
//            layoutManager?.onRestoreInstanceState(recyclerViewState)

            addOnScrollListener(object : RecyclerView.OnScrollListener() {
                override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                    super.onScrolled(recyclerView, dx, dy)
                    val lastVisisbleItemPosition = (recyclerView.layoutManager as LinearLayoutManager?)!!.findLastCompletelyVisibleItemPosition()
//                    val isLastPage : Boolean =

                }
            })
        }
    }

    override fun onGetAllUserPostsSuccess(result: List<PostImgInfo>) {
        myPostList = result
        binding.mprogressbar.visibility = View.GONE
        if(myPostList!!.size == 0) {
            binding.emptyTv.visibility = View.VISIBLE
        } else {
            initAdapter(myPostList!!)
        }
    }

    override fun onGetAllUserPostsFailure(code: Int, msg: String) {

    }

    override fun onFollowerSuccess(result: List<String>) {
        TODO("Not yet implemented")
    }

    override fun onFollowerFailure(code: Int, msg: String) {
    }

    override fun onFollowingSuccess(result: List<String>) {
        TODO("Not yet implemented")
    }

    override fun onFollowingFailure(code: Int, msg: String) {
    }

    override fun onGetCommunityProfileSuccess(result: PostProfile) {
        if(result.userProfile.profileImgUrl.isNullOrEmpty()) {
            binding.profileIv.setImageResource(R.drawable.user3)
        } else {
            Glide.with(this)
                .load("https://d14okywu7b1q79.cloudfront.net"+result.userProfile.profileImgUrl)
                .into(binding.profileIv)
        }
        binding.usernameTv.text = result.userProfile.nickName
        binding.postNumTv.text = result.postNum.toString()
        binding.followerNumTv.text = result.followerNum.toString()
        binding.followingNumTv.text = result.followingNum.toString()
    }

    override fun onGetCommunityProfileFailure(code: Int, msg: String) {
        TODO("Not yet implemented")
    }

    // 한 게시물 클릭시
    override fun onMyPostClick(data: PostImgInfo) {
        val myEmail = getMyEmail()
        bundle.putLong("postId", data.postId)
        bundle.putString("email", myEmail)
        val postFragment = PostFragment()
        postFragment.arguments = bundle
        parentFragmentManager.beginTransaction().replace(R.id.CommunityFragment, postFragment).commit()
    }
}
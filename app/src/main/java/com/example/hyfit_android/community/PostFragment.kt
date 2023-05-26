package com.example.hyfit_android.community

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.ProgressBar
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.example.hyfit_android.R
import com.example.hyfit_android.databinding.FragmentPostBinding

class PostFragment : Fragment(), AddFollowView, UnfollowView, GetOnePostView, LikePostView,UnlikePostView {

    lateinit var binding: FragmentPostBinding
    lateinit var progressBar: ProgressBar
    var onclicklikepostid=0
    var postid=36
    val email="oliver08@naver.com"


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentPostBinding.inflate(inflater, container, false)
        //이따가 해제하기
        //val postId = arguments?.getLong("postId")
        //val email=arguments?.getString("email")
        val email="oliver08@naver.com"
        val sharedPreferences = requireActivity().getSharedPreferences("auth", Context.MODE_PRIVATE)
        val myemail = sharedPreferences.getString("email", "")
        progressBar=binding.progressBar
        Log.d("emailemailhere", myemail!!)

        //
        //binding.postIv.clipToOutline = true
        //얘도 나중에 bundle로 넘겨주겟지 ..
        val postId:Long=36
        // 선택된 게시물 띄움
        progressBar.visibility=View.VISIBLE
        getOnePost(postId,email)
        binding.likeBtn.setOnClickListener {
            like(postId)
        }
        Log.d("getOnePost", "SUCCESS")

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

    private fun getOnePost(postId:Long, email:String) {
        val jwt = getJwt()!!
        val postService = PostService()
        postService.setGetOnePostView(this)
        progressBar.bringToFront()
        progressBar.visibility=View.VISIBLE
        postService.getOnePost(jwt, postId, email)
    }
    private fun like(id:Long){
        val jwt=getJwt()!!
        val postService=PostService()
        postService.setLikePostView(this)
        progressBar.visibility=View.VISIBLE
        postService.like(jwt,id)
    }
    fun unlike(id:Long){
        val jwt=getJwt()!!
        val postService=PostService()
        postService.setunlikePostView(this)
        progressBar.visibility=View.VISIBLE
        postService.unlike(jwt,id)
    }

    private fun imageset(imageurl:String, imageview: ImageView){
        Glide.with(this)
            .load(imageurl)
            .into(imageview)
    }

    override fun onAddFollowSuccess(result: String) {
        TODO("Not yet implemented")
    }

    override fun onAddFollowFailure(code: Int, msg: String) {
    }

    override fun onGetOnePostSuccess(result: PostResult) {
        Log.d("onGetOnePostSuccess", "hihi")
        val type=result.type
        val postLikeNumber=result.postLikeNumber
        val nickName=result.userProfile.nickName
        val writeremail=result.userProfile.email
        val titlecontent=result.post.content
        val postimage="https://d14okywu7b1q79.cloudfront.net"+result.imageUrl
        val postimageview=binding.postIv
        val userimage="https://d14okywu7b1q79.cloudfront.net"+result.userProfile.profileImageUrl
        val userimageview=binding.profileIv

        binding.titleTv.text=titlecontent
        binding.likeTv.text=postLikeNumber.toString()
        binding.writerTv.text=nickName
        binding.typeBtn.text=type
        progressBar.visibility = ProgressBar.GONE

        Log.d("postImage", postimage)
        Log.d("UserImage", postimage)
        imageset(postimage,postimageview)
        imageset(userimage,userimageview)
        progressBar.visibility = ProgressBar.GONE

    }

    override fun onGetOnePostFailure(code: Int, msg: String) {
        progressBar.visibility = ProgressBar.GONE

        Log.d("GetOnePostFailure", code.toString()+msg)
    }

    override fun onUnfollowSuccess(result: String) {
        TODO("Not yet implemented")
    }

    override fun onUnfollowFailure(code: Int, msg: String) {
    }

    override fun onLikeSuccess(result: LikePostResult) {
        onclicklikepostid=result.postId
        getOnePost(postId=result.postId.toLong(), email=email)
        progressBar.visibility=View.GONE

    }

    override fun onLikeFailure(code: Int) {
        unlike(postid.toLong())
        progressBar.visibility=View.GONE
    }

    override fun onUnlikeSuccess(result: String) {
        Log.d("unlikeSuccess", "Cong")
        getOnePost(postId=postid.toLong(), email=email)
        progressBar.visibility=View.GONE
    }

    override fun onUnlikeFailure(code: Int) {
        Log.d("unlikeFailure","sad")
    }

}
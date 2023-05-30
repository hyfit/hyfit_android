package com.example.hyfit_android.community

import android.content.Context
import android.graphics.Color
import android.os.Bundle
import android.os.Parcelable
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.addTextChangedListener
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.example.hyfit_android.R
import com.example.hyfit_android.databinding.FragmentPostBinding


class PostFragment : Fragment(), AddFollowView, UnfollowView, GetOnePostView, LikePostView, UnlikePostView,
        GetFollowingView, GetCommentListView, SaveCommentView {

        lateinit var binding: FragmentPostBinding
        lateinit var commentRVAdapter: CommentRVAdapter
        private lateinit var recyclerViewState: Parcelable
        private lateinit var commentList: ArrayList<PostCommentList>
        lateinit var progressBar: ProgressBar

        lateinit var myemail: String

        //    val email="oliver08@naver.com" //번들에서받으면바꾸기
        private var email = ""
        private var postId = 0L
        private var onfollow: Boolean = false
        lateinit var followingList: List<String>
        var onclicklikepostid = 0L

        //var postid=36L

        var isLoginUser = false


        override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
        ): View? {
            binding = FragmentPostBinding.inflate(inflater, container, false)

            activity?.window?.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN)

            postId = arguments?.getLong("postId")!!
            email = arguments?.getString("email")!!

            val sharedPreferences = requireActivity().getSharedPreferences("auth", Context.MODE_PRIVATE)
            myemail = sharedPreferences.getString("email", "")!!
            progressBar = binding.progressBar
            Log.d("emailemailhere", myemail!!)
            isLoginUser = if (email.equals(myemail)) true else false


            // 선택된 게시물 띄움
            progressBar.visibility = View.VISIBLE
            getFollowingList()

            //getOnePost(postId,email)
            binding.likeBtn.setOnClickListener {
                like(postId)
            }
            Log.d("getOnePost", "SUCCESS")
            binding.followBtn.setOnClickListener {
                addFollow(email)
            }
            binding.unfollowBtn.setOnClickListener {
                unfollow(email)
            }

            getOnePost(postId, email)

            getCommentList()


            return binding.root

        }

        override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
            super.onViewCreated(view, savedInstanceState)

            recyclerViewState = binding.commentListRv.layoutManager?.onSaveInstanceState()!!

            binding.backBtn.setOnClickListener {
                parentFragmentManager.popBackStack()
            }

            binding.likeBtn.setOnClickListener {
                like(postId)
            }
            Log.d("getOnePost", "SUCCESS")

            // 해당 포스트 유저가 로그인 한 유저인 경우
            if (isLoginUser) {
                binding.followBtn.visibility = View.INVISIBLE
                binding.unfollowBtn.visibility = View.INVISIBLE
            } else {
                binding.followBtn.setOnClickListener {
                    addFollow(email)
                }
                binding.unfollowBtn.setOnClickListener {
                    unfollow(email)
                }
            }

            binding.profileIv.setOnClickListener {
                val bundle = Bundle().apply {
                    this.putString("email", email)
                }
                val myPageFragment = MyPageFragment()
                myPageFragment.arguments = bundle
                parentFragmentManager
                    .beginTransaction()
                    .replace(R.id.fragment_container, myPageFragment)
                    .addToBackStack(null)
                    .commit()
            }


            binding.commentBtn.setOnClickListener {
                if (binding.commentEt.text.isEmpty()) {
                    val toast = Toast.makeText(requireContext(), "Please write the comments.", Toast.LENGTH_SHORT)
                    toast.show()
                } else {
                    saveComment(binding.commentEt.text.toString())

                }
            }

        }


        private fun getJwt(): String? {
            val spf = activity?.getSharedPreferences("auth", AppCompatActivity.MODE_PRIVATE)
            return spf!!.getString("jwt", "0")
        }
//    private fun getCommentList(){
//        val jwt = getJwt()!!
//        val postService = PostService()
//        postService.setGetCommentListView(this)
//        progressBar.bringToFront()
//        progressBar.visibility=View.VISIBLE
//        postService.getCommentList(postId)
//    }

        private fun addFollow(email: String) {
            val jwt = getJwt()!!
            val followService = FollowService()
            followService.setAddFollowView(this)
            progressBar.visibility = View.VISIBLE
            followService.addFollow(jwt, email)
        }

        private fun unfollow(email: String) {
            val jwt = getJwt()!!
            val followService = FollowService()
            followService.setUnfollowView(this)
            progressBar.visibility = View.VISIBLE
            followService.unfollow(jwt, email)
        }

        private fun getFollowingList() {
            val jwt = getJwt()!!
            val followService = FollowService()
            followService.setFollowingView(this)
            followService.getFollowingList(jwt)
            progressBar.visibility = View.VISIBLE
        }

        private fun getOnePost(postId: Long, email: String) {
            val jwt = getJwt()!!
            val postService = PostService()
            postService.setGetOnePostView(this)
            progressBar.bringToFront()
            progressBar.visibility = View.VISIBLE
            postService.getOnePost(postId, email)
        }

        private fun like(id: Long) {
            val jwt = getJwt()!!
            val postService = PostService()
            postService.setLikePostView(this)
            progressBar.visibility = View.VISIBLE
            postService.likePost(jwt, id)
        }

        private fun unlike(id: Long) {
            val jwt = getJwt()!!
            val postService = PostService()
            postService.setUnlikePostView(this)
            progressBar.visibility = View.VISIBLE
            postService.unlikePost(jwt, id)
        }

        private fun getCommentList() {
            val postService = PostService()
            postService.setGetCommentListView(this)
            postService.getCommentList(postId)
            progressBar.visibility = View.VISIBLE
        }

        private fun saveComment(comment: String) {
            val jwt = getJwt()
            val postService = PostService()
            postService.setSaveCommentView(this)
            var commentReq = SaveCommentReq(comment)

            postService.saveComment(jwt!!, postId, commentReq)
        }

        private fun imageset(imageurl: String, imageview: ImageView) {
            Glide.with(this)
                .load(imageurl)
                .into(imageview)
        }

        private fun initAdapter(commentList: ArrayList<PostCommentList>) {
            commentRVAdapter = CommentRVAdapter(requireContext(), commentList)
            binding.commentListRv.adapter = commentRVAdapter
            binding.commentListRv.layoutManager =
                LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)

            binding.commentListRv.apply {
                setHasFixedSize(true)
                setItemViewCacheSize(10)
                adapter = commentRVAdapter
                layoutManager = binding.commentListRv.layoutManager
                layoutManager?.onRestoreInstanceState(recyclerViewState)
            }
        }

        override fun onAddFollowSuccess(result: String) {
            Log.d("follow success", "success~")
            Log.d("Follow result", result)
            onfollow = true

            getOnePost(postId, email)

            getOnePost(postId.toLong(), email)

            progressBar.visibility = View.GONE
        }

        override fun onAddFollowFailure(code: Int, msg: String) {
            Log.d("follow failure", code.toString() + " " + msg)
        }

        override fun onGetOnePostSuccess(result: OnePost) {
            Log.d("onGetOnePostSuccess", "hihi")
            val type = result.type
            val postLikeNumber = result.postLikeNum
            val nickName = result.userProfile.nickName
            val writeremail = result.userProfile.email
            val titlecontent = result.post.content
            val postimage = "https://d14okywu7b1q79.cloudfront.net" + result.imageUrl
            val postimageview = binding.postIv
            val userimage = "https://d14okywu7b1q79.cloudfront.net" + result.userProfile.profileImgUrl
            val userimageview = binding.profileIv

            binding.titleTv.text = titlecontent
            binding.likeTv.text = postLikeNumber.toString()
            binding.writerTv.text = nickName
            binding.typeBtn.text = type
            Log.d("onfollowbtn", onfollow.toString())
            if (onfollow) {
                binding.followBtn.visibility = View.GONE
                binding.unfollowBtn.visibility = View.VISIBLE
            } else {
                binding.followBtn.visibility = View.VISIBLE
                binding.unfollowBtn.visibility = View.GONE
            }

            Log.d("postImage", postimage)
            Log.d("UserImage", postimage)
            imageset(postimage, postimageview)
            imageset(userimage, userimageview)
            progressBar.visibility = ProgressBar.GONE

        }

        override fun onGetOnePostFailure(code: Int, msg: String) {
            progressBar.visibility = ProgressBar.GONE

            Log.d("GetOnePostFailure", code.toString() + msg)
        }

        override fun onUnfollowSuccess(result: String) {
            Log.d("unfollow success", "success~")
            Log.d("unFollow result", result)
            onfollow = false

            getOnePost(postId, email)

            getOnePost(postId.toLong(), email)

            progressBar.visibility = View.GONE
        }

        override fun onUnfollowFailure(code: Int, msg: String) {
            Log.d("unfollow failure", code.toString() + " " + msg)
        }

        override fun onLikePostSuccess(result: PostLike) {
            onclicklikepostid = result.postId
            getOnePost(postId = result.postId, email = email)
            progressBar.visibility = View.GONE
        }

        override fun onLikePostFailure(code: Int, msg: String) {
            unlike(postId)
            progressBar.visibility = View.GONE
        }

        override fun onUnlikePostSuccess(result: String) {
            Log.d("unlikeSuccess", "Cong")
            getOnePost(postId = postId, email = email)
            progressBar.visibility = View.GONE
        }

        override fun onUnlikePostFailure(code: Int, msg: String) {
            Log.d("unlikeFailure", "sad")
        }

        override fun onFollowingSuccess(result: List<String>) {
            followingList = result.map { it.split(",")[0] }
            onfollow = followingList.contains(email)
            Log.d("followingList", followingList[0])
            getOnePost(postId, email)
            progressBar.visibility = View.GONE

        }

        override fun onFollowingFailure(code: Int, msg: String) {
            Log.d("followinglistsad", "sadsads")
        }


//    override fun onGetCommentListSuccess(result: PostCommentList) {
//        Log.d("commnetSuccess", "goodgood")
//        binding.commentWriterTv.text=result.email
//        binding.commentTv.text=result.content
//    }

//    override fun onGetCommentListFailure(code: Int, msg: String) {
//        TODO("Not yet implemented")
//    }

        override fun onGetCommentListSuccess(result: ArrayList<PostCommentList>) {
            commentList = result
            binding.progressBar.visibility = View.GONE
            initAdapter(commentList)
        }

        override fun onGetCommentListFailure(code: Int, msg: String) {
            Log.d("commentlistsad", "sadsads")
        }

        override fun onSaveCommentSuccess(result: PostComment) {
            binding.commentEt.text = null
            getCommentList()
        }

        override fun onSaveCommentFailure(code: Int, msg: String) {
            Log.d("savecommentsad", "sadsads")
        }


    }

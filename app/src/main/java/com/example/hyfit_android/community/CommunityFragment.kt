package com.example.hyfit_android.community

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Color
import android.os.Bundle
import android.os.Parcelable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.hyfit_android.databinding.FragmentCommunityBinding
import com.example.hyfit_android.MainActivity
import com.example.hyfit_android.R


class CommunityFragment: Fragment(), View.OnClickListener, GetAllPostsOfFollowingUsersView,
    GetAllPostsOfAllUsersView, GetCommunityProfileView, OnPostClickListener {
    lateinit var binding: FragmentCommunityBinding
    private lateinit var mainActivity: MainActivity

    private lateinit var communityRVAdapter: CommunityRVAdapter
    private lateinit var tagMap: HashMap<Button, Boolean>
    private lateinit var recyclerViewState: Parcelable
    private lateinit var bundle: Bundle
    private var postList: List<PostPagination>? = null
    private var isLastPage = false


    // 뷰를 만들 때 실행
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentCommunityBinding.inflate(inflater, container, false)

        return binding.root
    }


    // 뷰가 만들어지면 실행
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        // 로그인 유저 프로필 이미지 가져옴
        getCommunityProfile()

        // 첫 화면-> 팔로잉 유저 게시물 목록 보여줌(getFollowingPosts)
//        getFollowingPosts(null, null)

        // tag 초기화
        tagMap = HashMap<Button, Boolean>()
        tagMap[binding.hikingBtn] = false
        tagMap[binding.runningBtn] = false
        tagMap[binding.walkingBtn] = false
        tagMap[binding.runningBtn] = false
        tagMap[binding.stairClimbingBtn] = false

        // 태그 버튼 리스너 연결
        val keys = tagMap.keys
        keys.forEach{btn -> btn.setOnClickListener(this)}

    }

    // 사용자와 상호작용 할 수 있는 상태
    override fun onResume() {
        super.onResume()

        // recyclerview에 following 유저 게시물 띄움
        binding.followingBtn.setOnClickListener{
            getFollowingPosts(null, null)
        }

        // recyclerview에 모든 유저 게시물 띄움
        binding.allBtn.setOnClickListener {
            getAllPosts(null, null)
        }

        // post 클릭시
//        binding.postLayout.setOnClickListener {
////            mainActivity.replaceFragment(PostFragment())
//            parentFragmentManager.beginTransaction().apply {
//                replace(R.id.fragment_container, PostFragment().apply {
//                    arguments = Bundle().apply {
//                    }
//                })
//                commit()
//            }
//        }

        // 프로필 이미지 클릭시
        binding.profileImageview.setOnClickListener {
//            mainActivity.replaceFragment(MyPageFragment())
            parentFragmentManager.beginTransaction().apply {
                replace(R.id.fragment_container, MyPageFragment().apply {
                    arguments = Bundle().apply {
                    }
                })
                commit()
            }
        }

    }

    private fun getJwt():String?{
        val spf = activity?.getSharedPreferences("auth", AppCompatActivity.MODE_PRIVATE)
        return spf!!.getString("jwt","0")
    }


    private fun getFollowingPosts(type: String?=null, lastPostId: Long?=null) {
        binding.followingIv.visibility = View.VISIBLE
        binding.allIv.visibility = View.INVISIBLE
        binding.followingBtn.setTextColor(Color.parseColor("#000000"))
        binding.allBtn.setTextColor(Color.parseColor("#949494"))

        val jwt: String? = getJwt()
        val postService = PostService()

        postService.setGetAllPostsOfFollowingUsersView(this)
        postService.getAllPostsOfFollowingUsersWithType(jwt!!, lastPostId, type, 5)
        binding.cprogressbar.visibility = View.VISIBLE

    }

    private fun getAllPosts(type: String?=null, lastPostId: Long?=null) {
        binding.allIv.visibility = View.VISIBLE
        binding.followingIv.visibility = View.INVISIBLE
        binding.followingBtn.setTextColor(Color.parseColor("#949494"))
        binding.allBtn.setTextColor(Color.parseColor("#000000"))

        val jwt: String? = getJwt()
        val postService = PostService()

        postService.setGetAllPostsOfAllUsersView(this)
        postService.getAllPostsOfAllUsersWithType(jwt!!, lastPostId, type, 5)
        binding.cprogressbar.visibility = View.VISIBLE

    }

    private fun getCommunityProfile() {
        val jwt: String? = getJwt()
        val postService = PostService()
        val sharedPreferences = requireActivity().getSharedPreferences("auth", Context.MODE_PRIVATE)
        val myemail = sharedPreferences.getString("email", "")

        postService.setGetCommunityProfileView(this)
        postService.getCommunityProfile(myemail!!)

    }


    override fun onClick(v: View) {
        when (v.id) {
            R.id.hiking_btn -> {
                onTagClicked(binding.hikingBtn)
            }
            R.id.running_btn -> {
                onTagClicked(binding.runningBtn)
            }
            R.id.walking_btn -> {
                onTagClicked(binding.walkingBtn)
            }
            R.id.biking_btn -> {
                onTagClicked(binding.bikingBtn)
            }
            R.id.stair_climbing_btn -> {
                onTagClicked(binding.stairClimbingBtn)
            }
        }
    }


    @SuppressLint("ResourceAsColor", "NewApi")
    private fun onTagClicked(btn: Button) {
        val itr = tagMap.keys.iterator()
        //tag 버튼 선택 취소
        if(tagMap[btn] == true) {
            tagMap[btn] = false
            btn.setBackgroundResource(R.drawable.tag_btn_back)
            btn.setTextColor(resources.getColor(R.color.string_gray, resources.newTheme()))
        }
        // tag 버튼 선택(다른 tag 선택 취소)
        else if(tagMap[btn] == false){
            tagMap[btn] = true
            btn.setBackgroundResource(R.drawable.tag_btn_back_blue)
            btn.setTextColor(resources.getColor(R.color.string_white, resources.newTheme()))
            while (itr.hasNext()) {
                val key = itr.next()
                if (key != btn) {
                    tagMap[key] = false
                    key.setBackgroundResource(R.drawable.tag_btn_back)
                    key.setTextColor(resources.getColor(R.color.string_gray, resources.newTheme()))
                }
            }

        }

    }

    private fun initAdapter(postList: List<PostPagination>) {
        recyclerViewState = binding.postListRv.layoutManager?.onSaveInstanceState()!!
        communityRVAdapter = CommunityRVAdapter(mainActivity, postList, this)

        binding.postListRv.apply {
            setHasFixedSize(true)
            setItemViewCacheSize(10)
            adapter = communityRVAdapter
            layoutManager?.onRestoreInstanceState(recyclerViewState)

            addOnScrollListener(object : RecyclerView.OnScrollListener() {
                override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                    super.onScrolled(recyclerView, dx, dy)
                    val lastVisisbleItemPosition = (recyclerView.layoutManager as LinearLayoutManager?)!!.findLastCompletelyVisibleItemPosition()
//                    val isLastPage : Boolean =

                }
            })
        }
    }


    override fun onGetAllPostsOfFollowingUsersWithTypeSuccess(result: Slice) {
        postList = result.content
        if(postList!!.size == 0) {
            binding.emptyTv.visibility = View.VISIBLE
        } else {
            isLastPage = result.last
            initAdapter(postList!!)
        }
        binding.cprogressbar.visibility = View.INVISIBLE
    }
    override fun onGetAllPostsOfFollowingUsersWithTypeFailure(code: Int, msg: String) {
        TODO("Not yet implemented")
    }

    override fun onGetAllPostsOfAllUsersWithTypeSuccess(result: Slice) {
        postList = result.content
        if(postList!!.size == 0) {
            binding.emptyTv.visibility = View.VISIBLE
        } else {
            isLastPage = result.last
            initAdapter(postList!!)
        }
        binding.cprogressbar.visibility = View.INVISIBLE
    }
    override fun onGetAllPostsOfAllUsersWithTypeFailure(code: Int, msg: String) {
        TODO("Not yet implemented")
    }

    override fun onGetCommunityProfileSuccess(result: PostProfile) {
        if(result.userProfile.profileImgUrl.isNullOrEmpty()) {
            binding.profileImageview.setImageResource(R.drawable.user3)
        } else {
            Glide.with(this)
                .load("https://d14okywu7b1q79.cloudfront.net"+result.userProfile.profileImgUrl)
                .into(binding.profileImageview)
        }
    }
    override fun onGetCommunityProfileFailure(code: Int, msg: String) {
        TODO("Not yet implemented")
    }

    // 한 게시물 클릭시
    override fun onPostClick(data: PostPagination) {
        bundle.putLong("postId", data.postId)
        bundle.putString("email", data.email)
        val postFragment = PostFragment()
        postFragment.arguments = bundle
        parentFragmentManager.beginTransaction().replace(R.id.CommunityFragment, postFragment).commit()
    }
}


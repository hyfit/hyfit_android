package com.example.hyfit_android.community

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.example.hyfit_android.databinding.FragmentCommunityBinding
import com.example.hyfit_android.MainActivity
import com.example.hyfit_android.R

class CommunityFragment: Fragment(), View.OnClickListener {
    lateinit var binding: FragmentCommunityBinding
    private lateinit var communityRVAdapter: CommunityRVAdapter
    lateinit var mainActivity: MainActivity
    lateinit var tagMap: HashMap<Button, Boolean>

    // context 가져옴
    override fun onAttach(context: Context) {
        super.onAttach(context)
        mainActivity = context as MainActivity
    }

    // 뷰를 만들 때 실행
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentCommunityBinding.inflate(inflater, container, false)

        // 포스트 이미지뷰 테두리 둥글게
        binding.postIv.clipToOutline = true



        // 첫 화면-> 팔로잉 유저 게시물 목록 보여줌(getFollowingPosts)
        getFollowingPosts()

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

        return binding.root
    }


    // 뷰가 만들어지면 실행
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

    }


    override fun onStart() {
        super.onStart()

    }

    // 사용자와 상호작용 할 수 있는 상태
    override fun onResume() {
        super.onResume()

        // recyclerview에 following 유저 게시물 띄움
        binding.followingBtn.setOnClickListener{
            getFollowingPosts()
        }

        // recyclerview에 모든 유저 게시물 띄움
        binding.allBtn.setOnClickListener {
            getAllPosts()
        }

        // post 클릭시
        binding.postLayout.setOnClickListener {
//            mainActivity.replaceFragment(PostFragment())
            parentFragmentManager.beginTransaction().apply {
                replace(R.id.fragment_container, PostFragment().apply {
                    arguments = Bundle().apply {
                    }
                })
                commit()
            }
        }

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

    private fun getFollowingPosts() {
        binding.followingIv.visibility = View.VISIBLE
        binding.allIv.visibility = View.INVISIBLE
        binding.followingBtn.setTextColor(Color.parseColor("#000000"))
        binding.allBtn.setTextColor(Color.parseColor("#949494"))

        val jwt = getJwt()
        val postService = PostService()

    }

    private fun getAllPosts() {
        binding.allIv.visibility = View.VISIBLE
        binding.followingIv.visibility = View.INVISIBLE
        binding.followingBtn.setTextColor(Color.parseColor("#949494"))
        binding.allBtn.setTextColor(Color.parseColor("#000000"))

        val jwt = getJwt()
        val postService = PostService()

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
}

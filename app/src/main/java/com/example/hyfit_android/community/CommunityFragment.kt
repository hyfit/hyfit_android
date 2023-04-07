package com.example.hyfit_android.community

import android.content.Context
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.example.hyfit_android.databinding.FragmentCommunityBinding
import com.example.hyfit_android.MainActivity
import com.example.hyfit_android.R

class CommunityFragment: Fragment() {
    lateinit var binding: FragmentCommunityBinding
    private lateinit var communityRVAdapter: CommunityRVAdapter
//    lateinit var mainActivity: MainActivity
    lateinit var tagMap: HashMap<Button, Boolean>

//    // context 가져옴
//    override fun onAttach(context: Context) {
//        super.onAttach(context)
//        mainActivity = context as MainActivity
//    }

    // 뷰를 만들 때 실행
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentCommunityBinding.inflate(inflater, container, false)

        // 첫 화면-> 팔로잉 유저 게시물 목록 보여줌(getFollowingPosts)
        getFollowingPosts()

        // tag 초기화
        tagMap = HashMap<Button, Boolean>()
        tagMap[binding.hikingBtn] = false
        tagMap[binding.runningBtn] = false
        tagMap[binding.walkingBtn] = false
        tagMap[binding.runningBtn] = false
        tagMap[binding.stairClimbingBtn] = false

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


}

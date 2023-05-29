package com.example.hyfit_android.community

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.database.Cursor
import android.net.Uri
import android.os.Bundle
import android.os.Parcelable
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import com.bumptech.glide.Glide
import com.example.hyfit_android.R
import com.example.hyfit_android.User
import com.example.hyfit_android.UserRetrofitService
import com.example.hyfit_android.databinding.FragmentMyPageBinding
import okhttp3.MultipartBody
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody.Companion.asRequestBody
import java.io.File


class MyPageFragment: Fragment(), GetAllPostsOfUserView, GetFollowerView, GetFollowingView, GetCommunityProfileView, UpdateProfileImageView,OnMyPostClickListener {

    lateinit var binding: FragmentMyPageBinding

    private lateinit var myPageRVAdapter: MyPageRVAdapter
    private lateinit var recyclerViewState: Parcelable
    private lateinit var userEmail: String
    private lateinit var myPostList: ArrayList<PostImgInfo>
    private var isLoginUser: Boolean = false

    private var startForResult = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
        if(it.resultCode == Activity.RESULT_OK) {
            val imageUrl = it.data?.data

            val file = File(absolutelyPath(imageUrl, requireContext()))
            val requestBody = file.asRequestBody("image/*".toMediaTypeOrNull())
            val body = MultipartBody.Part.createFormData("file", file.name, requestBody)

            updateProfileImage(body)

        }
    }

    companion object{
        const val REQ_GALLERY = 1
    }


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
        recyclerViewState = binding.mypostListRv.layoutManager?.onSaveInstanceState()!!


        // 현재 유저 페이지가 로그인 한 유저의 페이지인지 아닌지 확인
        userEmail = arguments?.getString("email")!!
        val myEmail = getMyEmail()
        isLoginUser = if(myEmail.equals(userEmail)) true else false


        if(isLoginUser) {
            binding.editBtn.visibility = View.VISIBLE
            binding.editBtn.setOnClickListener {
                selectGallery()
            }
        } else {
            binding.editBtn.visibility = View.INVISIBLE
        }

        getCommunityProfile()

        // user의 모든 게시물 가져옴
        getAllUserPosts()

        binding.backBtn.setOnClickListener {
            parentFragmentManager.popBackStack()
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
        postService.setGetAllPostsOfUserView(this)
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

    private fun initAdapter(postInfoList: ArrayList<PostImgInfo>) {
        myPageRVAdapter = MyPageRVAdapter(requireContext(), postInfoList, this)
        binding.mypostListRv.adapter = myPageRVAdapter
        binding.mypostListRv.layoutManager = GridLayoutManager(requireContext(), 3)

        binding.mypostListRv.apply {
            setHasFixedSize(true)
            setItemViewCacheSize(10)
            adapter = myPageRVAdapter
            layoutManager = binding.mypostListRv.layoutManager
            layoutManager?.onRestoreInstanceState(recyclerViewState)

        }
    }

    // 갤러리에서 사진 가져옴
    private fun selectGallery() {
        // 퍼미션 체크
        val writePermission = ContextCompat.checkSelfPermission(
            requireContext(), android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
        val readPermission = ContextCompat.checkSelfPermission(
            requireContext(), android.Manifest.permission.READ_EXTERNAL_STORAGE
        )
        if(writePermission == PackageManager.PERMISSION_DENIED || readPermission == PackageManager.PERMISSION_DENIED) {
            ActivityCompat.requestPermissions(requireActivity(),
                arrayOf(android.Manifest.permission.WRITE_EXTERNAL_STORAGE, android.Manifest.permission.READ_EXTERNAL_STORAGE), REQ_GALLERY)
        } else {
            val intent = Intent(Intent.ACTION_PICK)
            intent.setDataAndType(
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*"
            )
            startForResult.launch(intent)
        }

    }

    // 파일 절대경로 반환
    private fun absolutelyPath(path: Uri?, context: Context): String {
        var proj: Array<String>  = arrayOf(MediaStore.Images.Media.DATA)
        var c: Cursor? = context.contentResolver.query(path!!, proj, null, null, null)
        var index = c?.getColumnIndexOrThrow(MediaStore.Images.Media.DATA)
        c?.moveToFirst()

        var result = c?.getString(index!!)
        return result!!
    }

    private fun updateProfileImage(body: MultipartBody.Part) {
        val jwt = getJwt()
        val userService = UserRetrofitService()
        userService.setUpdateProfileImageView(this)
        userService.updateProfileImage(jwt!!, body)
    }



    override fun onGetAllUserPostsSuccess(result: ArrayList<PostImgInfo>) {
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
        var bundle = Bundle().apply {
            putLong("postId", data.postId)
            putString("email", myEmail)
        }
        val postFragment = PostFragment()
        postFragment.arguments = bundle
        parentFragmentManager.beginTransaction()
            .add(R.id.fragment_container, postFragment)
            .addToBackStack(null)
            .commit()
    }

    override fun onUpdateProfileImageSuccess(result: User) {
        val myToast = Toast.makeText(requireContext().applicationContext, "Profile image has been updated.", Toast.LENGTH_SHORT)
        myToast.show()

        Glide.with(this)
            .load("https://d14okywu7b1q79.cloudfront.net"+result.profile_img)
            .into(binding.profileIv)
    }

    override fun onUpdateProfileImageFailure(code: Int, msg: String) {
        TODO("Not yet implemented")
    }
}
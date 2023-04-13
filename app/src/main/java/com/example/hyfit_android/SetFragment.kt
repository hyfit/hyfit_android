package com.example.hyfit_android

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.example.hyfit_android.Login.LogoutActivity
import com.example.hyfit_android.UserInfo.EditAccountInfoActivity
import com.example.hyfit_android.UserInfo.EditPasswordActivity1
import com.example.hyfit_android.UserInfo.GetUserView
import com.example.hyfit_android.UserInfo.ValidView
import com.example.hyfit_android.databinding.ActivityEditAccountInfoBinding
import com.example.hyfit_android.databinding.FragmentSetBinding
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

/**
 * A simple [Fragment] subclass.
 * Use the [SetFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class SetFragment : Fragment(), GetUserView {
    lateinit var binding: FragmentSetBinding
    lateinit var bindingedit:ActivityEditAccountInfoBinding
    lateinit var progressBar: ProgressBar

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentSetBinding.inflate(inflater, container, false)
        bindingedit= ActivityEditAccountInfoBinding.inflate(layoutInflater)

        progressBar = binding.progressBar

        binding.goLogout.setOnClickListener {
            val intent = Intent(getActivity(), LogoutActivity::class.java)
            startActivity(intent)
        }

        binding.edituserinfo.setOnClickListener {
            val intent = Intent(getActivity(), EditAccountInfoActivity::class.java)
            userget()
            progressBar.visibility = ProgressBar.VISIBLE
            startActivity(intent)
        }

        binding.editpassword.setOnClickListener {
            val intent=Intent(getActivity(), EditPasswordActivity1::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
            startActivity(intent)

        }



        return binding.root
    }

//    private fun valid(){
//        val spf = requireActivity().getSharedPreferences("auth", AppCompatActivity.MODE_PRIVATE)
//        val jwt:String?=spf!!.getString("jwt","0")
//        val usService=UserRetrofitService()
//        usService.setvalidView(this)
//        usService.valid(jwt)
//    }

    private fun userget() {
        val jwt: String? = getJwt()
        Log.d("jwtjwt", jwt.toString())

        val usService = UserRetrofitService()
        usService.setgetuserView(this)
        usService.userget(jwt)
    }



    private fun getJwt():String?{
        val spf = requireActivity().getSharedPreferences("auth", AppCompatActivity.MODE_PRIVATE)
        return spf!!.getString("jwt","0")
    }
    private fun saveJwt(jwt:String?){
        val spf=requireActivity().getSharedPreferences("auth", AppCompatActivity.MODE_PRIVATE)
        val editor=spf.edit()
        editor.putString("jwt", jwt)
        editor.apply()
        editor.commit()
    }
    override fun onUserSuccess(code: Int, result: User) {
        when(code){
            1000->{
                val intent=Intent(getActivity(), EditAccountInfoActivity::class.java)
                intent.putExtra("name", result.name)
                intent.putExtra("email", result.email)
                intent.putExtra("birth", result.birth.toString())
                intent.putExtra("nickName",result.nickName)
                intent.putExtra("gender", result.gender)
                intent.putExtra("introduce", result.introduce)
                intent.putExtra("phone", result.phone)
                progressBar.visibility = ProgressBar.GONE
                startActivity(intent)
                Log.d("GetUserSuccess", code.toString())
            }
        }
    }

    override fun onUserFailure(code: Int, msg: String) {
        Log.d("GetUserFail", msg)
    }

//    override fun onValidSuccess(code: Int, result: String) {
//        when(code){
//            1000->{
//                if(result=="valid"){
//                    Log.d("hivalid", result)
//                }
//                else if(result=="invalid"){
//                    Log.d("ininvalid", result)
//
//
//                }
//                else{
//                    Log.d("oldone", getJwt().toString())
//                    saveJwt(result)
//                    Log.d("newone", getJwt().toString())
//
//
//                }
////                val usService = UserRetrofitService()
////                usService.setgetuserView(this)
////                usService.userget(getJwt())
////                Log.d("ValidSuccess", code.toString())
//            }
//            else->Log.d("validsad", "sadsad")
//        }
//    }
//
//    override fun onValidFailure(code: Int, msg: String) {
//
//    }

}
package com.example.hyfit_android

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.hyfit_android.Login.LogoutActivity
import com.example.hyfit_android.UserInfo.EditAccountInfoActivity
import com.example.hyfit_android.UserInfo.EditPasswordActivity1
import com.example.hyfit_android.databinding.ActivityEditAccountInfoBinding
import com.example.hyfit_android.databinding.FragmentSetBinding

/**
 * A simple [Fragment] subclass.
 * Use the [SetFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class SetFragment : Fragment() {
    lateinit var binding: FragmentSetBinding
    lateinit var bindingedit:ActivityEditAccountInfoBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentSetBinding.inflate(inflater, container, false)
        bindingedit= ActivityEditAccountInfoBinding.inflate(layoutInflater)
        binding.goLogout.setOnClickListener {
            val intent = Intent(getActivity(), LogoutActivity::class.java)
            startActivity(intent)
        }

        binding.edituserinfo.setOnClickListener {
            val intent = Intent(getActivity(), EditAccountInfoActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
            //bindingedit.showemail.text="daun045@naver.com"
            startActivity(intent)
        }

        binding.editpassword.setOnClickListener {
            val intent=Intent(getActivity(), EditPasswordActivity1::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
            startActivity(intent)
        }



        return binding.root
    }

}
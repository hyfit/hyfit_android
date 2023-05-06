package com.example.hyfit_android.home

//import android.app.AlertDialog
import androidx.appcompat.app.AlertDialog
import android.app.Dialog
import android.content.ContentValues
import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ProgressBar
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.example.hyfit_android.BuildConfig
import com.example.hyfit_android.Join.JoinActivity1
import com.example.hyfit_android.Login.LogoutActivity
import com.example.hyfit_android.MainActivity
import com.example.hyfit_android.User
import com.example.hyfit_android.UserInfo.GetUserView
import com.example.hyfit_android.UserRetrofitService
import com.example.hyfit_android.databinding.FragmentMainBinding
import com.example.hyfit_android.exercise.ExerciseActivity
import com.nextnav.nn_app_sdk.NextNavSdk
import com.nextnav.nn_app_sdk.notification.AltitudeContextNotification
import com.nextnav.nn_app_sdk.notification.SdkStatus
import com.nextnav.nn_app_sdk.notification.SdkStatusNotification
import com.nextnav.nn_app_sdk.zservice.WarningMessages
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.selects.select
import java.util.*
import kotlin.properties.Delegates

/**
 * A simple [Fragment] subclass.
 * Use the [MainFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class MainFragment : Fragment(), GetUserView{
    lateinit var binding: FragmentMainBinding
    // pinncale
    private lateinit var sdk: NextNavSdk
    private val NEXTNAV_SERVICE_URL = "api.nextnav.io"
    private val API_KEY = BuildConfig.KEY_VALUE

    private lateinit var sdkMessageObservable: SdkStatusNotification
    private lateinit var altitudeObservable: AltitudeContextNotification

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        // pinnacle setting
        sdk = NextNavSdk.getInstance()
        sdkMessageObservable = SdkStatusNotification.getInstance()

        val mainActivity = activity as MainActivity
        binding = FragmentMainBinding.inflate(inflater, container, false)

        binding.progressBar.visibility = ProgressBar.GONE
//        val startBtn = binding.mainStartBtn
        binding.welcomeText.text = "Welcome, ${mainActivity.userNickName}"
//        startBtn.setOnClickListener {
//            if(mainActivity.initCode == 800 || mainActivity.initCode == 808 ||mainActivity.initCode == 0 ){
//                val intent = Intent(requireActivity(), ExerciseActivity::class.java)
//                startActivity(intent)
//            }
//            else {
//                binding.progressBar.visibility = ProgressBar.VISIBLE
//                lifecycleScope.launch {
//
//                    while(mainActivity.initCode != 800 ) {
//                        Log.d("STATUSCODE",mainActivity.initCode.toString())
//                        delay(1000)
//                    }
//                    val intent = Intent(requireActivity(), ExerciseActivity::class.java)
//                    startActivity(intent)
//                }
//            }
//        }

        return binding.root
    }


    private fun getJwt():String?{
        val spf = requireActivity().getSharedPreferences("auth", AppCompatActivity.MODE_PRIVATE)
        return spf!!.getString("jwt","0")
    }
    private fun userget() {
        val jwt: String? = getJwt()
        Log.d("jwtjwt", jwt.toString())

        val usService = UserRetrofitService()
        usService.setgetuserView(this)
        usService.userget(jwt)
    }

    private fun initPinnacle() {
        sdk.init(requireContext(), NEXTNAV_SERVICE_URL, API_KEY)
    }
    // pinnacle
    private fun calculateAlt(a : String, b:String, c : String) {
        sdk.startAltitudeCalculation(NextNavSdk.AltitudeCalculationFrequency.ONE,a,b,c)
//        sdk.startBarocalUpload(barocalCallback, true)
//        startBarocal(a,b,c,null)
    }

    override fun onStart() {
        super.onStart()
//       userget()
    }

    override fun onUserSuccess(code: Int, result: User) {
        binding.welcomeText.text = "Welcome, ${result.nickName}"
    }

    override fun onUserFailure(code: Int, msg: String) {
      Log.d("USERFAILURE",msg)
    }



}
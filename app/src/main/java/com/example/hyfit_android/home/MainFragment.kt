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
import androidx.viewpager.widget.ViewPager
import androidx.viewpager2.widget.ViewPager2
import com.example.hyfit_android.BuildConfig
import com.example.hyfit_android.Join.JoinActivity1
import com.example.hyfit_android.Login.LogoutActivity
import com.example.hyfit_android.MainActivity
import com.example.hyfit_android.User
import com.example.hyfit_android.UserInfo.GetUserView
import com.example.hyfit_android.UserRetrofitService
import com.example.hyfit_android.databinding.FragmentMainBinding
import com.example.hyfit_android.exercise.ExerciseActivity
import com.example.hyfit_android.exercise.StairActivity
import com.example.hyfit_android.goal.*
import com.nextnav.nn_app_sdk.NextNavSdk
import com.nextnav.nn_app_sdk.notification.AltitudeContextNotification
import com.nextnav.nn_app_sdk.notification.SdkStatus
import com.nextnav.nn_app_sdk.notification.SdkStatusNotification
import com.nextnav.nn_app_sdk.zservice.WarningMessages
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.selects.select
import kotlinx.coroutines.withContext
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.*
import kotlin.collections.ArrayList

class MainFragment : Fragment(), GetUserView, GetMountainView, GetBuildingView, GetGoalRecView{
    lateinit var binding: FragmentMainBinding

    // pinncale
    private lateinit var sdk: NextNavSdk
    private val NEXTNAV_SERVICE_URL = "api.nextnav.io"
    private val API_KEY = BuildConfig.KEY_VALUE

    private lateinit var sdkMessageObservable: SdkStatusNotification
    private lateinit var altitudeObservable: AltitudeContextNotification

    // modal
    private val goalSelectModal = GoalSelectFragment()
    private val typeSelectFragment = TypeSelectFragment()
    private lateinit var bundle : Bundle
    private  var mountainList: ArrayList<Goal>? = null
    private  var buildingList:  ArrayList<Goal>?= null

    private lateinit var viewPager: ViewPager2


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        userget()
        getMountainProgress()
        getBuildingProgress()
     //   getGoalRec()

        // pinnacle setting
        sdk = NextNavSdk.getInstance()
        sdkMessageObservable = SdkStatusNotification.getInstance()
        val mainActivity = activity as MainActivity
        binding = FragmentMainBinding.inflate(inflater, container, false)
        getGoalRec()
        // viewpager
        viewPager = binding.imageViewPager

        binding.progressBar.visibility = ProgressBar.GONE
        val startBtn = binding.outdoorStartBtn

        startBtn.setOnClickListener {
            if(mountainList != null && buildingList != null){
                openTypeSelectModal()
            }
            else {
                binding.progressBar.visibility = ProgressBar.VISIBLE
                lifecycleScope.launch {
                    while (mountainList == null && buildingList == null) {
                        delay(1000)
                        Log.d("MOUNTAIN", mountainList.toString())
                        Log.d("BUILDING", buildingList.toString())
                    }
                    binding.progressBar.visibility = ProgressBar.GONE
                    openTypeSelectModal()
                }
            }
        }

        binding.stairStartBtn.setOnClickListener {
            // 목표 불러와서 dialog 에 넘겨주기
            if(mountainList != null && buildingList != null) {
                openGoalSelectModal()
            } else {
                binding.progressBar.visibility = ProgressBar.VISIBLE
                lifecycleScope.launch {
                    while(mountainList == null && buildingList == null){
                        delay(1000)
                        Log.d("MOUNTAIN",mountainList.toString())
                        Log.d("BUILDING",buildingList.toString())
                    }
                    binding.progressBar.visibility = ProgressBar.GONE
                    openGoalSelectModal()
                }
            }
        }

        return binding.root
    }

    private fun openTypeSelectModal(){
                val bundle = Bundle().apply {
            putSerializable("building", buildingList)
            putSerializable("mountain", mountainList)
        }
        typeSelectFragment.arguments = bundle
        typeSelectFragment.show(parentFragmentManager, "goalSelect2")
    }

//    private fun openGoalSelectModal2() {
//        val bundle = Bundle().apply {
//            putSerializable("building", buildingList)
//            putSerializable("mountain", mountainList)
//        }
//        goalSelectFragment2.arguments = bundle
//        goalSelectFragment2.show(parentFragmentManager, "goalSelect2")
//    }
    private fun openGoalSelectModal() {
        val bundle = Bundle().apply {
            putSerializable("building", buildingList)
            putSerializable("mountain", mountainList)
            putString("type","stair")
        }
        goalSelectModal.arguments = bundle
        goalSelectModal.show(parentFragmentManager, "goalSelect")
    }

    private fun getMountainProgress(){
        val jwt: String? = getJwt()
        val goalService = GoalService()
        goalService.setGetMountainView(this)
        goalService.getMountainProgress(jwt!!)
    }

    private fun getBuildingProgress(){
        val jwt: String? = getJwt()
        val goalService = GoalService()
        goalService.setGetBuildingView(this)
        goalService.getBuildingProgress(jwt!!)
    }

    private fun getGoalRec(){
        val jwt: String? = getJwt()
        val goalService = GoalService()
        binding.progressBar1.visibility = ProgressBar.VISIBLE
        goalService.setGoalRecView(this)
        goalService.getGoalRec(jwt!!)
    }



    override fun onGetGoalRecSuccess(result: ArrayList<PlaceImage>) {
        Log.d("THISISRESULTT",result.toString())
        val pageAdapter = ImageAdapter(this,result)
        viewPager.adapter = pageAdapter
        binding.progressBar1.visibility = ProgressBar.GONE
        binding.indicator.setViewPager2(viewPager)

    }

    override fun onGetGoalRecFailure(code: Int, msg: String) {
        Log.d("GETGOALRECFAILURE",msg)
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

    }

    override fun onUserSuccess(code: Int, result: User) {
        binding.welcomeText.text = "Welcome, ${result.nickName}"
    }

    override fun onUserFailure(code: Int, msg: String) {
        Log.d("USERFAILURE",msg)
    }

    override fun onGetMountainSuccess(result: ArrayList<Goal>) {
        mountainList = result
        Log.d("MOUNTAINLIST",mountainList.toString())
    }

    override fun onGetMountainFailure(code: Int, msg: String) {
        TODO("Not yet implemented")
    }

    override fun onGetBuildingSuccess(result: ArrayList<Goal>) {
        buildingList = result
        Log.d("BUILDINGLIST",buildingList.toString())

    }

    override fun onGetBuildingFailure(code: Int, msg: String) {
        TODO("Not yet implemented")
    }


}
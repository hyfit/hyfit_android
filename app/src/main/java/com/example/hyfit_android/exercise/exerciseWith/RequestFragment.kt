package com.example.hyfit_android.exercise.exerciseWith

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.PackageManager
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.fragment.app.DialogFragment
import com.example.hyfit_android.BuildConfig
import com.example.hyfit_android.R
import com.example.hyfit_android.databinding.FragmentGoalSelectBinding
import com.example.hyfit_android.databinding.FragmentRequestBinding
import com.example.hyfit_android.exercise.*
import com.gmail.bishoybasily.stomp.lib.Event
import com.gmail.bishoybasily.stomp.lib.StompClient
import com.google.android.gms.maps.model.LatLng
import com.nextnav.nn_app_sdk.PhoneMetaData
import io.reactivex.disposables.Disposable
import okhttp3.OkHttpClient
import org.json.JSONObject
import java.time.LocalDateTime

class RequestFragment : DialogFragment(), DeleteExerciseWithView, StartExerciseView, ExerciseStartView{
    private lateinit var binding : FragmentRequestBinding
    private var exerciseWithId  = 0
    private var workoutType = ""
    private var user2lat = ""
    private var user2lon = ""
    private var myEmail = ""
    private var user2email = ""
    private var exercise1Id = 0
    private var exercise2Id = 0
    private val goalSelectFragment3 = GoalSelectFragment3()

    // websocket
    lateinit var stompConnection: Disposable
    lateinit var topic: Disposable
    private val intervalMillis = 1000L
    private val client = OkHttpClient()
    private val stomp = StompClient(client, intervalMillis)

    // location
    private var mLocationManager: LocationManager? = null
    private var mLocationListener: LocationListener? = null

    @RequiresApi(Build.VERSION_CODES.O)
    @SuppressLint("SetTextI18n")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding =  FragmentRequestBinding.inflate(inflater, container, false)
        dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))


        val bundle = arguments
        exerciseWithId = bundle?.getInt("exerciseWithId")!!
        workoutType = bundle?.getString("workoutType")!!
        user2lat = bundle?.getString("user2Lat")!!
        user2lon = bundle?.getString("user2Lon")!!
        myEmail = bundle?.getString("myEmail")!!
        user2email = bundle?.getString("user2email")!!

        binding.emailRequestName.text ="From. ${bundle?.getString("nickName")}"
        binding.workoutRequestText.text = "Let's $workoutType together!"

        // 승낙버튼
        binding.requestAccept.setOnClickListener{
            startExercise()
        }
        // 취소버튼
        binding.requestClose.setOnClickListener{

            deleteExerciseWith()

        }

        // location
        mLocationManager = PhoneMetaData.mContext.getSystemService(Context.LOCATION_SERVICE) as LocationManager
        mLocationListener = object : LocationListener {
            override fun onLocationChanged(location: Location) {
                var lat = 0.0
                var lng = 0.0
                if (location != null) {
                    lat = location.latitude
                    lng = location.longitude
                }
                binding.progressBar.visibility = View.GONE
                // 요청 받은사람이 요청 보낸사람한테 accept 메세지 (첫번째 request에 sender에게 전송)
                accept(myEmail!!,user2email!!,
                    exercise1Id!!, exercise2Id!!,workoutType,exerciseWithId!!, user2lat, user2lon,lat.toString(), lng.toString() )

                // 요청 보낸사람이 요청 받은사람한테 accept 메세지
                accept(user2email!!,myEmail!!, exercise2Id!!, exercise1Id!!,workoutType,exerciseWithId!!,lat.toString(), lng.toString(), user2lat, user2lon)
                mLocationListener?.let { mLocationManager?.removeUpdates(it) }
                dismiss()
            }
        }
        return binding.root
    }

    // websocket
    private fun accept( sender : String, receiver:  String, exercise1id : Int,exercise2id : Int, workoutType : String , data : Int, user1lat : String, user1lon : String, user2lat : String, user2lon : String){
        val jsonObject = JSONObject()
        jsonObject.put("type","ACCEPT")
        jsonObject.put("sender",sender)
        jsonObject.put("receiver",receiver)
        jsonObject.put("exercise1id",exercise1id)
        jsonObject.put("exercise2id",exercise2id)
        jsonObject.put("workoutType",workoutType)
        jsonObject.put("data",data)
        jsonObject.put("user1lat",user1lat)
        jsonObject.put("user1lon",user1lon)
        jsonObject.put("user2lat",user2lat)
        jsonObject.put("user2lon",user2lon)
        val jsonString = jsonObject.toString()
        Log.d("THISISSENDERDATA",jsonString)

        stomp.send("/pub/accept", jsonString).subscribe {
            if(it){ }
        }
    }

    private fun subscribe() {
        stomp.url = BuildConfig.STOMP_URL
        stompConnection = stomp.connect().subscribe {
            when (it.type) {
                Event.Type.OPENED -> {
                    Log.d("CONNECT", "OPENED")
                }
                Event.Type.CLOSED -> {
                    Log.d("CONNECT", "CLOSED")
                }
                Event.Type.ERROR -> {
                    Log.d("CONNECT", "ERROR")
                }

                null -> TODO()
            }
        }

    }
    private fun getJwt():String?{
        val spf = requireActivity().getSharedPreferences("auth", AppCompatActivity.MODE_PRIVATE)
        return spf!!.getString("jwt","0")
    }

    private fun deleteExerciseWith(){
        val exerciseWithService = ExerciseWithService()
        exerciseWithService.setDeleteExerciseWithView(this)
        exerciseWithService.deleteExerciseWith(exerciseWithId)

    }
    override fun onDeleteExerciseWithSuccess(result: Int) {
        dismiss()
    }

    override fun onDeleteExerciseWithFailure(code: Int, msg: String) {
        Log.d("DELETEFAILED", msg)
    }
    @RequiresApi(Build.VERSION_CODES.O)
    private fun startExercise(){
        binding.progressBar.visibility = View.VISIBLE
        val exerciseService = ExerciseService()
        val jwt: String? = getJwt()
        val current = LocalDateTime.now()
        exerciseService.setExerciseStartView(this)
        exerciseService.startExercise(jwt!!, ExerciseStartReq(workoutType, current.toString(),-1))
    }

    // exerciseWith 시작
    private fun startExerciseWith(exerciseId : Long){
        val jwt: String? = getJwt()
        val exerciseWithService = ExerciseWithService()
        exerciseWithService.setStartExerciseView(this)
        exerciseWithService.startExercise(jwt!!, ExerciseWithReq(exerciseWithId,exerciseId))
    }



    override fun onStartExerciseViewSuccess(result: ExreciseWith) {
        subscribe()

        exerciseWithId = result.exerciseWithId!!
        exercise1Id = result.user1ExerciseId!!
        exercise2Id = result.user2ExerciseId!!
        // 위치 가져오기
        if (ActivityCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            return
        }
        mLocationListener?.let { mLocationManager?.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0f, it) }


    }

    override fun onStartExerciseViewFailure(code: Int, msg: String) {
        Log.d("EXERCISESTART",msg)
    }

    override fun onExerciseStartSuccess(result: Exercise) {
        startExerciseWith(result.exerciseId!!)
    }

    override fun onExerciseStartFailure(code: Int, msg: String) {
        Log.d("EXERCISESTART",msg)
    }



}
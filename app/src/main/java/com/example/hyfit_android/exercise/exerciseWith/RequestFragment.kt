package com.example.hyfit_android.exercise.exerciseWith

import android.annotation.SuppressLint
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.DialogFragment
import com.example.hyfit_android.BuildConfig
import com.example.hyfit_android.R
import com.example.hyfit_android.databinding.FragmentGoalSelectBinding
import com.example.hyfit_android.databinding.FragmentRequestBinding
import com.example.hyfit_android.exercise.Exercise
import com.example.hyfit_android.exercise.ExerciseService
import com.example.hyfit_android.exercise.ExerciseStartReq
import com.example.hyfit_android.exercise.ExerciseStartView
import com.gmail.bishoybasily.stomp.lib.Event
import com.gmail.bishoybasily.stomp.lib.StompClient
import io.reactivex.disposables.Disposable
import okhttp3.OkHttpClient
import org.json.JSONObject
import java.time.LocalDateTime

class RequestFragment : DialogFragment(), DeleteExerciseWithView, StartExerciseView, ExerciseStartView {
    private lateinit var binding : FragmentRequestBinding
    private var exerciseWithId  = 0
    private var workoutType = ""
    private val goalSelectFragment3 = GoalSelectFragment3()

    // websocket
    lateinit var stompConnection: Disposable
    lateinit var topic: Disposable
    private val intervalMillis = 1000L
    private val client = OkHttpClient()
    private val stomp = StompClient(client, intervalMillis)



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


        binding.emailRequestName.text ="From. ${bundle?.getString("nickName")}"
        binding.workoutTypeRequest.text = "workout type : $workoutType"

        // 승낙버튼
        binding.requestAccept.setOnClickListener{
            // 목표 선택해야함
            if(workoutType.equals("climbing")  || workoutType.equals("stair")){
                // 목표 선택으로 넘기기
                val bundle = Bundle().apply {
                    putString("type",workoutType)
                    putInt("exerciseWithId",exerciseWithId)
//                    putString("MyNickName",myNickName)
//                    putSerializable("building", buildingList)
//                    putSerializable("mountain", mountainList)
                }
                goalSelectFragment3.arguments = bundle
                goalSelectFragment3.show(parentFragmentManager, "goalSelect3")
                dismiss()
            }
            else {
                // 바로 운동 시작
                startExercise()

            }
        }
        // 취소버튼
        binding.requestClose.setOnClickListener{
            deleteExerciseWith()
        }
        return binding.root
    }

    // websocket
    private fun accept(type:String, sender : String,receiver:  String, sender_nickName : String,receiver_nickName : String, workoutType : String, goalId : Int , data : Int){
        val jsonObject = JSONObject()
        jsonObject.put("type",type)
        jsonObject.put("sender",sender)
        jsonObject.put("receiver",receiver)
        jsonObject.put("sender_nickName",sender_nickName)
        jsonObject.put("receiver_nickName",receiver_nickName)
        jsonObject.put("workoutType",workoutType)
        jsonObject.put("goalId",goalId)
        jsonObject.put("data",data)
        val jsonString = jsonObject.toString()
        Log.d("THISISSENDERDATA",jsonString)

        stomp.send("/pub/accept", jsonString).subscribe {
            if(it){ }
        }
    }

    private fun subscribe(exerciseWithId : Int) {
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

        // exerciseWith 채널 구독
        topic = stomp.join("/sub/channel/${exerciseWithId}")
            .doOnError { error -> Log.d("ERROR", "subscribe error") }
            .subscribe { chatData ->
                val chatObject = JSONObject(chatData)
                Log.d("THISISCHATDATA!!!", chatObject.toString())
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
        subscribe(result.exerciseWithId!!)
        accept("ACCEPT", result.user2Email!!,result.user1Email!!, "", "",workoutType,-1,result.exerciseWithId!!)
        dismiss()

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
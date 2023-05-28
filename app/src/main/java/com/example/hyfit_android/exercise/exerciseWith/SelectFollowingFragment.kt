package com.example.hyfit_android.exercise.exerciseWith

import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentManager
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.hyfit_android.BuildConfig.STOMP_URL
import com.example.hyfit_android.R
import com.example.hyfit_android.community.FollowService
import com.example.hyfit_android.community.GetFollowingView
import com.example.hyfit_android.databinding.FragmentSelectFollowingBinding
import com.example.hyfit_android.exercise.Exercise
import com.example.hyfit_android.exercise.ExerciseService
import com.example.hyfit_android.exercise.ExerciseStartReq
import com.example.hyfit_android.exercise.ExerciseStartView
import com.example.hyfit_android.goal.Goal
import com.example.hyfit_android.goal.GoalDetailRVAdapter
import com.gmail.bishoybasily.stomp.lib.Event
import com.gmail.bishoybasily.stomp.lib.StompClient
import io.reactivex.disposables.Disposable
import okhttp3.OkHttpClient
import org.json.JSONObject
import java.time.LocalDateTime

class SelectFollowingFragment : DialogFragment(),GetFollowingView,FollowingClickListener,RequestExerciseView, ExerciseStartView{

    private lateinit var binding : FragmentSelectFollowingBinding
    private lateinit var loadingDialog : Dialog
    private lateinit var followingRVAdaptor : FollowingSelectRVAdaptor
    private var isSelected = 0
    private var clickEmail = ""
    private var myEmail = ""
    private var myNickName = ""
    private var workoutType = ""
    private var goalId = -1

    // websocket
    lateinit var stompConnection: Disposable
    lateinit var topic: Disposable
    private val intervalMillis = 1000L
    private val client = OkHttpClient()
    private val stomp = StompClient(client, intervalMillis)

    private lateinit var email : String
    // 나한테 메세지를 보낸 사람 (sender)
    private lateinit var receiver : String
    private lateinit var  receiver_nickName: String
    private var exerciseWithId =0

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentSelectFollowingBinding.inflate(inflater, container, false)
        dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        getFollowingList()
        binding.selectFollowingClose.setOnClickListener{
            dismiss()
        }
        binding.selectFollowingList.addItemDecoration(DividerItemDecoration(context, 1))
        val bundle = arguments
        myEmail = bundle?.getString("MyEmail").toString()
        myNickName = bundle?.getString("MyNickName").toString()
        workoutType = bundle?.getString("type").toString()
        goalId = bundle?.getInt("goalId")!!

        binding.requestBtn.setOnClickListener{
            if(isSelected == 0){
                Toast.makeText(requireContext(), "You need to select a friend.", Toast.LENGTH_SHORT).show()
            }
            else {
                receiver =  clickEmail.split(",")[0]
                receiver_nickName = clickEmail.split(",")[1]
                startExercise()
              //  requestExercise(receiver)
                // send("ENTER",myEmail,clickEmail)
            }
        }

        return binding.root
    }

    // websocket
    private fun request(type:String, sender : String,receiver:  String, sender_nickName : String,receiver_nickName : String, workoutType : String, goalId : Int , data : Int){
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

        stomp.send("/pub/request", jsonString).subscribe {
            if(it){ }
        }
    }

    private fun subscribe(exerciseWithId : Int) {
        stomp.url =STOMP_URL
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
//    override fun onDestroy() {
//        super.onDestroy()
//       // send("QUIT", email,receiver,exerciseWithId)
//       // topic.dispose()
//        stompConnection.dispose()
//    }

    // user1 일단 운동 시작
    @RequiresApi(Build.VERSION_CODES.O)
    private fun startExercise(){
        val exerciseService = ExerciseService()
        val jwt: String? = getJwt()
        val current = LocalDateTime.now()
        exerciseService.setExerciseStartView(this)
        exerciseService.startExercise(jwt!!, ExerciseStartReq(workoutType, current.toString(),goalId.toLong()))
    }

    override fun onExerciseStartSuccess(result: Exercise) {
        requestExercise(receiver, result.exerciseId?.toInt() ?: 0)
    }

    override fun onExerciseStartFailure(code: Int, msg: String) {
        TODO("Not yet implemented")
    }

    private fun requestExercise(user2Email : String, user1exerciseId : Int){
        val jwt: String? = getJwt()
        val exerciseWithService = ExerciseWithService()
        exerciseWithService.setRequestExerciseView(this)
        exerciseWithService.requestExercise(jwt!!,ExerciseWithReq1(user2Email,user1exerciseId))

    }

    override fun onRequestExerciseSuccess(result: ExreciseWith) {
        subscribe(result.exerciseWithId!!.toInt())
        // type : request로 보냄
        request("REQUEST",myEmail,receiver,myNickName, receiver_nickName,workoutType,goalId,result.exerciseWithId?.toInt() ?: 0)
        dismiss()
    }

    override fun onRequestExerciseFailure(code: Int, msg: String) {
        Log.d("REQUESTFAIL",msg)
    }


    private fun getFollowingList(){
        val followService =FollowService()
        followService.setFollowingView(this)
        val jwt: String? = getJwt()
        followService.getFollowingList(jwt!!)
       binding.progressBar.visibility = View.VISIBLE
    }
    override fun onFollowingSuccess(result:  List<String>) {
        initRecyclerView(result)
        Log.d("THISISRESULT",result.toString())
        binding.progressBar.visibility = View.GONE

    }

    override fun onFollowingFailure(code: Int, msg: String) {
        TODO("Not yet implemented")
    }


    private fun getJwt():String?{
        val spf = requireActivity().getSharedPreferences("auth", AppCompatActivity.MODE_PRIVATE)
        return spf!!.getString("jwt","0")
    }

    private fun initRecyclerView(result : List<String>) {
        followingRVAdaptor = FollowingSelectRVAdaptor(requireContext(), result, this)
        binding.selectFollowingList.adapter = followingRVAdaptor
        binding.selectFollowingList.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
    }

    override fun onFollowClick(data: String) {
       Log.d("ISCLICKED",data)
        isSelected = 1
        clickEmail = data
    }

    override fun onFollowNonClicked() {
        isSelected = 0
        clickEmail = ""
    }



}
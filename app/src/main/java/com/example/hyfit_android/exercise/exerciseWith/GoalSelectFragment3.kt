package com.example.hyfit_android.exercise.exerciseWith

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
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.hyfit_android.BuildConfig
import com.example.hyfit_android.MainActivity
import com.example.hyfit_android.R
import com.example.hyfit_android.databinding.FragmentGoalModalDeleteBinding
import com.example.hyfit_android.databinding.FragmentGoalSelect2Binding
import com.example.hyfit_android.databinding.FragmentGoalSelect3Binding
import com.example.hyfit_android.databinding.FragmentGoalSelectBinding
import com.example.hyfit_android.exercise.Exercise
import com.example.hyfit_android.exercise.ExerciseService
import com.example.hyfit_android.exercise.ExerciseStartReq
import com.example.hyfit_android.exercise.ExerciseStartView
import com.example.hyfit_android.exercise.exerciseWith.SelectFollowingFragment
import com.example.hyfit_android.goal.*
import com.example.hyfit_android.home.GoalSelectRVAdaptor
import com.example.hyfit_android.home.OnGoalClickListener
import com.gmail.bishoybasily.stomp.lib.Event
import com.gmail.bishoybasily.stomp.lib.StompClient
import io.reactivex.disposables.Disposable
import okhttp3.OkHttpClient
import org.json.JSONObject
import java.time.LocalDateTime

class GoalSelectFragment3 : DialogFragment(),GetMountainView, OnGoalClickListener, GetBuildingView,ExerciseStartView, StartExerciseView{
    private lateinit var binding: FragmentGoalSelect3Binding
    private lateinit var goalSelectRVAdaptor: GoalSelectRVAdaptor
    private lateinit var mountainList : ArrayList<Goal>
    private lateinit var buildingList : ArrayList<Goal>
    private var exerciseWithId  = 0
    private var isSelected = 0
    private var myEmail = ""
    private var myNickName = ""
    private var workoutType = ""
    private var goalId = -1

    // 친구 선택
    private val followingSelectFragment = SelectFollowingFragment()

    // websocket
    lateinit var stompConnection: Disposable
    lateinit var topic: Disposable
    private val intervalMillis = 1000L
    private val client = OkHttpClient()
    private val stomp = StompClient(client, intervalMillis)


    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding =  FragmentGoalSelect3Binding.inflate(inflater, container, false)
        dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        // X버튼
        binding.selectGoalClose.setOnClickListener{
            dismiss()
        }

        val mainActivity = activity as MainActivity

        mountainList = mainActivity.mountainList!!
        buildingList = mainActivity.buildingList!!
        initSearchRecyclerView(mountainList)

        val bundle = arguments

        exerciseWithId = bundle?.getInt("exerciseWithId")!!
        workoutType = bundle?.getString("type").toString()

        binding.goalBuildingBtn.setOnClickListener{
            initSearchRecyclerView(buildingList)
        }

        binding.goalMountainBtn.setOnClickListener{
            initSearchRecyclerView(mountainList)
        }
        // 구분선
        binding.selectGoalList.addItemDecoration(DividerItemDecoration(context, 1))

        // 시작버튼
        binding.selectBtn.setOnClickListener{
            startExercise()

        }
       //  getMountainProgress()

        return binding.root
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun startExercise(){
        val exerciseService = ExerciseService()
        val jwt: String? = getJwt()
        val current = LocalDateTime.now()
        exerciseService.setExerciseStartView(this)
        exerciseService.startExercise(jwt!!, ExerciseStartReq(workoutType, current.toString(),goalId.toLong()))
    }

    override fun onExerciseStartSuccess(result: Exercise) {
        startExercise(result.exerciseId!!)
    }

    override fun onExerciseStartFailure(code: Int, msg: String) {
       Log.d("EXERCISESTART",msg)
    }

    // exerciseWith 시작
    private fun startExercise(exerciseId : Long){
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
        TODO("Not yet implemented")
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


    private fun initSearchRecyclerView(GoalList : ArrayList<Goal>){
        goalSelectRVAdaptor = GoalSelectRVAdaptor(requireContext(),GoalList,this)
        binding.selectGoalList.adapter = goalSelectRVAdaptor
        binding.selectGoalList.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL,false)
    }

    private fun getJwt():String?{
        val spf = requireActivity().getSharedPreferences("auth", AppCompatActivity.MODE_PRIVATE)
        return spf!!.getString("jwt","0")
    }
    private fun getMountainProgress(){
        val jwt: String? = getJwt()
        val goalService = GoalService()
        goalService.setGetMountainView(this)
        binding.progressBar.visibility = View.VISIBLE
        goalService.getMountainProgress(jwt!!)
    }

    private fun getBuildingProgress(){
        val jwt: String? = getJwt()
        val goalService = GoalService()
        goalService.setGetBuildingView(this)
        binding.progressBar.visibility = View.VISIBLE
        goalService.getBuildingProgress(jwt!!)
    }


    override fun onGetMountainSuccess(result: ArrayList<Goal>) {
        binding.progressBar.visibility = View.GONE
        initSearchRecyclerView(result)
    }

    override fun onGetMountainFailure(code: Int, msg: String) {
        val list = ArrayList<Goal>()
        initSearchRecyclerView(list)
    }

    override fun onGetBuildingSuccess(result: ArrayList<Goal>) {
        binding.progressBar.visibility = View.GONE
        initSearchRecyclerView(result)
    }

    override fun onGetBuildingFailure(code: Int, msg: String) {
        val list = ArrayList<Goal>()
        initSearchRecyclerView(list)
    }




    override fun onItemClick(data: Goal) {
        goalId = data.goalId!!
        isSelected  = 1
    }

    override fun onItemNonSelected() {
        isSelected = 0
        goalId = -1
    }

}
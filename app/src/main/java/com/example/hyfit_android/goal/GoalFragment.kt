package com.example.hyfit_android.goal

import android.annotation.SuppressLint
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.hyfit_android.R
import com.example.hyfit_android.databinding.FragmentGoalBinding
import com.google.gson.Gson
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

/**
 * A simple [Fragment] subclass.
 * Use the [GoalFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class GoalFragment : Fragment() , GetGoalView, GetDoneGoalView, OnGoalChangeListener,SaveGoalView, DeleteGoalView{
    lateinit var binding: FragmentGoalBinding
    private var gson:Gson = Gson()
    private lateinit var goalDetailRVAdapter: GoalDetailRVAdapter
    private lateinit var goalList : ArrayList<Goal>
    private val deleteFragment = GoalModalDelete()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentGoalBinding.inflate(inflater, container, false)
        // 페이지 들어오자마자 getGoal
        getGoalProgress()
        val dialogButton = binding.goalPlus
        dialogButton.setOnClickListener {
            val dialogFragment = GoalModalFragment()
            dialogFragment.show(parentFragmentManager, "dialog1")
        }

        binding.goalDone.setOnClickListener{
            getGoalDone()
        }
        binding.goalInProgress.setOnClickListener{
            getGoalProgress()
        }
        return binding.root
    }
    private fun initRecyclerView(result : ArrayList<Goal>){
        goalDetailRVAdapter = GoalDetailRVAdapter(requireContext(), result,this)
        binding.myGoalList.adapter = goalDetailRVAdapter
        binding.myGoalList.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL,false)
    }

    private fun getJwt():String?{
        val spf = activity?.getSharedPreferences("auth", AppCompatActivity.MODE_PRIVATE)
        return spf!!.getString("jwt","0")
    }

    private fun getGoalProgress(){
        val jwt = getJwt()
        val goalService = GoalService()
        goalService.setGetGoalView(this)
         goalService.getGoalProgress(jwt!!)

    }
    private fun getGoalDone(){
        val jwt = getJwt()
//        val goalDoneJson = arguments?.getString("goal")
//        val goalDone = gson.fromJson(goalDoneJson, Goal::class.java)
        val goalService = GoalService()
        goalService.setGetDoneGoalView(this)
        goalService.getGoalDone(jwt!!)
    }
    @SuppressLint("ResourceAsColor")
    override fun onGetGoalSuccess(result: ArrayList<Goal>) {
//        initProgressRecyclerView(result)
        goalList = result
        initRecyclerView(result)
        binding.goalInProgress.setBackgroundResource(R.drawable.tag_btn_back_blue)
        binding.goalInProgress.setTextColor(Color.parseColor("#f3f3f3"))
        binding.goalDone.setBackgroundResource(R.drawable.ic_rectangle_65)
        binding.goalDone.setTextColor(Color.parseColor("#FF000000"))
    }

    override fun onGetGoalFailure(code: Int, msg: String) {
        if(code==2203){
            val goalList = ArrayList<Goal>()
            initRecyclerView(goalList)
        }
        if(code==2202){
            val goalList = ArrayList<Goal>()
            initRecyclerView(goalList)
        }
    }

    @SuppressLint("ResourceAsColor")
    override fun onGetDoneGoalSuccess(result: ArrayList<Goal>) {
//        goalList = result
//
        initRecyclerView(result)
        binding.goalDone.setBackgroundResource(R.drawable.tag_btn_back_blue)
        binding.goalDone.setTextColor(Color.parseColor("#f3f3f3"))
        binding.goalInProgress.setBackgroundResource(R.drawable.ic_rectangle_65)
       binding.goalInProgress.setTextColor(Color.parseColor("#FF000000"))

    }

    override fun onGetDoneGoalFailure(code: Int, msg: String) {
        if(code==2203){
            val goalList = ArrayList<Goal>()
            initRecyclerView(goalList)
        }
        if(code==2202){
            val goalList = ArrayList<Goal>()
            initRecyclerView(goalList)
        }
    }

    override fun onItemClick(data: Goal) {
        Log.d("result",data.toString())
        val bundle = Bundle().apply {

            data.goalId?.let { putLong("goalId", it.toLong()) }
            putString("goalTitle",data.place)
            putString("goalRate",data.rate.toString())
        }
        deleteFragment.arguments = bundle
        deleteFragment.show(parentFragmentManager, "dialog_delete")
    }

    override fun onItemChange() {
        Toast.makeText(requireContext(), "delete!!!", Toast.LENGTH_SHORT).show()
        getGoalProgress()
//        goalDetailRVAdapter.notifyDataSetChanged()
//        goalDetailRVAdapter.updateData()
    }

    override fun onSaveGoalSuccess(result: Goal) {
        GlobalScope.launch {
            getGoalProgress()
            withContext(Dispatchers.Main){
                for (fragment in childFragmentManager.fragments) {
                    if (fragment is DialogFragment) {
                         fragment.dismiss()
                     }
                 }
            }
        }
        getGoalProgress()
    }

    override fun onSaveGoalFailure(code: Int, msg: String) {

    }

    override fun onDeleteGoalSuccess(result: String) {
        GlobalScope.launch {
            getGoalProgress()
            withContext(Dispatchers.Main) {
                deleteFragment.dismiss()
            }
        }
    }

    override fun onDeleteGoalFailure(code: Int, msg: String) {
    }

}
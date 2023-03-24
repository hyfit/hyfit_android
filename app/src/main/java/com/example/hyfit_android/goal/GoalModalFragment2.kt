package com.example.hyfit_android.goal

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.hyfit_android.R
import com.example.hyfit_android.databinding.FragmentGoalModal2Binding
import com.example.hyfit_android.databinding.FragmentGoalModalBinding
import com.google.android.libraries.places.api.Places
import com.google.gson.Gson
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"


class GoalModalFragment2 : DialogFragment(), GetPlaceView{
    lateinit var binding: FragmentGoalModal2Binding
    private lateinit var goalSearchRVAdapter: GoalSearchRVAdapter
    private lateinit var type : String
    private var gson: Gson = Gson()
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentGoalModal2Binding.inflate(inflater, container, false)

        binding.searchClose.setOnClickListener{
            dismiss()
        }
        val bundle = arguments
        type = bundle?.getString("type").toString()
        dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        getGoalPlace("Asia")
        binding.placeAmerica.setOnClickListener{
            getGoalPlace("America")
        }
        binding.placeAsia.setOnClickListener{
            getGoalPlace("Asia")
        }
        binding.placeEurope.setOnClickListener{
            getGoalPlace("Europe")
        }


        return binding.root
    }

    private fun initSearchRecyclerView(placeList : ArrayList<Place>){
        goalSearchRVAdapter = GoalSearchRVAdapter(requireContext(),placeList)
        binding.searchGoalList.adapter = goalSearchRVAdapter
        binding.searchGoalList.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL,false)
    }

    private fun getGoalPlace(continents : String){

        val goalPlaceJson = arguments?.getString("goalPlace")
        val goalPlace = gson.fromJson(goalPlaceJson, Place::class.java)
        val placeService = PlaceService()
        placeService.setGetPlaceView(this)
        placeService.getGoalAllPlace(type,continents)
    }


    override fun onGetPlaceSuccess(result: ArrayList<Place>) {
        Log.d("this is!",result.toString())
        initSearchRecyclerView(result);
    }

    override fun onGetPlaceFailure(code: Int, msg: String) {
        TODO("Not yet implemented")
    }

}
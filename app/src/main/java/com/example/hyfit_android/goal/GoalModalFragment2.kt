package com.example.hyfit_android.goal

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.hyfit_android.databinding.FragmentGoalModal2Binding
import com.google.gson.Gson
import kotlin.properties.Delegates

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"


class GoalModalFragment2 : DialogFragment(), GetPlaceView, OnItemClickListener, GetPlacePageView{
    lateinit var binding: FragmentGoalModal2Binding
    private lateinit var goalSearchRVAdapter: GoalSearchRVAdapter
    private lateinit var type : String
    private lateinit var place : String
    private var gson: Gson = Gson()
    private var page = 1;
    private var continents = "Asia"
    private var isSelected = 0;
    private var totalPage =1;
    val fragment = GoalModalFragment3()
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentGoalModal2Binding.inflate(inflater, container, false)

        // 닫기
        binding.searchClose.setOnClickListener{
            dismiss()
        }
        // 구분선
        binding.searchGoalList.addItemDecoration(DividerItemDecoration(context, 1))
        // modal1에서 온 데이터 받기
        val bundle = arguments
        type = bundle?.getString("type").toString()

        // background 투명하게
        dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        // 각 대륙별 페이지수
        placePagination()
        // 각 대륙 별 데이터 받기 (처음)
        getPlaceByPage()
        // 처음(1/1) 설정
        binding.goalCurrentPage.text = "(1/"+totalPage.toString()+")"

        binding.placeAmerica.setOnClickListener{
            continents = "America"
            page = 1;
            placePagination()
//            binding.goalCurrentPage.text = "(1/"+totalPage.toString()+")"
//            getPlaceByPage()
        }
        binding.placeAsia.setOnClickListener{
            continents = "Asia"
            page = 1;
            placePagination()
//            binding.goalCurrentPage.text = "(1/"+totalPage.toString()+")"
//            getPlaceByPage()

        }
        binding.placeEurope.setOnClickListener{
            continents = "Europe"
            page = 1;
            placePagination()
//            binding.goalCurrentPage.text = "(1/"+totalPage.toString()+")"
//            getPlaceByPage()
        }

        binding.placePaginationLeft.setOnClickListener{
            if(page===1){
                Toast.makeText(requireContext(), "This is the first page.", Toast.LENGTH_SHORT).show()
            }
            else{
                page -= 1
                binding.goalCurrentPage.text="(" + page.toString() + "/"+totalPage.toString()+")"
                getPlaceByPage()
            }
        }
        binding.placePaginationRight.setOnClickListener{
            if(page==totalPage){
                Toast.makeText(requireContext(), "This is the last page.", Toast.LENGTH_SHORT).show()
            }
            else{
                page +=1
                binding.goalCurrentPage.text="(" + page.toString() + "/"+totalPage.toString()+")"
                getPlaceByPage()
            }
        }

        binding.searchNext.setOnClickListener{
            if(isSelected==1){
                val bundle = Bundle().apply {
                    putString("type", type)
                    putString("place",place)
                }
                fragment.arguments = bundle
                fragment.show(parentFragmentManager, "dialog3")
                dismiss()
            }
            else {
                Toast.makeText(requireContext(), "please select", Toast.LENGTH_SHORT).show()
            }
        }

        return binding.root
    }

    private fun initSearchRecyclerView(placeList : ArrayList<Place>){
        goalSearchRVAdapter = GoalSearchRVAdapter(requireContext(),placeList,this)
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

    private fun getPlaceByPage(){
        val goalPlaceJson = arguments?.getString("goalPlace")
        val placeService = PlaceService()
        placeService.setGetPlaceView(this)
        placeService.getPlaceByPage(type,continents,page)
    }

    private fun placePagination(){
        val placeService = PlaceService()
        placeService.setPlacePageView(this)
        placeService.getPageSize(type,continents)
    }



    override fun onGetPlaceSuccess(result: ArrayList<Place>) {
        initSearchRecyclerView(result);
    }

    override fun onGetPlaceFailure(code: Int, msg: String) {
        TODO("Not yet implemented")
    }

    override fun onItemClick(data: Place) {
        place = data.name.toString()
//        binding.searchNext.text = "select"
        isSelected  = 1
    }

    override fun onItemNonSelected() {
//        binding.searchNext.text = "next"
        isSelected = 0
    }

    override fun onGetPlacePageSuccess(result: Int) {
        totalPage = result;
        binding.goalCurrentPage.text = "(1/$totalPage)"
        getPlaceByPage()
    }

    override fun onGetPlacePageFailure(code: Int, msg: String) {
        TODO("Not yet implemented")
    }

}
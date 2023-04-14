package com.example.hyfit_android.home

//import android.app.AlertDialog
import androidx.appcompat.app.AlertDialog
import android.app.Dialog
import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.example.hyfit_android.Join.JoinActivity1
import com.example.hyfit_android.Login.LogoutActivity
import com.example.hyfit_android.databinding.FragmentMainBinding
import com.example.hyfit_android.exercise.ExerciseActivity
import kotlinx.coroutines.selects.select

/**
 * A simple [Fragment] subclass.
 * Use the [MainFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class MainFragment : Fragment() {
    lateinit var binding: FragmentMainBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentMainBinding.inflate(inflater, container, false)

        val startBtn = binding.mainStartBtn
        startBtn.setOnClickListener {
            activity?.let {
                val intent = Intent(context, ExerciseActivity::class.java)
                startActivity(intent)
            }
        }
        
        //val selectBtn = binding.selectGoalBtn
        //selectBtn.setOnClickListener{
            //GoalDialogFragment().show(parentFragmentManager, "selectgoal")
            //val dialogFragment = GoalDialogFragment()
            //dialogFragment.show()

            /* 다이얼로그 (this)에서 오류
            val items = arrayOf()
            var checkedItemPosition = 0
            var selectedGoal: String? = null
            val builder = AlertDialog.Builder(this)
                .setTitle("Select Your Goal")
                .setSingleChoiceItems(items, checkedItemPosition, object : DialogInterface.OnClickListener {
                    override fun onClick(dialog: DialogInterface, which: Int) {
                        checkedItemPosition = which
                    }
                })
                .setPositiveButton("SELECT", object : DialogInterface.OnClickListener {
                    override fun onClick(dialog: DialogInterface?, which: Int) {
                        Log.d("selectedGoal", "checkedItemPosition : $checkedItemPosition")
                    }
                })
                .show()
             */


       // }
        return binding.root
    }

    private fun getJwt():String?{
        val spf = activity?.getSharedPreferences("auth", AppCompatActivity.MODE_PRIVATE)
        return spf!!.getString("jwt","0")
    }

}
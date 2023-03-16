package com.example.hyfit_android

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.hyfit_android.databinding.FragmentSetBinding

/**
 * A simple [Fragment] subclass.
 * Use the [SetFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class SetFragment : Fragment() {
    lateinit var binding: FragmentSetBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentSetBinding.inflate(inflater, container, false)

        return binding.root
    }

}
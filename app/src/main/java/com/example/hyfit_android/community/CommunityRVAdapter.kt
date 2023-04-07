package com.example.hyfit_android.community

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.hyfit_android.databinding.PostItemBinding

class CommunityRVAdapter(val context: Context, val result: ArrayList<Post>) : RecyclerView.Adapter<CommunityRVAdapter.ViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding: PostItemBinding = PostItemBinding.inflate(LayoutInflater.from(parent.context))

        return ViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return result.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(result[position])
    }

    inner class ViewHolder(val binding: PostItemBinding): RecyclerView.ViewHolder(binding.root) {

        fun bind(post: Post){



        }


    }
}
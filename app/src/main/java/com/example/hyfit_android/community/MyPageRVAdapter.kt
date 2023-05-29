package com.example.hyfit_android.community

import android.content.Context
import android.os.Build
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.hyfit_android.databinding.MypostItemBinding

class MyPageRVAdapter(val context: Context, val result: ArrayList<PostImgInfo>, val clickListener: OnMyPostClickListener) : RecyclerView.Adapter<MyPageRVAdapter.ViewHolder>()  {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyPageRVAdapter.ViewHolder {
        val binding: MypostItemBinding = MypostItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return result.size
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onBindViewHolder(holder: MyPageRVAdapter.ViewHolder, position: Int) {
        // 한 게시물 눌렀을 때
        holder.binding.myPostLayout.setOnClickListener {
            clickListener.onMyPostClick(result[holder.absoluteAdapterPosition])
        }

        holder.bind(result[holder.absoluteAdapterPosition])

    }

    inner class ViewHolder(val binding: MypostItemBinding): RecyclerView.ViewHolder(binding.root) {

        fun bind(postInfo: PostImgInfo) {
            val postImageView = binding.postIv
            val postLikeNumTv = binding.likeNumTv
            val postCommentNumTv = binding.commentNumTv

            Glide.with(itemView)
                .load("https://d14okywu7b1q79.cloudfront.net"+postInfo.postImgUrl)
                .into(postImageView)

            postLikeNumTv.text = postInfo.postLikeNum.toString()
            postCommentNumTv.text = postInfo.postCommentNum.toString()
        }
    }

}
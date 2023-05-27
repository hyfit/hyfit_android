package com.example.hyfit_android.community

import android.content.Context
import android.os.Build
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.hyfit_android.R
import com.example.hyfit_android.databinding.PostItemBinding
import java.time.Clock
import java.time.LocalDateTime
import java.time.temporal.ChronoUnit

class CommunityRVAdapter(val context: Context, val result: List<PostPagination>, val clickListener: OnPostClickListener) : RecyclerView.Adapter<CommunityRVAdapter.ViewHolder>() {
    init {
        setHasStableIds(true)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding: PostItemBinding = PostItemBinding.inflate(LayoutInflater.from(parent.context))
        return ViewHolder(binding)
    }

    override fun getItemId(position: Int): Long {
        return super.getItemId(position)
    }

    override fun getItemCount(): Int {
        return result.size
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(result[holder.absoluteAdapterPosition])

        // 한 게시물 눌렀을 때
        holder.binding.postLayout.setOnClickListener {
            clickListener.onPostClick(result[holder.absoluteAdapterPosition])
        }

    }

    inner class ViewHolder(val binding: PostItemBinding): RecyclerView.ViewHolder(binding.root) {
        @RequiresApi(Build.VERSION_CODES.O)
        fun bind(post: PostPagination){
            val postImageView = binding.postIv
            val profileImageView = binding.profileIv
            val writerTextView = binding.writerTv
            val dateTextView = binding.dateTv
            val contentTextView = binding.contentTv

            postImageView.clipToOutline = true
            contentTextView.text = post.content
            Glide.with(itemView)
                .load("https://d14okywu7b1q79.cloudfront.net"+post.imageUrl)
                .into(postImageView)

            // 프로필 이미지 없는 경우 기본 프로필 이미지 보여줌
            if(post.profileImg.isNullOrEmpty()) {
                profileImageView.setImageResource(R.drawable.user3)
            } else {
                Glide.with(itemView)
                    .load("https://d14okywu7b1q79.cloudfront.net"+post.profileImg)
                    .into(profileImageView)
            }

            writerTextView.text = post.nickname
            var localDateTime = LocalDateTime.now(Clock.systemDefaultZone())
            var postDateTime = post.createdAt

            // 같은 날 생성된 게시물
            if(localDateTime.year == postDateTime.year && localDateTime.month == postDateTime.month && localDateTime.dayOfMonth == postDateTime.dayOfMonth) {
               // 같은 시간
                if(localDateTime.hour == postDateTime.hour) {
                    var minutes = ChronoUnit.MINUTES.between(postDateTime, localDateTime)
                    dateTextView.text = if (minutes == 0L || minutes == 1L) "1 minute ago" else minutes.toString() + " minutes ago"
                }
                // 다른 시간
                else {
                    var hours = ChronoUnit.HOURS.between(postDateTime, localDateTime)
                    dateTextView.text = if (hours == 1L) "1 hour ago" else hours.toString() + "  hours ago"
                }
            // 다른 날
            } else {
                var days = ChronoUnit.DAYS.between(postDateTime, localDateTime)
                // 한 달 이상
                if(days >= 30L) {
                    var months = ChronoUnit.MONTHS.between(postDateTime, localDateTime)
                    dateTextView.text = if(months == 1L) "1 month ago" else months.toString() + " months ago"
                }
                // 한 달 미만
                else{
                    dateTextView.text = if(days == 1L) "1 day ago" else days.toString() + " days ago"
            }
            }

        }

    }
}
package com.example.hyfit_android.community

import android.content.Context
import android.os.Build
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.hyfit_android.R
import com.example.hyfit_android.databinding.CommentItemBinding
import java.time.Clock
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.time.temporal.ChronoUnit

class CommentRVAdapter(val context: Context, val result: ArrayList<PostCommentList>) : RecyclerView.Adapter<CommentRVAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CommentRVAdapter.ViewHolder {
        val binding: CommentItemBinding = CommentItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return result.size
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onBindViewHolder(holder: CommentRVAdapter.ViewHolder, position: Int) {

        holder.bind(result[position])

    }

    inner class ViewHolder(val binding: CommentItemBinding): RecyclerView.ViewHolder(binding.root) {

        @RequiresApi(Build.VERSION_CODES.O)
        fun bind(commentInfo: PostCommentList) {
            val profileIv = binding.profileIv
            val contentTv = binding.commentTv
            val writerTv = binding.commentWriterTv
            val dateTv = binding.dateTv

            if (commentInfo.profileImg.isNullOrEmpty()) {
                profileIv.setImageResource(R.drawable.user3)
            } else {
                Glide.with(itemView)
                    .load("https://d14okywu7b1q79.cloudfront.net"+commentInfo.profileImg)
                    .into(profileIv)
            }
            contentTv.text = commentInfo.content
            writerTv.text = commentInfo.nickName

            val localDateTime = LocalDateTime.now(Clock.systemDefaultZone())
            val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss")
            val postDateTime = LocalDateTime.parse(commentInfo.createdAt, formatter)

            // 같은 날 생성된 게시물
            if(localDateTime.year == postDateTime.year && localDateTime.month == postDateTime.month && localDateTime.dayOfMonth == postDateTime.dayOfMonth) {
                // 같은 시간
                if(localDateTime.hour == postDateTime.hour) {
                    val minutes = ChronoUnit.MINUTES.between(postDateTime, localDateTime)
                    dateTv.text = if (minutes == 0L || minutes == 1L) "1 minute ago" else minutes.toString() + " minutes ago"
                }
                // 다른 시간
                else {
                    val hours = ChronoUnit.HOURS.between(postDateTime, localDateTime)
                    dateTv.text = if (hours == 1L) "1 hour ago" else hours.toString() + "  hours ago"
                }
                // 다른 날
            } else {
                val days = ChronoUnit.DAYS.between(postDateTime, localDateTime)
                // 한 달 이상
                if(days >= 30L) {
                    val months = ChronoUnit.MONTHS.between(postDateTime, localDateTime)
                    dateTv.text = if(months == 1L) "1 month ago" else months.toString() + " months ago"
                }
                // 한 달 미만
                else{
                    dateTv.text = if(days == 1L) "1 day ago" else days.toString() + " days ago"
                }
            }


        }
    }
}
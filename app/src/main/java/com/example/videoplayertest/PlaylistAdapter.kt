package com.example.videoplayertest

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.lifecycle.ViewModel
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView

class PlaylistAdapter: ListAdapter<String, PlaylistAdapter.PlaylistViewHolder>(VideoLinksDiffUtil()) {

    var onItemClickListener: OnItemClickListener? = null

    class PlaylistViewHolder(item: View): RecyclerView.ViewHolder(item){


        val videoText: TextView = item.findViewById<TextView>(R.id.video_text)
        val videoItem = item.findViewById<ConstraintLayout>(R.id.video_item)

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PlaylistViewHolder {
//        Log.d("Chura", "onCreateViewHolder: $")
        val view = LayoutInflater.from((parent.context)).inflate(R.layout.video_item, parent, false)
        return PlaylistViewHolder(view)

    }

    override fun onBindViewHolder(holder: PlaylistViewHolder, position: Int) {
//        Log.d("Chura", "onBindViewHolder: $position")
        holder.videoText.text = getItem(position)
        holder.videoItem.setOnClickListener {
            onItemClickListener?.onItemClick(getItem(position))
        }
    }

    interface OnItemClickListener{
        fun onItemClick(link: String)
    }
}
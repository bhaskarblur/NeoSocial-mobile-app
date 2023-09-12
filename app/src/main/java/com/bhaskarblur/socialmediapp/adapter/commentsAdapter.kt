package com.bhaskarblur.socialmediapp.adapter

import android.content.Context
import android.content.SharedPreferences
import android.preference.PreferenceManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bhaskarblur.socialmediapp.R
import com.bhaskarblur.socialmediapp.models.comments
import com.squareup.picasso.Picasso
import jp.wasabeef.picasso.transformations.CropCircleTransformation

class commentsAdapter : RecyclerView.Adapter<commentsAdapter.viewHolder> {

    private lateinit var context_ : Context;
    public lateinit var list : ArrayList<comments>;
    public lateinit var listener_ : commentsAdapter.onClickListener;
    constructor(context_: Context, list: ArrayList<comments>) : super() {
        this.context_ = context_
        this.list = list
    }

    constructor() : super()


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): commentsAdapter.viewHolder {
        var view = LayoutInflater.from(context_).inflate(R.layout.comments_layout, parent, false);
        return commentsAdapter.viewHolder(view, listener_);
    }

    override fun onBindViewHolder(holder: commentsAdapter.viewHolder, position: Int) {
        if(list.get(position).profilepic !=null && !list.get(position).profilepic.equals("")) {
            Picasso.get().load(list.get(position).profilepic)
                .resize(120, 120)
                .transform(CropCircleTransformation()).into(holder.user_pfp);
        }
        holder.user_name.setText(list.get(position).postedByUsername.toString());

        holder.comment.setText(list.get(position).comment.toString());

        val preferences: SharedPreferences? = PreferenceManager.getDefaultSharedPreferences(context_);

        if(preferences?.getString("userEmail", "").equals(list.get(position).postedBy)) {
            holder.delete.setVisibility(View.VISIBLE);
        }
        else {
            holder.delete.setVisibility(View.GONE);
        }
    }

    override fun getItemCount(): Int {
        return list.size;
    }

    class viewHolder(itemView: View,  listener: commentsAdapter.onClickListener) : RecyclerView.ViewHolder(itemView) {

        var user_pfp: ImageView = itemView.findViewById(R.id.user_icon);
        var user_name : TextView = itemView.findViewById(R.id.user_name);
        var comment : TextView = itemView.findViewById(R.id.comment_txt);
        var delete : ImageView = itemView.findViewById(R.id.delete_icon);
        init {

            delete.setOnClickListener {
                listener.onDeleteClick(adapterPosition);
            }
        }
    }

    interface onClickListener {

        fun onDeleteClick(position_ : Int);

    }

    public fun setOnClickInterface(listener: onClickListener) {
        this.listener_ = listener;

    }

}
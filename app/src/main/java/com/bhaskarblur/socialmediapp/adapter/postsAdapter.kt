package com.bhaskarblur.socialmediapp.adapter

import android.content.Context
import android.text.Layout
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.bhaskarblur.socialmediapp.R
import com.bhaskarblur.socialmediapp.models.Posts
import com.squareup.picasso.Picasso
import jp.wasabeef.picasso.transformations.CropCircleTransformation

class postsAdapter : RecyclerView.Adapter<postsAdapter.viewHolder> {

    private lateinit var context_ : Context;
    private lateinit var list : ArrayList<Posts>;

    constructor(context_: Context, list: ArrayList<Posts>) : super() {
        this.context_ = context_
        this.list = list
    }

    constructor() : super()


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): postsAdapter.viewHolder {
        var view = LayoutInflater.from(context_).inflate(R.layout.posts_layout, parent, false);
        return viewHolder(view);
    }

    override fun onBindViewHolder(holder: postsAdapter.viewHolder, position: Int) {
        Picasso.get().load(list.get(position).userDetails?.profilepic)
            .resize(120, 120)
            .transform(CropCircleTransformation()).into(holder.user_pfp);

        holder.user_name.setText(list.get(position).userDetails?.postedByUsername.toString());

        holder.user_location.setText(list.get(position).location.toString());
        Picasso.get().load(list.get(position).image).resize(800, 800).into(holder.post_image);
        holder.profile_caption.setText(list.get(position).posttext.toString());
        holder.likes_count.setText(list.get(position).likesCount.toString());
        holder.comment_count.setText(list.get(position).commentsCount.toString());

        if(list.get(position).isLiked == 1) {
            holder.like_button.setImageResource(R.drawable.love_on);
        }
        else {
            holder.like_button.setImageResource(R.drawable.love_off);
        }
        if(list.get(position).isSaved == 1) {
            holder.save_button.setImageResource(R.drawable.bookmark_on);
        }
        else {
            holder.save_button.setImageResource(R.drawable.bookmark_off);
        }
    }

    override fun getItemCount(): Int {
        return list.size;
    }

    class viewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        var user_pfp: ImageView = itemView.findViewById(R.id.user_icon);
        var user_name : TextView = itemView.findViewById(R.id.user_name);
        var user_location : TextView = itemView.findViewById(R.id.user_location);
        var post_image : ImageView = itemView.findViewById(R.id.post_image);
        var likes_count : TextView = itemView.findViewById(R.id.likes_count);
        var comment_count : TextView = itemView.findViewById(R.id.comments_count);
        var profile_caption : TextView = itemView.findViewById(R.id.post_caption);
        var like_button : ImageView = itemView.findViewById(R.id.love_icon);
        var comment_button : ImageView = itemView.findViewById(R.id.comment_icon);
        var save_button : ImageView = itemView.findViewById(R.id.save_icon);
    }
}
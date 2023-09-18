package com.bhaskarblur.socialmediapp.adapter

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bhaskarblur.socialmediapp.R
import com.bhaskarblur.socialmediapp.models.Posts
import com.squareup.picasso.Picasso
import jp.wasabeef.picasso.transformations.CropCircleTransformation
import java.lang.String
import kotlin.Int
import kotlin.toString

class postsAdapter : RecyclerView.Adapter<postsAdapter.viewHolder> {

    private lateinit var context_ : Context;
    public lateinit var list : ArrayList<Posts>;
    public lateinit var listener_ : onClickListener;
    private var email = "";
    constructor(context_: Context, list: ArrayList<Posts>, email: kotlin.String) : super() {
        this.context_ = context_
        this.list = list
        this.email = email;
    }

    constructor() : super()


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): postsAdapter.viewHolder {
        var view = LayoutInflater.from(context_).inflate(R.layout.posts_layout, parent, false);
        return viewHolder(view, listener_);
    }

    override fun onBindViewHolder(holder: postsAdapter.viewHolder, position: Int) {
        if(list.get(position).userDetails?.profilepic !=null) {
            Picasso.get().load(list.get(position).userDetails?.profilepic)
                .resize(120, 120)
                .transform(CropCircleTransformation()).into(holder.user_pfp);
        }

        holder.user_name.setText(list.get(position).userDetails?.postedByUsername.toString());

        holder.user_location.setText(list.get(position).location.toString());
        Picasso.get().load(list.get(position).image).resize(800, 800).into(holder.post_image);
        holder.profile_caption.setText(list.get(position).posttext.toString());
        holder.likes_count.setText(list.get(position).likesCount.toString());
        holder.comment_count.setText(list.get(position).commentsCount.toString());


        if (list.get(position).userDetails!!.postedBy.equals(email)) {
            holder.delete_btn.visibility = View.VISIBLE;
        }
        else {
            holder.delete_btn.visibility = View.GONE;
        }
        if(list.get(position).isLiked == 1) {
            holder.like_button.setImageResource(R.drawable.love_on);
            holder.like_button.setTag(R.drawable.love_on);
        }
        else {
            holder.like_button.setImageResource(R.drawable.love_off);
            holder.like_button.setTag(R.drawable.love_off);
        }
        if(list.get(position).isSaved == 1) {
            holder.save_button.setImageResource(R.drawable.bookmark_on);
            holder.save_button.setTag(R.drawable.bookmark_on);
        }
        else {
            holder.save_button.setImageResource(R.drawable.bookmark_off);
            holder.save_button.setTag(R.drawable.bookmark_off);
        }

    }

    override fun getItemCount(): Int {
        return list.size;
    }

    class viewHolder(itemView: View, listener: onClickListener) : RecyclerView.ViewHolder(itemView) {

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
        var delete_btn : ImageView = itemView.findViewById(R.id.delete_icon2);
        init {

            like_button.setOnClickListener {
                if(adapterPosition != RecyclerView.NO_POSITION) {
                    var action : Int;
                    val resource = like_button.getTag();
                    val d = Log.d("tag", resource.toString());
                    if(like_button.getTag().equals(R.drawable.love_off)) {
                        action = 0;
                        like_button.setImageResource(R.drawable.love_on);
                        like_button.setTag(R.drawable.love_on);
                        likes_count.setText(String.valueOf(likes_count.text.toString().toInt() +1 ))
                    }
                    else {
                        action = 1;
                        like_button.setImageResource(R.drawable.love_off);
                        like_button.setTag(R.drawable.love_off);
                        likes_count.setText(String.valueOf(likes_count.text.toString().toInt() - 1 ))
                    }

                    listener.onLikeClick(adapterPosition, action);
                }
            }

            comment_button.setOnClickListener {
                    if(adapterPosition != RecyclerView.NO_POSITION) {
                        listener.onCommentClick(adapterPosition);

                    }
            }
            save_button.setOnClickListener {
                if(adapterPosition != RecyclerView.NO_POSITION) {
                    var action : Int;
                    val resource = like_button.getTag();
                    val d = Log.d("tag", resource.toString());
                    if(save_button.getTag().equals(R.drawable.bookmark_off)) {
                        action = 0;
                        save_button.setImageResource(R.drawable.bookmark_on);
                        save_button.setTag(R.drawable.bookmark_on);


                    }
                    else {
                        action = 1;
                        save_button.setImageResource(R.drawable.bookmark_off);
                        save_button.setTag(R.drawable.bookmark_off);
                    }

                    listener.onSaveClick(adapterPosition, action);
                }
            }

            delete_btn.setOnClickListener {
                if(adapterPosition != RecyclerView.NO_POSITION) {
                    listener.onDeleteClick(    adapterPosition);
                }
            }
            user_name.setOnClickListener  {
                if(adapterPosition != RecyclerView.NO_POSITION) {
                    listener.onProfileClick(    adapterPosition);
                }
            }
            user_pfp.setOnClickListener {
                if(adapterPosition != RecyclerView.NO_POSITION) {
                    listener.onProfileClick(    adapterPosition);
                }
            }

            likes_count.setOnClickListener({
                if(adapterPosition != RecyclerView.NO_POSITION) {
                    listener.onLikesCountClick(adapterPosition);
                }
            });
        }
    }

    interface onClickListener {

        fun onDeleteClick( position_ : Int);
         fun onLikeClick( position_ : Int,  action : Int);

        fun onProfileClick( position_ : Int);

        fun onSaveClick( position_ : Int,  action : Int);

        fun onCommentClick( position_ : Int);

        fun onLikesCountClick( position_ : Int);
    }

    public fun setOnClickInterface(listener: onClickListener) {
        this.listener_ = listener;

    }
}
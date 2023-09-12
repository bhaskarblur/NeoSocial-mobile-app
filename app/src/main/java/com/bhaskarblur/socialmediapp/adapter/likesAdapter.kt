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
import com.bhaskarblur.socialmediapp.models.likes
import com.squareup.picasso.Picasso
import jp.wasabeef.picasso.transformations.CropCircleTransformation

class likesAdapter : RecyclerView.Adapter<likesAdapter.viewHolder> {

    private lateinit var context_ : Context;
    public lateinit var list : ArrayList<likes>;
    public lateinit var listener_ : likesAdapter.onClickListener;
    constructor(context_: Context, list: ArrayList<likes>) : super() {
        this.context_ = context_
        this.list = list
    }

    constructor() : super()


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): likesAdapter.viewHolder {
        var view = LayoutInflater.from(context_).inflate(R.layout.likes_layout, parent, false);
        return likesAdapter.viewHolder(view, listener_);
    }

    override fun onBindViewHolder(holder: likesAdapter.viewHolder, position: Int) {
        if(list.get(position).profilepic !=null && !list.get(position).profilepic.equals("")) {
            Picasso.get().load(list.get(position).profilepic)
                .resize(120, 120)
                .transform(CropCircleTransformation()).into(holder.user_pfp);
        }
        holder.user_name.setText(list.get(position).postedByUsername.toString());

    }

    override fun getItemCount(): Int {
        return list.size;
    }

    class viewHolder(itemView: View,  listener: likesAdapter.onClickListener) : RecyclerView.ViewHolder(itemView) {

        var user_pfp: ImageView = itemView.findViewById(R.id.user_icon);
        var user_name : TextView = itemView.findViewById(R.id.user_name);
        init {

            itemView.setOnClickListener({
                if(adapterPosition != RecyclerView.NO_POSITION) {
                    listener.onItemClick(adapterPosition);
                }
            });

        }
    }

    interface onClickListener {

        fun onItemClick(position_ : Int);

    }

    public fun setOnClickInterface(listener: onClickListener) {
        this.listener_ = listener;

    }

}
package com.example.githubuserapi.view.favorite


import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.githubuserapi.R
import com.example.githubuserapi.local.Favorite

class FavoriteAdapter(private val listUser: List<Favorite>) :
    RecyclerView.Adapter<FavoriteAdapter.ListViewHolder>() {
    private lateinit var onItemClickCallback: OnItemClickCallback

    fun setOnItemClickCallback(onItemClickCallback: OnItemClickCallback) {
        this.onItemClickCallback = onItemClickCallback
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, i: Int): ListViewHolder {
        val view: View =
            LayoutInflater.from(viewGroup.context)
                .inflate(R.layout.item_row_favorite, viewGroup, false)
        return ListViewHolder(view)
    }

    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
        val user = listUser[position]
        Glide.with(holder.imgPhoto.context)
            .load(user.avatarUrl)
            .centerCrop()
            .into(holder.imgPhoto)
        holder.tvName.text = user.login
        holder.itemView.setOnClickListener { onItemClickCallback.onItemClicked(listUser[holder.adapterPosition]) }
        holder.ivFav.setOnClickListener {
            onItemClickCallback.onDeleteItemClicked(listUser[holder.adapterPosition])
        }
    }

    override fun getItemCount(): Int = listUser.size

    class ListViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var imgPhoto: ImageView = itemView.findViewById(R.id.img_item_avatar)
        var tvName: TextView = itemView.findViewById(R.id.tv_item_name)
        var ivFav: ImageView = itemView.findViewById(R.id.iv_fav)
    }

    interface OnItemClickCallback {
        fun onItemClicked(data: Favorite)

        fun onDeleteItemClicked(data: Favorite)
    }

}
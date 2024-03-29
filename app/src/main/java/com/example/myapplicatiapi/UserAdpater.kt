package com.example.myapplicatiapi

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import retrofit2.Call
import retrofit2.Response

class UserAdpater (
    private val context: Context,
    private val userList: List<UserDataItem>) :
    RecyclerView.Adapter<UserAdpater.ViewHolder>() {

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var body: TextView = itemView.findViewById(R.id.body_textview)
        var id: TextView = itemView.findViewById(R.id.id_textview)
        var title: TextView = itemView.findViewById(R.id.title_textview)
        var userId: TextView = itemView.findViewById(R.id.userId_textview)
        var updateImage: ImageButton = itemView.findViewById(R.id.updateImage)
        var deleteImage: ImageButton = itemView.findViewById(R.id.deleteImage)

    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.item_view, parent, false)
        return ViewHolder(view)
    }


    override fun getItemCount(): Int {
        return userList.size
    }

    @SuppressLint("CheckResult")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.userId.text = userList[position].userId.toString()
        holder.id.text = userList[position].id.toString()
        holder.title.text = userList[position].title
        holder.body.text = userList[position].body

        holder.updateImage.setOnClickListener {
            val updateData = userList[position].id
            val data = UserDataItem(updateData, 1,"body", "title")
            ApiSever.createRetrofit().updatePost(data, updateData)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe {
                    Toast.makeText(context, "Update ${it.userId}", Toast.LENGTH_SHORT).show()
                }
        }

        holder.deleteImage.setOnClickListener {
            val deleteData = userList[position].id
            ApiSever.createRetrofit().deletePost(deleteData)
                .enqueue(object : retrofit2.Callback<Void>{
                    override fun onResponse(call: Call<Void>, response: Response<Void>) {
                        if (response.isSuccessful){
                            Toast.makeText(context, "deleted success", Toast.LENGTH_SHORT).show()
                        }else{
                            Toast.makeText(context, "Failed to item delete", Toast.LENGTH_SHORT).show()
                        }
                    }
                    override fun onFailure(call: Call<Void>, t: Throwable) {
                        Toast.makeText(context, "Failed to delete item", Toast.LENGTH_SHORT).show()
                    }

                })
        }

    }
}
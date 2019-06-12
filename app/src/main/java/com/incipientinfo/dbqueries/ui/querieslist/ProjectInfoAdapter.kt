package com.incipientinfo.dbqueries.ui.querieslist

import android.annotation.SuppressLint
import android.net.Uri
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.incipientinfo.dbqueries.POJO.Queries
import com.incipientinfo.dbqueries.R
import com.incipientinfo.dbqueries.common.OnItemClickListener
import kotlinx.android.synthetic.main.row_list_layout.view.*

class ListAdapter(private var queriesList: ArrayList<Queries>, private var onClickListener: OnItemClickListener) :
    RecyclerView.Adapter<ListAdapter.ViewHolder>() {

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        @SuppressLint("CheckResult", "SetTextI18n")
        fun bind(
            queries: Queries,
            onClickListener: OnItemClickListener
        ) {

            try {
                val name = queries.name
                val itemGender = queries.gender
                if (name!!.isNotEmpty() && itemGender!!.isNotEmpty()) {
                    itemView.txtName.text = "$name ($itemGender)"
                }

                val itemEmail = queries.email
                if (itemEmail!!.isNotEmpty()) {
                    itemView.txtEmail.text = itemEmail
                }

                val itemPhone = queries.phoneNum
                if (itemPhone!!.isNotEmpty()) {
                    itemView.txtPhone.text = itemPhone
                }


                val url = queries.imgUri
                val uri = Uri.parse(url)
                if (url!!.isNotEmpty()) {
                    itemView.imgProfile.setImageURI(uri)
                } else {
                    itemView.imgProfile.setBackgroundResource(R.drawable.ic_profile)
                }


                itemView.imgDelete.setOnClickListener {
                    onClickListener.onItemClick(itemView.imgDelete, queries, adapterPosition)
                }

                itemView.imgEdit.setOnClickListener {
                    onClickListener.onItemClick(itemView.imgEdit, queries, adapterPosition)
                }

            } catch (e: Exception) {
                e.printStackTrace()
            }


        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.row_list_layout, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        try {
            queriesList[position].let { project ->
                holder.bind(project, onClickListener)
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }


    }

    override fun getItemCount(): Int {
        return queriesList.size
    }


}
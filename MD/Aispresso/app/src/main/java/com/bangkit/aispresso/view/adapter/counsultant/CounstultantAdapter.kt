package com.bangkit.aispresso.view.adapter.counsultant

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bangkit.aispresso.data.model.counsultant.Data
import com.bangkit.aispresso.databinding.ItemCounsultantBinding
import com.bangkit.aispresso.view.detil.DetailHistoryActivity
import com.bangkit.aispresso.view.midtrans.MidtransActivity
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions

class CounstultantAdapter(var listCounsultant : List<Data>) : RecyclerView.Adapter<CounstultantAdapter.ViewHolder>(){


    class ViewHolder(var binding: ItemCounsultantBinding): RecyclerView.ViewHolder(binding.root) {


    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = ItemCounsultantBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.binding.tvNama.text = listCounsultant[position].first_name
        holder.binding.tvEmail.text = listCounsultant[position].email

        Glide.with(holder.itemView.context).load(listCounsultant[position].avatar).into(holder.binding.ivFoto)

        holder.binding.btnChat.setOnClickListener {
            val intent = Intent(it.context, MidtransActivity::class.java)
            it.context.startActivity(intent)
        }

    }

    override fun getItemCount(): Int {
        return listCounsultant.size
    }
}
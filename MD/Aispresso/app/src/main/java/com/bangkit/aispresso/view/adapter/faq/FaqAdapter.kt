package com.bangkit.aispresso.view.adapter.faq

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isGone
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.bangkit.aispresso.data.model.faq.FaqModel
import com.bangkit.aispresso.databinding.ItemFaqBinding

class FaqAdapter(private var faq : ArrayList<FaqModel>): RecyclerView.Adapter<FaqAdapter.ListViewHolder>() {

    class ListViewHolder (val binding : ItemFaqBinding) : RecyclerView.ViewHolder(binding.root) {

    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ListViewHolder {
        var view = ItemFaqBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return ListViewHolder(view)
    }

    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
        holder.binding.tvTitle.text = faq[position].title
        holder.binding.tvSubTitle.text = faq[position].subTitle

        holder.binding.ivAdd.setOnClickListener {
            holder.binding.tvSubTitle.isGone = false
            holder.binding.ivClear.isVisible = true
            holder.binding.ivAdd.isVisible = false
        }
        holder.binding.ivClear.setOnClickListener {
            holder.binding.tvSubTitle.isGone = true
            holder.binding.ivClear.isVisible = false
            holder.binding.ivAdd.isVisible = true
        }
    }

    fun setDataFaq(FaqList : ArrayList<FaqModel>){
        this.faq = FaqList
    }

    override fun getItemCount(): Int {
        return faq.size
    }

}
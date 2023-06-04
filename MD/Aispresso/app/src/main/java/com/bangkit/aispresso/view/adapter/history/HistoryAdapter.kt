package com.bangkit.aispresso.view.adapter.history

import android.app.Activity
import android.graphics.BitmapFactory
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bangkit.aispresso.R
import com.bangkit.aispresso.data.model.history.coffe.ClassifyCoffeModel
import com.bangkit.aispresso.databinding.RcItemBinding


class HistoryAdapter(private val activity: Activity) :
    RecyclerView.Adapter<HistoryAdapter.RegisterViewHolder>() {
    var listRegister = ArrayList<ClassifyCoffeModel>()

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): HistoryAdapter.RegisterViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.rc_item, parent, false)
        return RegisterViewHolder(view)
    }

    override fun onBindViewHolder(holder: HistoryAdapter.RegisterViewHolder, position: Int) {
        holder.bind(listRegister[position])


    }

    override fun getItemCount(): Int = this.listRegister.size


    fun createLoopId(position: Int): String {
        var loopId = ""
        for (i in 0 until position) {
            loopId += "0"
        }
        return loopId
    }


    inner class RegisterViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val binding = RcItemBinding.bind(itemView)
        fun bind(registerModel: ClassifyCoffeModel) {
            binding.tvNomor.text = createLoopId(adapterPosition)
            binding.tvNama.text = registerModel.classified
            binding.tvAlamat.text = registerModel.considers

            val registerImage: ByteArray = registerModel.image!!
            val bitmap = BitmapFactory.decodeByteArray(registerImage, 0, registerImage.size)
            binding.ivFoto.setImageBitmap(bitmap)

            //show popup menu
        }
    }


    fun addItem(registerModel: ClassifyCoffeModel) {
        this.listRegister.add(registerModel)
        notifyItemInserted(this.listRegister.size - 1)
    }

    fun updateItem(position: Int, registerModel: ClassifyCoffeModel) {
        this.listRegister[position] = registerModel
        notifyItemChanged(position, registerModel)
    }

    fun removeItem(position: Int) {
        this.listRegister.removeAt(position)
        notifyItemRemoved(position)
        notifyItemRangeChanged(position, this.listRegister.size)

    }
}
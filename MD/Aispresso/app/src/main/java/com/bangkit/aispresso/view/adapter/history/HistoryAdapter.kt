package com.bangkit.aispresso.view.adapter.history

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Build
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.RecyclerView
import com.bangkit.aispresso.R
import com.bangkit.aispresso.data.model.history.coffe.ClassifyCoffeModel
import com.bangkit.aispresso.databinding.RcItemBinding
import com.bangkit.aispresso.view.detil.DetailHistoryActivity
import java.io.OutputStream


class HistoryAdapter() :
    RecyclerView.Adapter<HistoryAdapter.RegisterViewHolder>() {
    var listHistory = ArrayList<ClassifyCoffeModel>()

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): HistoryAdapter.RegisterViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.rc_item, parent, false)
        return RegisterViewHolder(view)
    }

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    override fun onBindViewHolder(holder: HistoryAdapter.RegisterViewHolder, position: Int) {
        holder.bind(listHistory[position])
    }

    override fun getItemCount(): Int = this.listHistory.size


    fun createLoopId(position: Int): String {
        var loopId = ""
        for (i in 0 until position) {
            loopId += "0"
        }
        return loopId
    }


    inner class RegisterViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val binding = RcItemBinding.bind(itemView)
        @RequiresApi(Build.VERSION_CODES.TIRAMISU)
        fun bind(historyModel: ClassifyCoffeModel) {
            binding.tvNomor.text = createLoopId(adapterPosition)
            binding.tvTtile.text = historyModel.classified
            binding.tvSubtitle.text = historyModel.considers

            val historyImage: ByteArray = historyModel.image!!
            val bitmap = BitmapFactory.decodeByteArray(historyImage, 0, historyImage.size)
            binding.ivFoto.setImageBitmap(bitmap)

            binding.cvItem.setOnClickListener {

                val intent = Intent(it.context, DetailHistoryActivity::class.java)
                intent.putExtra("gambar", bitmap)
                intent.putExtra("title", listHistory[position].classified)
                intent.putExtra("subtitle", listHistory[position].considers)
                it.context.startActivity(intent)
            }

            //show popup menu
        }
    }


    fun addItem(registerModel: ClassifyCoffeModel) {
        this.listHistory.add(registerModel)
        notifyItemInserted(this.listHistory.size - 1)
    }

    fun updateItem(position: Int, registerModel: ClassifyCoffeModel) {
        this.listHistory[position] = registerModel
        notifyItemChanged(position, registerModel)
    }

    fun removeItem(position: Int) {
        this.listHistory.removeAt(position)
        notifyItemRemoved(position)
        notifyItemRangeChanged(position, this.listHistory.size)

    }
}
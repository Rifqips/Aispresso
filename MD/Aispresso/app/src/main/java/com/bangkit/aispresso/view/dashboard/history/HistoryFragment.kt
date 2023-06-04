package com.bangkit.aispresso.view.dashboard.history

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.coordinatorlayout.widget.CoordinatorLayout
import com.bangkit.aispresso.data.sqlite.ClassifyHelper
import com.bangkit.aispresso.databinding.FragmentHistoryBinding
import com.bangkit.aispresso.view.adapter.history.HistoryAdapter


class HistoryFragment : Fragment() {

    private lateinit var binding: FragmentHistoryBinding

    private lateinit var classifyHelper: ClassifyHelper
    private lateinit var adapter: HistoryAdapter
    private val EXTRA_STATE = "EXTRA_SATE"
    private var coordinatorLayout: CoordinatorLayout? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentHistoryBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


//        binding.rvRegister.layoutManager = LinearLayoutManager(requireActivity())
//        binding.rvRegister.setHasFixedSize(true)
//        adapter = HistoryAdapter(requireActivity())
//        binding.rvRegister.adapter = adapter
//
//        coordinatorLayout = binding.clDetail
//
//        handleViewOnEmpty()
//
//        classifyHelper = ClassifyHelper.getInstance(requireActivity())
//        classifyHelper.open()
//        if (savedInstanceState == null) {
//            loadRegister()
//        } else {
//            val list = savedInstanceState.getParcelableArrayList<ClassifyCoffeModel>(EXTRA_STATE)
//            if (list != null) {
//                adapter.listRegister = list
//            }
//        }
    }

//    override fun onSaveInstanceState(outState: Bundle) {
//        super.onSaveInstanceState(outState)
//        outState.putParcelableArrayList(EXTRA_STATE, adapter.listRegister)
//    }
//
//    private fun loadRegister() {
//        GlobalScope.launch(Dispatchers.Main) {
//            binding.progressbar.visibility = View.VISIBLE
//            val cursor = classifyHelper.queryAll()
//            val register = mapCursorToArrayList(cursor)
//            binding.progressbar.visibility = View.INVISIBLE
//
//            if (register.size > 0) {
//                adapter.listRegister = register
//                handleViewOnEmpty()
//            } else {
//                adapter.listRegister = ArrayList()
//                handleViewOnEmpty()
////                showSnackbarMessage("Tidak ada data saat ini")
//            }
//        }
//    }
//    private fun handleViewOnEmpty() {
//        if (adapter.listRegister.size > 0) {
//            binding.tvEmpty.visibility = View.GONE
//        } else {
//            binding.tvEmpty.visibility = View.VISIBLE
//        }
//    }
//
//    private fun showSnackbarMessage(message: String) {
//        Snackbar.make(binding.rvRegister, message, Snackbar.LENGTH_SHORT).show()
//    }
//
//
//    @SuppressLint("NotifyDataSetChanged")
//    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
//        super.onActivityResult(requestCode, resultCode, data)
//        if (data != null) {
//            when (requestCode) {
//                REQUEST_ADD -> if (resultCode == RESULT_ADD) {
//                    val regist =
//                        data.getParcelableExtra<ClassifyCoffeModel>(EXTRA_REGISTRATION) as ClassifyCoffeModel
//                    adapter.addItem(regist)
//                    binding.rvRegister.smoothScrollToPosition(adapter.itemCount - 1)
//                    adapter.notifyDataSetChanged()
//                    showSnackbarMessage("Satu item berhasil ditambahkan")
//                    handleViewOnEmpty()
//
//                }
//                REQUEST_UPDATE -> when (resultCode) {
//                    RESULT_UPDATE -> {
//                        val regist =
//                            data.getParcelableExtra<ClassifyCoffeModel>(EXTRA_REGISTRATION) as ClassifyCoffeModel
//                        val position = data.getIntExtra(EXTRA_POSITION, 0)
//                        adapter.updateItem(position, regist)
//                        binding.rvRegister.smoothScrollToPosition(position)
//                        adapter.notifyDataSetChanged()
//                        showSnackbarMessage("Satu item berhasil diubah")
//                        handleViewOnEmpty()
//                    }
//                    RESULT_DELETE -> {
//                        val position = data.getIntExtra(EXTRA_POSITION, 0)
//                        adapter.removeItem(position)
//                        adapter.notifyDataSetChanged()
//                        showSnackbarMessage("Satu item berhasil dihapus")
//                        handleViewOnEmpty()
//                    }
//
//                }
//            }
//        }
//    }

}
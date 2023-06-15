package labs.nusantara.smartrinsebusiness.ui.layanan

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.RecyclerView
import labs.nusantara.smartrinsebusiness.databinding.ItemServiceBinding
import labs.nusantara.smartrinsebusiness.service.response.ServiceGetItem

class ServiceAdapter(
    private val listServiceGetItem: MutableList<ServiceGetItem>,
    private val serviceViewModel: ServiceViewModel,
    private val token: String
) :
    RecyclerView.Adapter<ServiceAdapter.ListViewHolder>() {


    inner class ListViewHolder(private val userBinding: ItemServiceBinding) :
        RecyclerView.ViewHolder(userBinding.root) {

        @SuppressLint("SetTextI18n", "SimpleDateFormat")
        fun bind(data: ServiceGetItem) {
            userBinding.apply {
                tvJenisService.text = data.jenisService
                tvPriceService.text = data.price.toString()
            }

        }

        init {
            userBinding.btnDelete.setOnClickListener {
                val position = bindingAdapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    val deletedItem = listServiceGetItem[position]
                    showDeleteConfirmationDialog(it.context, deletedItem, position)
                }
            }
        }

        private fun showDeleteConfirmationDialog(context: Context, item: ServiceGetItem, position: Int) {
            val alertDialog = AlertDialog.Builder(context)
                .setTitle("Konfirmasi Penghapusan")
                .setMessage("Apakah Anda yakin ingin menghapus item ini?")
                .setPositiveButton("Ya") { _, _ ->
                    deleteItem(item, position)
                }
                .setNegativeButton("Tidak", null)
                .create()

            alertDialog.show()
        }

        private fun deleteItem(item: ServiceGetItem, position: Int) {
            serviceViewModel.delService(token, item.id)
            listServiceGetItem.removeAt(position)
            notifyItemRemoved(position)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListViewHolder {
        val userBinding =
            ItemServiceBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ListViewHolder(userBinding)
    }

    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
        holder.bind(listServiceGetItem[position])
    }

    override fun getItemCount(): Int = listServiceGetItem.size

}
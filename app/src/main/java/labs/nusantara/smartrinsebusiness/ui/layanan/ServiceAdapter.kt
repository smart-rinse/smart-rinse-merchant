package labs.nusantara.smartrinsebusiness.ui.layanan

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import labs.nusantara.smartrinsebusiness.databinding.ItemServiceBinding
import labs.nusantara.smartrinsebusiness.service.response.ServiceGetItem

class ServiceAdapter (private val listServiceGetItem: List<ServiceGetItem>) :
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
package labs.nusantara.smartrinsebusiness.ui.order

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import labs.nusantara.smartrinsebusiness.R
import labs.nusantara.smartrinsebusiness.databinding.ItemOrderBinding
import labs.nusantara.smartrinsebusiness.service.response.OrdersItemItem
import labs.nusantara.smartrinsebusiness.service.response.ServiceGetItem
import labs.nusantara.smartrinsebusiness.ui.layanan.ServiceViewModel
import java.text.SimpleDateFormat
import java.util.*

class OrderAdapter(
    private val listTransactionData: List<List<OrdersItemItem>>,
    private val orderViewModel: OrderViewModel,
    private val token: String
) :
    RecyclerView.Adapter<OrderAdapter.ListViewHolder>() {

    private lateinit var recyclerView: RecyclerView

    private val sortedListHistoryData =
        listTransactionData.flatten().sortedByDescending { it.dateTransaction }

    inner class ListViewHolder(private val userBinding: ItemOrderBinding) :
        RecyclerView.ViewHolder(userBinding.root) {

        @SuppressLint("SetTextI18n", "SimpleDateFormat")
        fun bind(data: OrdersItemItem) {
            val inputFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")
            inputFormat.timeZone = TimeZone.getTimeZone("UTC")
            val outputFormat = SimpleDateFormat("dd MMMM yyyy")

            userBinding.apply {
                tvTransactionId.text = data.idTransaction
                tvTransactionName.text = data.user
                try {
                    val parsedDate: Date = inputFormat.parse(data.dateTransaction) as Date
                    val outputDate: String = outputFormat.format(parsedDate)
                    tvTransactionDate.text = outputDate
                } catch (e: Exception) {
                    e.printStackTrace()
                }
                if (data.status == "In Progress") {
                    val backgroundDrawable = ContextCompat.getDrawable(
                        itemView.context,
                        R.drawable.background_rounded_yellow
                    )
                    tvTransactionStatus.background = backgroundDrawable
                    tvTransactionCostBefore.background = backgroundDrawable
                    tvTransactionCostBefore.visibility = View.VISIBLE
                    tvTransactionCostBefore.text = "Rp. ${data.totalCost}"
                } else {
                    val backgroundDrawable = ContextCompat.getDrawable(
                        itemView.context,
                        R.drawable.background_rounded_green
                    )
                    tvTransactionStatus.background = backgroundDrawable
                    btnArchive.visibility = View.GONE
                    tvTransactionCostBefore.visibility = View.GONE
                    tvTransactionPemasukan.visibility = View.VISIBLE
                    tvTransactionCost.visibility = View.VISIBLE
                    tvTransactionCost.text = "Rp. ${data.totalCost}"
                }

                val textColor = ContextCompat.getColor(itemView.context, R.color.white)
                tvTransactionStatus.setTextColor(textColor)
                tvTransactionStatus.text = data.status
            }
        }

        init {
            userBinding.btnArchive.setOnClickListener {
                val position = bindingAdapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    showArchiveConfirmationDialog(
                        itemView.context,
                        sortedListHistoryData[position]
                    )
                }
            }
        }

        private fun showArchiveConfirmationDialog(
            context: Context,
            data: OrdersItemItem
        ) {
            val builder = AlertDialog.Builder(context)
            builder.setTitle("Selesaikan Transaksi")
                .setMessage("Apakah Anda yakin ingin menyelesaikan transaksi ini?")
                .setPositiveButton("Ya") { _, _ ->
                    updateItem(data)
                }
                .setNegativeButton("Tidak", null)
                .create()
                .show()
        }

        private fun updateItem(item: OrdersItemItem) {
            orderViewModel.putOrderTransaction(token, item.idTransaction)
            notifyDataSetChanged()
            recyclerView.postDelayed({
                orderViewModel.getOrderTransaction(token)
            }, 100)
        }


    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListViewHolder {
        val userBinding =
            ItemOrderBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        recyclerView = parent as RecyclerView
        return ListViewHolder(userBinding)
    }

    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
        holder.bind(sortedListHistoryData[position])
    }

    override fun getItemCount(): Int = sortedListHistoryData.size
}

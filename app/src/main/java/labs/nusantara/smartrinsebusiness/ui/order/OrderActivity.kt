package labs.nusantara.smartrinsebusiness.ui.order

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import labs.nusantara.smartrinsebusiness.databinding.ActivityOrderBinding
import labs.nusantara.smartrinsebusiness.utils.ViewModelFactory

class OrderActivity : AppCompatActivity() {

    private lateinit var binding: ActivityOrderBinding
    private lateinit var factory: ViewModelFactory
    private val orderViewModel: OrderViewModel by viewModels { factory }
    private var token: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityOrderBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.apply {
            title = "Daftar Pesanan"
            setDisplayHomeAsUpEnabled(true)
        }
        factory = ViewModelFactory.getInstance(this)

        loadData()
    }

    private fun loadData() {
        orderViewModel.getSession().observe(this@OrderActivity) { sessionModel ->
            token = sessionModel.token
            val tokenAuth = sessionModel.token
            if (!sessionModel.isLogin) {
                Toast.makeText(this, "Silakan login terlebih dahulu.", Toast.LENGTH_SHORT).show()
            }
            if (!sessionModel.isLaundry) {
                Toast.makeText(this, "Silakan buat merchant terlebih dahulu.", Toast.LENGTH_SHORT).show()
                binding.imageNotFound.visibility = View.VISIBLE
                binding.tvNotFound.visibility = View.VISIBLE
            } else if (sessionModel.isLaundry) {
                orderViewModel.getOrderTransaction(tokenAuth)
                orderViewModel.isLoading.observe(this@OrderActivity) { load ->
                    showLoading(load)
                }
                orderViewModel.listDataTrx.observe(this@OrderActivity) { listData ->
                    if (listData.isNotEmpty()) {
                        binding.imageNotFound.visibility = View.GONE
                        binding.tvNotFound.visibility = View.GONE
                        binding.rvOrder.apply {
                            layoutManager = LinearLayoutManager(context)
                            setHasFixedSize(true)
                        }
                        binding.rvOrder.adapter = OrderAdapter(listData, orderViewModel, tokenAuth)
                    } else {
                        binding.imageNotFound.visibility = View.VISIBLE
                        binding.tvNotFound.visibility = View.VISIBLE
                        binding.rvOrder.adapter = null
                    }
                }

            }
        }
    }



    private fun showLoading(isLoading: Boolean) {
        when (isLoading) {
            true -> binding.progressBar.visibility = View.VISIBLE
            false -> binding.progressBar.visibility = View.GONE
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressedDispatcher.onBackPressed()
        return super.onSupportNavigateUp()
    }
}
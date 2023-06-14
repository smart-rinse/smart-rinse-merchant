package labs.nusantara.smartrinsebusiness.ui.layanan

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.core.content.ContextCompat
import labs.nusantara.smartrinsebusiness.R
import labs.nusantara.smartrinsebusiness.databinding.ActivityServiceBinding
import labs.nusantara.smartrinsebusiness.utils.ViewModelFactory

class ServiceActivity : AppCompatActivity() {

    private lateinit var binding: ActivityServiceBinding
    private lateinit var factory: ViewModelFactory
    private val serviceViewModel: ServiceViewModel by viewModels { factory }
    private var token: String? = null
    private var laundryId: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityServiceBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.apply {
            title = "Layanan"
            setDisplayHomeAsUpEnabled(true)
        }
        factory = ViewModelFactory.getInstance(this)

        loadData()
        binding.btnSave.setOnClickListener { processSave() }

    }

    private fun loadData() {
        serviceViewModel.getSession().observe(this@ServiceActivity) {
            token = it.token
            val tokenAuth = it.token
            if (!it.isLogin) {
                Toast.makeText(this, "Silakan login terlebih dahulu.", Toast.LENGTH_SHORT).show()
            }
            if(!it.isLaundry) {
                Toast.makeText(this, "Silakan buat merchant terlebih dahulu.", Toast.LENGTH_SHORT).show()
                binding.edtJenis.isEnabled = false
                binding.edtPrice.isEnabled = false
                val backgroundColor = ContextCompat.getColor(this, R.color.sr_white_gray)
                binding.btnSave.setBackgroundColor(backgroundColor)

            }else if(it.isLaundry) {
                binding.edtJenis.isEnabled = true
                binding.edtPrice.isEnabled = true
                val backgroundColor = ContextCompat.getColor(this, R.color.sr_main_green)
                binding.btnSave.setBackgroundColor(backgroundColor)

                serviceViewModel.getMerchantInfo(tokenAuth)
                serviceViewModel.isLoading.observe(this@ServiceActivity) { load ->
                    showLoading(load)
                }
                serviceViewModel.listMerchantOwner.observe(this@ServiceActivity) {listData ->
                    val data = listData?.firstOrNull()

                    data?.let {
                        val data = listData.firstOrNull()
                        data?.let {
                            laundryId = it.id
                        }
                    }
                }
            }
        }
    }

    private fun processSave() {
        serviceViewModel.getSession().observe(this@ServiceActivity) {
            token = it.token
            val tokenAuth = it.token
            if (!it.isLogin) {

            } else {
                try {
                    val jenisLayanan = binding.edtJenis.text.toString()
                    val priceLayanan = binding.edtPrice.text.toString()

                    if(jenisLayanan.isNotEmpty() && priceLayanan.isNotEmpty()){
                        laundryId?.let { laundryId ->
                            simpanApi(tokenAuth, laundryId, jenisLayanan, priceLayanan)
                        }
                    }
                } catch (e: Exception) {
                    Log.d("Error Message ", e.toString())
                }


            }
        }
    }

    private fun simpanApi(tokenAuth: String, laundryId: String, jenis: String, price: String) {
        serviceViewModel.postService(tokenAuth, laundryId, jenis, price)

        serviceViewModel.isLoading.observe(this) { load ->
            showLoading(load)
        }

        serviceViewModel.toastText.observe(this@ServiceActivity) {
            it.getContentIfNotHandled()?.let { toastText ->
                Toast.makeText(
                    this@ServiceActivity, toastText, Toast.LENGTH_SHORT
                ).show()
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
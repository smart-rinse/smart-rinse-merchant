package labs.nusantara.smartrinsebusiness.ui.layanan

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import labs.nusantara.smartrinsebusiness.R
import labs.nusantara.smartrinsebusiness.databinding.ActivityServiceBinding
import labs.nusantara.smartrinsebusiness.service.response.ServiceGetItem
import labs.nusantara.smartrinsebusiness.ui.login.LoginActivity
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
                binding.imageNotFound.visibility = View.VISIBLE
                binding.tvNotFound.visibility = View.VISIBLE
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
                        laundryId = it.id
                        loadService(tokenAuth, it.id)
                    }
                }
            }
        }
    }

    private fun loadService(tokenAuth: String, laundryId: String) {
        serviceViewModel.getServiceOwner(tokenAuth, laundryId)
        binding.rvService.apply {
            layoutManager = LinearLayoutManager(this@ServiceActivity)
            setHasFixedSize(true)
        }

        serviceViewModel.listServiceOwner.observe(this@ServiceActivity) { listData ->
            if (listData.isNotEmpty()) {
                binding.imageNotFound.visibility = View.GONE
                binding.tvNotFound.visibility = View.GONE
                binding.rvService.adapter = ServiceAdapter(listData as MutableList<ServiceGetItem>, serviceViewModel, tokenAuth)
            } else {
                binding.imageNotFound.visibility = View.VISIBLE
                binding.tvNotFound.visibility = View.VISIBLE
                binding.rvService.adapter = null
            }
        }
        serviceViewModel.isLoading.observe(this@ServiceActivity) { load ->
            showLoading(load)
        }
    }

    private fun processSave() {
        serviceViewModel.getSession().observe(this@ServiceActivity) {
            token = it.token
            val tokenAuth = it.token
            if (!it.isLogin) {
                gotoLogin()
            } else {
                try {
                    val jenisLayanan = binding.edtJenis.text.toString()
                    val priceLayanan = binding.edtPrice.text.toString()

                    if(jenisLayanan.isNotEmpty() && priceLayanan.isNotEmpty()){
                        laundryId?.let { laundryId ->
                            simpanApi(tokenAuth, laundryId, jenisLayanan, priceLayanan)
                        }
                        binding.edtJenis.setText("")
                        binding.edtPrice.setText("")
                        binding.edtJenis.clearFocus()
                        binding.edtPrice.clearFocus()

                        // Refresh RecyclerView
                        laundryId?.let { laundryId ->
                            loadService(tokenAuth, laundryId)
                        }
                    }
                } catch (e: Exception) {
                    Log.d("Error Message ", e.toString())
                }


            }
        }
    }

    private fun gotoLogin() {
        val intent = Intent(this, LoginActivity::class.java)
        intent.flags =
            Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(intent)
        finish()
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
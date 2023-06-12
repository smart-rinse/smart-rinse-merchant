package labs.nusantara.smartrinsebusiness.ui.merchant

import android.app.TimePickerDialog
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.TimePicker
import androidx.activity.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import labs.nusantara.smartrinsebusiness.databinding.ActivityMerchantBinding
import labs.nusantara.smartrinsebusiness.utils.ViewModelFactory
import java.util.*

class MerchantActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMerchantBinding
    private lateinit var factory: ViewModelFactory
    private val merchantViewModel: MerchantViewModel by viewModels { factory }
    private var token: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMerchantBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.apply {
            title = "Buka Merchant"
            setDisplayHomeAsUpEnabled(true)
        }
        factory = ViewModelFactory.getInstance(this)

        binding.btnSave.setOnClickListener { processSave() }

    }

    private fun processSave() {
        merchantViewModel.getSession().observe(this@MerchantActivity) {
            token = it.token
            val tokenAuth = it.token
            if (!it.isLogin) {

            } else {
                val namaMerchant = binding.edtName.text.toString()
                val tahunMerchant = binding.edtYear.text.toString()
                val alamatMerchant = binding.edtAddress.text.toString()
                val latitudeMerchant = binding.edtLatitude.text.toString()
                val longitudeMerchant = binding.edtLongitude.text.toString()
                val bukaMerchant = binding.edtOpen.text.toString()
                val tutupMerchant = binding.edtClose.text.toString()
                val rekMerchant = binding.edtRekening.text.toString().toInt()

                merchantViewModel.postMerchant(
                    tokenAuth,
                    namaMerchant,
                    tahunMerchant,
                    alamatMerchant,
                    latitudeMerchant,
                    longitudeMerchant,
                    bukaMerchant,
                    tutupMerchant,
                    rekMerchant
                )
                merchantViewModel.isLoading.observe(this) { load ->
                    showLoading(load)
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
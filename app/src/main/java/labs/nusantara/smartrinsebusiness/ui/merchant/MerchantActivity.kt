package labs.nusantara.smartrinsebusiness.ui.merchant

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import labs.nusantara.smartrinsebusiness.databinding.ActivityMerchantBinding
import labs.nusantara.smartrinsebusiness.utils.ViewModelFactory

class MerchantActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMerchantBinding
    private lateinit var factory: ViewModelFactory
    private val merchantViewModel: MerchantViewModel by viewModels { factory }
    private var token: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMerchantBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        factory = ViewModelFactory.getInstance(this)

    }
}
package labs.nusantara.smartrinsebusiness.ui.home

import android.app.AlertDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.activity.viewModels
import labs.nusantara.smartrinsebusiness.R
import labs.nusantara.smartrinsebusiness.databinding.ActivityHomeBinding
import labs.nusantara.smartrinsebusiness.ui.layanan.ServiceActivity
import labs.nusantara.smartrinsebusiness.ui.login.LoginActivity
import labs.nusantara.smartrinsebusiness.ui.merchant.MerchantActivity
import labs.nusantara.smartrinsebusiness.ui.order.OrderActivity
import labs.nusantara.smartrinsebusiness.ui.setting.SettingActivity
import labs.nusantara.smartrinsebusiness.utils.ViewModelFactory

class HomeActivity : AppCompatActivity() {

    private lateinit var binding: ActivityHomeBinding
    private lateinit var factory: ViewModelFactory
    private val homeViewModel: HomeViewModel by viewModels { factory }
    private var token: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.hide()
        factory = ViewModelFactory.getInstance(this)

        token = intent.getStringExtra("extra_token")
        token?.let { Log.d("TOKEN : ", it) }

        //Function to Menu
        binding.cardMerchantTopLeft.setOnClickListener { gotoMerchant() }
        binding.cardServiceTopRight.setOnClickListener { gotoService() }
        binding.cardOrderBottomLeft.setOnClickListener { gotoOrder() }
        binding.cardSettingBottomRight.setOnClickListener { gotoSetting() }
        binding.btnLogout.setOnClickListener { gotoExit() }
    }

    private fun gotoMerchant() {
        val intent = Intent(this, MerchantActivity::class.java)
        startActivity(intent)
    }

    private fun gotoService() {
        val intent = Intent(this, ServiceActivity::class.java)
        startActivity(intent)
    }

    private fun gotoOrder() {
        val intent = Intent(this, OrderActivity::class.java)
        startActivity(intent)
    }

    private fun gotoSetting() {
        val intent = Intent(this, SettingActivity::class.java)
        startActivity(intent)
    }

    private fun gotoExit() {
        val builder = AlertDialog.Builder(this)
        builder.setTitle(getString(R.string.title_confirm_logout))
            .setMessage(getString(R.string.desc_confirm_logout))
            .setPositiveButton("Ya") { dialog, _ ->
                homeViewModel.logout()
                dialog.dismiss()
                gotoLogin()
            }
            .setNegativeButton("Tidak") { dialog, _ ->
                dialog.dismiss()
            }
        val dialog = builder.create()
        dialog.show()
    }

    private fun gotoLogin() {
        val intent = Intent(this, LoginActivity::class.java)
        intent.flags =
            Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(intent)
        finish()
    }


}
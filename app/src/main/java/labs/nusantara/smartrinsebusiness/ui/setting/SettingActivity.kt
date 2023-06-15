package labs.nusantara.smartrinsebusiness.ui.setting

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import labs.nusantara.smartrinsebusiness.databinding.ActivitySettingBinding
import labs.nusantara.smartrinsebusiness.ui.login.LoginActivity
import labs.nusantara.smartrinsebusiness.utils.ViewModelFactory

class SettingActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySettingBinding
    private lateinit var factory: ViewModelFactory
    private val settingViewModel: SettingViewModel by viewModels { factory }
    private var valueUserId: String = ""
    private var valueToken: String = ""
    private var token: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySettingBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.apply {
            title = "Profile Account"
            setDisplayHomeAsUpEnabled(true)
        }

        factory = ViewModelFactory.getInstance(this)
        loadData()
    }

    private fun loadData() {
        settingViewModel.getSession().observe(this@SettingActivity) {
            token = it.token
            val tokenAuth = it.token
            val ownerId = it.ownerId
            if (!it.isLogin) {
                Toast.makeText(this, "Silakan login terlebih dahulu.", Toast.LENGTH_SHORT).show()
                gotoLogin()
            }else {
                val userId = ownerId
                val userToken = tokenAuth
                if (userId != null && userToken != null) {

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

    override fun onSupportNavigateUp(): Boolean {
        onBackPressedDispatcher.onBackPressed()
        return super.onSupportNavigateUp()
    }
}
package labs.nusantara.smartrinsebusiness.ui.register

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import labs.nusantara.smartrinsebusiness.R
import labs.nusantara.smartrinsebusiness.databinding.ActivityRegisterBinding
import labs.nusantara.smartrinsebusiness.ui.login.LoginActivity
import labs.nusantara.smartrinsebusiness.utils.ViewModelFactory

class RegisterActivity : AppCompatActivity() {

    private lateinit var binding : ActivityRegisterBinding
    private lateinit var factory: ViewModelFactory
    private val registerViewModel: RegisterViewModel by viewModels { factory }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.hide()
        binding.labelLogin.setOnClickListener { gotoLogin() }
        factory = ViewModelFactory.getInstance(this)
        funRegister()
    }

    private fun funRegister() {
        binding.apply {
            btnRegister.setOnClickListener {
                if(edtPassword.text.toString() == edtConfpassword.text.toString()) {
                    if(edtName.length() == 0){
                        edtName.error = getString(R.string.rule_nama)
                    }else if(edtEmail.length() == 0 || !binding.edtEmail.error.isNullOrEmpty()){
                        edtEmail.error = getString(R.string.rule_email)
                    }else if(edtPassword.length() == 0 || !binding.edtPassword.error.isNullOrEmpty()){
                        edtPassword.error = getString(R.string.rule_password)
                    }else{
                        prosesRegister()
                    }
                }else {
                    edtConfpassword.error = getString(R.string.rule_confpassword)
                }

            }
        }
    }

    private fun prosesRegister() {
        // Show Loading Bar
        registerViewModel.isLoading.observe(this@RegisterActivity) {
            binding.progressBar.visibility = if (it) View.VISIBLE else View.GONE
        }

        // Send Data to Model
        binding.apply {
            registerViewModel.postRegister(
                edtName.text.toString(),
                edtEmail.text.toString(),
                edtPassword.text.toString(),
                edtConfpassword.text.toString()
            )
        }

        // Get Notification
        registerViewModel.toastText.observe(this@RegisterActivity) {
            it.getContentIfNotHandled()?.let { toastText ->
                Toast.makeText(
                    this@RegisterActivity, toastText, Toast.LENGTH_SHORT
                ).show()
            }
        }

        // Action Register Done
        registerViewModel.registerResponse.observe(this@RegisterActivity) { response ->
            if (response.success) {
                startActivity(Intent(this@RegisterActivity, LoginActivity::class.java))
                finish()
            }
        }
    }

    private fun gotoLogin() {
        val intent = Intent(this, LoginActivity::class.java)
        startActivity(intent)
        finish()
    }
}
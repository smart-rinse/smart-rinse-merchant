package labs.nusantara.smartrinsebusiness.ui.login

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import labs.nusantara.smartrinsebusiness.R
import labs.nusantara.smartrinsebusiness.databinding.ActivityLoginBinding
import labs.nusantara.smartrinsebusiness.ui.home.HomeActivity
import labs.nusantara.smartrinsebusiness.ui.register.RegisterActivity
import labs.nusantara.smartrinsebusiness.utils.SessionModel
import labs.nusantara.smartrinsebusiness.utils.ViewModelFactory

class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding
    private lateinit var factory: ViewModelFactory
    private val loginViewModel: LoginViewModel by viewModels { factory }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.hide()
        binding.labelRegister.setOnClickListener { gotoRegister() }
        factory = ViewModelFactory.getInstance(this)
        funLogin()
    }

    private fun funLogin() {
        binding.apply {
            btnLogin.setOnClickListener {
                if (edtEmail.length() == 0 || !binding.edtEmail.error.isNullOrEmpty()) {
                    edtEmail.error = getString(R.string.rule_email)
                } else if (edtPassword.length() == 0 || !binding.edtPassword.error.isNullOrEmpty()) {
                    edtPassword.error = getString(R.string.rule_password)
                } else {
                    prosesLogin()
                }
            }
        }
    }

    private fun prosesLogin() {
        // Show Loading Bar
        loginViewModel.isLoading.observe(this@LoginActivity) {
            binding.progressBar.visibility = if (it) View.VISIBLE else View.GONE
        }

        // Send Data to Model
        binding.apply {
            loginViewModel.postLogin(
                edtEmail.text.toString(),
                edtPassword.text.toString()
            )
        }

        // Get Notification
        loginViewModel.toastText.observe(this@LoginActivity) {
            it.getContentIfNotHandled()?.let { toastText ->
                if(toastText == "null"){
                    val toastTextNew = "Login Failed"
                    Toast.makeText(
                        this@LoginActivity, toastTextNew, Toast.LENGTH_SHORT
                    ).show()
                }else {
                    val toastTextNew = "Login Berhasil"
                    Toast.makeText(
                        this@LoginActivity, toastTextNew, Toast.LENGTH_SHORT
                    ).show()
                }

            }
        }

        // Get Session Token
        loginViewModel.loginResponse.observe(this@LoginActivity) { response ->
            if (response.success) {
                saveSession(
                    SessionModel(
                        response.data.ownerId,
                        response.data.isLaundry,
                        response.data.email,
                        response.data.name,
                        AUTH_KEY + (response.data.accessToken),
                        true
                    )
                )

                loginViewModel.login()
                val token = response.data.accessToken
                val intent = Intent(this@LoginActivity, HomeActivity::class.java)
                intent.putExtra("extra_token", token)
                startActivity(intent)
                finish()
            }
        }
    }

    private fun saveSession(session: SessionModel) {
        loginViewModel.saveSession(session)
    }

    private fun gotoRegister() {
        val intent = Intent(this, RegisterActivity::class.java)
        startActivity(intent)
        finish()
    }

    companion object {
        private const val AUTH_KEY = "Bearer "
    }
}
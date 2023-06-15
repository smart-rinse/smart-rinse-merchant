package labs.nusantara.smartrinsebusiness.ui.setting

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import com.bumptech.glide.Glide
import labs.nusantara.smartrinsebusiness.databinding.ActivitySettingBinding
import labs.nusantara.smartrinsebusiness.ui.login.LoginActivity
import labs.nusantara.smartrinsebusiness.utils.ViewModelFactory
import labs.nusantara.smartrinsebusiness.utils.reduceFileImage
import labs.nusantara.smartrinsebusiness.utils.uriToFile
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.File

class SettingActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySettingBinding
    private lateinit var factory: ViewModelFactory
    private val settingViewModel: SettingViewModel by viewModels { factory }
    private var valueOwnerId: String = ""
    private var valueToken: String = ""
    private var token: String? = null
    private var getFile: File? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySettingBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.apply {
            title = "Data Pribadi"
            setDisplayHomeAsUpEnabled(true)
        }

        factory = ViewModelFactory.getInstance(this)
        loadData()
        binding.btnUpdate.setOnClickListener { updateProfile() }
        binding.imgUpload.setOnClickListener { startGallery() }
    }

    private fun startGallery() {
        val intent = Intent()
        intent.action = Intent.ACTION_GET_CONTENT
        intent.type = "image/*"
        val chooser = Intent.createChooser(intent, "Choose a Picture")
        launcherIntentGallery.launch(chooser)
    }

    private val launcherIntentGallery = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == RESULT_OK) {
            val selectedImg = result.data?.data as Uri
            selectedImg.let { uri ->
                val file = uriToFile(uri, this@SettingActivity)
                getFile = file
                Glide.with(this@SettingActivity)
                    .load(selectedImg)
                    .circleCrop()
                    .into(binding.imageOwner)
            }
        }
    }

    private fun updateProfile() {
        try {
            val userCity = binding.edtKota.text.toString().toRequestBody("text/plain".toMediaType())
            val userTelp = binding.edtTelepon.text.toString().toRequestBody("text/plain".toMediaType())
            val selectedButton = binding.radioGroup.checkedRadioButtonId
            var userGender: RequestBody? = null

            if (selectedButton != -1) {
                val valueGender = when (selectedButton) {
                    binding.radMale.id -> "Laki-Laki"
                    binding.radFemale.id -> "Perempuan"
                    else -> ""
                }
                userGender = valueGender.toRequestBody("text/plain".toMediaTypeOrNull())
                Log.d("GenderValue : ", "$userGender")
            }

            when {
                getFile != null -> {
                    val file = reduceFileImage(getFile as File)
                    val requestImageFile = file.asRequestBody("image/jpeg".toMediaTypeOrNull())
                    val imageMultipart: MultipartBody.Part = MultipartBody.Part.createFormData(
                        "photo",
                        file.name,
                        requestImageFile
                    )

                    if (userGender != null) {
                        editProcess(userTelp, userCity, userGender, imageMultipart)
                    }
                }
                getFile == null -> {
                    Toast.makeText(this, "Masukan foto Anda terlebih dahulu untuk melakukan perubahan.", Toast.LENGTH_SHORT).show()
                }
            }

        } catch (e: Exception) {
            Log.d("Error Message ", e.toString())
        }
    }

    private fun editProcess(
        userTelp: RequestBody,
        userCity: RequestBody,
        userGender: RequestBody,
        imageMultipart: MultipartBody.Part
    ) {
        // Show Loading Bar
        settingViewModel.isLoading.observe(this@SettingActivity) {
            binding.progressBar.visibility = if (it) View.VISIBLE else View.GONE
        }

        // Send Data to Model
        binding.apply {
            settingViewModel.putProfileOwner(
                valueToken,
                valueOwnerId,
                userTelp,
                userCity,
                userGender,
                imageMultipart
            )
        }

        // Get Notification
        settingViewModel.toastText.observe(this@SettingActivity) {
            it.getContentIfNotHandled()?.let { toastText ->
                Toast.makeText(
                    this@SettingActivity, toastText, Toast.LENGTH_SHORT
                ).show()
            }
        }
    }

    private fun loadData() {
        settingViewModel.getSession().observe(this@SettingActivity) {
            token = it.token
            valueToken = it.token
            valueOwnerId = it.ownerId
            val tokenAuth = it.token
            val ownerId = it.ownerId
            val ownerName = it.name
            val ownerEmail = it.email
            if (!it.isLogin) {
                Toast.makeText(this, "Silakan login terlebih dahulu.", Toast.LENGTH_SHORT).show()
                gotoLogin()
            } else {
                binding.edtName.setText(ownerName)
                binding.edtEmail.setText(ownerEmail)

                settingViewModel.getOwnerDetail(tokenAuth, ownerId)
                settingViewModel.isLoading.observe(this@SettingActivity) { load ->
                    showLoading(load)
                }
                settingViewModel.listOwnerDetail.observe(this@SettingActivity) { listData ->
                    val data = listData?.firstOrNull()
                    data?.let {
                        try {
                            if (it.photo != null && it.photo.isNotEmpty()) {
                                Glide.with(this@SettingActivity)
                                    .load(it.photo)
                                    .circleCrop()
                                    .into(binding.imageOwner)
                            }
                            if (it.gender.isNotEmpty()) {
                                if (it.gender == "Laki-Laki" || it.gender == "Laki Laki") {
                                    binding.radMale.isChecked = true
                                }
                                if (it.gender == "Perempuan") {
                                    binding.radFemale.isChecked = true
                                }
                            }
                            binding.edtTelepon.setText(it.telephone)
                            binding.edtKota.setText(it.city)
                        } catch (e: Exception) {
                            e.printStackTrace()
                        }
                    }
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
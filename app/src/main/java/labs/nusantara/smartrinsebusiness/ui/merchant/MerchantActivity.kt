package labs.nusantara.smartrinsebusiness.ui.merchant

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import labs.nusantara.smartrinsebusiness.databinding.ActivityMerchantBinding
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

class MerchantActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMerchantBinding
    private lateinit var factory: ViewModelFactory
    private val merchantViewModel: MerchantViewModel by viewModels { factory }
    private var token: String? = null
    private var getFile: File? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMerchantBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.apply {
            title = "Buka Merchant"
            setDisplayHomeAsUpEnabled(true)
        }
        factory = ViewModelFactory.getInstance(this)

        loadData()
        binding.btnSave.setOnClickListener { processSave() }
        binding.imgUpload.setOnClickListener { startGallery() }

    }

    private fun loadData() {
        merchantViewModel.getSession().observe(this@MerchantActivity) {
            token = it.token
            val tokenAuth = it.token
            if (!it.isLogin) {
                Toast.makeText(this, "Silakan login terlebih dahulu.", Toast.LENGTH_SHORT).show()
            }
            if (!it.isLaundry) {
                binding.btnUpdate.visibility = View.GONE
                binding.btnSave.visibility = View.VISIBLE

            } else if (it.isLaundry) {
                binding.btnUpdate.visibility = View.VISIBLE
                binding.btnSave.visibility = View.GONE

                merchantViewModel.getMerchantInfo(tokenAuth)
                merchantViewModel.isLoading.observe(this@MerchantActivity) { load ->
                    showLoading(load)
                }
                merchantViewModel.listMerchantOwner.observe(this@MerchantActivity) { listData ->
                    val data = listData?.firstOrNull()

                    data?.let {
                        val data = listData.firstOrNull()
                        data?.let {
                            val namaMerchant =
                                Editable.Factory.getInstance().newEditable(it.namaLaundry)
                            val tahunMerchant =
                                Editable.Factory.getInstance().newEditable(it.tanggalBerdiri)
                            val alamatMerchant =
                                Editable.Factory.getInstance().newEditable(it.alamat)
                            val latitudeMerchant =
                                Editable.Factory.getInstance().newEditable(it.latitude)
                            val longitudeMerchant =
                                Editable.Factory.getInstance().newEditable(it.longitude)
                            val telpMerchant =
                                Editable.Factory.getInstance().newEditable(it.telephone)
                            val bukaMerchant =
                                Editable.Factory.getInstance().newEditable(it.jamBuka)
                            val tutupMerchant =
                                Editable.Factory.getInstance().newEditable(it.jamTutup)
                            val rekMerchant =
                                Editable.Factory.getInstance().newEditable(it.rekening.toString())
                            val bankMerchant = Editable.Factory.getInstance().newEditable(it.bank)

                            binding.edtName.text = namaMerchant
                            binding.edtYear.text = tahunMerchant
                            binding.edtAddress.text = alamatMerchant
                            binding.edtLatitude.text = latitudeMerchant
                            binding.edtLongitude.text = longitudeMerchant
                            binding.edtTelp.text = telpMerchant
                            binding.edtOpen.text = bukaMerchant
                            binding.edtClose.text = tutupMerchant
                            binding.edtRekening.text = rekMerchant
                            binding.edtBank.text = bankMerchant

                            if (it.photo.isNotEmpty()) {
                                Glide.with(this@MerchantActivity)
                                    .load(data.photo)
                                    .circleCrop()
                                    .transition(DrawableTransitionOptions.withCrossFade(500))
                                    .into(binding.imageMerchant)
                            }

                        }

                    }
                }
            }
        }
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
                val file = uriToFile(uri, this@MerchantActivity)
                getFile = file
                Glide.with(this@MerchantActivity)
                    .load(selectedImg)
                    .circleCrop()
                    .into(binding.imageMerchant)
            }
        }
    }

    private fun processSave() {
        merchantViewModel.getSession().observe(this@MerchantActivity) {
            token = it.token
            val tokenAuth = it.token
            if (!it.isLogin) {

            } else {
                try {
                    val namaMerchant =
                        binding.edtName.text.toString().toRequestBody("text/plain".toMediaType())
                    val tahunMerchant =
                        binding.edtYear.text.toString().toRequestBody("text/plain".toMediaType())
                    val alamatMerchant =
                        binding.edtAddress.text.toString().toRequestBody("text/plain".toMediaType())
                    val latitudeMerchant = binding.edtLatitude.text.toString()
                        .toRequestBody("text/plain".toMediaType())
                    val longitudeMerchant = binding.edtLongitude.text.toString()
                        .toRequestBody("text/plain".toMediaType())
                    val bukaMerchant =
                        binding.edtOpen.text.toString().toRequestBody("text/plain".toMediaType())
                    val tutupMerchant =
                        binding.edtClose.text.toString().toRequestBody("text/plain".toMediaType())
                    val rekMerchant = binding.edtRekening.text.toString()
                        .toRequestBody("text/plain".toMediaType())
                    val bankMerchant =
                        binding.edtBank.text.toString().toRequestBody("text/plain".toMediaType())
                    val telpMerchant =
                        binding.edtTelp.text.toString().toRequestBody("text/plain".toMediaType())


                    when {
                        getFile != null -> {
                            val file = reduceFileImage(getFile as File)
                            val requestImageFile =
                                file.asRequestBody("image/jpeg".toMediaTypeOrNull())
                            val imageMultipart: MultipartBody.Part =
                                MultipartBody.Part.createFormData(
                                    "photo",
                                    file.name,
                                    requestImageFile
                                )

                            processSave(
                                tokenAuth,
                                namaMerchant,
                                tahunMerchant,
                                alamatMerchant,
                                latitudeMerchant,
                                longitudeMerchant,
                                bukaMerchant,
                                tutupMerchant,
                                rekMerchant,
                                bankMerchant,
                                telpMerchant,
                                imageMultipart,
                            )
                        }
                    }
                } catch (e: Exception) {
                    Log.d("Error Message ", e.toString())
                }


            }
        }
    }

    private fun processSave(
        tokenAuth: String,
        namaMerchant: RequestBody,
        tahunMerchant: RequestBody,
        alamatMerchant: RequestBody,
        latitudeMerchant: RequestBody,
        longitudeMerchant: RequestBody,
        bukaMerchant: RequestBody,
        tutupMerchant: RequestBody,
        rekMerchant: RequestBody,
        bankMerchant: RequestBody,
        telpMerchant: RequestBody,
        imageMultipart: MultipartBody.Part

    ) {
        merchantViewModel.postMerchant(
            tokenAuth,
            namaMerchant,
            tahunMerchant,
            alamatMerchant,
            latitudeMerchant,
            longitudeMerchant,
            bukaMerchant,
            tutupMerchant,
            rekMerchant,
            bankMerchant,
            telpMerchant,
            imageMultipart
        )
        merchantViewModel.isLoading.observe(this) { load ->
            showLoading(load)
        }
        merchantViewModel.toastText.observe(this@MerchantActivity) {
            it.getContentIfNotHandled()?.let { toastText ->
                Toast.makeText(
                    this@MerchantActivity, toastText, Toast.LENGTH_SHORT
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
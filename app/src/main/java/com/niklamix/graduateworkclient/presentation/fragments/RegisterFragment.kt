package com.niklamix.graduateworkclient.presentation.fragments

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.graphics.drawable.VectorDrawable
import android.net.Uri
import android.os.Bundle
import android.os.TransactionTooLargeException
import android.provider.MediaStore
import android.text.Editable
import android.text.TextWatcher
import android.util.Base64
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.google.android.material.textfield.TextInputLayout
import com.niklamix.graduateworkclient.R
import com.niklamix.graduateworkclient.databinding.FragmentRegisterBinding
import com.niklamix.graduateworkclient.domain.entity.UserItemRead
import com.niklamix.graduateworkclient.domain.entity.UserItemWrite
import com.niklamix.graduateworkclient.presentation.viewmodel.UserChangeViewModel
import com.squareup.picasso.Picasso
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileOutputStream


class RegisterFragment : Fragment() {
    private val args by navArgs<RegisterFragmentArgs>()

    private lateinit var viewModel: UserChangeViewModel
    private lateinit var pickImageLauncher: ActivityResultLauncher<Intent>

    private var screenMode: String = MODE_UNKNOWN
    private var userItemRead: UserItemRead? = null

    private var _binding: FragmentRegisterBinding? = null
    private val binding: FragmentRegisterBinding
        get() = _binding ?: throw RuntimeException("FragmentRegisterBinding == null")

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentRegisterBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        parseParams()
        startImageActivityResult()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProvider(this)[UserChangeViewModel::class.java]
        addTextChangeListener()
        launchRightMode()
        observeViewModel()
        binding.btnBackRegister.setOnClickListener {
            findNavController().popBackStack()
        }
    }

    private fun parseParams() {
        val args = args.registerArgs
        val mode = args.mode
        if (mode != MODE_UPDATE && mode != MODE_REGISTER && mode != MODE_ADMIN_UPDATE) {
            throw RuntimeException("Unknown screen mode $mode")
        }
        screenMode = mode
        userItemRead = args.userItem
    }

    private fun startImageActivityResult() {
        pickImageLauncher =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
                if (result.resultCode == Activity.RESULT_OK) {
                    val data: Intent? = result.data
                    val imageUri: Uri? = data?.data
                    binding.ivPhotoReg.setImageURI(imageUri)
                }
            }
    }

    private fun setImageView() {

        val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        pickImageLauncher.launch(intent)

    }

    private fun launchRightMode() {
        println(screenMode)
        when (screenMode) {
            MODE_UPDATE -> launchUpdateMode()
            MODE_REGISTER -> launchRegisterMode()
            MODE_ADMIN_UPDATE -> launchUpdateMode()

        }
    }

    private fun launchUpdateMode() {
        with(binding) {
            tvRegistration.text = getString(R.string.tv_update_profile_text)
            etNameReg.setText(userItemRead?.name ?: "")
            etSurnameReg.setText(userItemRead?.surname ?: "")
            etEmailReg.setText(userItemRead?.email ?: "")
            etCityReg.setText(userItemRead?.city ?: "")
            etCountryReg.setText(userItemRead?.country ?: "")
            etPhoneReg.setText(userItemRead?.phone ?: "")
            etLoginReg.setText(userItemRead?.login ?: "")
            ivPhotoReg.setImageBitmap(convertBase64InImage(userItemRead?.photo))
            btnUploadPhoto.setOnClickListener {
                setImageView()
            }
            btnRegisterReg.setOnClickListener {
                launchUpdatedUserProfileFragment()
            }
        }
    }

    private fun launchRegisterMode() {
        binding.btnUploadPhoto.setOnClickListener {
            setImageView()
        }
        binding.btnRegisterReg.setOnClickListener {
            launchUpdatedUserProfileFragment()
        }
    }

    private fun launchUpdatedUserProfileFragment() {
        if (screenMode == MODE_UPDATE) {
            viewModel.updateUserItem(initUserItem(), context, false)
            viewModel.updateSuccessful.observe(viewLifecycleOwner) {
                if (it) {
                    val item = viewModel.getUpdatedCurrentUser(context)
                    findNavController().navigate(
                        RegisterFragmentDirections.actionRegisterFragmentToUserProfileFragment(item!!)
                    )
                }
            }
        } else if (screenMode == MODE_REGISTER) {
            viewModel.addUserItem(initUserItem(), context)
            viewModel.addSuccessful.observe(viewLifecycleOwner) {
                if (it) {
                    val item = viewModel.getUpdatedCurrentUser(context)
                    findNavController().navigate(
                        RegisterFragmentDirections.actionRegisterFragmentToUserProfileFragment(item!!)
                    )
                }
            }
        } else if (screenMode == MODE_ADMIN_UPDATE) {
            viewModel.updateSuccessful.observe(viewLifecycleOwner) {
                if (it) {
                    viewModel.updateUserItem(initUserItem(), context, true, userItemRead?.id)
                    findNavController().popBackStack()
                }
            }
        }

    }

    private fun launchProfileFragment() {

    }

    private fun initUserItem() =
        UserItemWrite(
            binding.etNameReg.text.toString().trim().capitalize(),
            binding.etSurnameReg.text.toString().trim().capitalize(),
            binding.etEmailReg.text.toString().trim(),
            binding.etCountryReg.text.toString().capitalize(),
            binding.etCityReg.text.toString().capitalize(),
            binding.etPhoneReg.text.toString().trim(),
            getImageAsBase64(binding.ivPhotoReg),
            binding.etLoginReg.text.toString().lowercase().trim(),
            binding.etPasswordReg.text.toString().trim()
        )

    private fun observeViewModel() {
        viewModel.errorInputName.observe(viewLifecycleOwner) {
            val message = if (it) {
                getString(R.string.til_name_error)
            } else {
                null
            }
            binding.tilNameReg.error = message
        }

        viewModel.errorInputSurname.observe(viewLifecycleOwner) {
            val message = if (it) {
                getString(R.string.til_surname_error)
            } else {
                null
            }
            binding.tilSurnameReg.error = message
        }
        viewModel.errorInputEmail.observe(viewLifecycleOwner) {
            val message = if (it) {
                getString(R.string.til_email_error)
            } else {
                null
            }
            binding.tilEmailReg.error = message
        }

        viewModel.errorInputCountry.observe(viewLifecycleOwner) {
            val message = if (it) {
                getString(R.string.til_country_error)
            } else {
                null
            }
            binding.tilCountryReg.error = message
        }
        viewModel.errorInputCity.observe(viewLifecycleOwner) {
            val message = if (it) {
                getString(R.string.til_city_error)
            } else {
                null
            }
            binding.tilCityReg.error = message
        }

        viewModel.errorInputPhone.observe(viewLifecycleOwner) {
            val message = if (it) {
                getString(R.string.til_phone_error)
            } else {
                null
            }
            binding.tilPhoneReg.error = message
        }
        viewModel.errorInputLogin.observe(viewLifecycleOwner) {
            val message = if (it) {
                getString(R.string.til_login_error)
            } else {
                null
            }
            binding.tilLoginReg.error = message
        }

        viewModel.errorInputPassword.observe(viewLifecycleOwner) {
            val message = if (it) {
                getString(R.string.til_password_error)
            } else {
                null
            }
            binding.tilPasswordReg.error = message
        }
    }

    private fun addTextChangeListener() {
        binding.etNameReg.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                viewModel.resetErrorInputName()
            }
            override fun afterTextChanged(p0: Editable?) {}

        })
        binding.etSurnameReg.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                viewModel.resetErrorInputSurname()
            }
            override fun afterTextChanged(p0: Editable?) {}
        })
        binding.etEmailReg.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                viewModel.resetErrorInputEmail()
            }
            override fun afterTextChanged(p0: Editable?) {}

        })
        binding.etCountryReg.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                viewModel.resetErrorInputCountry()
            }
            override fun afterTextChanged(p0: Editable?) {}
        })
        binding.etCityReg.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                viewModel.resetErrorInputCity()
            }
            override fun afterTextChanged(p0: Editable?) {}

        })
        binding.etPhoneReg.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                viewModel.resetErrorInputPhone()
            }
            override fun afterTextChanged(p0: Editable?) {}
        })
        binding.etLoginReg.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                viewModel.resetErrorInputLogin()
            }
            override fun afterTextChanged(p0: Editable?) {}

        })
        binding.etPasswordReg.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                viewModel.resetErrorInputPassword()
            }
            override fun afterTextChanged(p0: Editable?) {}
        })
    }

    private fun getImageAsBase64(imageView: ImageView): String? {
        val drawable = imageView.drawable ?: return null

        val bitmap: Bitmap? = getBitmap(drawable)

        val outputStream = ByteArrayOutputStream()
        bitmap?.compress(Bitmap.CompressFormat.PNG, 100, outputStream)
        val byteArray: ByteArray = outputStream.toByteArray()

        return Base64.encodeToString(byteArray, Base64.DEFAULT)
    }

    private fun getBitmap(drawable: Drawable?): Bitmap? {
        if (drawable is BitmapDrawable) {
            return drawable.bitmap
        } else if (drawable is VectorDrawable) {
            val bitmap = Bitmap.createBitmap(
                drawable.intrinsicWidth,
                drawable.intrinsicHeight,
                Bitmap.Config.ARGB_8888
            )
            val canvas = Canvas(bitmap)
            drawable.setBounds(0, 0, canvas.width, canvas.height)
            drawable.draw(canvas)
            return bitmap
        }
        return null
    }

    private fun convertBase64InImage(image: String?): Bitmap? {
        if (image != null) {
            val decodedBytes: ByteArray = Base64.decode(image, Base64.DEFAULT)
            return BitmapFactory.decodeByteArray(decodedBytes, 0, decodedBytes.size)
        }
        return null
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        private const val MODE_UPDATE = "mode_update"
        private const val MODE_REGISTER = "mode_register"
        private const val MODE_ADMIN_UPDATE = "mode_admin_update"
        private const val MODE_UNKNOWN = ""
    }
}
package com.niklamix.graduateworkclient.presentation.fragments

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import android.util.Base64
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.niklamix.graduateworkclient.databinding.FragmentUserProfileBinding
import com.niklamix.graduateworkclient.domain.entity.RegisterArgs
import com.niklamix.graduateworkclient.domain.entity.UserItemRead

class UserProfileFragment : Fragment() {

    private val args by navArgs<UserProfileFragmentArgs>()

    private lateinit var userItemRead: UserItemRead

    private var _binding: FragmentUserProfileBinding? = null
    private val binding: FragmentUserProfileBinding
        get() = _binding ?: throw RuntimeException("FragmentUserProfileBinding == null")

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentUserProfileBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        userItemRead = args.userItemRead
        initView()
        binding.btnBackProfile.setOnClickListener {
            findNavController().navigate(
                UserProfileFragmentDirections.actionUserProfileFragmentToWelcomeFragment()
            )
        }
        binding.btnSearchUsers.setOnClickListener {
            findNavController().navigate(
                UserProfileFragmentDirections.actionUserProfileFragmentToSearchUserFragment(
                    userItemRead.adminFlag)
            )
        }
        binding.btnUpdateProfile.setOnClickListener {
            lunchRegisterFragment(RegisterArgs("mode_update", userItemRead))
        }
    }

    private fun lunchRegisterFragment(args: RegisterArgs) {
        findNavController().navigate(
            UserProfileFragmentDirections.actionUserProfileFragmentToRegisterFragment(args)
        )
    }

    private fun initView() {
        with(binding) {
            tvUserName.text = userItemRead.name
            tvUserSurname.text = userItemRead.surname
            tvUserLogin.text = String.format(tvUserLogin.text.toString(), userItemRead.login)
            tvUserEmail.text = String.format(tvUserEmail.text.toString(), userItemRead.email)
            tvUserCity.text = String.format(tvUserCity.text.toString(), userItemRead.city)
            tvUserCountry.text = String.format(tvUserCountry.text.toString(),
                userItemRead.country)
            tvUserPhone.text = String.format(tvUserPhone.text.toString(), userItemRead.phone)
            tvUserDateCreate.text = String.format(tvUserDateCreate.text.toString(),
            userItemRead.createdDate)
            tvUserDateUpdate.text = String.format(tvUserDateUpdate.text.toString(),
            userItemRead.lastModifiedDate)
            if (userItemRead.photo != null) {
                binding.ivPhotoProfile.setImageBitmap(convertBase64InImage(userItemRead.photo))
            }

        }
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
}
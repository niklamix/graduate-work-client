package com.niklamix.graduateworkclient.presentation.fragments

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.niklamix.graduateworkclient.R
import com.niklamix.graduateworkclient.databinding.FragmentAuthBinding
import com.niklamix.graduateworkclient.domain.entity.RegisterArgs
import com.niklamix.graduateworkclient.domain.entity.UserItemRead
import com.niklamix.graduateworkclient.presentation.viewmodel.UserViewModel
import java.util.Locale

class AuthFragment : Fragment() {

    private lateinit var viewModel: UserViewModel

    private var _binding: FragmentAuthBinding? = null
    private val binding: FragmentAuthBinding
        get() = _binding ?: throw RuntimeException("FragmentAuthBinding == null")

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAuthBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProvider(this)[UserViewModel::class.java]
        lunchBackStackFragment()
        authorize()
        lunchRegisterFragment()
    }

    private fun lunchRegisterFragment() {
        binding.btnSignUpLogin.setOnClickListener {
            findNavController().navigate(
                AuthFragmentDirections.actionAuthFragmentToRegisterFragment(RegisterArgs(
                    "mode_register", null))
            )
        }
    }

    private fun authorize() {
        binding.btnLogin.setOnClickListener {
            updateCredentials()
            addTextChangeListener()
            observeViewModel()
            if (viewModel.errorInputLogin.value == false && viewModel.errorInputPassword.value == false) {
                viewModel.getCurrentUserItem(context)
                if (viewModel.userItem.value != null) {
                    lunchUserProfileFragment(viewModel.userItem.value!!)
                }

            }
        }
    }

    private fun lunchBackStackFragment() {
        binding.btnBackLogin.setOnClickListener {
            findNavController().navigate(
                AuthFragmentDirections.actionAuthFragmentToWelcomeFragment()
            )
        }
    }

    private fun addTextChangeListener() {
        binding.etLoginLogin.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                viewModel.resetErrorInputLogin()
            }
            override fun afterTextChanged(p0: Editable?) {}

        })
        binding.etPasswordLogin.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                viewModel.resetErrorInputPassword()
            }
            override fun afterTextChanged(p0: Editable?) {}
        })
    }

    private fun observeViewModel() {
        viewModel.errorInputLogin.observe(viewLifecycleOwner) {
            val message = if (it) {
                getString(R.string.til_login_error)
            } else {
                null
            }
            binding.tilLoginLogin.error = message
        }

        viewModel.errorInputPassword.observe(viewLifecycleOwner) {
            val message = if (it) {
                getString(R.string.til_password_error)
            } else {
                null
            }
            binding.tilPasswordLogin.error = message
        }
    }

    private fun updateCredentials() {
        val login: String = binding.etLoginLogin.text.toString().lowercase(Locale.getDefault())
        val password: String = binding.etPasswordLogin.text.toString()

        viewModel.updateCredentials(login, password)
    }

    private fun lunchUserProfileFragment(userItemRead: UserItemRead) {
        val args = RegisterArgs("mode_edit", userItemRead)
        findNavController().navigate(
            AuthFragmentDirections.actionAuthFragmentToUserProfileFragment(args)
        )
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
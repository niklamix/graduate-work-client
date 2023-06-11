package com.niklamix.graduateworkclient.presentation.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.niklamix.graduateworkclient.databinding.FragmentWelcomeBinding
import com.niklamix.graduateworkclient.domain.entity.RegisterArgs

class WelcomeFragment : Fragment() {

    private var _binding: FragmentWelcomeBinding? = null
    private val binding: FragmentWelcomeBinding
        get() = _binding ?: throw RuntimeException("FragmentWelcomeBinding == null")

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentWelcomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.btnSignInWelcome.setOnClickListener {
            lunchAuthFragment()
        }
        binding.btnSingUpWelcome.setOnClickListener {
            lunchRegisterFragment(RegisterArgs("mode_register", null))
        }
    }

    private fun lunchRegisterFragment(args: RegisterArgs) {
        findNavController().navigate(
            WelcomeFragmentDirections.actionWelcomeFragmentToRegisterFragment(args)
        )
    }

    private fun lunchAuthFragment() {
        findNavController().navigate(
            WelcomeFragmentDirections.actionWelcomeFragmentToAuthFragment()
        )
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
package com.niklamix.graduateworkclient.presentation.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.niklamix.graduateworkclient.databinding.FragmentSearchUserBinding
import com.niklamix.graduateworkclient.domain.entity.RegisterArgs
import com.niklamix.graduateworkclient.domain.entity.UserFilter
import com.niklamix.graduateworkclient.presentation.UserListAdapter
import com.niklamix.graduateworkclient.presentation.viewmodel.SearchUserViewModel
import kotlin.properties.Delegates

class SearchUserFragment : Fragment() {
    private val args by navArgs<SearchUserFragmentArgs>()

    private lateinit var viewModel: SearchUserViewModel
    private lateinit var userListAdapter: UserListAdapter

    private var adminFlag by Delegates.notNull<Boolean>()

    private var _binding: FragmentSearchUserBinding? = null
    private val binding: FragmentSearchUserBinding
        get() = _binding ?: throw RuntimeException("FragmentSearchUserBinding == null")

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSearchUserBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        adminFlag = args.adminFlag
        setupRecyclerView()
        viewModel = ViewModelProvider(this)[SearchUserViewModel::class.java]
        binding.tilSearchUser.setEndIconOnClickListener {
            val filter = binding.etSearchUser.text.toString().split(" ")
            val userFilter = UserFilter(filter.getOrElse(0) { "" }, filter.getOrElse(1) { "" })
            viewModel.getUserList(0, 10, userFilter, context)
            viewModel.userList.observe(viewLifecycleOwner) {
                userListAdapter.submitList(it)
            }
        }
        binding.btnBackSearch.setOnClickListener {
            findNavController().popBackStack()
        }
    }

    private fun setupRecyclerView() {
        val rvUserList = binding.rvUserList
        userListAdapter = UserListAdapter(adminFlag)
        rvUserList.adapter = userListAdapter
        setupUserProfileClickListener()
        setupUpdateUserClickListener()
        setupSwipeListener(rvUserList)
        if (adminFlag) {
            setupUpdateAdminFlagClickListener()
            setupUpdateEnabledClickListener()
        }
    }

    private fun setupUserProfileClickListener() {
        userListAdapter.onUserItemClickListener = {
            findNavController().navigate(
                SearchUserFragmentDirections.actionSearchUserFragmentToUserProfileFragment(
                    RegisterArgs("mode_read", it)
                )
            )

        }
    }

    private fun setupUpdateUserClickListener() {
        userListAdapter.onUpdateUserItemClickListener = {
            findNavController().navigate(
                SearchUserFragmentDirections.actionSearchUserFragmentToRegisterFragment(
                    RegisterArgs("mode_admin_update", it)
                )
            )
        }
    }

    private fun setupUpdateAdminFlagClickListener() {
        userListAdapter.onUpdateAdminFlagClickListener = {
            viewModel.changeAdminFlag(it, context)
        }
    }

    private fun setupUpdateEnabledClickListener() {
        userListAdapter.onUpdateEnabledFlagClickListener = {
            viewModel.changeEnabledFlag(it, context)
        }
    }

    private fun setupSwipeListener(rvShopList: RecyclerView) {
        val callback =
            object :
                ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT) {
                override fun onMove(
                    recyclerView: RecyclerView,
                    viewHolder: RecyclerView.ViewHolder,
                    target: RecyclerView.ViewHolder
                ): Boolean {
                    return false
                }

                override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                    val item = userListAdapter.currentList[viewHolder.adapterPosition]
                    viewModel.deleteUser(item, context)
                }
            }
        val itemTouchHelper = ItemTouchHelper(callback)
        itemTouchHelper.attachToRecyclerView(rvShopList)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
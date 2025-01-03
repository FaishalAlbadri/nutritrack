package com.pab.nutritrack.ui.fragment.setting

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import com.pab.nutritrack.data.user.UserItem
import com.pab.nutritrack.databinding.FragmentSettingBinding
import com.pab.nutritrack.ui.landingpage.LandingPageActivity
import com.pab.nutritrack.ui.profile.ChangePasswordActivity
import com.pab.nutritrack.ui.profile.ProfileActivity
import com.pab.nutritrack.ui.target.TargetActivity
import com.pab.nutritrack.utils.viewmodel.ViewModelFactory

class SettingFragment : Fragment() {

    private var _binding: FragmentSettingBinding? = null
    val binding get() = _binding!!
    private lateinit var viewModel: ViewModelFactory
    private val settingViewModel: SettingViewModel by viewModels { viewModel }
    private lateinit var userItem: UserItem

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentSettingBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelFactory.getInstance(requireContext())

        binding.apply {
            btnProfil.setOnClickListener {
                startActivity(
                    Intent(requireActivity(), ProfileActivity::class.java).putExtra(
                        "data",
                        userItem
                    )
                )
            }

            btnTarget.setOnClickListener {
                startActivity(
                    Intent(requireActivity(), TargetActivity::class.java).putExtra(
                        "goals",
                        userItem.goals
                    )
                )
            }

            btnChangePassword.setOnClickListener {
                startActivity(
                    Intent(requireActivity(), ChangePasswordActivity::class.java)
                )
            }

            btnLogout.setOnClickListener {
                settingViewModel.logout()
                settingViewModel.deleteCache(requireActivity())
                startActivity(
                    Intent(requireActivity(), LandingPageActivity::class.java)
                        .addFlags(
                            Intent.FLAG_ACTIVITY_NEW_TASK or
                                    Intent.FLAG_ACTIVITY_CLEAR_TASK or
                                    Intent.FLAG_ACTIVITY_CLEAR_TOP
                        )
                )
                requireActivity().finish()
            }
        }

        settingViewModel.apply {
            message.observe(viewLifecycleOwner) {
                it.getContentIfNotHandled()?.let {
                    Toast.makeText(requireActivity(), it, Toast.LENGTH_SHORT).show()
                }
            }
            userResponse.observe(viewLifecycleOwner) {
                userItem = it
                binding.txtUsername.text = it.userName
            }
        }
    }

    override fun onResume() {
        super.onResume()
        settingViewModel.getUsers()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}
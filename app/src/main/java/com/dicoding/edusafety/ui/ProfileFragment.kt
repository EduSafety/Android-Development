package com.dicoding.edusafety.ui

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.dicoding.edusafety.R
import com.dicoding.edusafety.databinding.FragmentProfileBinding
import com.dicoding.edusafety.viewmodel.MainViewModel
import com.dicoding.edusafety.viewmodel.MainViewModelApi
import com.dicoding.edusafety.viewmodel.ViewModelFactory
import com.dicoding.edusafety.viewmodel.ViewModelFactoryApi
import com.firebase.ui.auth.AuthUI

class ProfileFragment : Fragment() {
    private lateinit var binding: FragmentProfileBinding

    private val viewModel by viewModels<MainViewModel> {
        ViewModelFactory.getInstance(requireContext())
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentProfileBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.btnLogout.setOnClickListener {
            signOut()
        }

        // Tambahkan TextWatcher untuk setiap EditText
        binding.edtUsername.addTextChangedListener(textWatcher)
        binding.edtPhoneNumber.addTextChangedListener(textWatcher)
        binding.campusName.addTextChangedListener(textWatcher)

        binding.btnSave.setOnClickListener {
            if (binding.btnSave.isEnabled) {
                saveProfile()
            }
        }

        updateCurrentUser()
        refreshProfile()
        clearCodeAccess()
    }

    private val textWatcher = object : TextWatcher {
        override fun beforeTextChanged(charSequence: CharSequence?, start: Int, count: Int, after: Int) {

        }

        override fun onTextChanged(charSequence: CharSequence?, start: Int, before: Int, count: Int) {
            checkFieldsForEmptyValues()
        }

        override fun afterTextChanged(editable: Editable?) {
        }
    }

    // Metode untuk memeriksa apakah semua EditText tidak kosong
    private fun checkFieldsForEmptyValues() {
        val username = binding.edtUsername.text.toString()
        val phoneNumber = binding.edtPhoneNumber.text.toString()
        val campus = binding.campusName.text.toString()

        // Jika semua EditText tidak kosong, aktifkan tombol Simpan
        val enableButton = !username.isEmpty()  && !phoneNumber.isEmpty() && !campus.isEmpty()
        binding.btnSave.isEnabled = enableButton

    }

    private fun saveProfile() {
        Toast.makeText(requireContext(), "Profil berhasil disimpan", Toast.LENGTH_SHORT).show()
    }

    private fun signOut() {
        viewModel.logout()
        AuthUI.getInstance().signOut(requireContext()).addOnCompleteListener {
            val intent = Intent(requireContext(), InitialPage::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
            startActivity(intent)
            activity?.finish()
        }
    }

    private fun updateCurrentUser(){
        val factoryPref: ViewModelFactoryApi = ViewModelFactoryApi.getInstance(requireContext())
        val viewModel = ViewModelProvider(this, factoryPref)[MainViewModelApi::class.java]

        val factoryDS: ViewModelFactory = ViewModelFactory.getInstance(requireContext())
        val viewModelDS = ViewModelProvider(this, factoryDS)[MainViewModel::class.java]


        binding.btnSave.setOnClickListener{
            viewModelDS.getTokenUser().observe(requireActivity(), Observer { token ->
                if (token != null){
                    val username = binding.edtUsername.text.toString()
                    val phone = binding.edtPhoneNumber.text
                    val campusCode = binding.campusName.text.toString()
                    viewModel.updateCurrentUser(token, username, campusCode, phone)
                    saveProfile()
                }
            })
        }
    }

    fun refreshProfile(){
        val factoryPref: ViewModelFactoryApi = ViewModelFactoryApi.getInstance(requireContext())
        val viewModel = ViewModelProvider(this, factoryPref)[MainViewModelApi::class.java]

        val factoryDS: ViewModelFactory = ViewModelFactory.getInstance(requireContext())
        val viewModelDS = ViewModelProvider(this, factoryDS)[MainViewModel::class.java]

        viewModelDS.getTokenUser().observe(requireActivity(), Observer { token ->
            if (token != null){
                viewModel.getCurrentUser(token)
                viewModel.currentUser.observe(requireActivity(), Observer { user ->
                    if(user != null){
                        binding.tvUsername.text = user.fullname
                        binding.edtUsername.setText(user.fullname.toString())
                        binding.edtPhoneNumber.setText(user.phone.toString())
                        binding.imageView.setImageResource(R.drawable.photoprofile)
                    }
                })
            }
        })
    }

    private fun clearCodeAccess(){
        binding.campusName.setOnFocusChangeListener { view, hasFocus ->
            // Check if the EditText has gained focus
            if (hasFocus) {
                // Clear the text when EditText gains focus
                binding.campusName.text.clear()
            }else{
                binding.campusName.setText("University Code Access")
            }
        }
    }
}
package cz.lpatak.mycoachesdiary.ui.auth

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import androidx.annotation.StringRes
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import cz.lpatak.mycoachesdiary.R
import cz.lpatak.mycoachesdiary.data.model.auth.AuthResult
import cz.lpatak.mycoachesdiary.data.model.auth.ResetPasswordFormState
import cz.lpatak.mycoachesdiary.databinding.FragmentHomeBinding
import cz.lpatak.mycoachesdiary.databinding.FragmentResetPasswordBinding
import cz.lpatak.mycoachesdiary.ui.auth.viewmodel.AuthViewModel
import cz.lpatak.mycoachesdiary.ui.myteams.HomeFragmentDirections
import cz.lpatak.mycoachesdiary.ui.myteams.util.TeamsAdapter
import cz.lpatak.mycoachesdiary.util.PreferenceManger
import cz.lpatak.mycoachesdiary.util.afterTextChanged
import cz.lpatak.mycoachesdiary.util.showToast
import org.koin.androidx.viewmodel.ext.android.viewModel

class ResetPasswordFragment: Fragment() {
    private val authViewModel: AuthViewModel by viewModel()
    private lateinit var binding: FragmentResetPasswordBinding

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_reset_password, container, false)
        with(binding) {
            btnReset.setOnClickListener { resetPassword() }
        }

        return binding.root
    }

    private fun resetPassword(){
        if (authViewModel.resetPassword(binding.etUsername.text.toString())) {
            showToast("Password was reset. Check your email")
            findNavController().navigateUp()
        } else {
            showToast("Incorrect mail format.")
        }
    }


}
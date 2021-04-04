package cz.lpatak.mycoachesdiary.ui.auth

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.StringRes
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.findNavController
import cz.lpatak.mycoachesdiary.R
import cz.lpatak.mycoachesdiary.data.model.auth.*
import cz.lpatak.mycoachesdiary.databinding.FragmentRegisterBinding
import cz.lpatak.mycoachesdiary.ui.auth.viewmodel.AuthViewModel
import cz.lpatak.mycoachesdiary.util.showToast
import org.koin.androidx.viewmodel.ext.android.viewModel

class RegisterFragment : Fragment() {
    private lateinit var binding: FragmentRegisterBinding
    private val authViewModel: AuthViewModel by viewModel()

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_register, container, false)
        with(binding) {
            lifecycleOwner = this@RegisterFragment
            btnRegister.setOnClickListener { register() }
            switchToLogin.setOnClickListener { navigateToLogin() }
        }

        authViewModel.getCurrentLoggedInUser()?.observe(viewLifecycleOwner, loggedInUserObserver)
        authViewModel.registerFormState.observe(viewLifecycleOwner, registerFormStateObserver)
        authViewModel.registerResult.observe(viewLifecycleOwner, registerResultObserver)

        return binding.root
    }


    private fun navigateToHomeScreen() {
        view?.let {
            val directions = RegisterFragmentDirections.actionNavigationRegisterToNavigationHome()
            it.findNavController().navigate(directions)
        }
    }

    private fun navigateToLogin() {
        view?.let {
            val directions = RegisterFragmentDirections.actionRegisterFragmentToNavigationLogin()
            it.findNavController().navigate(directions)
        }
    }


    private fun showRegisterFailed(@StringRes errorString: Int) {
        showToast(errorString)
    }

    private val registerFormStateObserver: Observer<RegisterFormState> = Observer {
        val registerState = it ?: return@Observer

        if (registerState.usernameError != null) {
            binding.etUsername.error = getString(registerState.usernameError)
        }
        if (registerState.passwordError != null) {
            binding.etPassword.error = getString(registerState.passwordError)
        }
        if (registerState.passwordError2 != null) {
            binding.etPassword2.error = getString(registerState.passwordError2)
        }
        if (registerState.passwordMatchError != null) {
            binding.etPassword.error = getString(registerState.passwordMatchError)
            binding.etPassword2.error = getString(registerState.passwordMatchError)
        }
    }

    private val registerResultObserver: Observer<AuthResult> = Observer {
        val registerResult = it ?: return@Observer

        if (registerResult.error != null) {
            showRegisterFailed(registerResult.error)
        }
        if (registerResult.success != null) {
            navigateToHomeScreen()
        }

    }

    private val loggedInUserObserver: Observer<LoggedInUserView> = Observer {
        it?.let {
            navigateToHomeScreen()
        }
    }

    private fun register() {
        if (
                authViewModel.registerDataCheck(
                        binding.etUsername.text.toString(),
                        binding.etPassword.text.toString(),
                        binding.etPassword2.text.toString()
                )
        ) {
            authViewModel.register(
                    binding.etUsername.text.toString(),
                    binding.etPassword.text.toString()
            )
        }
    }

}
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
import cz.lpatak.mycoachesdiary.data.model.auth.AuthResult
import cz.lpatak.mycoachesdiary.data.model.auth.LoggedInUserView
import cz.lpatak.mycoachesdiary.data.model.auth.LoginFormState
import cz.lpatak.mycoachesdiary.databinding.FragmentLoginBinding
import cz.lpatak.mycoachesdiary.ui.auth.viewmodel.AuthViewModel
import cz.lpatak.mycoachesdiary.util.showToast
import org.koin.androidx.viewmodel.ext.android.viewModel

class LoginFragment : Fragment() {
    private lateinit var binding: FragmentLoginBinding
    private val authViewModel: AuthViewModel by viewModel()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_login, container, false)
        with(binding) {
            lifecycleOwner = this@LoginFragment
            btnLogin.setOnClickListener { login() }
            switchToRegister.setOnClickListener { navigateToRegistration() }
            resetPassword.setOnClickListener { navigateToResetPassword() }
        }

        authViewModel.getCurrentLoggedInUser()?.observe(viewLifecycleOwner, loggedInUserObserver)
        authViewModel.loginFormState.observe(viewLifecycleOwner, loginFormStateObserver)
        authViewModel.authResult.observe(viewLifecycleOwner, authResultObserver)
        return binding.root
    }


    private fun navigateToHomeScreen() {
        view?.let {
            val directions = LoginFragmentDirections.actionNavigationLoginToNavigationHome()
            it.findNavController().navigate(directions)
        }
    }

    private fun navigateToRegistration() {
        view?.let {
            val directions = LoginFragmentDirections.actionNavigationLoginToRegisterFragment()
            it.findNavController().navigate(directions)
        }
    }

    private fun navigateToResetPassword() {
        view?.let {
            val directions =
                LoginFragmentDirections.actionNavigationLoginToNavigationResetPassword()
            it.findNavController().navigate(directions)
        }
    }

    private val authResultObserver: Observer<AuthResult> = Observer {
        val loginResult = it ?: return@Observer

        if (loginResult.error != null) {
            showLoginFailed(loginResult.error)
        }
        if (loginResult.success != null) {
            navigateToHomeScreen()
        }

    }

    private val loginFormStateObserver: Observer<LoginFormState> = Observer {
        val loginState = it ?: return@Observer

        if (loginState.usernameError != null) {
            binding.etUsername.error = getString(loginState.usernameError)
        }
        if (loginState.passwordError != null) {
            binding.etPassword.error = getString(loginState.passwordError)
        }
    }

    private val loggedInUserObserver: Observer<LoggedInUserView> = Observer {
        it?.let {
            navigateToHomeScreen()
        }
    }

    private fun showLoginFailed(@StringRes errorString: Int) {
        showToast(errorString)
    }

    private fun login() {
        if (authViewModel.loginDataCheck(
                binding.etUsername.text.toString(),
                binding.etPassword.text.toString()
            )
        ) {
            authViewModel.login(
                binding.etUsername.text.toString(),
                binding.etPassword.text.toString()
            )
        }

    }

}
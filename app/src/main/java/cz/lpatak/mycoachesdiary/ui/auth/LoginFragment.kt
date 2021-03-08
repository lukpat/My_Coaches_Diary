package cz.lpatak.mycoachesdiary.ui.auth

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import androidx.annotation.StringRes
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.findNavController
import cz.lpatak.mycoachesdiary.data.model.auth.LoggedInUserView
import cz.lpatak.mycoachesdiary.data.model.auth.LoginFormState
import cz.lpatak.mycoachesdiary.data.model.auth.LoginResult
import cz.lpatak.mycoachesdiary.databinding.FragmentLoginBinding
import cz.lpatak.mycoachesdiary.ui.auth.viewmodel.AuthViewModel
import cz.lpatak.mycoachesdiary.util.afterTextChanged
import cz.lpatak.mycoachesdiary.util.showToast
import org.koin.androidx.viewmodel.ext.android.viewModel

class LoginFragment : Fragment() {

    private val authViewModel: AuthViewModel by viewModel()
    private var _binding: FragmentLoginBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentLoginBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        binding.etUsername.afterTextChanged {
            authViewModel.loginDataChanged(
                    binding.etUsername.text.toString(),
                    binding.etPassword.text.toString()
            )
        }

        binding.etPassword.apply {
            afterTextChanged {
                authViewModel.loginDataChanged(
                        binding.etUsername.text.toString(),
                        binding.etPassword.text.toString()
                )
            }

            setOnEditorActionListener { _, actionId, _ ->
                when (actionId) {
                    EditorInfo.IME_ACTION_DONE ->
                        if (binding.btnLogin.isEnabled) {
                            binding.btnLogin.callOnClick()
                        }
                }
                false
            }
        }

        binding.btnLogin.setOnClickListener {
            binding.loading.visibility = View.VISIBLE
            authViewModel.login(
                    binding.etUsername.text.toString(),
                    binding.etPassword.text.toString()
            )
        }

        binding.switchToRegister.setOnClickListener {
            navigateToRegistration()
        }

        authViewModel.getCurrentLoggedInUser()?.observe(viewLifecycleOwner, loggedInUserObserver)
        authViewModel.loginFormState.observe(viewLifecycleOwner, loginFormStateObserver)
        authViewModel.loginResult.observe(viewLifecycleOwner, loginResultObserver)

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

    private fun showLoginFailed(@StringRes errorString: Int) {
        showToast(errorString)
    }


    private val loginFormStateObserver: Observer<LoginFormState> = Observer {
        val loginState = it ?: return@Observer

        binding.btnLogin.isEnabled = loginState.isDataValid

        if (loginState.usernameError != null) {
            binding.etUsername.error = getString(loginState.usernameError)
        }
        if (loginState.passwordError != null) {
            binding.etPassword.error = getString(loginState.passwordError)
        }
    }

    private val loginResultObserver: Observer<LoginResult> = Observer {
        val loginResult = it ?: return@Observer

        binding.loading.visibility = View.GONE
        if (loginResult.error != null) {
            showLoginFailed(loginResult.error)
        }
        if (loginResult.success != null) {
            navigateToHomeScreen()
        }

    }

    private val loggedInUserObserver: Observer<LoggedInUserView> = Observer {
        it?.let {
            navigateToHomeScreen()
        }
    }
}
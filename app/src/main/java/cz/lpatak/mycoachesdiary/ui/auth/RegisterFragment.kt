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
import cz.lpatak.mycoachesdiary.data.model.auth.*
import cz.lpatak.mycoachesdiary.databinding.FragmentRegisterBinding
import cz.lpatak.mycoachesdiary.ui.auth.viewmodel.AuthViewModel
import cz.lpatak.mycoachesdiary.util.afterTextChanged
import cz.lpatak.mycoachesdiary.util.showToast
import org.koin.androidx.viewmodel.ext.android.viewModel

class RegisterFragment : Fragment() {

    private val authViewModel: AuthViewModel by viewModel()
    private var _binding: FragmentRegisterBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentRegisterBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        binding.etUsername.afterTextChanged {
            authViewModel.registerDataChanged(
                    binding.etUsername.text.toString(),
                    binding.etPassword.text.toString(),
                    binding.etPassword2.text.toString()
            )
        }

        binding.etPassword.apply {
            afterTextChanged {
                authViewModel.registerDataChanged(
                        binding.etUsername.text.toString(),
                        binding.etPassword.text.toString(),
                        binding.etPassword2.text.toString()
                )
            }

            binding.etPassword2.apply {
                afterTextChanged {
                    authViewModel.registerDataChanged(
                            binding.etUsername.text.toString(),
                            binding.etPassword.text.toString(),
                            binding.etPassword2.text.toString()
                    )
                }

                setOnEditorActionListener { _, actionId, _ ->
                    when (actionId) {
                        EditorInfo.IME_ACTION_DONE ->
                            if (binding.btnRegister.isEnabled) {
                                binding.btnRegister.callOnClick()
                            }
                    }
                    false
                }
            }

            binding.btnRegister.setOnClickListener {
                binding.loading.visibility = View.VISIBLE
                authViewModel.register(
                        binding.etUsername.text.toString(),
                        binding.etPassword.text.toString()
                )
            }

            binding.switchToRegister.setOnClickListener {
                navigateToLogin()
            }

            authViewModel.getCurrentLoggedInUser()
                    ?.observe(viewLifecycleOwner, loggedInUserObserver)
            authViewModel.registerFormState.observe(viewLifecycleOwner, registerFormStateObserver)
            authViewModel.registerResult.observe(viewLifecycleOwner, registerResultObserver)

        }
    }

    private fun navigateToHomeScreen() {
        view?.let {
            val directions =
                    RegisterFragmentDirections.actionNavigationRegisterToNavigationHome()
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


    /**
     * Observes login form validations
     */
    private val registerFormStateObserver: Observer<RegisterFormState> = Observer {
        val registerState = it ?: return@Observer

        binding.btnRegister.isEnabled = registerState.isDataValid

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

    /**
     * Observes login action result
     */
    private val registerResultObserver: Observer<AuthResult> = Observer {
        val registerResult = it ?: return@Observer

        binding.loading.visibility = View.GONE
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

}
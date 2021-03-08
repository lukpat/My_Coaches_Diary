package cz.lpatak.mycoachesdiary.ui.myteams

import android.app.AlertDialog
import android.content.DialogInterface
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import cz.lpatak.mycoachesdiary.R
import cz.lpatak.mycoachesdiary.data.model.DBConstants.Companion.TEAM_NAME_KEY
import cz.lpatak.mycoachesdiary.data.model.DBConstants.Companion.TEAM_SEASON_KEY
import cz.lpatak.mycoachesdiary.data.model.Result
import cz.lpatak.mycoachesdiary.databinding.FragmentHomeBinding
import cz.lpatak.mycoachesdiary.ui.auth.viewmodel.AuthViewModel
import cz.lpatak.mycoachesdiary.ui.myteams.util.TeamsAdapter
import cz.lpatak.mycoachesdiary.ui.myteams.viewmodel.MyTeamsViewModel
import cz.lpatak.mycoachesdiary.util.PreferenceManger
import org.koin.androidx.viewmodel.ext.android.viewModel

class HomeFragment : Fragment() {
    private val myTeamsViewModel: MyTeamsViewModel by viewModel()
    private val authViewModel: AuthViewModel by viewModel()
    private lateinit var binding: FragmentHomeBinding
    private lateinit var preferenceManager: PreferenceManger
    private val adapter: TeamsAdapter = TeamsAdapter()

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        preferenceManager = myTeamsViewModel.getPreferenceManager()

        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_home, container, false)
        with(binding) {
            lifecycleOwner = this@HomeFragment
            teamsList.adapter = adapter
            btnLogout.setOnClickListener {
                val directions = HomeFragmentDirections.actionGlobalNavigationLogin()
                AlertDialog.Builder(it.context)
                        .setTitle(R.string.logout_title)
                        .setMessage(R.string.logout_message)
                        .setPositiveButton(R.string.yes, DialogInterface.OnClickListener { _, _ ->
                            authViewModel.logOut()
                            it.findNavController().navigate(directions)
                        })
                        .setNegativeButton(R.string.no, null)
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .show()
            }
            txtUser.text = authViewModel.getCurrentUserEmail()
            binding.txtTeam.text = setCurrentTeam()
        }

        adapter.setViewModel(myTeamsViewModel)

        return binding.root
    }


    override fun onStart() {
        super.onStart()
        loadTeams()
    }

    private fun loadTeams() {
        myTeamsViewModel.loadTeams().observe(viewLifecycleOwner, { result ->
            binding.result = result
            if (result is Result.Success) {
                adapter.submitList(result.data)
            }
        })
    }

    private fun setCurrentTeam(): String {
        val teamName = preferenceManager.getStringValue(TEAM_NAME_KEY)
        val teamSeason = preferenceManager.getStringValue(TEAM_SEASON_KEY)
        return if (teamName.isNullOrEmpty()) {
            resources.getString(R.string.no_team_selected)
        } else {
            "$teamName ($teamSeason)"
        }
    }

}
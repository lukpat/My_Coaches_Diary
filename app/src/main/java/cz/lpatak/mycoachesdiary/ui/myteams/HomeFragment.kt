package cz.lpatak.mycoachesdiary.ui.myteams

import android.app.AlertDialog
import android.content.DialogInterface
import android.os.Bundle
import android.view.*
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
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
            txtTeam.text = setCurrentTeam()
            fabAddTeam.setOnClickListener {
                val directions= HomeFragmentDirections.actionNavigationHomeToNavigationAddTeam()
                findNavController().navigate(directions)
            }
        }

        adapter.setViewModel(myTeamsViewModel)

        return binding.root
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.home_menu, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.logout-> {
                    val directions = HomeFragmentDirections.actionGlobalNavigationLogin()
                    AlertDialog.Builder(context)
                            .setMessage(R.string.logout_message)
                            .setPositiveButton(R.string.yes, DialogInterface.OnClickListener { _, _ ->
                                authViewModel.logOut()
                                findNavController().navigate(directions)
                            })
                            .setNegativeButton(R.string.no, null)
                            .show()
            }
        }
        return super.onOptionsItemSelected(item)
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
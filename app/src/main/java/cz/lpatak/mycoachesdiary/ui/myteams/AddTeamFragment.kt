package cz.lpatak.mycoachesdiary.ui.myteams

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import cz.lpatak.mycoachesdiary.R
import cz.lpatak.mycoachesdiary.data.model.Team
import cz.lpatak.mycoachesdiary.databinding.FragmentAddTeamBinding
import cz.lpatak.mycoachesdiary.ui.myteams.viewmodel.MyTeamsViewModel
import cz.lpatak.mycoachesdiary.ui.myteams.viewmodel.TeamUIModel
import org.koin.androidx.viewmodel.ext.android.viewModel


class AddTeamFragment : Fragment() {
    private val myTeamsViewModel: MyTeamsViewModel by viewModel()
    private lateinit var binding: FragmentAddTeamBinding

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_add_team, container, false)
        with(binding) {
            lifecycleOwner = this@AddTeamFragment
            teamModel = TeamUIModel()
            btnAddTeam.setOnClickListener { createTeam() }
        }
        return binding.root
    }


    private fun createTeam() {
        myTeamsViewModel.addTeam(
                Team(
                        "",
                        "",
                        binding.teamModel!!.name.value.toString(),
                        binding.teamModel!!.season.value.toString()
                )
        )
        findNavController().navigateUp()
    }


}
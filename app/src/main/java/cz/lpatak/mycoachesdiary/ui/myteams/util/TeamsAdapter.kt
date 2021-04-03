package cz.lpatak.mycoachesdiary.ui.myteams.util

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.navigation.findNavController
import androidx.recyclerview.widget.DiffUtil
import cz.lpatak.mycoachesdiary.R
import cz.lpatak.mycoachesdiary.data.model.Team
import cz.lpatak.mycoachesdiary.databinding.TeamItemBinding
import cz.lpatak.mycoachesdiary.ui.base.DataBoundListAdapter
import cz.lpatak.mycoachesdiary.ui.myteams.HomeFragmentDirections
import cz.lpatak.mycoachesdiary.ui.myteams.viewmodel.MyTeamsViewModel

class TeamsAdapter(
    private val onClick: ((Team) -> Unit)? = null
) : DataBoundListAdapter<Team, TeamItemBinding>(
    diffCallback = object : DiffUtil.ItemCallback<Team>() {
        override fun areItemsTheSame(oldItem: Team, newItem: Team): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Team, newItem: Team): Boolean {
            return oldItem == newItem
        }
    }
) {
    private lateinit var viewModel: MyTeamsViewModel

    fun setViewModel(viewModel: MyTeamsViewModel) {
        this.viewModel = viewModel
    }

    override fun createBinding(parent: ViewGroup): TeamItemBinding {
        return DataBindingUtil.inflate<TeamItemBinding>(
            LayoutInflater.from(parent.context),
            R.layout.team_item,
            parent, false
        ).apply {
            this.root.setOnClickListener {
                this.team?.let { goToTeamDetail(this.root, it) }
            }
        }
    }


    override fun bind(binding: TeamItemBinding, item: Team) {
        binding.team = item
    }

    private fun goToTeamDetail(view: View, team: Team) {
        val directions = HomeFragmentDirections.actionNavigationHomeToNavigationTeamDetail(team)
        view.findNavController().navigate(directions)
    }
}


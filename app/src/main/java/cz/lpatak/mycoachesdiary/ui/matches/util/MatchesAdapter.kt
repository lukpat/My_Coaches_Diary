package cz.lpatak.mycoachesdiary.ui.matches.util

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.navigation.findNavController
import androidx.recyclerview.widget.DiffUtil
import cz.lpatak.mycoachesdiary.R
import cz.lpatak.mycoachesdiary.data.model.Match
import cz.lpatak.mycoachesdiary.databinding.MatchItemBinding
import cz.lpatak.mycoachesdiary.ui.base.DataBoundListAdapter
import cz.lpatak.mycoachesdiary.ui.matches.MatchesFragmentDirections
import cz.lpatak.mycoachesdiary.ui.matches.viewmodel.MatchesViewModel

class MatchesAdapter(
    private val onClick: ((Match) -> Unit)? = null
) : DataBoundListAdapter<Match, MatchItemBinding>(
    diffCallback = object : DiffUtil.ItemCallback<Match>() {
        override fun areItemsTheSame(oldItem: Match, newItem: Match): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Match, newItem: Match): Boolean {
            return oldItem == newItem
        }
    }
) {
    private lateinit var viewModel: MatchesViewModel

    fun setViewModel(viewModel: MatchesViewModel) {
        this.viewModel = viewModel
    }

    override fun createBinding(parent: ViewGroup): MatchItemBinding {
        return DataBindingUtil.inflate<MatchItemBinding>(
            LayoutInflater.from(parent.context),
            R.layout.match_item,
            parent, false
        ).apply {
            this.root.setOnClickListener {
                this.match?.let { goToMatchDetail(this.root, it) }
            }
        }
    }


    override fun bind(binding: MatchItemBinding, item: Match) {
        binding.match = item
        binding.dateAndTypeOfMatch =
            viewModel.convertDateToString(item.date!!) + " (" + item.type + ")"
    }

    private fun goToMatchDetail(view: View, match: Match) {
        val directions =
            MatchesFragmentDirections.actionNavigationMatchesToNavigationMatchDetail(match)
        view.findNavController().navigate(directions)
    }
}

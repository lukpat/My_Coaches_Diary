package cz.lpatak.mycoachesdiary.ui.trainings

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import cz.lpatak.mycoachesdiary.R
import cz.lpatak.mycoachesdiary.data.model.Result
import cz.lpatak.mycoachesdiary.databinding.FragmentTrainingsBinding
import cz.lpatak.mycoachesdiary.ui.trainings.util.TrainingsAdapter
import cz.lpatak.mycoachesdiary.ui.trainings.viewmodel.TrainingsViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class TrainingsFragment : Fragment() {
    private val trainingsViewModel: TrainingsViewModel by viewModel()
    private lateinit var binding: FragmentTrainingsBinding
    private val adapter: TrainingsAdapter = TrainingsAdapter()

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_trainings, container, false)
        with(binding) {
            lifecycleOwner = this@TrainingsFragment
            trainingsList.adapter = adapter
            fabAddTraining.setOnClickListener {
                val directions =
                        TrainingsFragmentDirections.actionNavigationTrainingsToNavigationAddTraining()
                findNavController().navigate(directions)
            }
            isTeamSelected = trainingsViewModel.isTeamSelected()
        }

        adapter.setViewModel(trainingsViewModel)
        adapter.setPreferenceManager(trainingsViewModel.getPreferenceManager())

        return binding.root
    }

    override fun onStart() {
        super.onStart()
        loadTrainings()
    }

    private fun loadTrainings() {
        trainingsViewModel.loadTeams().observe(viewLifecycleOwner, { result ->
            binding.result = result
            if (result is Result.Success) {
                adapter.submitList(result.data)
            }
        })
    }

}
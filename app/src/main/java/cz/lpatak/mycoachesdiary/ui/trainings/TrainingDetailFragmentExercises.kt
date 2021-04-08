package cz.lpatak.mycoachesdiary.ui.trainings

import android.os.Bundle
import android.view.*
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import cz.lpatak.mycoachesdiary.R
import cz.lpatak.mycoachesdiary.data.model.ExerciseInTraining
import cz.lpatak.mycoachesdiary.data.model.Result
import cz.lpatak.mycoachesdiary.databinding.FragmentTrainingDetailExercisesBinding
import cz.lpatak.mycoachesdiary.ui.trainings.util.ExerciseInTrainingAdapter
import cz.lpatak.mycoachesdiary.ui.trainings.util.UpdateExerciseTimeDialog
import cz.lpatak.mycoachesdiary.ui.trainings.viewmodel.TrainingsViewModel
import org.koin.android.ext.android.bind
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.util.*


class TrainingDetailFragmentExercises : Fragment() {
    private lateinit var binding: FragmentTrainingDetailExercisesBinding
    val adapter: ExerciseInTrainingAdapter = ExerciseInTrainingAdapter(null)
    private val trainingsViewModel: TrainingsViewModel by viewModel()

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(
                inflater,
                R.layout.fragment_training_detail_exercises,
                container,
                false
        )

        with(binding) {
            lifecycleOwner = this@TrainingDetailFragmentExercises
            fabAddExerciseToTraining.setOnClickListener {
                val directions =
                        TrainingDetailFragmentExercisesDirections.actionNavigationTrainingDetailExercisesToNavigationAddExerciseToTraining()
                findNavController().navigate(directions)
            }
            exercisesList.adapter = adapter
        }

        adapter.setViewModel(trainingsViewModel)

        return binding.root
    }

    override fun onStart() {
        super.onStart()
        loadExercises()
    }

    fun loadExercises() {
        trainingsViewModel.loadExercisesInTraining().observe(viewLifecycleOwner, { result ->
            binding.result = result
            if (result is Result.Success) {
                adapter.submitList(result.data)
                setUI()
            }
        })
    }

    private fun setUI(){
        var time = 0
        adapter.notifyDataSetChanged()
        for (exercise in adapter.currentList){
            time += exercise.time
        }

        val str = "$time min"
        binding.exerciseTime.text = str
    }

    fun updateExerciseTime(exercise: ExerciseInTraining) {
        val dialog = UpdateExerciseTimeDialog(exercise, this, false)
        dialog.show(this.requireActivity().supportFragmentManager, "update exercise time in training")
        setUI()
    }
}
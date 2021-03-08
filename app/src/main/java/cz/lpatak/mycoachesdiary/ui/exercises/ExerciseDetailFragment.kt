package cz.lpatak.mycoachesdiary.ui.exercises

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.net.toUri
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.navArgs
import androidx.work.*
import cz.lpatak.mycoachesdiary.R
import cz.lpatak.mycoachesdiary.databinding.FragmentExerciseDetailBinding
import cz.lpatak.mycoachesdiary.ui.exercises.viewmodel.ExerciseUIModel


class ExerciseDetailFragment : Fragment() {

    private val args: ExerciseDetailFragmentArgs by navArgs()
    private lateinit var binding: FragmentExerciseDetailBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding =
            DataBindingUtil.inflate(
                inflater,
                R.layout.fragment_exercise_detail,
                container,
                false
            )
        with(binding) {
            lifecycleOwner = this@ExerciseDetailFragment
            exerciseModel = ExerciseUIModel()
        }

        setExercise(binding.exerciseModel)

        return binding.root
    }


    private fun setExercise(uiModel: ExerciseUIModel?) {
        val exercise = args.exercise
        val categoryText = "Kategorie: " + exercise.category

        uiModel?.name?.value = exercise.name
        binding.category.text = categoryText
        uiModel?.description?.value = exercise.description
        uiModel?.fileUri?.value = exercise.imageUrl!!.toUri()
    }

}
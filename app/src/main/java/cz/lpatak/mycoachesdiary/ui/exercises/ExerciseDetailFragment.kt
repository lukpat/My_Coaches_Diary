package cz.lpatak.mycoachesdiary.ui.exercises

import android.app.AlertDialog
import android.content.DialogInterface
import android.os.Bundle
import android.view.*
import androidx.core.net.toUri
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import cz.lpatak.mycoachesdiary.R
import cz.lpatak.mycoachesdiary.databinding.FragmentExerciseDetailBinding
import cz.lpatak.mycoachesdiary.ui.exercises.viewmodel.ExerciseUIModel
import cz.lpatak.mycoachesdiary.ui.exercises.viewmodel.ExercisesViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel


class ExerciseDetailFragment : Fragment() {

    private val args: ExerciseDetailFragmentArgs by navArgs()
    private lateinit var binding: FragmentExerciseDetailBinding
    private val exercisesViewModel: ExercisesViewModel by viewModel()
    private val exerciseUIModel: ExerciseUIModel by viewModel()

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        binding =
                DataBindingUtil.inflate(inflater, R.layout.fragment_exercise_detail, container, false)
        binding.exerciseModel = exerciseUIModel

        setExercise()

        return binding.root
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (exercisesViewModel.isExerciseOwner(args.exercise.owner.toString())) {
            setHasOptionsMenu(true)
        } else {
            setHasOptionsMenu(false)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.delete_menu, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.delete -> {
                AlertDialog.Builder(context)
                        .setMessage(R.string.delete_exercise_alert)
                        .setPositiveButton(R.string.yes, DialogInterface.OnClickListener { _, _ ->
                            exercisesViewModel.deleteExercise(args.exercise.id.toString())
                            findNavController().navigateUp()
                        })
                        .setNegativeButton(R.string.no, null)
                        .show()
            }
        }

        return super.onOptionsItemSelected(item)
    }


    private fun setExercise() {
        val exercise = args.exercise

        exerciseUIModel.name.value = exercise.name
        exerciseUIModel.category.value = exercise.category
        exerciseUIModel.description.value = exercise.description
        exerciseUIModel.fileUri.value = exercise.imageUrl!!.toUri()
    }

}
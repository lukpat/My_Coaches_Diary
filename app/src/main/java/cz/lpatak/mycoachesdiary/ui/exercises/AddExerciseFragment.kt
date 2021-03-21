package cz.lpatak.mycoachesdiary.ui.exercises

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.*
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.work.*
import cz.lpatak.mycoachesdiary.R
import cz.lpatak.mycoachesdiary.data.model.Exercise
import cz.lpatak.mycoachesdiary.databinding.FragmentAddExerciseBinding
import cz.lpatak.mycoachesdiary.ui.exercises.util.ImageUploaderWorker
import cz.lpatak.mycoachesdiary.ui.exercises.util.NewExerciseWorker
import cz.lpatak.mycoachesdiary.ui.exercises.viewmodel.ExerciseUIModel
import cz.lpatak.mycoachesdiary.ui.exercises.viewmodel.ExercisesViewModel
import cz.lpatak.mycoachesdiary.util.showToast
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.util.*

class AddExerciseFragment : Fragment() {

    private lateinit var binding: FragmentAddExerciseBinding
    private val exercisesViewModel: ExercisesViewModel by viewModel()
    private val exerciseUIModel: ExerciseUIModel = ExerciseUIModel()

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        binding =
                DataBindingUtil.inflate(inflater, R.layout.fragment_add_exercise, container, false)
        with(binding) {
            lifecycleOwner = this@AddExerciseFragment
            exerciseModel = exerciseUIModel
            addImageButton.setOnClickListener { launchGallery() }
        }
        return binding.root
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.save_menu, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.save -> {
                if (exerciseUIModel.checkInputs()) {
                    startCreatingNewExercise()
                } else {
                    showToast(getString(R.string.exercise_error))
                }
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun startCreatingNewExercise() {
        if (binding.exerciseModel?.fileUri?.value == null) {
            exercisesViewModel.addExercise(
                    Exercise(
                            "",
                            "",
                            exerciseUIModel.name.value,
                            exerciseUIModel.name.value?.toUpperCase(Locale.ROOT),
                            binding.exerciseCategory.selectedItem.toString(),
                            exerciseUIModel.description.value,
                            ""
                    )
            )
            showToast(getString(R.string.exercise_created))
            findNavController().navigateUp()
        } else {
            val constraints = Constraints.Builder()
                    .setRequiredNetworkType(NetworkType.CONNECTED)
                    .build()

            val uploadImageWorker: OneTimeWorkRequest =
                    OneTimeWorkRequestBuilder<ImageUploaderWorker>()
                            .setConstraints(constraints)
                            .setInputData(
                                    workDataOf(
                                            NewExerciseWorker.NAME to binding.exerciseModel?.name?.value,
                                            NewExerciseWorker.CATEGORY to binding.exerciseCategory.selectedItem.toString(),
                                            NewExerciseWorker.DESCRIPTION to binding.exerciseModel?.description?.value,
                                            ImageUploaderWorker.KEY_IMAGE_URI to binding.exerciseModel?.fileUri?.value.toString()
                                    )
                            )
                            .build()

            val newExerciseWorker: OneTimeWorkRequest =
                    OneTimeWorkRequestBuilder<NewExerciseWorker>()
                            .setConstraints(constraints)
                            .build()

            WorkManager.getInstance(requireContext())
                    .beginWith(uploadImageWorker)
                    .then(newExerciseWorker)
                    .enqueue()
            findNavController().navigateUp()
        }
    }

    private fun launchGallery() {
        val intent = Intent(Intent.ACTION_OPEN_DOCUMENT)
        intent.type = "image/*"
        startActivityForResult(intent, RC_TAKE_PICTURE)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == RC_TAKE_PICTURE) {
            if (resultCode == Activity.RESULT_OK) {
                binding.exerciseModel?.fileUri?.value = data?.data
            } else {
                showToast("Picking picture failed.")
            }
        }
    }

    companion object {
        private const val RC_TAKE_PICTURE = 233
    }
}

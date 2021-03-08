package cz.lpatak.mycoachesdiary.ui.exercises.util

import android.content.Context
import android.net.Uri
import androidx.core.net.toUri
import androidx.work.CoroutineWorker
import androidx.work.Data
import androidx.work.WorkerParameters
import cz.lpatak.mycoachesdiary.data.repositories.ExerciseRepositoryImpl
import cz.lpatak.mycoachesdiary.data.source.FirestoreSource
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

class ImageUploaderWorker(
        context: Context,
        workerParams: WorkerParameters
) : CoroutineWorker(context, workerParams) {

    private val repository = ExerciseRepositoryImpl(FirestoreSource())

    override suspend fun doWork(): Result {
        val fileUri = inputData.getString(KEY_IMAGE_URI)?.toUri()
        fileUri?.let { return uploadImageFromURI(it) }
        throw IllegalStateException("Image URI doesn't exist")
    }

    private suspend fun uploadImageFromURI(fileUri: Uri): Result = suspendCoroutine { cont ->
        repository.uploadImageWithUri(fileUri) { result, _ ->
            when (result) {
                is cz.lpatak.mycoachesdiary.data.model.Result.Success -> {
                    val data = Data.Builder()
                            .putAll(inputData)
                            .putString(KEY_UPLOADED_URI, result.data.toString())
                    cont.resume(Result.success(data.build()))
                }

                is cz.lpatak.mycoachesdiary.data.model.Result.Error -> {
                    cont.resume(Result.failure())
                }
            }
        }
    }


    companion object {
        const val KEY_IMAGE_URI: String = "key-image-uri"
        const val KEY_UPLOADED_URI: String = "key-uploaded-uri"
    }
}
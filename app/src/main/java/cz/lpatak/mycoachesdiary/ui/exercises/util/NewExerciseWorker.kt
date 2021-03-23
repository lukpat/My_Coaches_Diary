/**
 *                           MIT License
 *
 *                 Copyright (c) 2019 Amr Elghobary
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */
package cz.lpatak.mycoachesdiary.ui.exercises.util

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import cz.lpatak.mycoachesdiary.data.model.Exercise
import cz.lpatak.mycoachesdiary.data.repositories.ExerciseRepositoryImpl
import cz.lpatak.mycoachesdiary.data.source.FirestoreSource
import java.util.*

class NewExerciseWorker(
        context: Context,
        workerParams: WorkerParameters
) : CoroutineWorker(context, workerParams) {

    private val repository = ExerciseRepositoryImpl(FirestoreSource())

    override suspend fun doWork(): Result {
        val name = checkNotNull(inputData.getString(NAME))
        val category = checkNotNull(inputData.getString(CATEGORY))
        val description = checkNotNull((inputData.getString(DESCRIPTION)))
        val imageUri = checkNotNull(inputData.getString(ImageUploaderWorker.KEY_UPLOADED_URI))

        val exercise =
                Exercise("", "", name, name.toUpperCase(Locale.ROOT), category, description, imageUri)

        when (repository.addExercise(exercise)) {
            is cz.lpatak.mycoachesdiary.data.model.Result.Success -> {
                return Result.success()
            }
            is cz.lpatak.mycoachesdiary.data.model.Result.Error -> {
                return Result.failure()
            }
        }

        return Result.failure()
    }


    companion object {
        const val NAME = "key-name-new-exercise"
        const val CATEGORY = "key-category-new-exercise"
        const val DESCRIPTION = "key-description-new-exercise"
    }
}
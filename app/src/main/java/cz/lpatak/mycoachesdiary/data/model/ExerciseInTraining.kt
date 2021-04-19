package cz.lpatak.mycoachesdiary.data.model

import android.os.Parcel
import android.os.Parcelable
import com.google.firebase.firestore.DocumentId

data class ExerciseInTraining(
    @DocumentId
    val id: String?,
    val name: String?,
    val category: String?,
    val description: String?,
    val imageUrl: String?,
    var time: Int
) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readInt()
    )

    @Suppress("unused")
    constructor() : this("", "", "", "", "", 0)

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(id)
        parcel.writeString(name)
        parcel.writeString(category)
        parcel.writeString(description)
        parcel.writeString(imageUrl)
        parcel.writeInt(time)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<ExerciseInTraining> {
        override fun createFromParcel(parcel: Parcel): ExerciseInTraining {
            return ExerciseInTraining(parcel)
        }

        override fun newArray(size: Int): Array<ExerciseInTraining?> {
            return arrayOfNulls(size)
        }
    }
}
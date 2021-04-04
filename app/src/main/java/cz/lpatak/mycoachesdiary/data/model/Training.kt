package cz.lpatak.mycoachesdiary.data.model

import android.os.Parcel
import android.os.Parcelable
import com.google.firebase.Timestamp
import com.google.firebase.firestore.DocumentId
import java.util.*

data class Training(
        @DocumentId
        val id: String?,
        val place: String?,
        val rating: Int,
        val date: Timestamp?,
        val startTime: String?,
        val endTime: String?,
        val players: Int,
        val goalkeepers: Int
) : Parcelable {
    constructor(parcel: Parcel) : this(
            parcel.readString(),
            parcel.readString(),
            parcel.readInt(),
            Timestamp(Date(parcel.readLong())),
            parcel.readString(),
            parcel.readString(),
            parcel.readInt(),
            parcel.readInt()
    )


    @Suppress("unused")
    constructor() : this("", "", 0, Timestamp(Date(0)), "", "", 0, 0)


    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(id)
        parcel.writeString(place)
        parcel.writeInt(rating)
        parcel.writeString(date.toString())
        parcel.writeString(startTime)
        parcel.writeString(endTime)
        parcel.writeInt(players)
        parcel.writeInt(goalkeepers)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Training> {
        override fun createFromParcel(parcel: Parcel): Training {
            return Training(parcel)
        }

        override fun newArray(size: Int): Array<Training?> {
            return arrayOfNulls(size)
        }
    }
}
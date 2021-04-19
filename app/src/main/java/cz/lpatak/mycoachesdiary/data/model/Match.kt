package cz.lpatak.mycoachesdiary.data.model

import android.os.Parcel
import android.os.Parcelable
import com.google.firebase.Timestamp
import com.google.firebase.firestore.DocumentId
import java.util.*

data class Match(
    @DocumentId
    val id: String?,
    var team: String?,
    val opponent: String?,
    val date: Timestamp?,
    val type: String?,
    val playingTime: Int,
    val note: String?,
    //stats part
    val scoreTeam: Int,
    val scoreOpponent: Int,
    val powerPlaysTeam: Int,
    val powerPlaysOpponent: Int,
    val powerPlaysTeamSuccess: Int,
    val powerPlaysOpponentSuccess: Int,
    val shotsTeam: Int,
    val shotsOpponent: Int,
    val shotsToBlock: Int,
    val shotsOutside: Int
) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        Timestamp(Date(parcel.readLong())),
        parcel.readString(),
        parcel.readInt(),
        parcel.readString(),
        parcel.readInt(),
        parcel.readInt(),
        parcel.readInt(),
        parcel.readInt(),
        parcel.readInt(),
        parcel.readInt(),
        parcel.readInt(),
        parcel.readInt(),
        parcel.readInt(),
        parcel.readInt()
    )

    @Suppress("unused")
    constructor() : this("", "", "", Timestamp(Date(0)), "", 0, "", 0, 0, 0, 0, 0, 0, 0, 0, 0, 0)

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(id)
        parcel.writeString(team)
        parcel.writeString(opponent)
        parcel.writeString(date.toString())
        parcel.writeString(type)
        parcel.writeInt(playingTime)
        parcel.writeString(note)
        parcel.writeInt(scoreTeam)
        parcel.writeInt(scoreOpponent)
        parcel.writeInt(powerPlaysTeam)
        parcel.writeInt(powerPlaysOpponent)
        parcel.writeInt(powerPlaysTeamSuccess)
        parcel.writeInt(powerPlaysOpponentSuccess)
        parcel.writeInt(shotsTeam)
        parcel.writeInt(shotsOpponent)
        parcel.writeInt(shotsToBlock)
        parcel.writeInt(shotsOutside)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Match> {
        override fun createFromParcel(parcel: Parcel): Match {
            return Match(parcel)
        }

        override fun newArray(size: Int): Array<Match?> {
            return arrayOfNulls(size)
        }
    }
}

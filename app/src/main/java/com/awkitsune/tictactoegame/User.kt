package com.awkitsune.tictactoegame

import android.graphics.*
import android.os.Parcel
import android.os.Parcelable

class User(): Parcelable {
    var username = ""
    var gamesAmount = 0
    var gamesWon = 0
    var gamesLost = 0
    var avatarEncoded: String = ""

    constructor(parcel: Parcel) : this() {
        username = parcel.readString().toString()
        gamesAmount = parcel.readInt()
        gamesWon = parcel.readInt()
        gamesLost = parcel.readInt()
        avatarEncoded = parcel.readString().toString()
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(username)
        parcel.writeInt(gamesAmount)
        parcel.writeInt(gamesWon)
        parcel.writeInt(gamesLost)
        parcel.writeString(avatarEncoded)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<User> {
        override fun createFromParcel(parcel: Parcel): User {
            return User(parcel)
        }

        override fun newArray(size: Int): Array<User?> {
            return arrayOfNulls(size)
        }
    }
}
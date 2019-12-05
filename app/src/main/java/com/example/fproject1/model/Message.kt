package com.example.fproject1.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
class Message(val message : String) : Parcelable {
    constructor() : this("")
}
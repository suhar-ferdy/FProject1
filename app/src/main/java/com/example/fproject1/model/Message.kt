package com.example.fproject1.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
class Message(var fromID : String, var toID : String ,val friendName : String,val text : String) :
    Parcelable {
    constructor() : this("","","","")
}
package com.example.fproject1.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
class LiveEditorRoom(val roomID:String , val text : String) : Parcelable{
    constructor() : this("","")
}
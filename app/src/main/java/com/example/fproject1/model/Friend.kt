package com.example.fproject1.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
class Friend(val friendUid : String, val userId : String ) : Parcelable{
    constructor() : this("","")
}
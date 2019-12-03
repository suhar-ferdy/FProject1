package com.example.fproject1.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
class Account(var uid : String, val fname : String, val lname : String, val email : String, val pass : String, val profileImg : String, val userId : String) :
    Parcelable {
    constructor() : this("","","","","","" ,"")
}
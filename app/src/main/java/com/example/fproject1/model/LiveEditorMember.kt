package com.example.fproject1.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
class LiveEditorMember (val uid : String , val edit_permission : String , val job : String): Parcelable{
    constructor() : this("","","")
}
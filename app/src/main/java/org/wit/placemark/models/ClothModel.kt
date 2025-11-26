package org.wit.placemark.models


import android.net.Uri
import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class ClothModel(var id: Long = 0,
                      var title: String? = "",
                      var description: String? = "",
                      var image: Uri = Uri.EMPTY) : Parcelable
package com.it.training.home.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize


@Parcelize
data class UserResponseModel(
    var email: String? = "",
    var userList: Map<String, UserListResponseModel> = mapOf()
) : Parcelable

@Parcelize
data class UserListResponseModel(
    var idUser: String? = "",
    var name: String? = "",
    var weight: String? = "",
    var height: String? = ""
) : Parcelable
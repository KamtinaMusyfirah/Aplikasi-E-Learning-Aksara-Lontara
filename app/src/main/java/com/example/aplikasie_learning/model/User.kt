package com.example.aplikasie_learning.model

import android.os.Parcelable
import com.google.firebase.database.PropertyName
import kotlinx.parcelize.Parcelize

@Parcelize
data class User (
    @get:PropertyName("avatar_user")
    @set:PropertyName("avatar_user")
    var  avatarUser: String? = null,

    @get:PropertyName("email_user")
    @set:PropertyName("email_user")
    var  emailUser: String? = null,

    @get:PropertyName("name_user")
    @set:PropertyName("name_user")
    var  nameUser: String? = null,

    @get:PropertyName("uid_user")
    @set:PropertyName("uid_user")
    var  uidUser: String? = null,
) : Parcelable
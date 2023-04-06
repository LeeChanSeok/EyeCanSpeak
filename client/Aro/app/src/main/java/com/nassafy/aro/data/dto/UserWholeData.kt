package com.nassafy.aro.data.dto

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class UserWholeData @JvmOverloads constructor(
    var nickname: String? = "",
    var email: String? = "",
    var alarmOption: Boolean? = false,
    var auroraDisplayOption: Boolean? = true,
    var cloudDisplayOption: Boolean? = true,
    var auroraServiceEnabled: Boolean? = false,
    var meteorShowerServiceEnabled: Boolean = false,
) : Parcelable

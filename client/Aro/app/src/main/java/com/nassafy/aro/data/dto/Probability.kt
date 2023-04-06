package com.nassafy.aro.data.dto

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import java.time.LocalDateTime

@Parcelize
data class Probability @JvmOverloads constructor(
    var time : LocalDateTime
) : Parcelable

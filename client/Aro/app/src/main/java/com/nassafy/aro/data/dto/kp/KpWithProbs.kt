package com.nassafy.aro.data.dto.kp

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class KpWithProbs(
    val kps: List<Double> = arrayListOf(),
    val probs: List<Probability> = arrayListOf()
) : Parcelable
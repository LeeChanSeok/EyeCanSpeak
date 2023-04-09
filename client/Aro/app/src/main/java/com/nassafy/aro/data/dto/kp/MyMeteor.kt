package com.nassafy.aro.data.dto.kp

import com.nassafy.aro.data.dto.MeteorShower


data class MyMeteor @JvmOverloads constructor(
    var nation: String? = "",
    var meteorList: List<MeteorShower>? = ArrayList()
) : java.io.Serializable
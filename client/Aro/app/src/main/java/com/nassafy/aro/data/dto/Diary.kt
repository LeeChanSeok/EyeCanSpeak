package com.nassafy.aro.data.dto

import java.util.*

data class Diary @JvmOverloads constructor(
    var images: LinkedList<String> = LinkedList(),
    var memo: String = "",
    var nation: String? = "",
    var attractionName: String? = "",
    var description: String? = "",
) : java.io.Serializable
package com.nassafy.aro.data.dto

data class User @JvmOverloads constructor(
//    var email :String
//    password: String
//nickname : String
//auroraService : Boolean
//meteorService : Boolean
//auroraPlaces : List<Integer>
//meteorPlaces : List<Integer>
//    alarm
//    auroraDisplay
    var service: List<Boolean> = emptyList(),
) {
}
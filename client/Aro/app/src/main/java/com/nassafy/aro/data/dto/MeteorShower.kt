package com.nassafy.aro.data.dto

data class MeteorShower @JvmOverloads constructor(
    var meteorName: String? = "",
    var meteorOriginalName: String? = "",
    var predictDate: String? = "",
    var constellationImage: String? = "",
    var detailImage: String? = "",
)

//{
//    "nation": "감비아",
//    "meteorList": [
//    {
//        "meteorName": "물병자리 에타 유성우",
//        "meteorOriginalName": "η-Aquariids",
//        "predictDate": "2023-04-19",
//        "constellationImage": "https://nassafy.s3.ap-northeast-2.amazonaws.com/별자리/물병자리/icon.png",
//        "detailImage": "https://nassafy.s3.ap-northeast-2.amazonaws.com/별자리/물병자리/image.jpg"
//    },                                                                     ]                                                                  }
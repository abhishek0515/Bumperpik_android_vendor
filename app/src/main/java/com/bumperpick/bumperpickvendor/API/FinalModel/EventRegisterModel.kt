package com.bumperpick.bumperpickvendor.API.FinalModel

data class EventRegisterModel(
    val code: Int,
    val `data`: DataXXXXXXXX,
    val message: String
)
data class EventModel(
    val code: Int,
    val `data`: List<DataXXXXXXXX>,
    val message: String
)
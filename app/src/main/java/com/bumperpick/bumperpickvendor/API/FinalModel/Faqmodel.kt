package com.bumperpick.bumperpickvendor.API.FinalModel

data class Faqmodel(
    val code: Int,
    val `data`: List<faq>,
    val message: String
)
data class faq(
    val answer: String,
    val id: Int,
    val is_customer: Int,
    val is_vendor: Int,
    val question: String
)
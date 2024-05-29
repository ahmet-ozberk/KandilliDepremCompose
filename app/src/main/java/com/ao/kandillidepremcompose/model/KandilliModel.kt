package com.ao.kandillidepremcompose.model

import java.io.Serializable

data class KandilliModel(
    val id: Int,
    val tarih: String? = null,
    val saat: String? = null,
    val enlem: String? = null,
    val boylam: String? = null,
    val derinlik: String? = null,
    val buyukluk: String? = null,
    val yer: String? = null
) : Serializable

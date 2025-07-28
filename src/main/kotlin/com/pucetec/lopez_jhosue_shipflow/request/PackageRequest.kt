package com.pucetec.lopez_jhosue_shipflow.request

import com.fasterxml.jackson.annotation.JsonProperty
import com.pucetec.lopez_jhosue_shipflow.models.entities.Type

data class PackageRequest (
    val type: String,
    val weight: Float,
    val description: String,

    @JsonProperty("city_from")
    val cityFrom: String,

    @JsonProperty("city_to")
    val cityTo: String
)
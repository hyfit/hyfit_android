package com.example.hyfit_android.goal

import com.google.gson.annotations.SerializedName

data class GeocodeResponse(
    @SerializedName("results") val results: List<Result>,
    @SerializedName("status") val status: String
)
data class Result(
    @SerializedName("formatted_address") val formattedAddress: String,
    @SerializedName("geometry") val geometry: Geometry,
    @SerializedName("name") val name: String

)

data class Geometry(
    @SerializedName("location") val location: Location
)

data class Location(
    @SerializedName("lat") val latitude: Double,
    @SerializedName("lng") val longitude: Double
)
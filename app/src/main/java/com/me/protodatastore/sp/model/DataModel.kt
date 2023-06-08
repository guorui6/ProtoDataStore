package com.me.protodatastore.sp.model

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class DataModel(val persons: List<Person>)

@JsonClass(generateAdapter = true)
data class Person(
    val address: Address,
    val name: String,
    val age: Int,
    val gender: Int,
    val phone: String,
    val email: String,
    val isMarried: Boolean,
    val isEmployed: Boolean,
    val birthday: Long,
    val height: Float,
    val weight: Double,
    val favoriteColor: Int,
    val hobby: String,
    val favoriteFood: String,
    val occupation: String,
)

@JsonClass(generateAdapter = true)
data class Address(
    val street: String,
    val city: String,
    val state: String,
    val zip: Int
)

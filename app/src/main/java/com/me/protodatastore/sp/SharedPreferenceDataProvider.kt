package com.me.protodatastore.sp

import com.me.protodatastore.sp.model.Address
import com.me.protodatastore.sp.model.DataModel
import com.me.protodatastore.sp.model.Person

// Provide data for testing SharedPreference
object SPDataProvider {
    fun dataForTestSp(size: Int): DataModel {
        val persons = mutableListOf<Person>()
        repeat(size) {
            persons.add(
                Person(
                    Address("street $it", "city $it", "state", it),
                    "name $it",
                    20,
                    1,
                    "123456789",
                    "a$it@gmail.com",
                    true,
                    true,
                    123456789,
                    5.8f,
                    120.0,
                    1,
                    "hobby",
                    "favoriteFood",
                    "occupation",
                )
            )
        }
        return DataModel(persons)
    }
}


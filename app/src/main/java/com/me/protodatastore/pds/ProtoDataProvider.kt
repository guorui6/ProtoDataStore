package com.me.protodatastore.pds

import com.me.models.Address
import com.me.models.DataModel
import com.me.models.Person

// Provide data for testing ProtoDataStore
object ProtoDataProvider {
    fun dataForTestPds(size: Int): DataModel {
        return DataModel.newBuilder().addAllPerson(
            (0 until size).map {
                Person.newBuilder()
                    .setAddress(
                        Address.newBuilder()
                            .setStreet("street $it")
                            .setCity("city $it")
                            .setState("state")
                            .setZip(it)
                            .build()
                    )
                    .setName("name $it")
                    .setAge(20)
                    .setGender(1)
                    .setPhone("123456789")
                    .setEmail("a$it@gmail.com")
                    .setIsMarried(true)
                    .setIsEmployed(true)
                    .setBirthday(123456789)
                    .setHeight(5.8f)
                    .setWeight(120.0f)
                    .setFavoriteColor(1)
                    .setHobby("hobby")
                    .setFavoriteFood("favoriteFood")
                    .setOccupation("occupation")
                    .build()
            }
        ).build()
    }
}
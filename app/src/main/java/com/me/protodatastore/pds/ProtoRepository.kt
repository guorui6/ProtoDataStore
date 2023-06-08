package com.me.protodatastore.pds

import android.util.Log
import androidx.datastore.core.DataStore
import com.me.models.DataModel
import com.me.models.Person
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.first
import java.io.IOException

private const val TAG = "ProtoRepository"

class ProtoRepository(private val dataModelStore: DataStore<DataModel>) {

    val dataModelFlow = dataModelStore.data
        .catch { e ->
            if (e is IOException) {
                emit(DataModel.getDefaultInstance())
                Log.e(TAG, "Error reading: ", e)
            } else {
                throw e
            }
        }

    suspend fun saveData(dataModel: DataModel) {
        dataModelStore.updateData {
            dataModel.toBuilder().clearPerson().addAllPerson(dataModel.personList).build()
        }
    }

    suspend fun addPerson(personName: String) {
        dataModelStore.updateData { currentDataModel ->
            val person = Person.newBuilder().setName(personName).build()
            currentDataModel.toBuilder().addPerson(person).build()
        }
    }

    suspend fun delPerson(name: String) {
        dataModelStore.updateData { currentDataModel ->
            val person = currentDataModel.personList.firstOrNull { it.name == name }
            if (person != null) {
                val index = currentDataModel.personList.indexOf(person)
                currentDataModel.toBuilder().removePerson(index).build()
            } else {
                currentDataModel
            }
        }
    }

    suspend fun updatePersonByName(name: String) {
        dataModelStore.updateData { currentDataModel ->
            val person = currentDataModel.personList.firstOrNull { it.name == name }
            if (person != null) {
                val index = currentDataModel.personList.indexOf(person)
                val updatedPerson = person.toBuilder().setName(person.name.plus("_updated")).build()
                currentDataModel.toBuilder().setPerson(index, updatedPerson).build()
            } else {
                currentDataModel
            }
        }
    }

    suspend fun fetchInitData(): DataModel = dataModelStore.data.first()
}
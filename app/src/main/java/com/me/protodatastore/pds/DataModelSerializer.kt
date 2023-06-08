package com.me.protodatastore.pds

import androidx.datastore.core.CorruptionException
import androidx.datastore.core.Serializer
import com.google.protobuf.InvalidProtocolBufferException
import com.me.models.DataModel
import java.io.InputStream
import java.io.OutputStream

object DataModelSerializer : Serializer<DataModel> {
    override val defaultValue: DataModel
        get() = DataModel.getDefaultInstance()

    override suspend fun readFrom(input: InputStream): DataModel {
        try {
            return DataModel.parseFrom(input)
        } catch (e: InvalidProtocolBufferException) {
            throw CorruptionException("Cannot read proto.", e)
        }
    }

    override suspend fun writeTo(t: DataModel, output: OutputStream) {
        t.writeTo(output)
    }
}
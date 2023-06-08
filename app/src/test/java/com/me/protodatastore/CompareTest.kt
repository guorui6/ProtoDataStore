package com.me.protodatastore

import com.google.gson.Gson
import com.me.models.DataModel
import com.me.protodatastore.pds.ProtoDataProvider
import com.me.protodatastore.sp.SPDataProvider
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import org.junit.Test
import kotlin.math.pow

class CompareTest {

    @Test
    fun `compare with small size data`() {
        testWithSize(10)
    }

    @Test
    fun `compare with medium size data`() {
        testWithSize(100)
    }

    @Test
    fun `compare with large size data`() {
        testWithSize(1000)
    }

    @Test
    fun `compare with xLarge size data`() {
        testWithSize(10000)
    }

    @Test
    fun `summary test result`() {
        val array = arrayOf("Gson", "Moshi", "Proto")
        for (i in array.indices) {
            val mapResult = mutableMapOf<Int, MutableList<Result>>()
            val repeatTimes = 20
            for (base in 1..4) {
                val size = 10.0.pow(base.toDouble()).toInt()
                val list = mutableListOf<Result>()
                repeat(repeatTimes) {
                    when(i) {
                        0 -> list.add(testGson(size, false))
                        1 -> list.add(testMoshi(size, false))
                        2 -> list.add(testProto(size, false))
                    }
                }
                mapResult[size] = list
            }
            mapResult.forEach { (key, results) ->
                val resultSize = results.map { it.size }.average()
                val serializeTime = results.map { it.serializeTime }.average()
                val deserializeTime = results.map { it.deserializeTime }.average()
                println("**********  $key  **********")
                println("${array[i]} - serializeSize:     $resultSize KB")
                println("${array[i]} - serializeTime:     $serializeTime ms")
                println("${array[i]} - deserializeTime:   $deserializeTime ms")
            }
        }
    }

    private fun testWithSize(size: Int) {
        println("**************************  Size: $size  *************************")
        testGson(size)
        testMoshi(size)
        testProto(size)
    }

    private fun testProto(size: Int, print: Boolean = true): Result {
        val protoData = ProtoDataProvider.dataForTestPds(size)
        var start = System.currentTimeMillis()
        val bytes = protoData.toByteArray()
        var end = System.currentTimeMillis()
        val resultSize = bytes.size / 1024f
        val serializeTime = end - start
        if (print) {
            println("Proto - serializeSize:    $resultSize KB")
            println("Proto - serializeTime:    $serializeTime ms")
        }
        start = System.currentTimeMillis()
        val deserializeModel = DataModel.parseFrom(bytes)
        end = System.currentTimeMillis()
        val deserializeTime = end - start
        if (print) println("Proto - deserializeTime:  $deserializeTime ms")
        assert(deserializeModel.personList.size == size)
        return Result(resultSize, serializeTime, deserializeTime)
    }

    private fun testGson(size: Int, print: Boolean = true): Result {
        val gson = Gson()
        val model = SPDataProvider.dataForTestSp(size)
        var start = System.currentTimeMillis()
        val json = gson.toJson(model)
        var end = System.currentTimeMillis()
        val bytes = json.toByteArray()
        val resultSize = bytes.size / 1024f
        val serializeTime = end - start
        if (print) {
            println("Gson - serializeSize:     $resultSize KB")
            println("Gson - serializeTime:     $serializeTime ms")
        }
        start = System.currentTimeMillis()
        val deserializeModel =
            gson.fromJson(json, com.me.protodatastore.sp.model.DataModel::class.java)
        end = System.currentTimeMillis()
        val deserializeTime = end - start
        if (print) println("Gson - deserializeTime:   $deserializeTime ms")
        assert(deserializeModel.persons.size == size)
        return Result(resultSize, serializeTime, deserializeTime)
    }

    private fun testMoshi(size: Int, print: Boolean = true): Result {
        val model = SPDataProvider.dataForTestSp(size)
        val moshi = Moshi.Builder().add(KotlinJsonAdapterFactory()).build()
        val adapter = moshi.adapter(com.me.protodatastore.sp.model.DataModel::class.java)
        var start = System.currentTimeMillis()
        val json = adapter.toJson(model)
        var end = System.currentTimeMillis()
        val bytes = json.toByteArray()
        val resultSize = bytes.size / 1024f
        val serializeTime = end - start
        if (print) {
            println("Moshi - serializeSize:     $resultSize KB")
            println("Moshi - serializeTime:     $serializeTime ms")
        }
        start = System.currentTimeMillis()
        val deserializeModel = adapter.fromJson(json)
        end = System.currentTimeMillis()
        val deserializeTime = end - start
        if(print) println("Moshi - deserializeTime:   $deserializeTime ms")
        assert(deserializeModel?.persons?.size == size)
        return Result(resultSize, serializeTime, deserializeTime)
    }
}

data class Result(val size: Float, val serializeTime: Long, val deserializeTime: Long)
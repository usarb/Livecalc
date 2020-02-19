package com.livecalc.v1.Components

import com.livecalc.v1.Storage.OutputData

interface IResultGenerator {

    val resultGenerator: ResultGenerator

    fun prepareResult(): OutputData

    fun toJSON(): String{
        return resultGenerator.toJSON(prepareResult())
    }

    fun toDataClass(): OutputData {
        return resultGenerator.toDataClass(prepareResult())
    }
}
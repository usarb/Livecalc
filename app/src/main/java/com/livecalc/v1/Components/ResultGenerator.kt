package com.livecalc.v1.Components

import com.google.gson.Gson
import com.livecalc.v1.Storage.OutputData

class ResultGenerator {
    fun toJSON(outputData: OutputData):String{
        return Gson().toJson(outputData)
    }

    fun toDataClass(outputData: OutputData): OutputData {
        return outputData
    }
}
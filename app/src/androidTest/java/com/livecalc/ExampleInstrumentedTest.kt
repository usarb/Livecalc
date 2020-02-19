package com.livecalc

import android.util.Log
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.livecalc.v1.Calculator
import com.livecalc.v1.Components.InputAdaptor
import com.livecalc.v1.Storage.ProductSelected
import org.junit.Assert

import org.junit.Test
import org.junit.runner.RunWith

import org.junit.Assert.*

/**
 * Instrumented test, which will execute on an Android device.
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
@RunWith(AndroidJUnit4::class)
class ExampleInstrumentedTest {
    @Test
    fun useAppContext() {
        // Context of the app under test.
        val appContext = InstrumentationRegistry.getInstrumentation().targetContext
        assertEquals("com.livecalc", appContext.packageName)
    }

    @Test
    fun onProductQuantitySelect() {
        // Context of the app under test.
        val appContext = InstrumentationRegistry.getInstrumentation().targetContext
        val text = appContext.resources.openRawResource(R.raw.livecalc_input)
            .bufferedReader().use { it.readText() }

        val inputData = InputAdaptor().fromJSON(text)

        val calculator = Calculator()
        calculator.setAPIData(inputData)
        calculator.setProductsQuantity(hashMapOf(
            1 to ProductSelected(
                quantity = 12
            ),
            2 to ProductSelected(
                quantity = 17
            )
        ))
        calculator.liveCalc()
        //val result = calculator.getResult().discounts
        val result = calculator.getResult()
        Log.d("test", result.toString())
        Assert.assertEquals("com.livecalc", appContext.packageName)
    }
}

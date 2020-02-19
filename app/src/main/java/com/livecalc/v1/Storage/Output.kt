package com.livecalc.v1.Storage

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

enum class ClientType {
    JURIDICAL, PHYSICAL
}

data class Order (
    var clientType: ClientType,
    var products: HashMap <Int, ProductSelected>,
    var amount: Float = 0.0F,
    var amountDiscounted: Float = 0.0F
)

data class Orders (
    var basic: Order,
    var additional: Order? = null,
    var zeroPrice: Order? = null
)


data class WriteOff (
    var official: List <Product>,
    var unofficial: List <Product>
)



data class DiscountOutput(
    var id: Int,
    var sum: Float,
    var products: HashMap<Int, Int>,
    var productsToSelect: HashMap<Int, Int>,
    var points: Int
)
/**
 * Output data
 * @property products[HashMap] - list of products
 * @property products[HashMap] - list of products
 * @property discounts[HashMap] - list of applied discounts
 * @property cash[Cash] - required cash sum
 * @property writeOff[WriteOff] - list of write off documents
 * @property bonusPoints[Int] - bonus points generated by discounts
 * */
data class OutputData (
    var products: HashMap <Int, ProductSelected>,
    var orders: Orders,
    var discounts: HashMap<Int, DiscountOutput>,
    var cash: Cash,
    var writeOff: WriteOff,
    var bonusPoints: Int,
    var requiredCashIn: Cash
)
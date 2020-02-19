package com.livecalc.v1.Storage

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

enum class PaymentMethods{
    CASH, TRANSFER, CARD
}

enum class DiscountApplyToType {
    ORDER, EACH_PRODUCT, EACH_PRODUCT_IN_CONDITION
}

enum class DiscountPriceToApplyType{
    BASIC_PRICE, DISCOUNTED_PRICE
}

enum class DiscountFormula{
    PERCENT, VALUE

}
enum class DiscountResultType {
    AMOUNT, PRODUCTS_SAME, PRODUCTS_OTHER, POINTS
}

enum class ShipmentMethod {
    DIRECT, EXPEDITION, ONLINE
}

data class Product(
    @SerializedName("id")
    @Expose
    var id: Int,
    @SerializedName("only_juridical_sale")
    @Expose
    var onlyJuridicalSale: Boolean = false,
    @SerializedName("available")
    @Expose
    var available: Int,
    @SerializedName("price")
    @Expose
    var price: Float
)

data class ProductSelected(
    var quantity: Int,
    var price: Float = 0.0F,
    var amount: Float = 0.0F,
    var priceDiscounted: Float = 0.0F,
    var amountDiscounted: Float = 0.0F
)

data class Cash (
    var juridical: Float,
    var physical: Float
)
package com.livecalc.v1.Components

import android.util.Log
import com.livecalc.v1.Storage.*
import com.livecalc.v1.Storage.Order as OrderItem
import com.livecalc.v1.Components.Utils as Utils

/** Order calculation component
 * @property orders - list of orders (basic, additional, zero_price)
 * */
class Order(val storage: Storage) : IComponent {

    private var orders: Orders =
        Orders(
            OrderItem(
                ClientType.JURIDICAL,
                hashMapOf()
            )
        )

    fun getOrders(): Orders {
        return orders
    }

    fun calculateProductAmount(
        products: HashMap<Int, Product>,
        productsSelected: HashMap<Int, ProductSelected>
    ): HashMap<Int, ProductSelected> {
        if (productsSelected.isEmpty())
            return productsSelected

        val productsResult = hashMapOf<Int, ProductSelected>()
        for ((productID, product) in productsSelected) {
            product.price = Utils().roundUp(products[productID]!!.price, 2)
            product.priceDiscounted = product.price
            product.amount = Utils().roundUp(products[productID]!!.price * product.quantity, 2)
            product.amountDiscounted = product.amount
            productsResult[productID] = product
        }
        return productsResult
    }

    fun calculateRequiredCashIn() {
        storage.data.output.requiredCashIn.juridical =
            Utils().roundUp(storage.data.output.orders.additional!!.amountDiscounted / 100 * storage.data.input.orderSettings.requiredCashIn, 2)
        storage.data.output.requiredCashIn.physical =
            Utils().roundUp(storage.data.output.orders.basic.amountDiscounted / 100 * storage.data.input.orderSettings.requiredCashIn, 2)
    }

    fun split(zeroOrderProductsGenerated: HashMap<Int, Int>) {
        val basicProducts = hashMapOf<Int, ProductSelected>()
        val additionalProducts = hashMapOf<Int, ProductSelected>()
        val zeroOrderProducts = hashMapOf<Int, ProductSelected>()

        var orderBasicAmount = 0.0F
        var orderBasicAmountDiscounted = 0.0F
        var orderAdditionalAmount = 0.0F
        var orderAdditionalAmountDiscounted = 0.0F

        for ((productID, product) in storage.data.output.products) {
            val basicQuantity =
                Utils().roundDown(
                    product.quantity * (100 - storage.data.input.orderSettings.splitRatioJuridical) / 100,
                    0
                ).toInt()
            val additionalQuantity = product.quantity - basicQuantity

            if (zeroOrderProductsGenerated.containsKey(productID)) {
                zeroOrderProducts[productID] =
                    ProductSelected(
                        quantity = zeroOrderProductsGenerated[productID]!!,
                        price = 0.0F,
                        amount = 0.0F,
                        priceDiscounted = 0.0F,
                        amountDiscounted = 0.0F
                    )
            }

            basicProducts[productID] = ProductSelected(
                quantity = basicQuantity,
                price = product.price,
                amount = product.price * basicQuantity,
                priceDiscounted = product.price,
                amountDiscounted = product.price * basicQuantity
            )

            additionalProducts[productID] = ProductSelected(
                quantity = additionalQuantity,
                price = product.price,
                amount = product.price * additionalQuantity,
                priceDiscounted = product.price,
                amountDiscounted = product.price * additionalQuantity
            )

            orderBasicAmount += product.price * basicQuantity
            orderBasicAmountDiscounted += product.price * basicQuantity
            orderAdditionalAmount += product.price * additionalQuantity
            orderAdditionalAmountDiscounted += product.price * additionalQuantity

        }

        orders = Orders(
            basic = OrderItem(
                ClientType.PHYSICAL,
                basicProducts,
                amount = Utils().roundUp(orderBasicAmount, 2),
                amountDiscounted = Utils().roundUp(orderBasicAmountDiscounted, 2)
            ),
            additional = OrderItem(
                ClientType.JURIDICAL,
                additionalProducts,
                amount = Utils().roundUp(orderAdditionalAmount,2),
                amountDiscounted = Utils().roundUp(orderAdditionalAmountDiscounted, 2)
            ),
            zeroPrice = OrderItem(
                ClientType.JURIDICAL,
                zeroOrderProducts,
                amount = 0.0F
            )
        )

        storage.data.output.orders = orders
        Log.d("livecalc", "ORDERS:" + orders.toString())
    }
}
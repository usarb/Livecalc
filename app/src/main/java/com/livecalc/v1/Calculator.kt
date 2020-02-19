package com.livecalc.v1
import android.util.Log
import com.livecalc.v1.Components.IResultGenerator
import com.livecalc.v1.Storage.Storage
import com.livecalc.v1.Storage.*
import java.lang.Exception
import com.livecalc.v1.Components.Order as OrderComponent
import com.livecalc.v1.Components.Discount as DiscountComponent
import com.livecalc.v1.Components.ResultGenerator as ResultGenerator

/**
 * This class Calculate product amount, apply discounts and split order into (juridical|physical)
 * @constructor get InputData
 */
class Calculator : IResultGenerator {

    override var resultGenerator: ResultGenerator = ResultGenerator()

    private var storage: Storage = Storage()

    private val components: CalculatorComponents =
        CalculatorComponents(
            order = OrderComponent(storage),
            discount = DiscountComponent(storage)
        )

    //Todo fix this
    /**
     * Run live calculation
     */
    fun liveCalc() {

        try {
            calculateProductAmount()
            applyDiscounts()
            splitOrder()
            components.order.calculateRequiredCashIn()
        } catch (E: Exception) {
            Log.d("livecalc", E.message.toString())
        }

    }


    fun setAPIData(inputData: InputData){
        storage.setAPIData(inputData)
        components.discount.init(storage.getInputData().discounts)
    }

    /*fun setSalesHistory(salesHistory: SalesHistory){
        storage.data.input.salesHistory = salesHistory
    }*/

    fun setCashIn(cash:CashIn){
        storage.setCashIn(cash)
    }


    fun setProductsQuantity(productSelected: HashMap<Int, ProductSelected>){
        storage.setProductsQuantity(productSelected)
    }

    /**
     * Calculate product amount
     */
    private fun calculateProductAmount() {
        storage.setProductsQuantity(
            components.order.calculateProductAmount(storage.getInputData().products, storage.getOutputData().products)
        )
        if (storage.getInputData().products.isEmpty()) {
            throw Exception("Null amount")
        }
    }

    /**
     * Apply discounts
     * @return [Calculator] instance
     */
    private fun applyDiscounts(): Calculator {
        components.discount.createMap(storage.getInputData())
        components.discount.apply(storage.getInputData().discounts, storage.getInputData().products)
        return this
    }

    /**
     * Split order
     * @return [Calculator] instance
     */
    private fun splitOrder(): Calculator {
        components.order.split(components.discount.getZeroOrderProducts())
        return this
    }

    fun filterDiscountsOnViewEnter(){
        components.discount.filterOnViewEnter()
    }

    /**
     * Prepare results for return
     * @return result [OutputData] Data Class
     */
    override fun prepareResult(): OutputData {

        val cash = Cash(
            juridical = 0.0F,
            physical = 1542.34F
        )

        val writeOff = WriteOff(
            official = listOf(),
            unofficial = listOf()
        )
        storage.setOutputOrders(components.order.getOrders())
        storage.setOutputDiscounts(components.discount.getAppliedDiscounts())
        storage.setOutputCash(cash)
        storage.setWriteOff(writeOff)
        storage.setBonusPoints(components.discount.getBonusPoints())

        return OutputData(
            storage.getOutputData().products,
            components.order.getOrders(),
            components.discount.getAppliedDiscounts(),
            cash,
            writeOff,
            components.discount.getBonusPoints(),
            storage.getOutputData().requiredCashIn
        )
    }

    fun getResult():OutputData{
        return storage.getOutputData()
    }

    data class CalculatorComponents(
        var order: OrderComponent,
        var discount: DiscountComponent
    )
}
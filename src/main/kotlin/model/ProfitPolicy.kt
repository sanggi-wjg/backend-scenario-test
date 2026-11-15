package com.raynor.demo.model

import java.math.BigDecimal

interface ProfitPolicy {
    val id: Long
    val lowerBound: BigDecimal
    val upperBound: BigDecimal?
    val feeRate: BigDecimal

    fun calculate(paymentAmount: BigDecimal): ProfitResult
}

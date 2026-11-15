package com.raynor.demo

import com.raynor.demo.model.ProfitCalculatorResult
import java.math.BigDecimal

interface ProfitCalculator {
    fun calculateProfitAmount(paymentAmount: BigDecimal): ProfitCalculatorResult
}

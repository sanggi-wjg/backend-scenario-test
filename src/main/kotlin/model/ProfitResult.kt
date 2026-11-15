package com.raynor.demo.model

import java.math.BigDecimal

data class ProfitResult(
    val userProfitAmount: BigDecimal,
    val feeAmount: BigDecimal,
) {
    companion object {
        fun zero() = ProfitResult(BigDecimal.ZERO, BigDecimal.ZERO)
    }
}

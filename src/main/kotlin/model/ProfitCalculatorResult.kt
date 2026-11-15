package com.raynor.demo.model

import java.math.BigDecimal

data class ProfitCalculatorResult(
    val results: List<Result>,
) {
    data class Result(
        val policy: ProfitPolicy,
        val calculateResult: ProfitResult,
    )

    val totalUserProfitAmount: BigDecimal = results.sumOf { it.calculateResult.userProfitAmount }
    val totalFeeAmount: BigDecimal = results.sumOf { it.calculateResult.feeAmount }
}

package com.raynor.demo.model

import java.math.BigDecimal
import java.math.RoundingMode
import java.time.Instant

data class SecondProfitPolicy(
    override val id: Long,
    override val lowerBound: BigDecimal,
    override val upperBound: BigDecimal?,
    override val feeRate: BigDecimal,
    val startAt: Instant?,
    val endAt: Instant?,
    val categoryIds: Set<Long>,
) : ProfitPolicy {

    override fun calculate(paymentAmount: BigDecimal): ProfitResult {
        if (paymentAmount < lowerBound) return ProfitResult.zero()

        val boundedAmount = upperBound?.let { minOf(it, paymentAmount) } ?: paymentAmount
        val applicableAmount = boundedAmount - lowerBound
        // 원 단위 미만 절사
        val feeAmount = (applicableAmount * feeRate).setScale(0, RoundingMode.DOWN)

        return ProfitResult(
            userProfitAmount = applicableAmount - feeAmount,
            feeAmount = feeAmount,
        )
    }
}

package com.raynor.demo.repository

import com.raynor.demo.model.FirstProfitPolicy
import com.raynor.demo.model.ProfitPolicy
import java.math.BigDecimal
import java.time.Instant

class FirstProfitPolicyRepository : ProfitPolicyRepository {

    private val policiesInDB = listOf(
        FirstProfitPolicy(
            id = 1,
            lowerBound = BigDecimal(0),
            upperBound = BigDecimal(500_000),
            feeRate = BigDecimal("0.20"),
            startAt = null,
            endAt = Instant.parse("2026-10-31T15:00:00Z"),
        ),
        FirstProfitPolicy(
            id = 2,
            lowerBound = BigDecimal(500_000),
            upperBound = BigDecimal(1_000_000),
            feeRate = BigDecimal("0.1"),
            startAt = null,
            endAt = null,
        ),
        FirstProfitPolicy(
            id = 3,
            lowerBound = BigDecimal(1_000_000),
            upperBound = null,
            feeRate = BigDecimal("0.05"),
            startAt = null,
            endAt = null,
        ),
        // 2026-11-01 부터는 0.18 수수료
        FirstProfitPolicy(
            id = 4,
            lowerBound = BigDecimal(0),
            upperBound = BigDecimal(500_000),
            feeRate = BigDecimal("0.18"),
            startAt = Instant.parse("2026-10-31T15:00:00Z"),
            endAt = null,
        ),
    )

    fun findAvailablePolicies(baseAt: Instant): List<ProfitPolicy> {
        return policiesInDB
            .filter { (it.startAt == null || it.startAt <= baseAt) && (it.endAt == null || it.endAt > baseAt) }
    }
}

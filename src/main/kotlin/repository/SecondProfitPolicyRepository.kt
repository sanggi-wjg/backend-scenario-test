package com.raynor.demo.repository

import com.raynor.demo.model.ProfitPolicy
import com.raynor.demo.model.SecondProfitPolicy
import java.math.BigDecimal
import java.time.Instant

class SecondProfitPolicyRepository : ProfitPolicyRepository {

    private val policiesInDB = listOf(
        SecondProfitPolicy(
            id = 1,
            lowerBound = BigDecimal(0),
            upperBound = BigDecimal(500_000),
            feeRate = BigDecimal("0.20"),
            startAt = null,
            endAt = Instant.parse("2026-10-31T15:00:00Z"),
            categoryIds = emptySet(),
        ),
        SecondProfitPolicy(
            id = 2,
            lowerBound = BigDecimal(500_000),
            upperBound = BigDecimal(1_000_000),
            feeRate = BigDecimal("0.1"),
            startAt = null,
            endAt = null,
            categoryIds = emptySet(),
        ),
        SecondProfitPolicy(
            id = 3,
            lowerBound = BigDecimal(1_000_000),
            upperBound = null,
            feeRate = BigDecimal("0.05"),
            startAt = null,
            endAt = null,
            categoryIds = emptySet(),
        ),
        SecondProfitPolicy(
            id = 4,
            lowerBound = BigDecimal(0),
            upperBound = BigDecimal(500_000),
            feeRate = BigDecimal("0.18"),
            startAt = Instant.parse("2026-10-31T15:00:00Z"),
            endAt = null,
            categoryIds = emptySet(),
        ),
        SecondProfitPolicy(
            id = 5,
            lowerBound = BigDecimal(0),
            upperBound = null,
            feeRate = BigDecimal("0.10"),
            startAt = null,
            endAt = null,
            categoryIds = setOf(999L),
        ),
    )

    fun findAvailablePolicies(
        baseAt: Instant,
        categoryId: Long,
    ): List<ProfitPolicy> {
        val policies = policiesInDB
            .filter { (it.startAt == null || it.startAt <= baseAt) && (it.endAt == null || it.endAt > baseAt) }

        val commonPolicies = policies.filter { it.categoryIds.isEmpty() }
        val categoryPolicies = policies.filter { categoryId in it.categoryIds }
        // 카테고리 전용 정책이 있다면 카테고리 정책 / 없다면 일반 정책
        return categoryPolicies.ifEmpty { commonPolicies }
    }
}

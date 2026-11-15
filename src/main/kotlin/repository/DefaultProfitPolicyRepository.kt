package com.raynor.demo.repository

import com.raynor.demo.model.DefaultProfitPolicy
import com.raynor.demo.model.ProfitPolicy
import java.math.BigDecimal

class DefaultProfitPolicyRepository : ProfitPolicyRepository {

    private val policiesInDB = listOf(
        DefaultProfitPolicy(
            id = 1,
            lowerBound = BigDecimal(0),
            upperBound = BigDecimal(500_000),
            feeRate = BigDecimal("0.20"),
        ),
        DefaultProfitPolicy(
            id = 2,
            lowerBound = BigDecimal(500_000),
            upperBound = BigDecimal(1_000_000),
            feeRate = BigDecimal("0.1"),
        ),
        DefaultProfitPolicy(
            id = 3,
            lowerBound = BigDecimal(1_000_000),
            upperBound = null,
            feeRate = BigDecimal("0.05"),
        ),
    )

    fun findAvailablePolicies(): List<ProfitPolicy> {
        return policiesInDB
    }
}

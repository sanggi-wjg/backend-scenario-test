package com.raynor.demo

import com.raynor.demo.model.ProfitCalculatorResult
import com.raynor.demo.model.ProfitPolicy
import java.math.BigDecimal

class FirstScenarioProfitCalculator(
    private val profitPolicies: List<ProfitPolicy>,
) : ProfitCalculator {

    override fun calculateProfitAmount(paymentAmount: BigDecimal): ProfitCalculatorResult {
        val results = profitPolicies
            .map { policy ->
                ProfitCalculatorResult.Result(
                    policy = policy,
                    calculateResult = policy.calculate(paymentAmount),
                )
            }
        return ProfitCalculatorResult(results = results)
    }
}

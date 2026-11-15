import com.raynor.demo.SecondScenarioProfitCalculator
import com.raynor.demo.model.ProfitCalculatorResult
import com.raynor.demo.model.ProfitResult
import com.raynor.demo.repository.SecondProfitPolicyRepository
import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.collections.shouldHaveSize
import io.kotest.matchers.shouldBe
import io.mockk.clearAllMocks
import io.mockk.every
import io.mockk.mockkStatic
import io.mockk.unmockkAll
import java.math.BigDecimal
import java.time.Instant

class SecondScenarioProfitCalculatorTest :
    FunSpec(
        {
            val october = Instant.parse("2026-10-10T00:00:00Z")
            val repository = SecondProfitPolicyRepository()

            beforeTest {
                mockkStatic(Instant::class)
                every { Instant.now() } returns october
            }

            afterEach {
                unmockkAll()
                clearAllMocks()
            }

            test("카테고리 999는 고정 10%으로 계산 되어야 한다. 거래 금액: 2_000_000") {
                // given
                val categoryId = 999L
                val now = Instant.now()
                val paymentAmount = BigDecimal(2_000_000)

                val profitPolicies = repository.findAvailablePolicies(now, categoryId)
                val calculator = SecondScenarioProfitCalculator(profitPolicies)

                // when
                val result = calculator.calculateProfitAmount(paymentAmount)

                // then
                profitPolicies shouldHaveSize 1

                result.totalUserProfitAmount shouldBe BigDecimal(1_800_000)
                result.totalFeeAmount shouldBe BigDecimal(200_000)
                result shouldBe ProfitCalculatorResult(
                    results = listOf(
                        ProfitCalculatorResult.Result(
                            policy = profitPolicies.first(),
                            calculateResult = ProfitResult(
                                userProfitAmount = BigDecimal(1_800_000),
                                feeAmount = BigDecimal(200_000),
                            ),
                        ),
                    ),
                )
            }

            test("카테고리 지정 수수료가 없다면 기존 정책으로 계산 되어야 한다. 거래 금액: 2_000_000") {
                // given
                val categoryId = 7L
                val now = Instant.now()
                val paymentAmount = BigDecimal(2_000_000)

                val policies = repository.findAvailablePolicies(now, categoryId)
                val calculator = SecondScenarioProfitCalculator(policies)

                // when
                val result = calculator.calculateProfitAmount(paymentAmount)

                // then
                val firstPolicy = policies.first { it.id == 1L }
                val secondPolicy = policies.first { it.id == 2L }
                val thirdPolicy = policies.first { it.id == 3L }

                result.totalUserProfitAmount shouldBe BigDecimal(1_800_000)
                result.totalFeeAmount shouldBe BigDecimal(200_000)
                result shouldBe ProfitCalculatorResult(
                    results = listOf(
                        ProfitCalculatorResult.Result(
                            policy = firstPolicy,
                            calculateResult = ProfitResult(
                                userProfitAmount = BigDecimal(400_000),
                                feeAmount = BigDecimal(100_000),
                            ),
                        ),
                        ProfitCalculatorResult.Result(
                            policy = secondPolicy,
                            calculateResult = ProfitResult(
                                userProfitAmount = BigDecimal(450_000),
                                feeAmount = BigDecimal(50_000),
                            ),
                        ),
                        ProfitCalculatorResult.Result(
                            policy = thirdPolicy,
                            calculateResult = ProfitResult(
                                userProfitAmount = BigDecimal(950_000),
                                feeAmount = BigDecimal(50_000),
                            ),
                        ),
                    ),
                )
            }
        },
    )

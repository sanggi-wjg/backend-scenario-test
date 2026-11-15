import com.raynor.demo.FirstScenarioProfitCalculator
import com.raynor.demo.model.ProfitCalculatorResult
import com.raynor.demo.model.ProfitResult
import com.raynor.demo.repository.FirstProfitPolicyRepository
import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.shouldBe
import io.mockk.clearAllMocks
import io.mockk.every
import io.mockk.mockkStatic
import io.mockk.unmockkAll
import java.math.BigDecimal
import java.time.Instant

class FirstScenarioProfitCalculatorTest :
    FunSpec(
        {

            val october = Instant.parse("2026-10-10T00:00:00Z")
            val november = Instant.parse("2026-10-31T15:00:00Z")

            val repository = FirstProfitPolicyRepository()

            beforeTest {
                mockkStatic(Instant::class)
            }

            afterEach {
                unmockkAll()
                clearAllMocks()
            }

            test("2026년 10월 시점에는 기존과 동일한 수수료로 계산 되어야 한다. 거래 금액: 2_000_000") {
                // mock
                every { Instant.now() } returns october

                // given
                val paymentAmount = BigDecimal(2_000_000)
                val now = Instant.now()
                val policies = repository.findAvailablePolicies(now)
                val calculator = FirstScenarioProfitCalculator(policies)

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

            test("2026년 11월 시점에는 새로운 수수료로 계산 되어야 한다., 거래 금액: 2_000_000") {
                // mock
                every { Instant.now() } returns november

                // given
                val paymentAmount = BigDecimal(2_000_000)
                val now = Instant.now()
                val policies = repository.findAvailablePolicies(now)
                val calculator = FirstScenarioProfitCalculator(policies)

                // when
                val result = calculator.calculateProfitAmount(paymentAmount)

                // then
                val newPolicy = policies.first { it.id == 4L }
                val secondPolicy = policies.first { it.id == 2L }
                val thirdPolicy = policies.first { it.id == 3L }

                result.totalUserProfitAmount shouldBe BigDecimal(1_810_000)
                result.totalFeeAmount shouldBe BigDecimal(190_000)
                result shouldBe ProfitCalculatorResult(
                    results = listOf(
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
                        ProfitCalculatorResult.Result(
                            policy = newPolicy,
                            calculateResult = ProfitResult(
                                userProfitAmount = BigDecimal(410_000),
                                feeAmount = BigDecimal(90_000),
                            ),
                        ),
                    ),
                )
            }
        },
    )

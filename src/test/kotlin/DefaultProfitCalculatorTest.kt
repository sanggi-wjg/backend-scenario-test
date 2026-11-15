import com.raynor.demo.DefaultProfitCalculator
import com.raynor.demo.model.ProfitCalculatorResult
import com.raynor.demo.model.ProfitResult
import com.raynor.demo.repository.DefaultProfitPolicyRepository
import io.kotest.core.spec.style.FunSpec
import io.kotest.inspectors.forAll
import io.kotest.matchers.shouldBe
import java.math.BigDecimal

class DefaultProfitCalculatorTest :
    FunSpec(
        {
            val repository = DefaultProfitPolicyRepository()
            val policies = repository.findAvailablePolicies()
            val firstPolicy = policies.first { it.id == 1L }
            val secondPolicy = policies.first { it.id == 2L }
            val thirdPolicy = policies.first { it.id == 3L }
            val calculator = DefaultProfitCalculator(policies)

            context("DefaultProfitCalculator 계산 검증 - 해피 케이스") {
                test("거래 금액: 400_000") {
                    // given
                    val paymentAmount = BigDecimal(400_000)

                    // when
                    val result = calculator.calculateProfitAmount(paymentAmount)

                    // then
                    result.totalUserProfitAmount shouldBe BigDecimal(320_000)
                    result.totalFeeAmount shouldBe BigDecimal(80_000)
                    result shouldBe ProfitCalculatorResult(
                        results = listOf(
                            ProfitCalculatorResult.Result(
                                policy = firstPolicy,
                                calculateResult = ProfitResult(
                                    userProfitAmount = BigDecimal(320_000),
                                    feeAmount = BigDecimal(80_000),
                                ),
                            ),
                            ProfitCalculatorResult.Result(
                                policy = secondPolicy,
                                calculateResult = ProfitResult(
                                    userProfitAmount = BigDecimal.ZERO,
                                    feeAmount = BigDecimal.ZERO,
                                ),
                            ),
                            ProfitCalculatorResult.Result(
                                policy = thirdPolicy,
                                calculateResult = ProfitResult(
                                    userProfitAmount = BigDecimal.ZERO,
                                    feeAmount = BigDecimal.ZERO,
                                ),
                            ),
                        ),
                    )
                }

                test("거래 금액: 950_000") {
                    // given
                    val paymentAmount = BigDecimal(950_000)

                    // when
                    val result = calculator.calculateProfitAmount(paymentAmount)

                    // then
                    result.totalUserProfitAmount shouldBe BigDecimal(805_000)
                    result.totalFeeAmount shouldBe BigDecimal(145_000)
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
                                    userProfitAmount = BigDecimal(405_000),
                                    feeAmount = BigDecimal(45_000),
                                ),
                            ),
                            ProfitCalculatorResult.Result(
                                policy = thirdPolicy,
                                calculateResult = ProfitResult(
                                    userProfitAmount = BigDecimal.ZERO,
                                    feeAmount = BigDecimal.ZERO,
                                ),
                            ),
                        ),
                    )
                }

                test("거래 금액: 2_000_000") {
                    // given
                    val paymentAmount = BigDecimal(2_000_000)

                    // when
                    val result = calculator.calculateProfitAmount(paymentAmount)

                    // then
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

                test("거래 금액: 5_000_000") {
                    // given
                    val paymentAmount = BigDecimal(5_000_000)

                    // when
                    val result = calculator.calculateProfitAmount(paymentAmount)

                    // then
                    result.totalUserProfitAmount shouldBe BigDecimal(4_650_000)
                    result.totalFeeAmount shouldBe BigDecimal(350_000)
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
                                    userProfitAmount = BigDecimal(3_800_000),
                                    feeAmount = BigDecimal(200_000),
                                ),
                            ),
                        ),
                    )
                }
            }

            context("DefaultProfitCalculator 계산 검증 - 엣지 케이스") {
                test("거래 금액: 0") {
                    // given
                    val paymentAmount = BigDecimal(0)

                    // when
                    val result = calculator.calculateProfitAmount(paymentAmount)

                    // then
                    result.totalUserProfitAmount shouldBe BigDecimal(0)
                    result.totalFeeAmount shouldBe BigDecimal(0)
                    result shouldBe ProfitCalculatorResult(
                        results = listOf(
                            ProfitCalculatorResult.Result(
                                policy = firstPolicy,
                                calculateResult = ProfitResult(
                                    userProfitAmount = BigDecimal.ZERO,
                                    feeAmount = BigDecimal.ZERO,
                                ),
                            ),
                            ProfitCalculatorResult.Result(
                                policy = secondPolicy,
                                calculateResult = ProfitResult(
                                    userProfitAmount = BigDecimal.ZERO,
                                    feeAmount = BigDecimal.ZERO,
                                ),
                            ),
                            ProfitCalculatorResult.Result(
                                policy = thirdPolicy,
                                calculateResult = ProfitResult(
                                    userProfitAmount = BigDecimal.ZERO,
                                    feeAmount = BigDecimal.ZERO,
                                ),
                            ),
                        ),
                    )
                }

                test("거래 금액: 333_333 소수점") {
                    // given
                    val paymentAmount = BigDecimal(333_333)

                    // when
                    val result = calculator.calculateProfitAmount(paymentAmount)

                    // then
                    result.totalUserProfitAmount shouldBe BigDecimal(266_667)
                    result.totalFeeAmount shouldBe BigDecimal(66_666)
                    result shouldBe ProfitCalculatorResult(
                        results = listOf(
                            ProfitCalculatorResult.Result(
                                policy = firstPolicy,
                                calculateResult = ProfitResult(
                                    userProfitAmount = BigDecimal(266_667),
                                    feeAmount = BigDecimal(66_666),
                                ),
                            ),
                            ProfitCalculatorResult.Result(
                                policy = secondPolicy,
                                calculateResult = ProfitResult(
                                    userProfitAmount = BigDecimal.ZERO,
                                    feeAmount = BigDecimal.ZERO,
                                ),
                            ),
                            ProfitCalculatorResult.Result(
                                policy = thirdPolicy,
                                calculateResult = ProfitResult(
                                    userProfitAmount = BigDecimal.ZERO,
                                    feeAmount = BigDecimal.ZERO,
                                ),
                            ),
                        ),
                    )
                }

                test("거래 금액: 777_777 소수점") {
                    // given
                    val paymentAmount = BigDecimal(777_777)

                    // when
                    val result = calculator.calculateProfitAmount(paymentAmount)

                    // then
                    result.totalUserProfitAmount shouldBe BigDecimal(650_000)
                    result.totalFeeAmount shouldBe BigDecimal(127_777)
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
                                    userProfitAmount = BigDecimal(250_000),
                                    feeAmount = BigDecimal(27_777),
                                ),
                            ),
                            ProfitCalculatorResult.Result(
                                policy = thirdPolicy,
                                calculateResult = ProfitResult(
                                    userProfitAmount = BigDecimal.ZERO,
                                    feeAmount = BigDecimal.ZERO,
                                ),
                            ),
                        ),
                    )
                }

                test("거래 금액: 3_333_333 소수점") {
                    // given
                    val paymentAmount = BigDecimal(3_333_333)

                    // when
                    val result = calculator.calculateProfitAmount(paymentAmount)

                    // then
                    result.totalUserProfitAmount shouldBe BigDecimal(3_066_667)
                    result.totalFeeAmount shouldBe BigDecimal(266_666)
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
                                    userProfitAmount = BigDecimal(2_216_667),
                                    feeAmount = BigDecimal(116_666),
                                ),
                            ),
                        ),
                    )
                }
            }

            context("DefaultProfitCalculator 계산 검증 - 구간 경계값") {
                test("거래 금액: 500_000 (1구간 상한)") {
                    // given
                    val paymentAmount = BigDecimal(500_000)

                    // when
                    val result = calculator.calculateProfitAmount(paymentAmount)

                    // then
                    result.totalUserProfitAmount shouldBe BigDecimal(400_000)
                    result.totalFeeAmount shouldBe BigDecimal(100_000)
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
                                    userProfitAmount = BigDecimal.ZERO,
                                    feeAmount = BigDecimal.ZERO,
                                ),
                            ),
                            ProfitCalculatorResult.Result(
                                policy = thirdPolicy,
                                calculateResult = ProfitResult(
                                    userProfitAmount = BigDecimal.ZERO,
                                    feeAmount = BigDecimal.ZERO,
                                ),
                            ),
                        ),
                    )
                }

                test("거래 금액: 500_001 (2구간 시작)") {
                    // given
                    val paymentAmount = BigDecimal(500_001)

                    // when
                    val result = calculator.calculateProfitAmount(paymentAmount)

                    // then
                    result.totalUserProfitAmount shouldBe BigDecimal(400_001)
                    result.totalFeeAmount shouldBe BigDecimal(100_000)
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
                                    userProfitAmount = BigDecimal(1),
                                    feeAmount = BigDecimal.ZERO,
                                ),
                            ),
                            ProfitCalculatorResult.Result(
                                policy = thirdPolicy,
                                calculateResult = ProfitResult(
                                    userProfitAmount = BigDecimal.ZERO,
                                    feeAmount = BigDecimal.ZERO,
                                ),
                            ),
                        ),
                    )
                }

                test("거래 금액: 1_000_000 (2구간 상한)") {
                    // given
                    val paymentAmount = BigDecimal(1_000_000)

                    // when
                    val result = calculator.calculateProfitAmount(paymentAmount)

                    // then
                    result.totalUserProfitAmount shouldBe BigDecimal(850_000)
                    result.totalFeeAmount shouldBe BigDecimal(150_000)
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
                                    userProfitAmount = BigDecimal.ZERO,
                                    feeAmount = BigDecimal.ZERO,
                                ),
                            ),
                        ),
                    )
                }

                test("거래 금액: 1_000_001 (3구간 시작)") {
                    // given
                    val paymentAmount = BigDecimal(1_000_001)

                    // when
                    val result = calculator.calculateProfitAmount(paymentAmount)

                    // then
                    result.totalUserProfitAmount shouldBe BigDecimal(850_001)
                    result.totalFeeAmount shouldBe BigDecimal(150_000)
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
                                    userProfitAmount = BigDecimal(1),
                                    feeAmount = BigDecimal.ZERO,
                                ),
                            ),
                        ),
                    )
                }
            }

            context("DefaultProfitCalculator 계산 검증 - 합계 불변식") {
                test("전문가 수익 합과 수수료 합을 더하면 거래 금액과 같아야 한다.") {
                    // given
                    val paymentAmounts = listOf(
                        0,
                        1,
                        4,
                        5,
                        499_999,
                        500_000,
                        500_001,
                        999_999,
                        1_000_000,
                        1_000_001,
                        333_333,
                        777_777,
                        3_333_333,
                        123_456_789,
                    ).map { BigDecimal(it) }

                    paymentAmounts.forAll { paymentAmount ->
                        // when
                        val result = calculator.calculateProfitAmount(paymentAmount)

                        // then
                        result.totalUserProfitAmount + result.totalFeeAmount shouldBe paymentAmount
                    }
                }
            }
        },
    )

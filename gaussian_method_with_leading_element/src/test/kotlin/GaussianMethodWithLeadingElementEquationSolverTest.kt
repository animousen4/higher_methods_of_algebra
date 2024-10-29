import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.doubles.shouldBeBetween
import io.kotest.matchers.maps.shouldContain
import io.kotest.matchers.maps.shouldContainKey
import io.kotest.matchers.maps.shouldHaveSize
import io.kotest.matchers.should
import io.kotest.matchers.shouldBe
import kotlin.math.abs

class GaussianMethodWithLeadingElementEquationSolverTest : FunSpec({
    val tolerance = 0.001

    test("Test base example for the correct values dataset 1") {
        val gaussianMethodSolver = GaussianMethodWithLeadingElementEquationSolver(
            3,
            listOf(
                listOf(3.0, 2.0, -5.0),
                listOf(2.0, -1.0, 3.0),
                listOf(1.0, 2.0, -1.0)
            ),
            listOf(-1.0, 13.0, 9.0))

        val result = gaussianMethodSolver.solve()

        result.equationSolution.shouldHaveSize(3)

        result.equationSolution[1]!!.shouldBeBetween(3.0, 3.0, tolerance)
        result.equationSolution[2]!!.shouldBeBetween(5.0, 5.0, tolerance)
        result.equationSolution[3]!!.shouldBeBetween(4.0, 4.0, tolerance)

        result.determinant.shouldBeBetween(-30.0, -30.0, tolerance)
    }

    test("Test base example for the correct values dataset 2") {
        val gaussianMethodSolver = GaussianMethodWithLeadingElementEquationSolver(
            3,
            listOf(
                listOf(8.0, 7.0, 3.0),
                listOf(-7.0, -4.0, -4.0),
                listOf(-6.0, 5.0, -4.0)
            ),
            listOf(18.0, -11.0, -15.0))

        val result = gaussianMethodSolver.solve()

        result.equationSolution.shouldHaveSize(3)

        result.equationSolution[1]!!.shouldBeBetween(5.0, 5.0, tolerance)
        result.equationSolution[2]!!.shouldBeBetween(-1.0, -1.0, tolerance)
        result.equationSolution[3]!!.shouldBeBetween(-5.0, 5.0, tolerance)

        result.determinant.shouldBeBetween(83.0, 83.0, tolerance)
    }

    test("Test with big matrix") {
        val matrix : List<List<Double>>
        val n = 15

        val diag = 10.0
        val diag1 = 1.0

        val b = MutableList(n) {
            index -> index + 1.0
        }

        matrix = MutableList(n) {
            j ->
            MutableList(n) {
            i ->
                i.let {
                    if (i == j) {
                        return@let diag
                    }

                    if (abs(i - j) == 1) {
                        return@let diag1
                    }

                    if (i == 0) {
                        return@let diag1
                    }

                    if (j == 0) {
                        return@let diag1
                    }

                    return@let 0.0
                }
        } }


        val gaussianMethodSolver = GaussianMethodWithLeadingElementEquationSolver(
            n,
            matrix,
            b,)

        val result = gaussianMethodSolver.solve()

        println(result.equationSolution)
    }
})
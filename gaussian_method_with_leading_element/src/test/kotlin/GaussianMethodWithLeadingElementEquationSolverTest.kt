import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.doubles.shouldBeBetween
import io.kotest.matchers.maps.shouldContain
import io.kotest.matchers.maps.shouldContainKey
import io.kotest.matchers.maps.shouldHaveSize
import io.kotest.matchers.should
import io.kotest.matchers.shouldBe

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

        result.shouldHaveSize(3)

        result[1]!!.shouldBeBetween(3.0, 3.0, tolerance)
        result[2]!!.shouldBeBetween(5.0, 5.0, tolerance)
        result[3]!!.shouldBeBetween(4.0, 4.0, tolerance)

    }
})
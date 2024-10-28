import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.maps.shouldContain
import io.kotest.matchers.should

class GaussianMethodWithLeadingElementEquationSolverTest : FunSpec({


    test("Test base example for the correct values") {
        val gaussianMethodSolver = GaussianMethodWithLeadingElementEquationSolver(
            3,
            listOf(
                listOf(3.0, 2.0, -5.0),
                listOf(2.0, -1.0, 3.0),
                listOf(1.0, 2.0, -1.0)
            ),
            listOf(-1.0, 13.0, 9.0))

        val result = gaussianMethodSolver.solve()

        result.should {
            it.shouldContain(1, 3.0)
            it.shouldContain(2, 5.0)
            it.shouldContain(3, 4.0)
        }


    }
})
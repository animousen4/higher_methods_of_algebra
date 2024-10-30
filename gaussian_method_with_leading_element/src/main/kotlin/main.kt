
import kotlin.math.abs

/*

x1 = -1.0259249036759456594
x2 = 0.26921540663741880809
x3 = 0.33377083730175757816
x4 = 0.41900112402095106935
x5 = 0.50214282616467738727
x6 = 0.58549551800822071696
x7 = 0.66882689742906110231
x8 = 0.75216041137711391926
x9 = 0.83549389247574536438
x10 = 0.91882556754137809636
x11 = 1.00217533578641933
x12 = 1.0853459782703742592
x13 = 1.1702897851857837318
x14 = 1.2376810735477340765
x15 = 1.4788243830128211578

* */
fun main(args: Array<String>) {

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

    var bIndex = 0
    for (rows in matrix) {
        for (column in rows) {
            print("$column ")
        }

        //println()
        println("| ${b[bIndex]}")
        bIndex++
    }
    println()

    val gaussianMethodSolver = GaussianMethodWithLeadingElementEquationSolver(
        n,
        matrix,
        b,)

    val result = gaussianMethodSolver.solve()

    println(result.equationSolution)
    println("Det A = ${result.determinant}")


}
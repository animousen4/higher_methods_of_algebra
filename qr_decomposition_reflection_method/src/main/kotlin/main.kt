import org.jetbrains.kotlinx.multik.api.*
import org.jetbrains.kotlinx.multik.ndarray.data.*

import org.jetbrains.kotlinx.multik.ndarray.data.D2
import org.jetbrains.kotlinx.multik.ndarray.data.NDArray
import kotlin.math.abs

fun printlnMatrix(matrix: NDArray<Double, D2>) {
    val rows = matrix.shape[0]
    val cols = matrix.shape[1]
    for (i in 0 until rows) {
        for (j in 0 until cols) {
            print(String.format("%-6.3f", matrix[i, j]))
        }
        println()
    }
}

fun main() {
    val matrix : List<List<Double>>
    val n = 15

    val diag = 10.0
    val diag1 = 1.0

    val b = MutableList(n) {
            index -> mutableListOf(index + 1.0)
    }

    matrix = MutableList(n) {
            i ->
        MutableList(n) {
                j ->
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
    val A = mk.ndarray(matrix)
    val B = mk.ndarray(b)

    val equationSolver: EquationSolver = QrEquationDecompositionSolver(A, B, 15)
    val result = equationSolver.solve()


    println("R:")
    printlnMatrix(result.R)
    println("Q:")
    printlnMatrix(result.Q)
    println("Q*R:")
    printlnMatrix(result.QR)
    println("X:")
    printlnMatrix(result.X)
    println("Norm: ${result.norm}")
}

import kotlin.math.abs

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
                if (i == j && i != n-1) {
                    return@let diag
                }

                if (abs(i - j) == 1) {
                    return@let diag1
                }


                if (j == n - 1 && ( i == 0 || i == n - 1)) {
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

    val x = mutableListOf<Double>()
    for (i in 0 until result.equationSolution.size) {
        x.add(result.equationSolution[i + 1]!!)
    }

    println("X = ${result.equationSolution}")
    val residualVector = resVector(matrix, x, b)
    println("Residual vector r = b - A*x: $residualVector")
    val reversedA = result.reverseMatrix
    println("Reversed A^-1:")
    printMatrix(reversedA)
    val residualMatrix = resMatrix(matrix, reversedA)
    println("Residual matrix R = E - A * A^-1:")
    printMatrix(residualMatrix)
    val matrixNorm = matrixNorm(residualMatrix)
    println("Residual matrix norm ||E - A * A^(-1)|| : $matrixNorm")
    println("Det A = ${result.determinant}")


}
fun printMatrix(matrix : List<List<Double>>) {
    for (rows in matrix) {
        for (column in rows) {
            print("%10.3f".format(column))
        }
        println()
    }
}
fun matrixNorm(A: List<List<Double>>): Double {
    val n = A.size
    val m = A.firstOrNull()?.size ?: 0 // if the matrix is empty
    var maxSum = 0.0

    for (j in 0 until m) {
        var sum = 0.0
        for (row in A) {
            sum += abs(row[j])
        }
        if (sum > maxSum) {
            maxSum = sum
        }
    }
    return maxSum
}

fun multiplyMatrixVector(A: List<List<Double>>, x: List<Double>): List<Double> {
    val n = A.size
    val m = A.firstOrNull()?.size ?: 0

    if (m != x.size) {
        throw IllegalArgumentException("Amount of colums of matrix must correspond to the vector size")
    }

    val result = MutableList(n) { 0.0 }
    for (i in 0 until n) {
        for (j in 0 until m) {
            result[i] += A[i][j] * x[j]
        }
    }
    return result
}

fun multiplyMatrices(A: List<List<Double>>, B: List<List<Double>>): List<List<Double>> {
    val n = A.size
    val m = A.firstOrNull()?.size ?: 0
    val p = B.firstOrNull()?.size ?: 0
    if (m != B.size) {
        throw IllegalArgumentException("Amount of colums of matrix must correspond to the amount of another matrix rows amount")
    }
    val result = MutableList(n) { MutableList(p) { 0.0 } }
    for (i in 0 until n) {
        for (j in 0 until p) {
            for (k in 0 until m) {
                result[i][j] += A[i][k] * B[k][j]
            }
        }
    }
    return result
}


fun eMatrix(n: Int): List<List<Double>> {
    val result = MutableList(n) { MutableList(n) { 0.0 } }
    for (i in 0 until n) {
        result[i][i] = 1.0
    }
    return result
}
fun resVector(A: List<List<Double>>, x: List<Double>, b: List<Double>): List<Double> {
    val Ax = multiplyMatrixVector(A, x)
    val n = b.size
    val r = MutableList(n) { 0.0 }
    for (i in 0 until n) {
        r[i] = b[i] - Ax[i]
    }
    return r
}

fun resMatrix(A: List<List<Double>>, invA: List<List<Double>>): List<List<Double>> {
    val n = A.size
    val E = eMatrix(n)
    val AinvA = multiplyMatrices(A, invA)
    val R = MutableList(n) { MutableList(n) { 0.0 } }
    for (i in 0 until n) {
        for (j in 0 until n) {
            R[i][j] = E[i][j] - AinvA[i][j]
        }
    }
    return R
}



import kotlin.math.abs

fun main() {
    val n = 15
    val studentNumber = 6.0
    val eps = 1e-5
    val A: Array<DoubleArray>
    val b: DoubleArray = DoubleArray(n)
    val x: DoubleArray = DoubleArray(n)

    // Initialize matrix A and vector b
    val diag = 10.0
    val diag1 = 1.0

    A = Array(n) {
            j ->
        DoubleArray(n) {
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
    for (i in 0 until n) {
        x[i] = studentNumber + i
    }

    for (i in 0 until n) {
        b[i] = 0.0
        for (j in 0 until n) {
            b[i] += A[i][j] * x[j]
        }
    }
    println("A source:")
    printMatrix(A)
    println("x source:")
    printVector(x)
    println("b source:")
    printVector(b)
    println()

    val input = Input(
        n,
        A,
        b,
        eps
    )

    val solution: Solution = SolutionImpl()
    val output = solution.solve(input)

    println("Spectral norm A: ${output.spectralNormA}")
    println("\nMatrix B:")
    printMatrix(output.matrixB)
    println("\nVector c:")
    printVector(output.msiVectorC)
    println("Spectral Norm B: ${output.msiSpectralNormB}")
    println("Apriori amount of iterations: ${output.msiAprioriIterations}")
    println("Real amount of iterations: ${output.msiRealIterations}")
    println("Residual norm: ${output.msiResidual}")
    println("\nSolution (MSI) x:")
    printVector(output.msiSolution)
    println("\nMatrix lower triangle H:")
    printMatrix(output.seidelMatrixH)
    println("\nMatrix upper triangle F")
    printMatrix(output.seidelMatrixF)
    println("\n(E - H)^-1:")
    printMatrix(output.seidelEMinusHReversed)
    println("\nSolution (Seidel method) x:")
    printVector(output.seidelSolution)
    println("Residual norm Seidel: ${output.seidelResidual}")
    println("\nSolution (Minimal Residuals) x:")
    printVector(output.minResSolution)
    println("Number of iterations (Minimal Residuals): ${output.minResIterationAmount}")
    println("Residual norm (Minimal Residuals): ${output.minResResidual}")

}


private fun printMatrix(matrix: Array<DoubleArray>) {
    for (row in matrix) {
        for (value in row) {
            print("%10.3f".format(value))
        }
        println()
    }

}

private fun printVector(vector: DoubleArray) {
    for (value in vector) {
        print("%10.4f".format(value))
    }
    println()
}
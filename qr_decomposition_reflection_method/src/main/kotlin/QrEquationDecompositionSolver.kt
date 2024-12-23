import org.jetbrains.kotlinx.multik.api.identity
import org.jetbrains.kotlinx.multik.api.mk
import org.jetbrains.kotlinx.multik.api.zeros
import org.jetbrains.kotlinx.multik.ndarray.data.*
import kotlin.math.abs
import kotlin.math.sqrt

class QrEquationDecompositionSolver(
    val A:  D2Array<Double>,
    val B: D2Array<Double>,
    val n: Int,
) : EquationSolver {
    override fun solve(): EquationSolution {
        var R = A.copy()
        var Q = mk.identity<Double>(n)
        var f_ = B.copy()

        for (i in 0 until n - 1) {
            val sI = (i until n).map { R[it, i] }.toDoubleArray()
            var alfaI = 0.0
            sI.forEach { alfaI += it * it }
            alfaI = sqrt(alfaI)
            val e = DoubleArray(n - i) { 0.0 }
            e[0] = alfaI
            val wVector = DoubleArray(n - i)
            wVector[0] = sI[0] - alfaI
            for (j in 1 until sI.size) {
                wVector[j] = sI[j]
            }

            var denominator = 0.0
            for (j in 0 until n - i) {
                denominator += wVector[j] * sI[j]
            }
            val kappa = 1 / denominator

            val wWT = mk.zeros<Double>(n - i, n - i)
            for (j in 0 until n - i) {
                for (k in 0 until n - i) {
                    wWT[j, k] = wVector[j] * wVector[k] * kappa
                }
            }
            val E = mk.identity<Double>(n-i)
            for (j in 0 until n-i){
                for (k in 0 until n-i){
                    E[j, k] = E[j, k] - wWT[j, k]
                }
            }

            val QI = padToNMatrix(E, n - i, n)
            Q = multiplyMatrix(transposeMatrix(QI), Q)
            R = multiplyMatrix(QI, R)
            f_ = multiplyMatrix(QI, f_)
        }

        val X = backSubstitution(R, f_)
        return EquationSolution(Q, R, X, multiplyMatrix(Q, R), getNorm(A, X, B))
    }


    private fun padToNMatrix(matrix: NDArray<Double, D2>, nReal: Int, nExpected: Int): NDArray<Double, D2> {
        if (nReal == nExpected) {
            return matrix
        }
        val E = mk.identity<Double>(nExpected)
        for (i in 0 until nReal) {
            for (j in 0 until nReal) {
                E[nExpected - nReal + i, nExpected - nReal + j] = matrix[i, j]
            }
        }
        return E
    }

    private fun multiplyMatrix(A: NDArray<Double, D2>, B: NDArray<Double, D2>): NDArray<Double, D2> {
        val C = mk.zeros<Double>(A.shape[0], B.shape[1])
        for (i in 0 until A.shape[0]) {
            for (j in 0 until B.shape[1]) {
                var sum = 0.0
                for (k in 0 until A.shape[1]) {
                    sum += A[i, k] * B[k, j]
                }
                C[i, j] = sum
            }
        }
        return C
    }


    private fun transposeMatrix(matr: NDArray<Double, D2>): NDArray<Double, D2> {
        val n = matr.shape[0]
        val C = mk.zeros<Double>(n, n)
        for (i in 0 until n) {
            for (j in 0 until n) {
                C[j, i] = matr[i, j]
            }
        }
        return C
    }


    private fun substractMatrix(A: NDArray<Double, D2>, B: NDArray<Double, D2>): NDArray<Double, D2> {
        val C = mk.zeros<Double>(A.shape[0], A.shape[1])
        for (i in 0 until A.shape[0]) {
            for (j in 0 until A.shape[1]) {
                C[i, j] = A[i, j] - B[i, j]
            }
        }
        return C
    }
    // Searching for x
    private fun backSubstitution(U: NDArray<Double, D2>, b: NDArray<Double, D2>): NDArray<Double, D2> {
        val n = U.shape[0]
        val x = mk.zeros<Double>(n, 1)

        for (i in n - 1 downTo 0) {
            var sumAx = 0.0
            for (j in i + 1 until n) {
                sumAx += U[i, j] * x[j, 0]
            }
            x[i, 0] = (b[i, 0] - sumAx) / U[i, i]
        }
        return x
    }

    private fun getNorm(A: NDArray<Double, D2>, y: NDArray<Double, D2>, f: NDArray<Double, D2>): Double {
        val matr = substractMatrix(multiplyMatrix(A, y), f)
        var max = -Double.MAX_VALUE
        for (i in 0 until matr.shape[0]) {
            for (j in 0 until matr.shape[1]) {
                max = maxOf(max, abs(matr[i, j]))
            }
        }
        return max
    }

}
import kotlin.math.*

class SolutionImpl : Solution {

    override fun solve(input: Input): OutputResult {
        val A = input.a
        val b = input.b
        var xN1 = DoubleArray(input.n)
        var xN = DoubleArray(input.n)
        val xSeidN1 = DoubleArray(input.n)
        xSeidN1.fill(0.0)

        // Getting spectral norm of A
        val normA = getSpectralNorm(A, input.eps)

        // Construct matrix B for method of simple iterations
        val bMatrix = Array(input.n) { DoubleArray(input.n) }
        for (i in 0 until input.n) {
            for (j in 0 until input.n) {
                bMatrix[i][j] = if (i == j) {
                    1 - A[i][j] / normA
                } else {
                    -A[i][j] / normA
                }
            }
        }
        val normB = getSpectralNorm(bMatrix, input.eps)

        if (normB >= 1) {
            throw RuntimeException("The method does not converge: norm(B) <= 1")
        }

        // Vector c for apriori amount of iterations
        val c = DoubleArray(input.n) { i -> b[i] / normA }
        val normC = getVectorNorm(c)
        val aprioriIterations = ceil(ln(input.eps * (1 - normB) / normC) / ln(normB)).toInt()

        // Iterative process for simple iteration method
        var msiIterationAmount = 0
        var norm: Double
        while (true) {
            for (i in 0 until input.n) {
                xN1[i] = c[i]
                for (j in 0 until input.n) {
                    xN1[i] += bMatrix[i][j] * xN[j]
                }
            }
            norm = getNormX1MinusX2(xN, xN1)
            xN1.copyInto(xN)
            msiIterationAmount++

            if (norm <= input.eps) {
                break
            }
        }

        val residualNormMethodOfSimpleIteration = getVectorNorm(calculateResidual(A, xN, b))

        // Construct matrices H and F
        val h = Array(input.n) { DoubleArray(input.n) }
        val f = Array(input.n) { DoubleArray(input.n) }
        for (i in 0 until input.n) {
            for (j in 0 until input.n) {
                if (j <= i) {
                    h[i][j] = bMatrix[i][j]
                } else {
                    f[i][j] = bMatrix[i][j]
                }
            }
        }

        // Iterative process for Seidel method
        val eMinusHReversed = getReverseMatrix(eMinusMatrix(h))
        val p = multiplyMatrix(eMinusHReversed, f)
        val g = multiplyMatrixByVector(eMinusHReversed, b)

        val seidIterationsAmount = msiIterationAmount
        for (k in 0 until seidIterationsAmount) {
            val xSeidN = multiplyMatrixByVector(p, xSeidN1)
            for (i in 0 until input.n) {
                xSeidN1[i] = xSeidN[i] + g[i]
            }
        }

        val residualNormSeidel = getVectorNorm(calculateResidual(A, xSeidN1, b))

        // Minimal residual method
        val xMinRes = DoubleArray(input.n) { 0.0 }
        var minResIterationAmount = 0
        var r = calculateResidual(A, xMinRes, b)
        var resNorm = getVectorNorm(r)

        while (resNorm > input.eps) {
            val ar = multiplyMatrixByVector(A, r)
            val alp = multiplyMatrixOneOneVector(r, ar) / multiplyMatrixOneOneVector(ar, ar)
            for (i in 0 until input.n) {
                xMinRes[i] += alp * r[i]
            }
            r = calculateResidual(A, xMinRes, b)
            resNorm = getVectorNorm(r)
            minResIterationAmount++
        }

        return OutputResult(
            matrixA = A,
            vectorB = b,
            spectralNormA = normA,
            matrixB = bMatrix,
            msiVectorC = c,
            msiSpectralNormB = normB,
            msiAprioriIterations = aprioriIterations,
            msiRealIterations = msiIterationAmount,
            msiResidual = residualNormMethodOfSimpleIteration,
            msiSolution = xN,
            seidelMatrixH = h,
            seidelMatrixF = f,
            seidelEMinusHReversed = eMinusHReversed,
            seidelSolution = xSeidN1,
            seidelResidual = residualNormSeidel,
            minResSolution = xMinRes,
            minResIterationAmount = minResIterationAmount,
            minResResidual = resNorm
        )
    }



    private fun multiplyMatrixByVector(x1: Array<DoubleArray>, x2: DoubleArray): DoubleArray {
        val n = x1.size
        val result = DoubleArray(n)
        for (i in 0 until n) {
            result[i] = 0.0
            for (j in x2.indices) {
                result[i] += x1[i][j] * x2[j]
            }
        }
        return result
    }

    private fun multiplyMatrixOneOneVector(v1: DoubleArray, v2: DoubleArray): Double {
        var result = 0.0
        for (i in v1.indices) {
            result += v1[i] * v2[i]
        }
        return result
    }

    private fun multiplyMatrix(a: Array<DoubleArray>, b: Array<DoubleArray>): Array<DoubleArray> {
        val rows = a.size
        val cols = b[0].size
        val innerDim = b.size
        val result = Array(rows) { DoubleArray(cols) }

        for (i in 0 until rows) {
            for (j in 0 until cols) {
                for (k in 0 until innerDim) {
                    result[i][j] += a[i][k] * b[k][j]
                }
            }
        }
        return result
    }
    private fun eMinusMatrix(matrix: Array<DoubleArray>): Array<DoubleArray> {
        val n = matrix.size
        val result = Array(n) { DoubleArray(n) }

        for (i in 0 until n) {
            for (j in 0 until n) {
                result[i][j] = if (i == j) {
                    1 - matrix[i][j]
                } else {
                    -matrix[i][j]
                }
            }
        }
        return result
    }
    private fun getVectorNorm(vector: DoubleArray): Double {
        var norm = 0.0
        for (value in vector) {
            norm = max(norm, abs(value))
        }
        return norm
    }

    private fun getNormX1MinusX2(x1: DoubleArray, x2: DoubleArray): Double {
        var norm = 0.0
        for (i in x1.indices) {
            norm = max(norm, abs(x1[i] - x2[i]))
        }
        return norm
    }

    private fun calculateResidual(a: Array<DoubleArray>, x: DoubleArray, b: DoubleArray): DoubleArray {
        val n = a.size
        val residual = DoubleArray(n)
        for (i in 0 until n) {
            var Ax = 0.0
            for (j in 0 until n) {
                Ax += a[i][j] * x[j]
            }
            residual[i] = b[i] - Ax
        }
        return residual
    }

    private fun getReverseMatrix(matrix: Array<DoubleArray>): Array<DoubleArray> {
        val n = matrix.size
        val mMatr = Array(n) { DoubleArray(2 * n) }

        for (i in 0 until n) {
            for (j in 0 until n) {
                mMatr[i][j] = matrix[i][j]
            }
            mMatr[i][n + i] = 1.0
        }

        for (i in 0 until n) {
            val diagonalElement = mMatr[i][i]
            if (diagonalElement == 0.0) {
                throw IllegalArgumentException("Matrix cannot be inverted.")
            }

            for (j in 0 until 2 * n) {
                mMatr[i][j] /= diagonalElement
            }

            for (k in 0 until n) {
                if (k != i) {
                    val coefficient = mMatr[k][i]
                    for (j in 0 until 2 * n) {
                        mMatr[k][j] -= coefficient * mMatr[i][j]
                    }
                }
            }
        }

        val inverseMatrix = Array(n) { DoubleArray(n) }
        for (i in 0 until n) {
            for (j in 0 until n) {
                inverseMatrix[i][j] = mMatr[i][n + j]
            }
        }

        return inverseMatrix
    }


    private fun getSpectralNorm(matrix: Array<DoubleArray>, epsilon: Double): Double {
        val n = matrix.size
        val x = DoubleArray(n) { 1.0 }
        val y = DoubleArray(n)
        var lambdaN = 0.0
        var lambdaN1 = 0.0
        var norm: Double

        while (true) {
            for (i in 0 until n) {
                y[i] = 0.0
                for (j in 0 until n) {
                    y[i] += matrix[i][j] * x[j]
                }
            }
            lambdaN1 = 0.0
            for (i in 0 until n) {
                lambdaN1 = max(lambdaN1, abs(y[i]))
            }
            for (i in 0 until n) {
                x[i] = y[i] / lambdaN1
            }
            norm = abs(lambdaN1 - lambdaN)
            lambdaN = lambdaN1
            if (norm <= epsilon) {
                break;
            }
        }

        return lambdaN1
    }
}
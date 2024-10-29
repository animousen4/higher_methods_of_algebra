import kotlin.math.abs

class GaussianMethodWithLeadingElementEquationSolver(
    private val n: Int,
    private val sourceMatrix: List<List<Double>>,
    private val equationValues: List<Double>
) : EquationSolver {

    /// The function, that returns a max
    /// value in the row from selected diagonal element
    fun getMaxOfRow(columnListMatrix: List<List<Double>>, matrixSize: Int, diagPosition: Int) : ElementPosition {
        var j = diagPosition + 1
        var maxValue = ElementPosition(diagPosition, diagPosition)
        while (j < matrixSize) {
            if (abs(columnListMatrix[j][diagPosition]) > abs(columnListMatrix[maxValue.j][maxValue.i])) {
                maxValue = ElementPosition(diagPosition, j)
            }
            j++
        }

        return maxValue
    }

    override fun solve() : EquationSolution {


        /// number of variable in the column i with index i
        val variablePosition: MutableList<Int> = MutableList(n) { index -> index }
        val b: MutableList<Double> = equationValues.toMutableList()

        val columnListMatrix : MutableList<MutableList<Double>> = MutableList(n) { MutableList(n) { 0.0 } }
        /// transposing matrix
        /// we are working with columns
        for (i in 0 until n) {
            for (j in 0 until n) {
                    columnListMatrix[j][i] = sourceMatrix[i][j]
            }
        }


        var multipliedTransformation = 1.0
        var maxValue: Double
        var exchangeCount = 0;
        var maxElementPosition: ElementPosition

        for (currentDiagPointer in 0..<n) {
            maxElementPosition = getMaxOfRow(columnListMatrix, n, currentDiagPointer)
            maxValue = columnListMatrix[maxElementPosition.j][maxElementPosition.i]

            /// doing column exchange if leading element is not at position
            if (maxElementPosition != ElementPosition(currentDiagPointer, currentDiagPointer)) {
                val proxyColumn = columnListMatrix[maxElementPosition.j]
                columnListMatrix[maxElementPosition.j] = columnListMatrix[currentDiagPointer]
                columnListMatrix[currentDiagPointer] = proxyColumn

                /// marking that variable is moved
                val proxyPos = variablePosition[maxElementPosition.j]
                variablePosition[maxElementPosition.j] = variablePosition[currentDiagPointer]
                variablePosition[currentDiagPointer] = proxyPos

                exchangeCount++
            }

            /// dividing on the leading element
            for (toDividePointer in currentDiagPointer..< n) {
                columnListMatrix[toDividePointer][currentDiagPointer] = columnListMatrix[toDividePointer][currentDiagPointer] / maxValue
            }
            b[currentDiagPointer] = b[currentDiagPointer] / maxValue


            /// making zeros under the leading element
            for (kI in currentDiagPointer + 1..< n) {
                val toMultiply = columnListMatrix[currentDiagPointer][kI]
                for (kJ in currentDiagPointer..< n) {
                    columnListMatrix[kJ][kI] = columnListMatrix[kJ][kI] - toMultiply * columnListMatrix[kJ][currentDiagPointer]
                }
                b[kI] = b[kI] - toMultiply * b[currentDiagPointer]
            }

            /// we have divided by maxValue and discriminant is changed
            multipliedTransformation *= maxValue


        }

        var diagIndex = n - 1
        val transformedResultValues = MutableList(n) {0.0}
        /// calculating the variables values from the bottom
        while (diagIndex >= 0) {

            var j = diagIndex + 1
            var otherSum = 0.0
            while (j < n) {
                otherSum += columnListMatrix[j][diagIndex] * transformedResultValues[j]
                j++
            }

            transformedResultValues[diagIndex] = b[diagIndex] - otherSum;

            diagIndex--
        }

        val resultMap = mutableMapOf<Int, Double>()
        /// saving the result to the map
        for (i in 0 ..<n) {
            /// getting the actual variable in column and saving according value
            resultMap[variablePosition[i] + 1] = transformedResultValues[i]
        }

        /// calculating determinant
        val det = (if (exchangeCount % 2 == 0) 1 else -1) * multipliedTransformation

        return EquationSolution(resultMap, det)
    }

}
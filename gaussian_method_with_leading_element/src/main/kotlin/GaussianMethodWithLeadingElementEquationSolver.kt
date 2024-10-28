import kotlin.math.abs

class GaussianMethodWithLeadingElementEquationSolver(
    private val n: Int,
    private val sourceMatrix: List<List<Double>>,
    private val equationValues: List<Double>
) : EquationSolver {
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

    override fun solve() : Map<Int, Double> {


        val variablePosition: MutableList<Int> = MutableList(n) { index -> index }
        val afterStickValue: MutableList<Double> = equationValues.toMutableList()

        val columnListMatrix : MutableList<MutableList<Double>> = MutableList(n) { MutableList(n) { 0.0 } }

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

            if (maxElementPosition != ElementPosition(currentDiagPointer, currentDiagPointer)) {
                val proxyColumn = columnListMatrix[maxElementPosition.j]
                columnListMatrix[maxElementPosition.j] = columnListMatrix[currentDiagPointer]
                columnListMatrix[currentDiagPointer] = proxyColumn

                val proxyPos = variablePosition[maxElementPosition.j]
                variablePosition[maxElementPosition.j] = variablePosition[currentDiagPointer]
                variablePosition[currentDiagPointer] = proxyPos

                exchangeCount++
            }

            for (toDividePointer in currentDiagPointer..< n) {
                columnListMatrix[toDividePointer][currentDiagPointer] = columnListMatrix[toDividePointer][currentDiagPointer] / maxValue
            }
            afterStickValue[currentDiagPointer] = afterStickValue[currentDiagPointer] / maxValue


            for (kI in currentDiagPointer + 1..< n) {
                val toMultiply = columnListMatrix[currentDiagPointer][kI]
                for (kJ in currentDiagPointer..< n) {
                    columnListMatrix[kJ][kI] = columnListMatrix[kJ][kI] - toMultiply * columnListMatrix[kJ][currentDiagPointer]
                }
                afterStickValue[kI] = afterStickValue[kI] - toMultiply * afterStickValue[currentDiagPointer]
            }

            multipliedTransformation *= maxValue


        }

        var diagIndex = n - 1
        val transformedResultValues = MutableList(n) {0.0}
        while (diagIndex >= 0) {

            //transformedResultValues[diagIndex] = columnListMatrix[diagIndex][diagIndex]
            var j = diagIndex + 1
            var otherSum = 0.0
            while (j < n) {
                otherSum += columnListMatrix[j][diagIndex] * transformedResultValues[j]
                j++
            }

            transformedResultValues[diagIndex] = afterStickValue[diagIndex] - otherSum;

            diagIndex--
        }

        val resultMap = mutableMapOf<Int, Double>()
        for (i in 0 ..<n) {
            resultMap[variablePosition[i] + 1] = transformedResultValues[i]
        }

        return resultMap
    }

}
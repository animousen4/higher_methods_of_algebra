import java.util.*
import kotlin.math.abs


fun getMaxOfRow(columnListMatrix: List<List<Int>>, matrixSize: Int, diagPosition: Int) : ElementPosition {
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
/*

3

3 2 -5
2 -1 3
1 2 -1

-1 13 9

*/
fun main(args: Array<String>) {

    val columnListMatrix : List<List<Int>>
    val n: Int

    var maxElementPosition: ElementPosition
    val variablePosition: List<Int>
    val afterStickValue: List<Int>


    val scanner = Scanner(System.`in`)

    //print("Matrix size: ")
    n = scanner.nextInt()

    //println("Matrix: ")
    columnListMatrix = MutableList(n) { MutableList(n) { 0 } }

    for (i in 0 until n) {
        for (j in 0 until n) {
            val value = scanner.nextInt()
            columnListMatrix[j][i] = value
        }
    }

    //println("Values: ")
    afterStickValue = MutableList(n) { scanner.nextInt() }

    variablePosition = MutableList(n) {index -> index}


    var multipliedTransformation = 1
    var maxValue: Int
    var exchangeCount = 0;
    for (currentDiagPointer in 0..<n) {
        maxElementPosition = getMaxOfRow(columnListMatrix, n, currentDiagPointer)
        maxValue = columnListMatrix[maxElementPosition.j][maxElementPosition.i]

        if (columnListMatrix[currentDiagPointer][currentDiagPointer] > maxValue) {
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
            afterStickValue[toDividePointer] = afterStickValue[toDividePointer] / maxValue
        }

        multipliedTransformation *= maxValue
    }

    var diagIndex = n - 1
    val transformedResultValues = MutableList(n) {0}
    while (diagIndex >= 0) {

        //transformedResultValues[diagIndex] = columnListMatrix[diagIndex][diagIndex]
        var j = diagIndex + 1
        var otherSum = 0
        while (j < n) {
            otherSum += columnListMatrix[j][j] * transformedResultValues[j]
            j++
        }

        transformedResultValues[diagIndex] = afterStickValue[diagIndex] - otherSum;

        diagIndex--
    }

    println()
    for (i in 0 ..<n) {
        println("x${variablePosition[i] + 1} = ${transformedResultValues[i]}")
    }


}
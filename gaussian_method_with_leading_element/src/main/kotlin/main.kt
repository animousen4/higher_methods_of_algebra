import java.util.*


fun getMaxOfRow(columnListMatrix: List<List<Int>>, matrixSize: Int, diagPosition: Int) : ElementPosition {
    var j = diagPosition + 1
    var maxValue = ElementPosition(diagPosition, diagPosition)
    while (j < matrixSize) {
        if (columnListMatrix[j][diagPosition] > columnListMatrix[maxValue.j][maxValue.i]) {
            maxValue = ElementPosition(diagPosition, j)
        }
    }

    return maxValue
}

fun main(args: Array<String>) {

    val columnListMatrix : List<List<Int>>
    val n: Int

    var maxElementPosition: ElementPosition
    var diagPosition: Int
    val variablePosition: List<Int>


    val scanner = Scanner(System.`in`)

    print("Matrix size: ")
    n = scanner.nextInt()

    println("Matrix: ")
    columnListMatrix = MutableList(n) { MutableList(n) { 0 } }

    for (i in 0 until n) {
        for (j in 0 until n) {
            val value = scanner.nextInt()
            columnListMatrix[j][i] = value
        }
    }

    variablePosition = MutableList(n) {index -> index}



    var maxValue: Int
    for (currentDiagPointer in 1..<n) {
        maxElementPosition = getMaxOfRow(columnListMatrix, n, currentDiagPointer)
        maxValue = columnListMatrix[maxElementPosition.j][maxElementPosition.i]

        if (columnListMatrix[currentDiagPointer][currentDiagPointer] > maxValue) {
            val proxyColumn = columnListMatrix[maxElementPosition.j]
            columnListMatrix[maxElementPosition.j] = columnListMatrix[currentDiagPointer]
            columnListMatrix[currentDiagPointer] = proxyColumn
        }
    }


}
class Matrix(private val rows: Int, private val cols: Int, fromCli: Boolean = false) {
    private val matrix: List<MutableList<Double>>
    init {
        matrix = if (!fromCli) List(rows) { MutableList(cols) { 0.0 } }
        else List(rows) { readln().split(" ").map { it.toDouble() }.toMutableList() }
    }

    operator fun get(row: Int, col: Int) = matrix[row][col]
    operator fun set(row: Int, col: Int, value: Double) = matrix[row].set(col, value)
    override fun toString() = matrix.joinToString("\n") { it.joinToString(" ") }

    private fun transform(action: (x: Int, y: Int) -> Double): Matrix {
        val newMatrix = Matrix(rows, cols)
        for (row in 0 until rows)
            for (col in 0 until cols)
                newMatrix[row, col] = action(col, row)

        return newMatrix
    }

    fun transposeMain() = transform { x, y -> this[x, y] }
    fun transposeSide() = transform { x, y -> this[cols - 1 - x, rows - 1 - y] }
    fun transposeVertical() = transform { x, y -> this[y, cols - 1 - x] }
    fun transposeHorizontal() = transform { x, y -> this[rows - 1 - y, x] }
    operator fun times(multiplier: Int) = transform { x, y -> multiplier * this[y, x] }
    operator fun plus(other: Matrix): Matrix {
        if (rows != other.rows || cols != other.cols) error("The operation cannot be performed.")
        return transform { x, y -> this[y, x] + other[y, x] }
    }
    operator fun times(other: Matrix): Matrix {
        if (cols != other.rows) error("The operation cannot be performed.")
        val newMatrix = Matrix(rows, other.cols)
        for (row in 0 until rows)
            for (col in 0 until other.cols)
                for (i in 0 until cols)
                    newMatrix[row, col] += this[row, i] * other[i, col]
        return newMatrix
    }

    fun getDeterminant(m: List<MutableList<Double>> = matrix): Double {
        if (m.size == 2) return m[0][0] * m[1][1] - m[0][1] * m[1][0]

        var result = 0.0
        for (col in m.indices) {
            val subMatrix = m.map { it.toMutableList() }.toMutableList()
                .apply { removeAt(0); forEach { it.removeAt(col) } }
            result += (if (col % 2 == 0) 1 else -1) * m[0][col] * getDeterminant(subMatrix)
        }
        return result
    }
}

class Processor {
    private fun printResult(result: Any) = println("The result is:\n$result")
    val prefix = listOf("", "first ", "second ")
    private fun readMatrix(num: Int = 0): Matrix {
        print("Enter size of ${prefix[num]}matrix:")
        val (rows, cols) = readln().split(" ").map { it.toInt() }
        println("Enter ${prefix[num]}matrix:")
        return Matrix(rows, cols, true)
    }
    private fun transposeMatrix() {
        print(
            """ 
            |1. Main diagonal
            |2. Side diagonal
            |3. Vertical line
            |4. Horizontal line
            |Your choice:
            """.trimMargin()
        )
        val choice = readln().toInt()
        val matrix = readMatrix()
        printResult(
            when (choice) {
                1 -> matrix.transposeMain()
                2 -> matrix.transposeSide()
                3 -> matrix.transposeVertical()
                4 -> matrix.transposeHorizontal()
                else -> error("Not a valid choice!")
            }
        )
    }
    private fun showMenu() = print(
        """ 
        |1. Add matrices
        |2. Multiply matrix by a constant
        |3. Multiply matrices
        |4. Transpose matrix
        |5. Calculate a determinant
        |0. Exit
        |Your choice:
        """.trimMargin()
    )
    fun run() {
        while (true) {
            showMenu()
            when (readln().toInt()) {
                1 -> printResult(readMatrix(1) + readMatrix(2))
                2 -> printResult(readMatrix().also { print("Enter constant:") } * readln().toInt())
                3 -> printResult(readMatrix(1) * readMatrix(2))
                4 -> transposeMatrix()
                5 -> printResult(readMatrix().getDeterminant())
                else -> return
            }
            println()
        }
    }
}
fun main() = Processor().run()
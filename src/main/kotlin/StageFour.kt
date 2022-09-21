class Matrix(private val rows: Int, private val cols: Int, fromCli: Boolean = false) {
    private val matrix: List<MutableList<Double>>
    init {
        matrix = if (!fromCli) List(rows) { MutableList(cols) { 0.0 } }
        else List(rows) { readln().split(" ").map { it.toDouble() }.toMutableList() }
    }

    operator fun get(row: Int, col: Int) = matrix[row][col]
    operator fun set(row: Int, col: Int, value: Double) = matrix[row].set(col, value)

    operator fun plus(other: Matrix) = transform { x, y -> this[y, x] + other[y, x] }
    operator fun times(multiplier: Int) = transform { x, y -> multiplier * this[y, x] }
    operator fun times(other: Matrix): Matrix {
        val newMatrix = Matrix(rows, other.cols)
        for (row in 0 until rows)
            for (col in 0 until other.cols)
                for (i in 0 until cols)
                    newMatrix[row, col] += this[row, i] * other[i, col]
        return newMatrix
    }

    fun transposeMain() = transform { x, y -> this[x, y] }
    fun transposeSide() = transform { x, y -> this[cols - 1 - x, rows - 1 - y] }
    fun transposeVertical() = transform { x, y -> this[y, cols - 1 - x] }
    fun transposeHorizontal() = transform { x, y -> this[rows - 1 - y, x] }

    private fun transform(operate: (x: Int, y: Int) -> Double): Matrix {
        val newMatrix = Matrix(rows, cols)
        for (row in 0 until rows)
            for (col in 0 until cols)
                newMatrix[row, col] = operate(col, row)

        return newMatrix
    }

    fun isEqualSize(other: Matrix) = rows == other.rows && cols == other.cols
    fun canCompose(other: Matrix) = cols == other.rows

    override fun toString() = matrix.joinToString("\n") { it.joinToString(" ") }
}

class Processor {
    private fun printResult(matrix: Matrix) = println("The result is:\n$matrix")

    private fun readTwoMatrices() = readMatrix("first ") to readMatrix("second ")
    private fun readMatrix(prefix: String = ""): Matrix {
        print("Enter size of ${prefix}matrix:")
        val (rows, cols) = readln().split(" ").map { it.toInt() }
        println("Enter ${prefix}matrix:")
        return Matrix(rows, cols, true)
    }
    private fun addMatrices() {
        val (matrix1, matrix2) = readTwoMatrices()
        if (!matrix1.isEqualSize(matrix2)) error("The operation cannot be performed.")
        printResult(matrix1 + matrix2)
    }
    private fun scaleMatrix() {
        val matrix = readMatrix()
        print("Enter constant:")
        val multiplier = readln().toInt()
        printResult(matrix * multiplier)
    }
    private fun composeMatrices() {
        val (matrix1, matrix2) = readTwoMatrices()
        if (!matrix1.canCompose(matrix2)) error("The operation cannot be performed.")
        printResult(matrix1 * matrix2)
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

        printResult(when (choice) {
            1 -> matrix.transposeMain()
            2 -> matrix.transposeSide()
            3 -> matrix.transposeVertical()
            4 -> matrix.transposeHorizontal()
            else -> error("Not a valid choice!")
        })
    }

    private fun showMenu() = print(
        """ 
        |1. Add matrices
        |2. Multiply matrix by a constant
        |3. Multiply matrices
        |4. Transpose matrix
        |0. Exit
        |Your choice:
        """.trimMargin()
    )
    fun run() {
        while (true) {
            showMenu()
            when (readln().toInt()) {
                1 -> addMatrices()
                2 -> scaleMatrix()
                3 -> composeMatrices()
                4 -> transposeMatrix()
                else -> return
            }
            println()
        }
    }
}
fun main() = Processor().run()
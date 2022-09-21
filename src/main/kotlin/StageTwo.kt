fun main() {

    val (rows, cols) = readNums()  // readNums é uma função no do arquivo Funcoes
    val matrix = List(rows) { readNums() }

    val multiplier = readln().toInt()
    

    for (y in 0 until rows) {
        for (x in 0 until cols) {
            print("${multiplier * matrix[y][x]} ")
        }
        println()
    }

}
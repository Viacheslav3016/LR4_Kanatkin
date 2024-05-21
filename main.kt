val S_BOX = arrayOf(0x6, 0x4, 0xC, 0x5, 0x0, 0x7, 0x2, 0xE, 0x1, 0xF, 0x3, 0xD, 0x8, 0xA, 0x9, 0xB)

fun generateInverseSBox(sBox: Array<Int>): Array<Int> {
    val invSBox = Array(sBox.size) { 0 }
    for (i in sBox.indices) {
        invSBox[sBox[i]] = i
    }
    return invSBox
}

val INV_S_BOX = generateInverseSBox(S_BOX)

fun sBlockTransform(data: Int, sBox: Array<Int>): Int {
    var result = 0
    for (i in 0 until 4) {
        val nibble = (data shr (i * 4)) and 0xF
        val transformedNibble = sBox[nibble]
        result = result or (transformedNibble shl (i * 4))
    }
    return result
}

fun sBlockInverseTransform(data: Int, invSBox: Array<Int>): Int {
    return sBlockTransform(data, invSBox)
}

val P_BOX = arrayOf(0, 4, 8, 12, 1, 5, 9, 13, 2, 6, 10, 14, 3, 7, 11, 15)

fun pBlockTransform(data: Int, pBox: Array<Int>): Int {
    var result = 0
    for (i in pBox.indices) {
        val bit = (data shr i) and 1
        result = result or (bit shl pBox[i])
    }
    return result
}

fun pBlockInverseTransform(data: Int, pBox: Array<Int>): Int {
    val inversePBox = Array(pBox.size) { 0 }
    for (i in pBox.indices) {
        inversePBox[pBox[i]] = i
    }
    return pBlockTransform(data, inversePBox)
}

fun main() {
    // Тестові дані
    val originalData = 0x1234

    // S-блок: пряме і зворотне перетворення
    val sTransformed = sBlockTransform(originalData, S_BOX)
    val sInverseTransformed = sBlockInverseTransform(sTransformed, INV_S_BOX)

    // Перевірка
    println("Original Data: ${originalData.toString(16)}")
    println("S-Transformed: ${sTransformed.toString(16)}")
    println("S-Inverse Transformed: ${sInverseTransformed.toString(16)}")
    println("S-Block Test Passed: ${originalData == sInverseTransformed}")

    // P-блок: пряме і зворотне перетворення
    val pTransformed = pBlockTransform(originalData, P_BOX)
    val pInverseTransformed = pBlockInverseTransform(pTransformed, P_BOX)

    // Перевірка
    println("Original Data: ${originalData.toString(16)}")
    println("P-Transformed: ${pTransformed.toString(16)}")
    println("P-Inverse Transformed: ${pInverseTransformed.toString(16)}")
    println("P-Block Test Passed: ${originalData == pInverseTransformed}")
}

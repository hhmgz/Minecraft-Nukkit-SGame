fun main() {
    println(find(3, 4))
    println(find(45, 8))
}

fun find(a: Int, b: Int): Int {
    var i = 1
    var s = 3
    for (c: Int in 1..a) {
        i += s
        s += 2
    }
    i += 1
    i -= b
    return i
}
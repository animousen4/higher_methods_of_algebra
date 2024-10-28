class ElementPosition (
    val i: Int,
    val j: Int,
) {
    override fun equals(other: Any?): Boolean {
        if (other is ElementPosition) {
            return i == other.i && j == other.j
        }

        return false
    }

    override fun hashCode(): Int {
        var result = i
        result = 31 * result + j
        return result
    }
}
import org.jetbrains.kotlinx.multik.ndarray.data.D2
import org.jetbrains.kotlinx.multik.ndarray.data.NDArray

data class EquationSolution(
    val Q: NDArray<Double, D2>,
    val R: NDArray<Double, D2>,
    val X: NDArray<Double, D2>,
    val QR: NDArray<Double, D2>,
    val norm: Double
)

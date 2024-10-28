package ma.ensa.projet.beans
class Star(
    var name: String,
    var img: String,
    var star: Float
) {
    var id: Int = ++comp
        private set

    companion object {
        private var comp: Int = 0

        fun getComp(): Int = comp
        fun setComp(value: Int) {
            comp = value
        }
    }
}



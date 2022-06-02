package it.group24.lab5.webapp2.ticketcatalogue.utils

object Luhn {
    fun isValid(number: String): Boolean =
        with(number.replace(" ", "")) {
            matches("[0-9]{2,}".toRegex())
                    && reversed()
                .map { it.toString().toInt() }
                .mapEvenPositions {
                    (it * 2).let { if (it > 9) (it - 9) else it }
                }.sum().rem(10) == 0
        }
}

private fun <T> Iterable<T>.mapEvenPositions(transform: (T) -> T): Iterable<T> =
    mapIndexed { index, value -> if (index.isOdd) transform(value) else value }

private val Int.isOdd: Boolean get() = this.rem(2) == 1

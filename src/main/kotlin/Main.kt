package ua.pp.lumivoid

import org.slf4j.LoggerFactory

fun main() {
    Main.main()
}

object Main {
    private val logger = LoggerFactory.getLogger(this.javaClass)

    fun main() {
        logger.info("Starting!")
    }
}
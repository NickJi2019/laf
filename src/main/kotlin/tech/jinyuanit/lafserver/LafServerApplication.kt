package tech.jinyuanit.lafserver

import org.slf4j.LoggerFactory
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import java.net.*

@SpringBootApplication
class LafServerApplication {
    companion object {
        //        val preferences = Preferences.userRoot().node("laf")
        val log = LoggerFactory.getLogger(Companion::class.java)
        @JvmStatic
        fun main(args: Array<String>) {
            runApplication<LafServerApplication>(*args)
            log.info(Utils.findHostIP())
            val serverSocket = ServerSocket(8081)
            Thread(ConnectionHandler(serverSocket)).start()
            DataCenter.sqlQuery("SELECT * FROM data").also { println(it) }
        }
    }
}


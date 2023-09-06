package tech.jinyuanit.lafserver

import org.slf4j.LoggerFactory
import java.net.ServerSocket
import java.net.Socket


class ConnectionHandler(s: ServerSocket) : Runnable {
    val log = LoggerFactory.getLogger(this.javaClass)

    init {
        log.info("thread started:${this.javaClass}")
    }

    private val serverSocket:ServerSocket = s

    override fun run() {
        var source:Socket
        while (true){
            source = serverSocket.accept()
            Thread(DataHandler(source) ).start()
        }
    }
}
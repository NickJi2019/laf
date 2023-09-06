package tech.jinyuanit.lafserver

import org.slf4j.LoggerFactory
import tech.jinyuanit.lafserver.DataCenter.println
import java.net.Socket

class DataHandler(private var socket: Socket) :Runnable{
    val log = LoggerFactory.getLogger(this.javaClass)
    val ins = socket.getInputStream().bufferedReader()
    val ous = socket.getOutputStream().bufferedWriter()
    val help = "Welcome: LAF(lost and find)\n" +
            "GET:" + "\n"+
            "LAF GET ALL: get all of the laf data (json)"+"\n"+
            "LAF GET <id>: get one laf data with exactly id\n" +
            "PUSH:\n" +
            "LAF PUSH <data(json:[{\"id\":\"<id number>\",\"message\":\"<laf message(should include publisher information, lost-items information)>\",\"status\":\"[lost/resolved](resolved by default)\"},...])>\n" +
            "MESSAGE:\n" +
            "LAF MESSAGE <Strings(will be print)>\n"+
            "RESOLVE:\n" +
            "LAF RESOLVE <laf id(the status of laf will be turn(lost ==> found or found ==> lost))>\n" +
            "END:\n" +
            "LAF END (close the connection between the client and laf server)"
    init {
        log.info("thread started:${this.javaClass}")
    }

//    fun unSpriter(a: ArrayList<String>): String {
//        var st = ""
//        for (i in a) {
//            st += "$i "
//        }
//        st.trim()
//        return st
//    }
    fun processReceivedData(s: String): String {
        val ss = Utils.spriteString(s)
        if (ss.isEmpty()){
            return "Illegal format(1): wrong size: ${ss.size}"
        }

        if (ss[0] != "LAF") {
            return "Illegal format(2): This server do not accept any connection except LAF protocols (Lost And Find)"
        } else {
            if (ss.size<2){
                return "Illegal format(3)"
            }
            when (ss[1]) {
                "GET" -> {
                    if (ss.size < 3) {
                        return "Illegal format(4)"
                    }
                    if (ss[2] == "ALL") {
                        ous.println("LAF MESSAGE " + DataCenter.sqlQuery("SELECT * FROM data"))
                    }
                }
                "HELP"->ous.println(help)
                "PUSH" -> {
                    if (ss.size < 3) {
                        return "Illegal format(5)"
                    }
                    DataCenter.sqlQuery("")
                    ous.println("LAF MESSAGE")
                }
                "MESSAGE" -> {
                    if (ss.size < 3) {
                        return "Illegal format:MESSAGE(6)"
                    }
                    ss.removeAt(0)
                    ss.removeAt(0)
                    val mes = ss.joinToString()
                    log.info("message from client: $mes")
                }
                "RESOLVE"->{
                    if (ss.size < 3) {
                        return "Illegal format(7)"
                    }
                }
                "END"-> {
                    ous.println("connection end")
                    log.warn("connection end")
                    socket.close()
                }
                else -> {
                    return "Illegal format: Only 5 legal params: GET/PUSH/MESSAGE/RESOLVE/END\n$help"
                }
            }
        }
        return s
    }
    override fun run() {
        var data: String
        var predata:String
        var times =0
        try{
            while (true) {
                if (socket.isClosed) {
                    log.warn("closed")
                    break
                }
                if (ins.ready()) {
                    predata = ins.readLine()
                    data = processReceivedData(predata)
//                    DataCenter.outputWriter.write(data)
//                    DataCenter.outputWriter.newLine()
//                    DataCenter.outputWriter.flush()
                    log.info("from source received: $predata")
                    ous.write("LAF MESSAGE server: Data received: $data\nsha256 ${String(DataCenter.messageDigest.digest(data.toByteArray()))}\ntime:${++times}")
                    ous.newLine()
                    ous.flush()
                }
            }
        }catch (e:Exception) {
            e.printStackTrace()
        }finally {
            log.warn("socket $socket closed")
            DataCenter.clients.remove(socket)
        }
    }
}
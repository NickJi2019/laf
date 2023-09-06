package tech.jinyuanit.lafserver

import com.alibaba.fastjson2.JSON
import com.alibaba.fastjson2.JSONArray
import com.alibaba.fastjson2.JSONObject
import org.slf4j.LoggerFactory
import java.io.*
import java.net.Socket
import java.security.MessageDigest
import java.sql.DriverManager
import java.sql.ResultSet

object DataCenter {
    init {
        Class.forName("org.sqlite.JDBC")
    }

    val log = LoggerFactory.getLogger(this.javaClass)
    private val outputStream = PipedOutputStream()
    private val inputStream = PipedInputStream(outputStream)
    val messageDigest = MessageDigest.getInstance("sha256")
    val outputWriter = outputStream.bufferedWriter()
    val inputReader = inputStream.bufferedReader()
    val sqlConnection = DriverManager.getConnection("jdbc:sqlite::resource:data.db")
    val sqlStatement = sqlConnection.createStatement()
    fun sqlQuery(s:String):String{
        return sql2json(sqlStatement.executeQuery(s)).toJSONString()
    }
    val clients = ArrayList<Socket>()


    fun processBeforeTransitData(s: String): String {

        return s
    }
    fun sql2json(rs:ResultSet):JSONArray{
        val jsonArray = JSONArray()
        val metaData = rs.metaData
        val columnCount = metaData.columnCount
        while (rs.next()) {
            val obj = JSONObject()
            for (i in 1..columnCount) {
                val columnName = metaData.getColumnLabel(i)
                val value = rs.getObject(i)
                obj.put(columnName, value)
            }
            jsonArray.add(obj)
        }
        return jsonArray
    }

    fun BufferedWriter.println(str:String){
        this.write(str)
        this.newLine()
        this.flush()
    }
}
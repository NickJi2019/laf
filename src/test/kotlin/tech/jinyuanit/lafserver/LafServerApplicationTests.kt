package tech.jinyuanit.lafserver

import org.junit.jupiter.api.Test
import org.slf4j.LoggerFactory
import org.springframework.boot.test.context.SpringBootTest


@SpringBootTest
class LafServerApplicationTests {

    @Test
    fun contextLoads() {
        val log = LoggerFactory.getLogger(this.javaClass)
        val q = "DataCenter.sqlQuery(SELECT * FROM data;)"
        log.info(q)
        Utils.spriteString(q).forEach {
            print("$it ")
        }
        log.info(Utils.spriteString(q).joinToString())
    }
    @Test
    fun ip(){
        Utils.findHostIP().also { println(it) }
    }



}

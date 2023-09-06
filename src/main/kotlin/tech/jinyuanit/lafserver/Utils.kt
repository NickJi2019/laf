package tech.jinyuanit.lafserver

import org.springframework.util.StringUtils
import java.net.Inet4Address
import java.net.InetAddress
import java.net.NetworkInterface
import java.net.UnknownHostException
import java.util.*
import kotlin.collections.ArrayList


object Utils {
    fun findHostIP(): String? {
        try {
            val networks = NetworkInterface.getNetworkInterfaces()
            var addresses: Enumeration<InetAddress>
            while (networks.hasMoreElements()) {
                addresses = networks.nextElement().inetAddresses
                while (addresses.hasMoreElements()) {
                    val inetAddress = addresses.nextElement()
                    if (inetAddress is Inet4Address
                        && inetAddress.isSiteLocalAddress()
                    ) {
                        return inetAddress.getHostAddress()
                    }
                }
            }
        } catch (e: Exception) {
            // ignore;
        }
        // 兜底
        var tempIP: String? = "127.0.0.1"
        try {
            tempIP = InetAddress.getLocalHost().hostAddress
        } catch (e1: UnknownHostException) {
            // ignore
        }
        return tempIP
    }
    fun spriteString(s: String): ArrayList<String> {
        return ArrayList(s.split(" ", "\n"))
    }
}
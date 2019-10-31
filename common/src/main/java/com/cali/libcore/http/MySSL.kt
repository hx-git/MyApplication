package com.cali.libcore.http

import com.cali.common.kt.doWithTry
import java.security.SecureRandom
import java.security.cert.X509Certificate
import javax.net.ssl.*

class MySSL{
//    class MySSLSocketFactory:SSLSocketFactory(){
//        override fun getDefaultCipherSuites(): Array<String> {
//            return arrayOf()
//        }
//
//        override fun createSocket(s: Socket?, host: String?, port: Int, autoClose: Boolean): Socket {
//            val sslSocketFactory = SSLCertificateSocketFactory
//                .getDefault(0) as SSLCertificateSocketFactory
//            val sslSocket = sslSocketFactory.createSocket(s,"",port,autoClose) as SSLSocket
//            sslSocket.enabledProtocols = sslSocket.supportedProtocols
//            sslSocketFactory.setHostname(sslSocket,"")
//            return sslSocket
//        }
//
//        override fun createSocket(host: String?, port: Int): Socket {
//        }
//
//        override fun createSocket(host: String?, port: Int, localHost: InetAddress?, localPort: Int): Socket {
//        }
//
//        override fun createSocket(host: InetAddress?, port: Int): Socket {
//        }
//
//        override fun createSocket(
//            address: InetAddress?,
//            port: Int,
//            localAddress: InetAddress?,
//            localPort: Int
//        ): Socket {
//        }
//
//        override fun getSupportedCipherSuites(): Array<String> {
//            return arrayOf()
//        }
//
//    }

    companion object{
        fun createSSLSocketFactory(): SSLSocketFactory? {
            var sslFactory: SSLSocketFactory? = null
            doWithTry {
                val sc = SSLContext.getInstance("TLS")
                sc.init(null, arrayOf(TrustAllManager()), SecureRandom())
                sslFactory = sc.socketFactory
            }
            return sslFactory
        }
    }

    class TrustAllManager: X509TrustManager {
        override fun checkClientTrusted(chain: Array<out X509Certificate>?, authType: String?) {

        }

        override fun checkServerTrusted(chain: Array<out X509Certificate>?, authType: String?){

        }

        override fun getAcceptedIssuers(): Array<X509Certificate> {
            return arrayOf()
        }
    }

    class TrustAllHostNameVerifier: HostnameVerifier {
        override fun verify(hostname: String?, session: SSLSession?): Boolean {
            /**
             * IP直连替换前的域名  hostname为ip
             * 底层证书HOST校验实现
             */
//            return HttpsURLConnection.getDefaultHostnameVerifier()
//                .verify(HttpConfig.BASE_URL,session)
            return true
        }
    }
}
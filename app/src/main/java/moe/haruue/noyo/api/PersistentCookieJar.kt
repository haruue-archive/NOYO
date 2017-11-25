package moe.haruue.noyo.api

import moe.haruue.noyo.App
import okhttp3.Cookie
import okhttp3.CookieJar
import okhttp3.HttpUrl
import java.io.*


/**
 *
 * @author Haruue Icymoon haruue@caoyue.com.cn
 */
class PersistentCookieJar : CookieJar {

    private val cookieDir by lazy {
        File(App.instance.filesDir, "cookies")
    }

    private fun cookieFileForUrl(url: HttpUrl): File = File(cookieDir, url.host())

    override fun saveFromResponse(url: HttpUrl, cookies: MutableList<Cookie>) {
        val file = cookieFileForUrl(url)
        val oos = ObjectOutputStream(BufferedOutputStream(file.outputStream()))
        oos.writeObject(cookies)
    }

    override fun loadForRequest(url: HttpUrl): MutableList<Cookie> {
        val file = cookieFileForUrl(url)
        val ois = ObjectInputStream(BufferedInputStream(file.inputStream()))
        @Suppress("UNCHECKED_CAST")
        val cookies = ois.readObject() as MutableList<Cookie>?
        return cookies ?: mutableListOf()
    }

}
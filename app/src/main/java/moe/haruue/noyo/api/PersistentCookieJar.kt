package moe.haruue.noyo.api

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import moe.haruue.noyo.App
import okhttp3.Cookie
import okhttp3.CookieJar
import okhttp3.HttpUrl
import java.io.File


/**
 *
 * @author Haruue Icymoon haruue@caoyue.com.cn
 */
object PersistentCookieJar : CookieJar {

    val gson = Gson()

    private val cookieDir by lazy {
        val dir = File(App.instance.filesDir, "cookies")
        if (!dir.isDirectory) {
            dir.delete()
        }
        if (!dir.exists()) {
            dir.mkdirs()
        }
        dir
    }

    private fun cookieFileForUrl(url: HttpUrl): File = File(cookieDir, url.host())

    override fun saveFromResponse(url: HttpUrl, cookies: MutableList<Cookie>) {
        val file = cookieFileForUrl(url)
        val json = gson.toJson(cookies)
        file.writeText(json)
    }

    override fun loadForRequest(url: HttpUrl): MutableList<Cookie> {
        val file = cookieFileForUrl(url)
        var cookies: MutableList<Cookie>? = null
        if (file.exists()) {
            val json = file.readText()
            cookies = gson.fromJson(json, object : TypeToken<MutableList<Cookie>>(){}.type)
        }
        cookies = cookies ?: mutableListOf()
        return cookies
    }

    fun clearAllCookies() {
        cookieDir.listFiles()?.forEach {
            it.deleteRecursively()
        }
    }

}
package com.vocaengplus.vocaengplus.network

import com.vocaengplus.vocaengplus.util.CLIENT_ID
import com.vocaengplus.vocaengplus.util.CLIENT_SECRET
import com.vocaengplus.vocaengplus.util.SEARCH_API_URL
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.json.JSONObject
import java.io.BufferedReader
import java.io.DataOutputStream
import java.io.InputStreamReader
import java.lang.Exception
import java.net.HttpURLConnection
import java.net.URL
import java.net.URLEncoder

class SearchAPIService {

    suspend fun translate(source: String, target: String, text: String): Result<String> {
        return try {

            val text = URLEncoder.encode(text, "UTF-8")
            val url = URL(SEARCH_API_URL)
            val con = url.openConnection() as HttpURLConnection
            con.requestMethod = "POST"
            con.setRequestProperty("X-Naver-Client-Id", CLIENT_ID)
            con.setRequestProperty("X-Naver-Client-Secret", CLIENT_SECRET)
            // post request

            val postParams = "source=$source&target=$target&text=$text"
            con.doOutput = true
            val wr = DataOutputStream(con.outputStream)
            wr.writeBytes(postParams)
            wr.flush()
            wr.close()
            val responseCode = con.responseCode
            val br: BufferedReader = if (responseCode == 200) { // 정상 호출
                BufferedReader(InputStreamReader(con.inputStream))
            } else {  // 에러 발생
                BufferedReader(InputStreamReader(con.errorStream))
            }
            var inputLine: String?
            val response = StringBuffer()
            while (br.readLine().also { inputLine = it } != null) {
                response.append(inputLine)
            }
            br.close()
            val jsonObject = JSONObject(response.toString())
            val jsonObject2 =
                jsonObject.getJSONObject("message").getJSONObject("result") //결과가 들어있는 태그

            val result = jsonObject2.get("translatedText").toString()

            withContext(Dispatchers.Main) {
                Result.success(result)
            }

        } catch (e: Exception) {
            Result.failure(Exception())
        }
    }

}
package com.vocaengplus.vocaengplus.ui.search

import android.os.Bundle
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.vocaengplus.vocaengplus.R
import com.vocaengplus.vocaengplus.databinding.ActivitySearchBinding
import com.vocaengplus.vocaengplus.databinding.NaveraddwordBinding
import com.vocaengplus.vocaengplus.databinding.NaverhelpBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.json.JSONObject
import java.io.BufferedReader
import java.io.DataOutputStream
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URL
import java.net.URLEncoder

@AndroidEntryPoint
class SearchActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySearchBinding
    private val searchViewModel: SearchViewModel by viewModels()
    var isKortoEng = true

    val scope = CoroutineScope(Dispatchers.IO)

    private val helpDialog: AlertDialog by lazy {
        val dlgBinding = NaverhelpBinding.inflate(layoutInflater)
        AlertDialog.Builder(this)
            .setView(dlgBinding.root)
            .setNeutralButton("확인") { _, _ ->
                {

                }
            }.create()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySearchBinding.inflate(layoutInflater)
        setContentView(binding.root)

        init()
        initNaver()
    }

    private fun init() {

        imagebtn.setOnClickListener {
            if (isKortoEng) {
                source.text = "영어"
                target.text = "한국어"
                isKortoEng = false
            } else {
                source.text = "한국어"
                target.text = "영어"
                isKortoEng = true
            }
        }

        binding.run {
            helpButton.setOnClickListener {
                helpDialog.show()
            }

            switchImageButton.setOnClickListener {
                viewmodel
            }

            searchButton.setOnClickListener {

            }
        }


    }

    private fun initNaver() {
        val searchbtn = findViewById<Button>(R.id.searchButton)
        val searchresult = findViewById<TextView>(R.id.searchResultTextView)
        var result: String = ""

        searchbtn.setOnClickListener {
            val inputword = findViewById<EditText>(R.id.inputWordEditText)
            if (isKortoEng) {
                if (!validation.isValidateMeaning(inputword.text.toString())) {
                    Toast.makeText(this, "한국어 뜻 입력이 올바르지 않아 번역을 진행할 수 없습니다.", Toast.LENGTH_SHORT)
                        .show()
                    return@setOnClickListener
                }
            } else {
                if (!validation.isValidateWord(inputword.text.toString())) {
                    Toast.makeText(this, "영단어 입력이 올바르지 않아 번역을 진행할 수 없습니다.", Toast.LENGTH_SHORT)
                        .show()
                    return@setOnClickListener
                }
            }

            scope.launch {
                try {
                    val text = URLEncoder.encode(inputword.text.toString(), "UTF-8")
                    val apiURL = "https://openapi.naver.com/v1/papago/n2mt"
                    val url = URL(apiURL)
                    val con = url.openConnection() as HttpURLConnection
                    con.requestMethod = "POST"
                    con.setRequestProperty("X-Naver-Client-Id", clientId)
                    con.setRequestProperty("X-Naver-Client-Secret", clientSecret)
                    // post request

                    val postParams = if (isKortoEng) {
                        "source=ko&target=en&text=$text"
                    } else {
                        "source=en&target=ko&text=$text"
                    }
                    con.doOutput = true
                    val wr = DataOutputStream(con.outputStream)
                    wr.writeBytes(postParams)
                    wr.flush()
                    wr.close()
                    val responseCode = con.responseCode
                    val br: BufferedReader
                    br = if (responseCode == 200) { // 정상 호출
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
                    println(response.toString())
                    val jsonObject = JSONObject(response.toString())
                    val jsonObject2 =
                        jsonObject.getJSONObject("message").getJSONObject("result") //결과가 들어있는 태그

                    result = jsonObject2.get("translatedText").toString()
                    if (isKortoEng) {
                        result = result.replace(".", "") //.빼기
                    }

                } catch (e: Exception) {
                    println(e)
                }

                withContext(Dispatchers.Main) {
                    val resultarea = findViewById<TextView>(R.id.searchResultTextView)
                    resultarea.text = result

                    addWord()

                    val manager: InputMethodManager =
                        getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
                    manager.hideSoftInputFromWindow(
                        currentFocus!!.windowToken,
                        InputMethodManager.HIDE_NOT_ALWAYS
                    )

                }
            }


        }

    }

    private fun addWord() {
        val naveraddbtn = findViewById<TextView>(R.id.wordAddButton)
        naveraddbtn.setOnClickListener {
            val dlgBinding = NaveraddwordBinding.inflate(layoutInflater)
            if (isKortoEng) {
                if (findViewById<TextView>(R.id.searchResultTextView).text.toString().length <= 1) {
                    Toast.makeText(this, "영단어가 올바르지 않아 추가할 수 없습니다", Toast.LENGTH_SHORT).show()
                    return@setOnClickListener
                }
                if (!validation.isValidateWord(findViewById<TextView>(R.id.searchResultTextView).text.toString())) {
                    Toast.makeText(this, "영단어 입력이 올바르지 않아 번역을 진행할 수 없습니다.", Toast.LENGTH_SHORT)
                        .show()
                    return@setOnClickListener
                }

                dlgBinding.naveraddword.text =
                    findViewById<TextView>(R.id.searchResultTextView).text
                if (dlgBinding.naveraddword.text.toString().length > 40) {
                    dlgBinding.naveraddword.text =
                        dlgBinding.naveraddword.text.toString().substring(0, 41)
                }
                dlgBinding.naveraddmeaning.text =
                    findViewById<EditText>(R.id.inputWordEditText).text
            } else {
                if (!validation.isValidateMeaning(findViewById<TextView>(R.id.searchResultTextView).text.toString())) {
                    Toast.makeText(this, "한국어 뜻 입력이 올바르지 않아 번역을 진행할 수 없습니다.", Toast.LENGTH_SHORT)
                        .show()
                    return@setOnClickListener
                }

                dlgBinding.naveraddword.text = findViewById<EditText>(R.id.inputWordEditText).text
                dlgBinding.naveraddmeaning.text =
                    findViewById<TextView>(R.id.searchResultTextView).text
                if (dlgBinding.naveraddmeaning.text.toString().length > 40) {
                    dlgBinding.naveraddmeaning.text =
                        dlgBinding.naveraddmeaning.text.toString().substring(0, 41)
                }
            }
            val dlg = SearchAddDiaglog(dlgBinding.root)

            dlg.show(supportFragmentManager, "NaverAddDialog")
        }
    }
}
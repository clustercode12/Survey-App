package com.andresnp.surveyapp

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.content.res.Configuration
import android.os.Build
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v4.app.ActivityCompat
import android.support.v4.content.ContextCompat
import android.util.Log
import android.view.View
import android.widget.*
import java.util.*

class MainActivity : AppCompatActivity() {

    private var arrayTb = arrayOf(R.id.tbQuestion3, R.id.tbQuestion7b, R.id.tbQuestion8, R.id.tbQuestion17, R.id.tbQuestion22, R.id.tbQuestion45)
    private var arraySp = arrayOf(R.id.spSex, R.id.spQuestion2, R.id.spQuestion3, R.id.spQuestion4, R.id.spQuestion5, R.id.spQuestion6, R.id.spQuestion9, R.id.spQuestion10, R.id.spQuestion11, R.id.spQuestion13, R.id.spQuestion14, R.id.spQuestion15, R.id.spQuestion16b, R.id.spQuestion19, R.id.spQuestion24, R.id.spQuestion25, R.id.spQuestion26, R.id.spQuestion27, R.id.spQuestion28, R.id.spQuestion29, R.id.spQuestion30, R.id.spQuestion32, R.id.spQuestion33, R.id.spQuestion34, R.id.spQuestion35, R.id.spQuestion36, R.id.spQuestion37, R.id.spQuestion38, R.id.spQuestion39, R.id.spQuestion40, R.id.spQuestion41, R.id.spQuestion42, R.id.spQuestion43, R.id.spQuestion44, R.id.spQuestion46, R.id.spQuestion47)
    private var arrayEt = arrayOf(R.id.etColonyName, R.id.etInterviewedName, R.id.etAge, R.id.etQuestion1, R.id.etQuestion16b, R.id.etQuestion21, R.id.etQuestion22b, R.id.etQuestion23, R.id.etQuestion28, R.id.etQuestion29, R.id.etQuestion31, R.id.etQuestion47, R.id.etQuestion48)
    private var arrayCb = arrayOf(R.id.cbQuestion7_1, R.id.cbQuestion7_2, R.id.cbQuestion7_3, R.id.cbQuestion7_4, R.id.cbQuestion7_5, R.id.cbQuestion7_6, R.id.cbQuestion12_1, R.id.cbQuestion12_2, R.id.cbQuestion12_3, R.id.cbQuestion12_4, R.id.cbQuestion12_5, R.id.cbQuestion12_6, R.id.cbQuestion12_7, R.id.cbQuestion12_8, R.id.cbQuestion16_1, R.id.cbQuestion16_2, R.id.cbQuestion16_3, R.id.cbQuestion16_4, R.id.cbQuestion16_5, R.id.cbQuestion16_6, R.id.cbQuestion16_7, R.id.cbQuestion16_8, R.id.cbQuestion16_9, R.id.cbQuestion16_10, R.id.cbQuestion18_1, R.id.cbQuestion18_2, R.id.cbQuestion18_3, R.id.cbQuestion18_4, R.id.cbQuestion18_5, R.id.cbQuestion20_1, R.id.cbQuestion20_2, R.id.cbQuestion20_3, R.id.cbQuestion20_4, R.id.cbQuestion20_5, R.id.cbQuestion21_1, R.id.cbQuestion21_2, R.id.cbQuestion21_3, R.id.cbQuestion21_4, R.id.cbQuestion21_5, R.id.cbQuestion21_6, R.id.cbQuestion21_7)

    private var tb = arrayOfNulls<ToggleButton>(arrayTb.size)
    private var sp = arrayOfNulls<Spinner>(arraySp.size)
    private var et = arrayOfNulls<EditText>(arrayEt.size)
    private var cb = arrayOfNulls<CheckBox>(arrayCb.size)

    private val requestCode = 101
    private var language = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        language = Locale.getDefault().language

        requestPermissions()
        initialize()
    }

    private fun initialize() {
        for (i in arrayTb.indices) {
            tb[i] = findViewById(arrayTb[i])
        }

        for (i in arraySp.indices) {
            sp[i] = findViewById(arraySp[i])
        }

        for (i in arrayEt.indices) {
            et[i] = findViewById(arrayEt[i])
        }

        for (i in arrayCb.indices) {
            cb[i] = findViewById(arrayCb[i])
        }

        listeners()
    }

    private fun requestPermissions() {
        val permission = ContextCompat.checkSelfPermission(this,
            Manifest.permission.WRITE_EXTERNAL_STORAGE)

        if (permission != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(this,
                arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE),
                requestCode)
        }
    }

    fun saveAndExportClicked(v: View) {
        val questions: Array<String?> = getArrayOfQuestions()

        questions.forEachIndexed { index, it ->
            Log.i("Test", "${index+1} $it")

        }

        if (checkIfFieldsArePopulated(questions)) {
            val dbHandler = DBHandler(this, null, null, 1)

            val survey = Survey(
                et[0]?.text.toString(),
                et[1]?.text.toString(),
                Integer.parseInt(et[2]?.text.toString()),
                sp[0]?.selectedItem.toString(),
                questions
            )

            dbHandler.addSurvey(survey)
            exportTable()

            setContentView(R.layout.activity_main)
            initialize()

            Toast.makeText(this, getString(R.string.result_survey_saved), Toast.LENGTH_LONG).show()
        } else {
            Toast.makeText(this, getString(R.string.result_not_fields_populated), Toast.LENGTH_LONG).show()
        }
    }

    private fun exportTable() {
        val dbHandler = DBHandler(this, null, null, 1)

        dbHandler.exportTable(this)
    }

    fun changeLanguageClicked(v: View) {
    }

    private fun changeLanguage(context: Context, language: String) {

    }

    private fun checkIfFieldsArePopulated(questions: Array<String?>): Boolean {
        if (et[0]?.text.toString().isEmpty() || et[1]?.text.toString().isEmpty() || et[2]?.text.toString().isEmpty()) {
            return false
        }

        for (i in questions.indices) {
            if (questions[i]!!.isEmpty() && i != (questions.size - 1)) {
                return false
            }
        }

        return true
    }

    //This shorts out all the questions and gathers them in one array
    private fun getArrayOfQuestions() : Array<String?> {
        val numberQuestions = 48
        val questions: Array<String?> = arrayOfNulls<String?>(numberQuestions)

        for (i in 1..numberQuestions) {
            when (i) {
                // Only edittexts
                1 -> questions[i-1] = et[3]?.text.toString()
                23 -> questions[i-1] = et[7]?.text.toString()
                31 -> questions[i-1] = et[10]?.text.toString()
                48 -> questions[i-1] = et[12]?.text.toString()

                // Only spinners
                2, 4, 5, 6 -> questions[i-1] = sp[i-1]?.selectedItem.toString()
                in 9..11 -> questions[i-1] = sp[i-3]?.selectedItem.toString()
                in 13..15 -> questions[i-1] = sp[i-4]?.selectedItem.toString()
                19 -> questions[i-1] = sp[13]?.selectedItem.toString()
                in 24..30 -> if (i != 28 || i != 29)questions[i-1] = sp[i-10]?.selectedItem.toString()
                in 32..44 -> questions[i-1] = sp[i-11]?.selectedItem.toString()
                46 -> questions[i-1] = sp[i-12]?.selectedItem.toString()

                28 -> questions[i-1] = getAnswerFromSnipperWithEditText(i)
                29 -> questions[i-1] = getAnswerFromSnipperWithEditText(i)
                47 -> questions[i-1] = getAnswerFromSnipperWithEditText(i)

                // CheckBox
                7 -> questions[i-1] = getAnswerFromCheckBox(0,5, i)
                12 -> questions[i-1] = getAnswerFromCheckBox(7,14, i)
                16 -> questions[i-1] = getAnswerFromCheckBox(15,24, i)
                18 -> questions[i-1] = getAnswerFromCheckBox(25,29, i)
                20 -> questions[i-1] = getAnswerFromCheckBox(30,34, i)
                21 -> questions[i-1] = getAnswerFromCheckBox(35,41, i)

                // ToggleButtons
                3 -> questions[i-1] = getAnswerFromToggleButton(i, 0)
                8 -> questions[i-1] = getAnswerFromToggleButton(i, 2)
                17 -> questions[i-1] = getAnswerFromToggleButton(i, 3)
                22 -> questions[i-1] = getAnswerFromToggleButton(i, 4)
                45 -> questions[i-1] = getAnswerFromToggleButton(i, 5)
            }
        }

        return questions
    }

    //This gets the answer when the question can be no, predetermined answers, or other
    private fun getAnswerFromCheckBox(start: Int, end: Int, question: Int) : String{
        var answer = ""

        for (i in start..end) {
            var a = i
            if (i == 41) a -= 1

            if (cb[a]!!.isChecked) {
                if (answer.isNotEmpty()) answer += ", "

                answer += cb[a]!!.text.toString()
            }

        }

        if (question == 7) {
            if (answer.isNotEmpty()) {
                answer += " || "
                answer += tb[1]!!.text.toString()
            }
        } else if (question == 16 && et[4]!!.text.isNotEmpty()) {
            if (answer.isNotEmpty()) {
                answer += " || "
                answer += "${et[4]!!.text} ${sp[12]!!.selectedItem}"
            }
        } else if (question == 21 && et[5]!!.text.isNotEmpty()) {
            if (answer.isNotEmpty()) answer += ", "

            answer += et[5]!!.text.toString()
        }

        return answer
    }

    private fun getAnswerFromToggleButton(question: Int, tbInt: Int): String {
        var answer = ""

        if (question == 3) {
            answer = if (tb[tbInt]!!.isChecked) sp[2]!!.selectedItem.toString()
            else tb[tbInt]!!.text.toString()
        } else if (question == 8 || question == 17 || question == 45) {
            answer = tb[tbInt]!!.text.toString()
        } else if (question == 22) {
            answer = if (tb[tbInt]!!.isChecked) et[6]!!.text.toString()
            else tb[tbInt]!!.text.toString()
        }

        return answer
    }

    private fun getAnswerFromSnipperWithEditText(question: Int) : String {
        var answer = ""

        when (question) {
            28 -> {
                answer = if (sp[18]!!.selectedItemPosition == 9) et[8]!!.text.toString()
                else sp[18]!!.selectedItem.toString()
            }
            29 -> {
                answer = if (sp[19]!!.selectedItemPosition == 9) et[9]!!.text.toString()
                else sp[19]!!.selectedItem.toString()
            }
            47 -> {
                answer = if (sp[35]!!.selectedItemPosition == 5) et[11]!!.text.toString()
                else sp[35]!!.selectedItem.toString()
            }
        }

        return answer
    }

    private fun listeners() {
        //Question 3
        tb[0]?.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                sp[2]?.visibility = View.VISIBLE
            } else {
                sp[2]?.visibility = View.GONE
            }
        }
        //Question 22
        tb[4]?.setOnCheckedChangeListener { _, isChecked ->
            val txt = findViewById<TextView>(R.id.txtQuestion22b)
            if (isChecked) {
                et[6]?.visibility = View.VISIBLE
                txt.visibility = View.VISIBLE
            } else {
                et[6]?.visibility = View.GONE
                txt.visibility = View.GONE
            }
        }

        //Question 28
        sp[18]?.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
            override fun onNothingSelected(parent: AdapterView<*>?) {}
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                if (position == 9) {
                    et[8]?.visibility = View.VISIBLE
                } else {
                    et[8]?.visibility = View.GONE
                }
            }
        }

        //Question 29
        sp[19]?.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
            override fun onNothingSelected(parent: AdapterView<*>?) {}
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                if (position == 7) {
                    et[9]?.visibility = View.VISIBLE
                } else {
                    et[9]?.visibility = View.GONE
                }
            }
        }

        //Question 47
        sp[35]?.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
            override fun onNothingSelected(parent: AdapterView<*>?) {}
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                if (position == 5) {
                    et[11]?.visibility = View.VISIBLE
                } else {
                    et[11]?.visibility = View.GONE
                }
            }
        }
    }
}

// tbQuestion3 -> 0
// tbQuestion7b -> 1
// tbQuestion8 -> 2
// tbQuestion17 -> 3
// tbQuestion22 -> 4
// tbQuestion45 -> 5

// spSex -> 0
// spQuestion2 -> 1
// spQuestion3 -> 2
// spQuestion4 -> 3
// spQuestion5 -> 4
// spQuestion6 -> 5
// spQuestion9 -> 6
// spQuestion10 -> 7
// spQuestion11 -> 8
// spQuestion13 -> 9
// spQuestion14 -> 10
// spQuestion15 -> 11
// spQuestion16b -> 12
// spQuestion19 -> 13
// spQuestion24 -> 14
// spQuestion25 -> 15
// spQuestion26 -> 16
// spQuestion27 -> 17
// spQuestion28 -> 18
// spQuestion29 -> 19
// spQuestion30 -> 20
// spQuestion32 -> 21
// spQuestion33 -> 22
// spQuestion34 -> 23
// spQuestion35 -> 24
// spQuestion36 -> 25
// spQuestion37 -> 26
// spQuestion38 -> 27
// spQuestion39 -> 28
// spQuestion40 -> 29
// spQuestion41 -> 30
// spQuestion42 -> 31
// spQuestion43 -> 32
// spQuestion44 -> 33
// spQuestion46 -> 34
// spQuestion47 -> 35

// etColonyName -> 0
// etInterviewedName -> 1
// etAge -> 2
// etQuestion1 -> 3
// etQuestion16b -> 4
// etQuestion21 -> 5
// etQuestion22b -> 6
// etQuestion23 -> 7
// etQuestion28 -> 8
// etQuestion29 -> 9
// etQuestion31 -> 10
// etQuestion47 -> 11
// etQuestion48 -> 12

// cbQuestion7_1 -> 0
// cbQuestion7_2 -> 1
// cbQuestion7_3 -> 2
// cbQuestion7_4 -> 3
// cbQuestion7_5 -> 4
// cbQuestion7_6 -> 5

// cbQuestion12_1 -> 7
// cbQuestion12_2 -> 8
// cbQuestion12_3 -> 9
// cbQuestion12_4 -> 10
// cbQuestion12_5 -> 11
// cbQuestion12_6 -> 12
// cbQuestion12_7 -> 13
// cbQuestion12_8 -> 14
// cbQuestion16_1 -> 15
// cbQuestion16_2 -> 16
// cbQuestion16_3 -> 17
// cbQuestion16_4 -> 18
// cbQuestion16_5 -> 19
// cbQuestion16_6 -> 20
// cbQuestion16_7 -> 21
// cbQuestion16_8 -> 22
// cbQuestion16_9 -> 23
// cbQuestion16_10 -> 24
// cbQuestion18_1 -> 25
// cbQuestion18_2 -> 26
// cbQuestion18_3 -> 27
// cbQuestion18_4 -> 28
// cbQuestion18_5 -> 29
// cbQuestion20_1 -> 30
// cbQuestion20_2 -> 31
// cbQuestion20_3 -> 32
// cbQuestion20_4 -> 33
// cbQuestion20_5 -> 34
// cbQuestion21_1 -> 35
// cbQuestion21_2 -> 36
// cbQuestion21_3 -> 37
// cbQuestion21_4 -> 38
// cbQuestion21_5 -> 39
// cbQuestion21_6 -> 40
// cbQuestion21_7 -> 41
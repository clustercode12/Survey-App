package com.andresnp.surveyapp

class Survey(colonyName: String, interviewedName: String, age: Int, sex: String, var questions: Array<String?>) {
    var colonyName: String? = colonyName
    var interviewedName: String? = interviewedName
    var age: Int? = age
    var sex: String? = sex

}
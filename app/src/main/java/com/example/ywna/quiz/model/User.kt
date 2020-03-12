package com.example.ywna.quiz.model

import org.parceler.Parcel
import org.parceler.ParcelConstructor
import org.parceler.ParcelProperty

@Parcel
data class User @ParcelConstructor
constructor(@ParcelProperty("key") var key: String,
            @ParcelProperty("name") var name: String,
            @ParcelProperty("email") var email: String,
            @ParcelProperty("totalScore") var totalScore: Int,
            @ParcelProperty("attemptQuestion") var attemptQuestion: Boolean,
            @ParcelProperty("userType") var userType: String,
            @ParcelProperty("weeklyScore") var weeklyScore: Int,
            @ParcelProperty("attemptQuestionNo") var attemptQuestionNo: Int){
    constructor(): this("","","",0, false, "user", 0, 0)

    fun copy(user: User) {
        key = user.key
        name = user.name
        email = user.email
        totalScore = user.totalScore
        attemptQuestion = user.attemptQuestion
        userType = user.userType
        weeklyScore = user.weeklyScore
        attemptQuestionNo = user.attemptQuestionNo
    }

    companion object {
        val BY_TOTAL_SCORE: Comparator<User> = Comparator { user, t1 -> user.totalScore.compareTo(t1.totalScore) }
        val BY_WEEKLY_SCORE: Comparator<User> = Comparator { user, t1 -> user.weeklyScore.compareTo(t1.weeklyScore) }
    }
}
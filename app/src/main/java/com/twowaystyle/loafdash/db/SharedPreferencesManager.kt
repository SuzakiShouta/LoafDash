package com.twowaystyle.loafdash.db

import android.content.Context
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.twowaystyle.loafdash.model.SNSProperty

class SharedPreferencesManager(private val context: Context) {

    private val sharedPreferences = context.getSharedPreferences("MyPreferences", Context.MODE_PRIVATE)

    fun setUserId(userId: String){
        val key = "userId"
        saveString(key, userId)
    }

    fun getUserId(): String {
        val key = "userId"
        return getString(key, "")
    }

    fun setSNSProperties(snsProperties: List<SNSProperty>){
        val key = "snsProperties"
        saveSNSProperties(key, snsProperties)
    }

    fun getSNSProperties(): List<SNSProperty> {
        val key = "snsProperties"
        return getSNSProperties(key, """[]""")
    }

    fun setProfile(profile: String){
        val key = "profile"
        saveString(key, profile)
    }

    fun getProfile(): String {
        val key = "profile"
        return getString(key, "")
    }

    fun setKeepUserIds(keepUsers: Array<String>) {
        val key = "keepUserIds"
        saveStringArray(key, keepUsers)
    }

    fun getKeepUserIds(): Array<String> {
        val key = "keepUserIds"
        return getStringArray(key, arrayOf(""))
    }

    fun setPastEncounterUserIds(pastEncounterUsers: Array<String>) {
        val key = "pastEncounterUserIds"
        saveStringArray(key, pastEncounterUsers)
    }

    fun getPastEncounterUserIds(): Array<String> {
        val key = "pastEncounterUserIds"
        return getStringArray(key, arrayOf(""))
    }


    private fun saveString(key: String, value: String) {
        sharedPreferences.edit().putString(key, value).apply()
    }

    private fun getString(key: String, defaultValue: String): String {
        return sharedPreferences.getString(key, defaultValue) ?: defaultValue
    }

    // 一回JSONに変換してStringとして扱う
    private fun saveSNSProperties(key: String, value: List<SNSProperty>) {
        val jsonString = Gson().toJson(value)
        sharedPreferences.edit().putString(key, jsonString).apply()
    }

    private fun getSNSProperties(key: String, defaultValue: String): List<SNSProperty> {
        val jsonString = sharedPreferences.getString(key, defaultValue) ?: defaultValue
        val type = object : TypeToken<List<SNSProperty>>() {}.type
        return Gson().fromJson(jsonString, type)
    }

    private fun saveStringArray(key: String, stringArrayValue: Array<String>) {
        val stringSet = stringArrayValue.toSet()
        sharedPreferences.edit().putStringSet(key, stringSet).apply()
    }

    private fun getStringArray(key: String, defaultValue: Array<String>): Array<String> {
        val stringSet = sharedPreferences.getStringSet(key, emptySet()) ?: emptySet()
        return stringSet.toTypedArray()
    }
}
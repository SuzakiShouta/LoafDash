package com.twowaystyle.loafdash.db

import android.content.Context

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

    fun setKeepUsers(keepUsers: Array<String>) {
        val key = "keepUsers"
        saveStringArray(key, keepUsers)
    }

    fun getKeepUsers(): Array<String> {
        val key = "keepUsers"
        return getStringArray(key, arrayOf())
    }

    fun setPastEncounterUsers(pastEncounterUsers: Array<String>) {
        val key = "pastEncounterUsers"
        saveStringArray(key, pastEncounterUsers)
    }

    fun getPastEncounterUsers(): Array<String> {
        val key = "pastEncounterUsers"
        return getStringArray(key, arrayOf())
    }


    private fun saveString(key: String, value: String) {
        sharedPreferences.edit().putString(key, value).apply()
    }

    private fun getString(key: String, defaultValue: String): String {
        return sharedPreferences.getString(key, defaultValue) ?: defaultValue
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
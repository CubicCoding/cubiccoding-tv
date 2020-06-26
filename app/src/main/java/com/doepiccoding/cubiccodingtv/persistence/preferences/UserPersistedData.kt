package com.doepiccoding.cubiccodingtv.persistence.preferences

import android.content.Context
import android.content.SharedPreferences
import com.doepiccoding.cubiccodingtv.CubicCodingTVApplication
import java.util.*


object UserPersistedData {

    private var userSharedPrefs: SharedPreferences? = null
    private const val USER_PERSISTED_MODEL = "user.persisted.model"
    private const val EMAIL_KEY = "email.key"
    private const val USERNAME_KEY = "username.key"
    private const val NAME_KEY = "names.key"
    private const val FIRST_SURNAME_KEY = "first.surname.key"
    private const val SECOND_SURNAME_KEY = "second.surname.key"
    private const val AVATAR_KEY = "avatar.key"
    private const val COURSE_NAME = "course.key"
    private const val CLASSROOM_NAME = "classroom.key"
    private const val CREATED_DATE_KEY = "created.date.key"
    private const val TOKEN_KEY = "token.key"
    private const val LOGGED_KEY = "is.logged.key"
    private const val FIREBASE_TOKEN_KEY = "firebase.token.key"
    val calendar = Calendar.getInstance(TimeZone.getTimeZone("UTC"))

    var email: String
        get() {
            checkDevicePreferenceInit();return userSharedPrefs?.getString(
                EMAIL_KEY, ""
            ) ?: ""
        }
        set(value) {
            savePref {
                putString(
                    EMAIL_KEY,
                    value
                )
            }
        }

    var username: String
        get() {
            checkDevicePreferenceInit();
            return userSharedPrefs?.getString(
                USERNAME_KEY, "") ?: ""
        }
        set(value) {
            savePref {
                putString(
                    USERNAME_KEY,
                    value
                )
            }
        }

    var name: String
        get() {
            checkDevicePreferenceInit();return userSharedPrefs?.getString(
                NAME_KEY, ""
            ) ?: ""
        }
        set(value) {
            savePref {
                putString(
                    NAME_KEY,
                    value
                )
            }
        }

    var firstSurname: String
        get() {
            checkDevicePreferenceInit();return userSharedPrefs?.getString(
                FIRST_SURNAME_KEY, ""
            ) ?: ""
        }
        set(value) {
            savePref {
                putString(
                    FIRST_SURNAME_KEY,
                    value
                )
            }
        }

    var secondSurname: String
        get() {
            checkDevicePreferenceInit();return userSharedPrefs?.getString(
                SECOND_SURNAME_KEY, ""
            ) ?: ""
        }
        set(value) {
            savePref {
                putString(
                    SECOND_SURNAME_KEY,
                    value
                )
            }
        }

    var avatar: String
        get() {
            checkDevicePreferenceInit();return userSharedPrefs?.getString(
                AVATAR_KEY, ""
            ) ?: ""
        }
        set(value) {
            savePref {
                putString(
                    AVATAR_KEY,
                    value
                )
            }
        }

    var courseName: String
        get() {
            checkDevicePreferenceInit();return userSharedPrefs?.getString(
                COURSE_NAME, ""
            ) ?: ""
        }
        set(value) {
            savePref {
                putString(
                    COURSE_NAME,
                    value
                )
            }
        }

    var classroomName: String
        get() {
            checkDevicePreferenceInit();return userSharedPrefs?.getString(
                CLASSROOM_NAME, ""
            ) ?: ""
        }
        set(value) {
            savePref {
                putString(
                    CLASSROOM_NAME,
                    value
                )
            }
        }

    var createdDate: String
        get() {
            checkDevicePreferenceInit();return userSharedPrefs?.getString(
                CREATED_DATE_KEY, ""
            ) ?: ""
        }
        set(value) {
            savePref {
                putString(
                    CREATED_DATE_KEY,
                    value
                )
            }
        }

    var ccToken: String
        get() {
            checkDevicePreferenceInit();return userSharedPrefs?.getString(
                TOKEN_KEY, ""
            ) ?: ""
        }
        set(value) {
            savePref {
                putString(
                    TOKEN_KEY,
                    value
                )
            }
        }

    var firebaseToken: String
        get() {
            checkDevicePreferenceInit();return userSharedPrefs?.getString(
                FIREBASE_TOKEN_KEY, "") ?: ""
        }
        set(value) {
            savePref {
                putString(
                    FIREBASE_TOKEN_KEY,
                    value
                )
            }
        }

    var isLogged: Boolean
        get() {
            checkDevicePreferenceInit();return userSharedPrefs?.getBoolean(
                LOGGED_KEY, false) ?: false
        }
        set(value) {
            savePref {
                putBoolean(
                    LOGGED_KEY,
                    value
                )
            }
        }

    private fun savePref(codeBlock: SharedPreferences.Editor.() -> Unit) {
        checkDevicePreferenceInit()
        val editor = userSharedPrefs?.edit()
        editor?.apply { codeBlock(this) }
        editor?.apply()
    }

    @Synchronized
    private fun checkDevicePreferenceInit() {
        if (userSharedPrefs == null) {
            val context = CubicCodingTVApplication.instance
            userSharedPrefs = context.getSharedPreferences(
                USER_PERSISTED_MODEL, Context.MODE_PRIVATE
            )
        }
    }
}

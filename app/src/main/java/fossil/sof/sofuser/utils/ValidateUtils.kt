package fossil.sof.sofuser.utils

import android.util.Patterns
import fossil.sof.sofuser.application.Constants
import java.util.regex.Pattern

/**
 * Created by ninhvanluyen on 16/11/18.
 */
object ValidateUtils {
    private val USERNAME_PATTERN_FOR_LOGIN = "^[a-z0-9A-Z]*$"
    private val TOPUP_CARD = "^[a-z0-9A-Z]{6,32}$"

    fun isEmpty(string: String?): Boolean {
        if (string == null || string.trim().isEmpty())
            return true
        return false
    }

    fun validatePassword(password: String?): Boolean {
        if (!isEmpty(password) && password!!.length >= Constants.PASSWORD_MIN_LENGTH && password.length <= Constants.PASSWORD_MAX_LENGTH)
            return true
        return false
    }

    fun validateUsername(userName: String?): Boolean {
        if (isEmpty(userName)) {
            return false
        }
        if (userName!!.trim({ it <= ' ' }).length > Constants.USERNAME_MAX_LENGTH) {
            return false
        }
        val userNamePatternForLogin = Pattern.compile(USERNAME_PATTERN_FOR_LOGIN)
        val userNameMatcher = userNamePatternForLogin.matcher(userName)
        return userNameMatcher.matches()
    }

    fun validateOTPPhonebill(otp: String): Boolean {
        return Pattern.matches("^[1-9]{4}", otp)
    }

    fun validatePhone(phone: String?): Boolean {
        if (isEmpty(getValidPhone(phone)))
            return false
        return true
    }

    fun getValidPhone(phone: String?): String {
        if (!isEmpty(phone)) {
            val validPhone = phone!!.replace(" ", "")
            if (Patterns.PHONE.matcher(validPhone).matches() &&
                    validPhone.length >= Constants.PHONE_MIN_LENGTH &&
                    validPhone.length <= Constants.PHONE_MAX_LENGTH)
                return validPhone
        }
        return Constants.EMPTY
    }

    fun validateEmail(email: String?): Boolean {
        if (isEmpty(getValidEmail(email)))
            return false
        return true
    }

    fun getValidEmail(email: String?): String {
        if (!isEmpty(email)) {
            val validEmail = email!!.replace(" ", "")
            if (Patterns.EMAIL_ADDRESS.matcher(validEmail).matches())
                return validEmail
        }
        return Constants.EMPTY
    }

    fun validatePaymentPin(pin: String?): Boolean {
        if (pin == null || pin.isEmpty()) {
            return false
        }
        if (pin.length < 6) {
            return false
        }
        if (pin.length > 32) {
            return false
        }
        val topupPattern = Pattern.compile(TOPUP_CARD)
        val topupMatcher = topupPattern.matcher(pin)
        return topupMatcher.matches()
    }
}
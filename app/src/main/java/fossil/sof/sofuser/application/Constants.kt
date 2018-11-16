package fossil.sof.sofuser.application

object Configs {
    val IS_DEBUG = true
    val SERVER_URL = "https://unsplash.com/napi/"

    val CONNECTION_TIME_OUT: Long = 30
    val READ_TIME_OUT: Long = 35
}

object Constants {
    val EMPTY = ""
    val CHARSET = "UTF-8"
    val UNKNOWN = "unknown"

    val PASSWORD_MAX_LENGTH = 64
    val PASSWORD_MIN_LENGTH = 6
    val PHONE_MAX_LENGTH = 11
    val PHONE_MIN_LENGTH = 9
    val USERNAME_MAX_LENGTH = 64


    val FIRST_PAGE = 1
    val LAST_PAGE = "-1"
    val PAGE_LENGTH = 20
    var isFHD = false
    var INDEX = -1
}


object LanguageID {
    val ENG = "en"
    val MY = "my"
}

object ErrorCodes {
    val SERVER_MAINTENANCE = -8000
    val UPDATE_NEW_VERSION = -8001
    val NET_WORK_PROBLEM = -8002
    val ERROR_MESSAGE = -8003
    val NO_DATA_RESULT = -8004
    val NULL_FILE = -8005
    val INVALID_TOKEN = -1504
    val INVALID_API = -15033

}

object ErrorMessage {
    val NET_WORK_PROBLEM = "Please check your internet connection."
    val NO_DATA_RESULT = "System error. Please try again later."
    val NULL_FILE = "Can not find this file in your storage."
}

object PreferencesKeys {

    // UI
    val LANGUAGE_ID = "language_id"

    // App
    val FACEBOOK_APP_ID = "FACEBOOK_APP_ID"
    val CURRENCY = "CURRENCY"
    val FAN_PAGE = "FAN_PAGE"
    val APP_CONFIG = "APP_CONFIG"

    val USER_TOKEN = "GUEST_TOKEN"
    val SOURCE_CHOOSE = "SOURCE_CHOOSE"
    val INTERVAL = "INTERVAL_AUTO_CHANGE"
    val ENABLE_AUTO_SOURCE = "AUTO_CHANGE_FROM_SOURCE"
    val ENABLE_AUTO_AI = "AUTO_CHANGE_FROM_AI"
    val WIFI_ONLY = "WIFI_ONLY"
    val UUID = "UUID"
    val DEVICE_ID = "DEVICE_ID"
    val ADVID = "ADVID"
    val GCM_ID = "gcm_id"
    val LOGIN_TYPE = "LOGIN_TYPE"
}
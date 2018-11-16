package fossil.sof.sofuser.data.api.responses

import fossil.sof.sofuser.application.Constants

/**
 * Created by ninhvanluyen on 16/11/18.
 */
class LoadMoreUsers<T> {
    private var items:ArrayList<T>  = arrayListOf<T>()
    private var quota_max = 0
    private var quota_remaining = 0
    private var has_more = false
    private var error_id = 0
    private var error_message = ""
    private var error_name = ""
    fun getHasMore() = has_more
    fun getUsers() = items
    fun getErrorID() = error_id
    fun getErrors(): String = error_message

}
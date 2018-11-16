package fossil.sof.sofuser.domain.models

/**
 * Created by ninhvanluyen on 16/11/18.
 */
interface User {

    fun GetAccountId(): Int
    fun isEmployee(): Boolean
    fun getLastModifiedDate(): Long
    fun getLastAccessDate(): Long
    fun getCreationDate(): Long
    fun getReputationChangeYear(): Int
    fun getReputationChangeQuarter(): Int
    fun getReputationChangeMonth(): Int
    fun getReputationChangeWeek(): Int
    fun getReputationChangeDay(): Int
    fun getReputation(): Int
    fun getUserId(): Int
    fun getAcceptRate(): Int
    fun getUserType(): String
    fun getLocation(): String
    fun getWebsiteUrl(): String
    fun getLink(): String
    fun getProfileImage(): String
    fun getDisplayName(): String
}
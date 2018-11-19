package fossil.sof.sofuser.domain.models

interface Reputation {
    fun getCreationDate(): String
    fun getUserId(): Int
    fun getPostId(): Int
    fun getReputationChange(): Int
    fun getReputationHistoryType(): String
}
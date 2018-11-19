package fossil.sof.sofuser.data.entities

import fossil.sof.sofuser.domain.models.Reputation
import fossil.sof.sofuser.utils.AppUtils

class ReputationEntity : Reputation {
    private var reputation_history_type = ""
    private var reputation_change: Int = 0
    private var post_id: Int = 0
    private var user_id: Int = 0
    private var creation_date = 0L
    override fun getReputationHistoryType() = reputation_history_type
    override fun getReputationChange() = reputation_change
    override fun getPostId() = post_id
    override fun getUserId() = user_id
    override fun getCreationDate() = AppUtils.convertLongToStringDate(creation_date)
}
package fossil.sof.sofuser.application.user_detail

import android.databinding.DataBindingUtil
import android.databinding.ObservableField
import android.view.View
import fossil.sof.sofuser.databinding.ItemReputationBinding
import fossil.sof.sofuser.domain.models.Reputation
import fossil.sof.sofuser.libs.BaseViewHolder

class ItemReputationViewHolder(view: View) : BaseViewHolder(view) {
    lateinit var reputation: Reputation
    var reputationType = ObservableField<String>()
    var changeHistory = ObservableField<String>()
    var date = ObservableField<String>()
    var postId = ObservableField<String>()
    lateinit var viewBinding: ItemReputationBinding

    override fun onBind() {

    }

    override fun bindData(data: Any, position: Int) {
        reputation = data as Reputation
        reputationType.set(reputation.getReputationHistoryType())
        date.set(reputation.getCreationDate())
        changeHistory.set("${reputation.getReputationChange()}")
        postId.set("${reputation.getPostId()}")
        viewBinding = DataBindingUtil.bind(itemView)!!
        viewBinding.viewHolder = this
    }

    override fun onClick(p0: View?) {

    }

}
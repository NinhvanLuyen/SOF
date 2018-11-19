package fossil.sof.sofuser.application.news_feed

import android.databinding.DataBindingUtil
import android.databinding.ObservableField
import android.view.View
import com.like.LikeButton
import com.like.OnLikeListener
import fossil.sof.sofuser.databinding.ItemUserBinding
import fossil.sof.sofuser.domain.models.User
import fossil.sof.sofuser.libs.BaseViewHolder

class ItemUserViewHolder(view: View, var delegate: ItemDelegate) : BaseViewHolder(view) {
    var avatar = ObservableField<String>()
    var name = ObservableField<String>()
    var reputation = ObservableField<String>()
    var localtion = ObservableField<String>()
    private lateinit var user: User
    lateinit var viewBinding: ItemUserBinding
    override fun onBind() {

    }

    override fun bindData(data: Any, position: Int) {
        user = data as User
        avatar.set(user.getProfile_image())
        name.set(user.getDisplay_name())
        reputation.set(user.getReputationFormat())
        localtion.set(user.getLocation())
        viewBinding = DataBindingUtil.bind(itemView)!!
        viewBinding.starButton.isLiked = user.isBookmark()
        viewBinding.starButton.setOnLikeListener(object : OnLikeListener {
            override fun liked(p0: LikeButton?) {
                delegate.bookMarkUser(user, true)
                user.setBookmark(true)
            }
            override fun unLiked(p0: LikeButton?) {
                delegate.bookMarkUser(user, false)
                user.setBookmark(false)


            }
        })
        viewBinding.viewHolder = this
    }

    override fun onClick(p0: View?) {
        delegate.viewDetailUser(user)

    }

}
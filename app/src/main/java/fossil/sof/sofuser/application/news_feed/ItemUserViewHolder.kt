package fossil.sof.sofuser.application.news_feed

import android.databinding.DataBindingUtil
import android.databinding.ObservableField
import android.view.View
import com.like.LikeButton
import com.like.OnLikeListener
import fossil.sof.sofuser.databinding.ItemUserBinding
import fossil.sof.sofuser.data.entities.UserEntity
import fossil.sof.sofuser.libs.BaseViewHolder
import fossil.sof.sofuser.utils.UIUtils

class ItemUserViewHolder(view: View, var delegate: ItemDelegate) : BaseViewHolder(view) {
    var avatar = ObservableField<String>()
    var name = ObservableField<String>()
    var reputation = ObservableField<String>()
    var localtion = ObservableField<String>()
    private lateinit var user: UserEntity
    lateinit var viewBinding: ItemUserBinding
    override fun onBind() {

    }

    override fun bindData(data: Any, position: Int) {
        user = data as UserEntity
        avatar.set(user.profile_image)
        name.set(user.display_name)
        reputation.set(UIUtils.getShortNumberFormat(user.reputation))
        localtion.set(user.location)
        viewBinding = DataBindingUtil.bind(itemView)!!
        viewBinding.starButton.isLiked = user.isBookmark
        viewBinding.starButton.setOnLikeListener(object : OnLikeListener {
            override fun liked(p0: LikeButton?) {
                delegate.bookMarkUser(user, true)
                user.isBookmark = true
            }

            override fun unLiked(p0: LikeButton?) {
                delegate.bookMarkUser(user, false)
                user.isBookmark = false


            }
        })
        viewBinding.viewHolder = this
    }

    override fun onClick(p0: View?) {
        delegate.viewDetailUser(user,layoutPosition)

    }

}
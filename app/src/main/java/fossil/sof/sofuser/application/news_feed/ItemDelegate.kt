package fossil.sof.sofuser.application.news_feed

import fossil.sof.sofuser.data.entities.UserEntity

interface ItemDelegate {
    fun viewDetailUser(user: UserEntity,position:Int)
    fun bookMarkUser(user: UserEntity, isBookmark: Boolean)
}
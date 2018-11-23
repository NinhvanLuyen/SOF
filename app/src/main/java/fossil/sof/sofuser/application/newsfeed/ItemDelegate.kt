package fossil.sof.sofuser.application.newsfeed

import fossil.sof.sofuser.data.entities.UserEntity

interface ItemDelegate {
    fun viewDetailUser(user: UserEntity,position:Int)
    fun bookMarkUser(user: UserEntity, isBookmark: Boolean)
}
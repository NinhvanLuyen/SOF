package fossil.sof.sofuser.application.news_feed

import fossil.sof.sofuser.domain.models.User

interface ItemDelegate {
    fun viewDetailUser(user: User)
    fun bookMarkUser(user: User, isBookmark: Boolean)
}
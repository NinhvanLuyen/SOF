package fossil.sof.sofuser.application.bookmark

import android.view.View
import fossil.sof.sofuser.R
import fossil.sof.sofuser.application.newsfeed.ItemDelegate
import fossil.sof.sofuser.application.newsfeed.ItemUserViewHolder
import fossil.sof.sofuser.data.entities.UserEntity
import fossil.sof.sofuser.libs.BaseAdapter
import fossil.sof.sofuser.libs.BaseViewHolder
import fossil.sof.sofuser.libs.BlankViewHolder
import io.reactivex.subjects.BehaviorSubject

class BookmarkAdapter(var delegate: ItemDelegate) : BaseAdapter() {

    private val DATA = 0
    private var scrollToTop = BehaviorSubject.create<Boolean>()

    init {
        insertSection(DATA, mutableListOf<UserEntity>())

    }

    override fun layout(sectionRow: SectionRow): Int = R.layout.item_user

    override fun viewHolder(layout: Int, view: View): BaseViewHolder {
        return if (layout == R.layout.item_user)
            ItemUserViewHolder(view, delegate)
        else
            BlankViewHolder(view)
    }

    fun addData(datas: MutableList<UserEntity>) {
        if (datas.isNotEmpty()) {
            setSection(DATA, datas)
            notifyDataSetChanged()
        }
    }

    fun searchAndNotifyItemChange(listBookmarked: MutableList<UserEntity>) {
        if (listBookmarked.size > sections[DATA].size) {
            //add item
            for ((index, user) in listBookmarked.withIndex()) {
                var isFounded = false
                if (user.isBookmark) {
                    for (u in (sections[DATA] as MutableList<UserEntity>)) {
                        if (u.user_id == user.user_id) {
                            isFounded = true
                        }
                    }
                    if (!isFounded) {
                        (sections[DATA] as MutableList<UserEntity>).add(user)
                        notifyItemInserted((sections[DATA] as MutableList<UserEntity>).size)
                        scrollToTop.onNext(true)
                    }
                }
            }
        } else {
            //remove item
            var iter = (sections[DATA] as MutableList<UserEntity>).iterator()
            var index = 0
            while (iter.hasNext()) {
                var useriter = iter.next()
                var ufounded = listBookmarked.find { useriter.user_id == it.user_id }
                if (ufounded == null) {
                    iter.remove()
                    notifyItemRemoved(index)
                }
                index++
            }
        }
    }

    fun removeData() {
        setSection(DATA, mutableListOf<UserEntity>())
        notifyDataSetChanged()
    }

    fun getScrollToTop() = scrollToTop
}
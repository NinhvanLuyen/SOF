package fossil.sof.sofuser.application.book_mark

import android.view.View
import fossil.sof.sofuser.R
import fossil.sof.sofuser.application.news_feed.ItemDelegate
import fossil.sof.sofuser.application.news_feed.ItemUserViewHolder
import fossil.sof.sofuser.data.entities.UserEntity
import fossil.sof.sofuser.libs.BaseAdapter
import fossil.sof.sofuser.libs.BaseViewHolder
import fossil.sof.sofuser.libs.BlankViewHolder

class BookmarkAdapter(var delegate: ItemDelegate) : BaseAdapter() {

    private val DATA = 0

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
    fun removeData() {
        setSection(DATA, mutableListOf<UserEntity>())
        notifyDataSetChanged()
    }
}
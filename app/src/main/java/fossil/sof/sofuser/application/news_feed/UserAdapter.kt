package fossil.sof.sofuser.application.news_feed

import android.view.View
import fossil.sof.sofuser.R
import fossil.sof.sofuser.data.entities.UserEntity
import fossil.sof.sofuser.libs.BaseAdapter
import fossil.sof.sofuser.libs.BaseViewHolder
import fossil.sof.sofuser.libs.BlankViewHolder
import timber.log.Timber

class UserAdapter(var delegate: ItemDelegate) : BaseAdapter() {
    private val DATA = 0
    private val LOAD = 1

    init {
        insertSection(DATA, mutableListOf<UserEntity>())
        insertSection(LOAD, mutableListOf<Any>())

    }

    override fun layout(sectionRow: SectionRow): Int {
        return if (sectionRow.section == DATA)
            R.layout.item_user
        else
            R.layout.item_loading
    }

    override fun viewHolder(layout: Int, view: View): BaseViewHolder {
        return if (layout == R.layout.item_user)
            ItemUserViewHolder(view, delegate)
        else
            BlankViewHolder(view)
    }

    fun addData(datas: List<UserEntity>, loadMore: Boolean) {
        if (datas.isNotEmpty()) {
            val currentPosition = sections[DATA].size + 1
            (sections[DATA] as MutableList<UserEntity>).addAll(datas)
            if (loadMore) {
                if (sections[LOAD].isEmpty()) {
                    (sections[LOAD] as MutableList<Any>).add(Any())
                    notifyItemRangeInserted(currentPosition, datas.size + 1)
                } else {
                    notifyItemRangeInserted(currentPosition, datas.size)
                }
            } else {
                if (sections[LOAD].isEmpty()) {
                    notifyItemRangeInserted(currentPosition, datas.size)
                } else {
                    setSection(LOAD, mutableListOf<Any>())
                    notifyDataSetChanged()
                }
            }
        } else {
            if (!loadMore) {
                sections.set(LOAD, mutableListOf<Any>())
                notifyDataSetChanged()
            }
        }
    }

    fun removeData() {
        setSection(DATA, mutableListOf<UserEntity>())
        setSection(LOAD, mutableListOf<Any>())
        notifyDataSetChanged()
    }

    fun notifySingleItemChange(userViewDetail: UserEntity, position: Int) {
        Timber.e("data changed at $position checked is ${userViewDetail.isBookmark}")
        (sections[DATA] as MutableList<UserEntity>)[position] = userViewDetail
        notifyItemChanged(position)
    }
}
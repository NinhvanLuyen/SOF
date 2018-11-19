package fossil.sof.sofuser.application.news_feed

import android.view.View
import fossil.sof.sofuser.R
import fossil.sof.sofuser.domain.models.User
import fossil.sof.sofuser.libs.BaseAdapter
import fossil.sof.sofuser.libs.BaseViewHolder
import fossil.sof.sofuser.libs.BlankViewHolder

class UserAdapter(var delegate: ItemDelegate): BaseAdapter() {
    private val DATA = 0
    private val LOAD = 1

    init {
        insertSection(DATA, mutableListOf<User>())
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
            ItemUserViewHolder(view,delegate)
        else
            BlankViewHolder(view)
    }

    fun addData(datas: List<User>, loadMore: Boolean) {
        if (datas.isNotEmpty()) {
            val currentPosition = sections[DATA].size + 1
            (sections[DATA] as MutableList<User>).addAll(datas)
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
        setSection(DATA, mutableListOf<User>())
        setSection(LOAD, mutableListOf<Any>())
        notifyDataSetChanged()
    }
}
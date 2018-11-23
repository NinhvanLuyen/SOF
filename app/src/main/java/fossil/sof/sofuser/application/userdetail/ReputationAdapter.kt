package fossil.sof.sofuser.application.userdetail

import android.view.View
import fossil.sof.sofuser.R
import fossil.sof.sofuser.domain.models.Reputation
import fossil.sof.sofuser.libs.BaseAdapter
import fossil.sof.sofuser.libs.BaseViewHolder
import fossil.sof.sofuser.libs.BlankViewHolder

class ReputationAdapter(): BaseAdapter() {
    private val DATA = 0
    private val LOAD = 1

    init {
        insertSection(DATA, mutableListOf<Reputation>())
        insertSection(LOAD, mutableListOf<Any>())

    }

    override fun layout(sectionRow: SectionRow): Int {
        return if (sectionRow.section == DATA)
            R.layout.item_reputation
        else
            R.layout.item_loading
    }

    override fun viewHolder(layout: Int, view: View): BaseViewHolder {
        return if (layout == R.layout.item_reputation)
            ItemReputationViewHolder(view)
        else
            BlankViewHolder(view)
    }

    fun addData(datas: List<Reputation>, loadMore: Boolean) {
        if (datas.isNotEmpty()) {
            val currentPosition = sections[DATA].size + 1
            (sections[DATA] as MutableList<Reputation>).addAll(datas)
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
        setSection(DATA, mutableListOf<Reputation>())
        setSection(LOAD, mutableListOf<Any>())
        notifyDataSetChanged()
    }
}
package fossil.sof.sofuser.libs

import android.databinding.DataBindingUtil
import android.databinding.ViewDataBinding
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.trello.rxlifecycle2.android.ActivityEvent

/**
 * Created by ninhvanluyen on 1/10/18.
 */
abstract class BaseAdapter : RecyclerView.Adapter<BaseViewHolder>() {

    val sections: MutableList<MutableList<*>> = ArrayList()

    fun clearSection() {
        sections.clear()
    }

    fun addSection(section: MutableList<Any>) {
        sections.add(ArrayList(section))
    }

    fun <T> setSection(position: Int, section: MutableList<T>) {
        sections[position] = section
    }

    fun <T> insertSection(position: Int, section: MutableList<T>) {
        sections.add(position, section)
    }

    protected abstract fun layout(sectionRow: SectionRow): Int

    protected abstract fun viewHolder(layout: Int, view: View): BaseViewHolder

    override fun onViewDetachedFromWindow(holder: BaseViewHolder) {
        super.onViewDetachedFromWindow(holder)
        holder.lifecycleEvent(ActivityEvent.STOP)
        if (!hasObservers())
            holder.lifecycleEvent(ActivityEvent.DESTROY)
    }

    override fun onViewAttachedToWindow(holder: BaseViewHolder) {
        super.onViewAttachedToWindow(holder)
        holder.lifecycleEvent(ActivityEvent.START)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder {
//        val view = inflateView(parent, viewType)
        val viewDataBinding = DataBindingUtil.inflate<ViewDataBinding>(LayoutInflater.from(parent.context), viewType, parent, false)
        val viewHolder = viewHolder(viewType, viewDataBinding.root)
        viewHolder.lifecycleEvent(ActivityEvent.CREATE)
        return viewHolder
    }

    override fun onBindViewHolder(holder: BaseViewHolder, position: Int) {
        val data = objectFromPosition(position)
        holder.bindData(data, position)
        holder.onBind()
    }

    override fun getItemViewType(position: Int): Int {
        return layout(sectionRowFromPosition(position))
    }

    override fun getItemCount(): Int {
        var itemCount = 0
        for (section in sections) {
            itemCount += section.size
        }
        return itemCount
    }

    private fun objectFromPosition(position: Int): Any {
        return objectFromSectionRow(sectionRowFromPosition(position))
    }

    private fun objectFromSectionRow(sectionRow: SectionRow): Any {
        return sections[sectionRow.section][sectionRow.row]!!
    }

    private fun sectionRowFromPosition(position: Int): SectionRow {
        val sectionRow = SectionRow()
        var cursor = 0
        for (section in sections) {
            if (position > cursor + section.size - 1) {
                sectionRow.nextSection()
                cursor += section.size
            } else {
                sectionRow.row = position - cursor
                return sectionRow
            }
        }
        throw RuntimeException("Position " + position + " not found in sections")
    }

    private fun inflateView(viewGroup: ViewGroup, viewType: Int): View {
        val layoutInflater = LayoutInflater.from(viewGroup.context)
        return layoutInflater.inflate(viewType, viewGroup, false)
    }

    protected class SectionRow {
        var section = 0
        var row = 0

        fun nextSection() {
            section++
            row = 0
        }
    }
}
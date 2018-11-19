package fossil.sof.sofuser.libs.wiggets

import android.content.Context
import android.content.res.Resources
import android.graphics.PorterDuff
import android.graphics.Typeface
import android.os.Build
import android.support.v4.content.ContextCompat
import android.support.v4.view.ViewPager
import android.util.AttributeSet
import android.util.TypedValue
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import fossil.sof.sofuser.R
import fossil.sof.sofuser.utils.UIUtils
import java.util.ArrayList

class NavigatorButton : LinearLayout {

    private val DP = Resources.getSystem().displayMetrics.density

    /**
     * Height of the space the indicator takes up at the bottom of the view.
     */
    private val mIndicatorHeight = (DP * 2).toInt()
    private var views: ArrayList<LinearLayout>? = null
    private var viewPager: ViewPager? = null
    private var resource: MutableList<Int>? = null
    private var color_not_seleted: Int = 0
    private var color_selected: Int = 0
    private var titles: MutableList<String>? = null
    private var myContext: Context? = null

    constructor(context: Context) : super(context) {
        this.myContext = context
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            elevation = 2.0f
        }
    }

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {
        this.myContext = context
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            elevation = 2.0f
        }
    }


    /**
     * @param titles                       is title of button
     * @param resource_color_seleted       : is color when button is selected
     * @param resource_color_not_selected: is color when button is not selected
     * @param background                   :is background of navigation button
     * @param current                      : is position default you want selected start is 0
     */
    fun createView(titles: MutableList<String>, resource_color_seleted: Int, resource_color_not_selected: Int, background: Int, current: Int, titleSmall: Boolean = false) {
        this.titles = titles
        this.color_selected = resource_color_seleted
        this.color_not_seleted = resource_color_not_selected
        setBackgroundResource(background)
        views = ArrayList()
        for (i in titles.indices) {
            val linearLayout = LinearLayout(context)
            val title = TextView(context)
            title.typeface = UIUtils.getTypeFace(context)
            val indicator = View(context)

            title.setTextSize(TypedValue.COMPLEX_UNIT_SP, 18F)
            if (titleSmall)
                title.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14F)

            indicator.setBackgroundResource(resource_color_seleted)
            val indicatorLLParams = LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, mIndicatorHeight)
            indicatorLLParams.gravity = Gravity.BOTTOM
            indicator.layoutParams = indicatorLLParams
            title.text = titles[i]
            title.setTextColor(ContextCompat.getColor(context, color_not_seleted))
            val titlelayoutParams = LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT, 1F)
            title.gravity = Gravity.CENTER
            title.layoutParams = titlelayoutParams
            linearLayout.orientation = LinearLayout.VERTICAL
            linearLayout.gravity = Gravity.CENTER
            title.setTextColor(ContextCompat.getColor(context, color_not_seleted))
            if (current >= 0 && current == i) {
                title.setTextColor(ContextCompat.getColor(context, color_selected))
            }
            linearLayout.addView(title)
            linearLayout.addView(indicator)
            linearLayout.setTag(R.string.tag, i)
            linearLayout.setOnClickListener {
                changeState(i)
                changeViewpager(i)
            }
            val layoutParamLL = LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT, 1f)
            linearLayout.layoutParams = layoutParamLL

            views!!.add(linearLayout)
        }
        for (i in views!!.indices) {
            addView(views!![i])
        }
    }

    fun createView(titles: MutableList<String>?, resources: MutableList<Int>, resource_color_seleted: Int, resource_color_not_selected: Int, background: Int, current: Int) {
        this.titles = titles
        this.resource = resources
        this.color_not_seleted = resource_color_not_selected
        this.color_selected = resource_color_seleted
        setBackgroundResource(background)
        views = ArrayList()
        if (titles == null) {
            for (i in resources.indices) {

                val linearLayout = LinearLayout(context)
                linearLayout.orientation = LinearLayout.VERTICAL
                linearLayout.gravity = Gravity.CENTER
                val icon = ImageView(context)
                icon.scaleType = ImageView.ScaleType.CENTER_INSIDE
                icon.setImageResource(resources[i])
                icon.setColorFilter(ContextCompat.getColor(context!!, color_not_seleted), PorterDuff.Mode.MULTIPLY)
                if (current >= 0 && current == i) {
                    icon.setColorFilter(ContextCompat.getColor(context!!, color_selected), PorterDuff.Mode.MULTIPLY)
                }
                linearLayout.addView(icon)
                linearLayout.setTag(R.string.tag, i)
                linearLayout.setOnClickListener { v ->
                    changeState(i)
                    changeViewpager(i)
                }
                var layoutParamLL = LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT, 1f)
                linearLayout.layoutParams = layoutParamLL

                views!!.add(linearLayout)
            }
        } else {
            for (i in titles!!.indices) {
                val linearLayout = LinearLayout(context)
                val title = TextView(context)
                title.text = titles[i]
                title.setTextColor(ContextCompat.getColor(context!!, color_not_seleted))
                val titlelayoutParams = LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT, 1f)
                title.gravity = Gravity.CENTER
                title.layoutParams = titlelayoutParams
                linearLayout.orientation = LinearLayout.VERTICAL
                linearLayout.gravity = Gravity.CENTER_HORIZONTAL
                val icon = ImageView(context)
                icon.scaleType = ImageView.ScaleType.CENTER_INSIDE
                icon.setImageResource(resources[i])
                val imageviewlp = LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
                imageviewlp.setMargins(0, context!!.resources.getDimension(R.dimen.activity_ssmall_margin).toInt(), 0, 0)
                icon.layoutParams = imageviewlp
                title.setTextColor(ContextCompat.getColor(context!!, color_not_seleted))
                title.setTypeface(UIUtils.getTypeFace(context), Typeface.BOLD)
                icon.setColorFilter(ContextCompat.getColor(context!!, color_not_seleted), PorterDuff.Mode.MULTIPLY)
                if (current >= 0 && current == i) {
                    title.setTextColor(ContextCompat.getColor(context!!, color_selected))
                    icon.setColorFilter(ContextCompat.getColor(context!!, color_selected), PorterDuff.Mode.MULTIPLY)
                }
                linearLayout.addView(icon)
                linearLayout.addView(title)
                linearLayout.setTag(R.string.tag, i)
                linearLayout.setOnClickListener { v ->
                    changeState(i)
                    changeViewpager(i)
                }
                var layoutParamLL = LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT, 1f)
                layoutParamLL.leftMargin = context!!.resources.getDimension(R.dimen.activity_small_margin).toInt()
                layoutParamLL.rightMargin = context!!.resources.getDimension(R.dimen.activity_small_margin).toInt()
                linearLayout.layoutParams = layoutParamLL

                views!!.add(linearLayout)
            }
        }
        for (i in views!!.indices) {
            addView(views!![i])
        }
    }

    private fun changeState(position: Int) {

        if (titles != null && resource != null) {
            for (i in views!!.indices) {
                val view = views!![i]
                val icon = view.getChildAt(0) as ImageView
                val title = view.getChildAt(1) as TextView

                if (view.getTag(R.string.tag) as Int == position) {
                    view.isSelected = true
                    title.setTextColor(ContextCompat.getColor(context, color_selected))
                    val drawable = resources.getDrawable(resource!![position])
                    if (Build.VERSION.SDK_INT > 21) {
                        drawable.setTint(ContextCompat.getColor(context, color_selected))
                    }
                    icon.setColorFilter(ContextCompat.getColor(context, color_selected), PorterDuff.Mode.MULTIPLY)

                } else {
                    title.setTextColor(ContextCompat.getColor(context, color_not_seleted))
                    icon.setColorFilter(ContextCompat.getColor(context, color_not_seleted), PorterDuff.Mode.MULTIPLY)
                }
            }
        } else if (titles != null && resource == null) {
            for (i in views!!.indices) {
                val view = views!![i]
                val title = view.getChildAt(0) as TextView
                val indicator = view.getChildAt(1) as View
                if (view.getTag(R.string.tag) as Int == position) {
                    view.isSelected = true
                    title.setTextColor(ContextCompat.getColor(context, color_selected))
                    indicator.setBackgroundResource(color_selected)

                } else {
                    title.setTextColor(ContextCompat.getColor(context, color_not_seleted))
                    indicator.setBackgroundResource(R.color.no_background)
                }
            }
        } else {
            for (i in views!!.indices) {
                val view = views!![i]
                val icon = view.getChildAt(0) as ImageView
                if (view.getTag(R.string.tag) as Int == position) {
                    view.isSelected = true
                    val drawable = resources.getDrawable(resource!![position])
                    if (Build.VERSION.SDK_INT > 21) {
                        drawable.setTint(ContextCompat.getColor(context, color_selected))
                    }
                    icon.setColorFilter(ContextCompat.getColor(context, color_selected), PorterDuff.Mode.MULTIPLY)

                } else {
                    icon.setColorFilter(ContextCompat.getColor(context, color_not_seleted), PorterDuff.Mode.MULTIPLY)
                }
            }
        }
    }

    private fun changeViewpager(position: Int) {
        for (view in views!!) {
            if (view.getTag(R.string.tag) as Int == position) {
                if (viewPager != null) {
                    viewPager!!.currentItem = position
                }
            }
        }
    }

    fun setViewPagerListener(viewPager: ViewPager) {
        this.viewPager = viewPager
        viewPager.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
            override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {
                if (positionOffset == 0f) {
                    changeState(position)
                }
            }

            override fun onPageSelected(position: Int) {
                changeState(position)
            }

            override fun onPageScrollStateChanged(state: Int) {}
        })
    }
}
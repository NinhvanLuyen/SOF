package fossil.sof.sofuser.libs.wiggets

import android.content.Context
import android.graphics.Canvas
import android.graphics.Typeface
import android.util.AttributeSet
import fossil.sof.sofuser.R
import fossil.sof.sofuser.utils.UIUtils
import fossil.sof.sofuser.utils.ValidateUtils

/**
 * Created by ninhvanluyen on 2/2/18.
 */
class LTextView(context: Context,attrs: AttributeSet?) : android.support.v7.widget.AppCompatTextView(context,attrs) {

    fun setLText(st: String) {
        if (ValidateUtils.isEmpty(st)) {
            return
        }
        text = st
    }
    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
    }
    init {
        init(context,attrs)
    }


    private fun init(ctx: Context, attrs: AttributeSet?) {
        var requestBold = false
        if (attrs != null) {
            val typedArray = ctx.obtainStyledAttributes(attrs, R.styleable.LTextView)
            requestBold = typedArray.getBoolean(R.styleable.LTextView_LBold, false)
            typedArray.recycle()
        }

        val typeface = UIUtils.getTypeFace()
        if (requestBold) {
            setTypeface(typeface, Typeface.BOLD)
        } else {
            setTypeface(typeface)
        }
    }
}
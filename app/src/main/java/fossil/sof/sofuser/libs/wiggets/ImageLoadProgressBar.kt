package fossil.sof.sofuser.libs.wiggets

import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.RectF
import android.graphics.Paint.ANTI_ALIAS_FLAG
import com.facebook.drawee.drawable.ProgressBarDrawable
import fossil.sof.sofuser.R


/**
 * Created by ninhvanluyen on 1/30/18.
 */
class ImageLoadProgressBar : ProgressBarDrawable() {


    internal var level: Float = 0.toFloat()

    internal var paint = Paint(ANTI_ALIAS_FLAG)

    internal var color = R.color.colorPrimary

    internal val oval = RectF()

    internal var radius = 10F

    init {
        paint.strokeWidth = 1F
        paint.style = Paint.Style.STROKE
    }

    override fun onLevelChange(level: Int): Boolean {
        this.level = level.toFloat()
        invalidateSelf()
        return true
    }

    override fun draw(canvas: Canvas) {
        oval.set(canvas.width / 2 - radius, canvas.height / 2 - radius,
                canvas.width / 2 + radius, canvas.height / 2 + radius)

        drawCircle(canvas, level, color)
    }


    private fun drawCircle(canvas: Canvas, level: Float, color: Int) {
        paint.color = color
        var angle = 360 / 1f
        angle *= level
        canvas.drawArc(oval, 0F, Math.round(angle).toFloat(), false, paint)
    }


}
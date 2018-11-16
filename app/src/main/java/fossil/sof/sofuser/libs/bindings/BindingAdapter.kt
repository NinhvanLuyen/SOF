package fossil.sof.sofuser.libs.bindings

import android.annotation.SuppressLint
import android.graphics.Color
import android.graphics.Typeface
import android.view.View
import android.view.ViewGroup
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.TextView
import fossil.sof.sofuser.utils.ValidateUtils
import com.facebook.drawee.view.SimpleDraweeView

/**
 * Created by ninhvanluyen on 1/11/18.
 */

object BindingAdapter {
    @JvmStatic
    @android.databinding.BindingAdapter("app:imageUrl")
    fun loadImage(view: SimpleDraweeView, imageUrl: String?) {
        if (!ValidateUtils.isEmpty(imageUrl))
            view.setImageURI(imageUrl)
    }

    @JvmStatic
    @android.databinding.BindingAdapter("android:typeface")
    fun textView(view: TextView, style: String?) {
        if (!ValidateUtils.isEmpty(style))
            when (style) {
                "bold" -> view.setTypeface(null, Typeface.BOLD)
                else -> view.setTypeface(null, Typeface.NORMAL)
            }
    }


    @SuppressLint("SetJavaScriptEnabled")
    @android.databinding.BindingAdapter("loadUrl")
    fun loadDataHTML(view: WebView, url: String) {
        view.loadUrl(url)
    }

    @JvmStatic
    @android.databinding.BindingAdapter("bgColor")
    fun bgColor(view: ViewGroup, colorCode: String?) {
        if (colorCode != null)
            view.setBackgroundColor(Color.parseColor(colorCode.replace("#", "#7F", true)))
    }
}
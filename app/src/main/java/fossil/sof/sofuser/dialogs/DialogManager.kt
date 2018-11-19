package fossil.sof.sofuser.dialogs
import android.content.Context
import android.content.DialogInterface
import android.support.v7.app.AlertDialog
import android.widget.TextView
import fossil.sof.sofuser.R
import fossil.sof.sofuser.utils.UIUtils


class DialogManager {
    companion object {
        fun showDialogMessage(message: String, context: Context) {
            val textView = TextView(context, null, R.style.TextViewStyle)
//            textView.typeface = UIUtils.getTypeFace()
            textView.text = message
            val margin = context.resources.getDimension(R.dimen.activity_margin).toInt()
            textView.setPadding(margin, margin, margin, 0)

            AlertDialog.Builder(context)
                    .setView(textView)
                    .setCancelable(true)
                    .setPositiveButton(R.string.OK, { _, _ -> })
                    .create().show()
        }

        fun showDialogMessageWithAction(message: String, action: Int, context: Context, listener: DialogInterface.OnClickListener) {
            val textView = TextView(context, null, R.style.TextViewStyle)
            textView.text = message
            val margin = context.resources.getDimension(R.dimen.activity_margin).toInt()
            textView.setPadding(margin, margin, margin, 0)

            AlertDialog.Builder(context)
                    .setView(textView)
                    .setCancelable(true)
                    .setPositiveButton(action, listener)
                    .create().show()
        }

        fun showActionDialogNotClose(message: String, action: String, context: Context, listener: DialogInterface.OnClickListener) {
            val textView = TextView(context, null, R.style.TextViewStyle)
            textView.typeface = UIUtils.getTypeFace(context)
            textView.text = message
            val margin = context.resources.getDimension(R.dimen.activity_margin).toInt()
            textView.setPadding(margin, margin, margin, 0)

            AlertDialog.Builder(context)
                    .setView(textView)
                    .setCancelable(false)
                    .setPositiveButton(action, listener)
                    .create().show()
        }

    }
}
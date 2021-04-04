package cz.lpatak.mycoachesdiary.util

import android.widget.Toast
import androidx.fragment.app.Fragment


fun Fragment.showToast(message: Int) {
    Toast.makeText(context, context?.getString(message), Toast.LENGTH_SHORT).show()
}

fun Fragment.showToast(message: String) {
    Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
}

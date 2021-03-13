package cz.lpatak.mycoachesdiary.util

import android.net.Uri
import android.view.View
import android.widget.ImageView
import androidx.databinding.BindingAdapter
import com.squareup.picasso.Picasso


object BindingAdapters {


    @Suppress("unused")
    @BindingAdapter("invisibleUnless")
    @JvmStatic
    fun invisibleUnless(view: View, visible: Boolean) {
        view.visibility = if (visible) View.VISIBLE else View.INVISIBLE
    }


    @Suppress("unused")
    @BindingAdapter("goneUnless")
    @JvmStatic
    fun goneUnless(view: View, visible: Boolean) {
        view.visibility = if (visible) View.VISIBLE else View.GONE
    }


    @JvmStatic
    @BindingAdapter("imageUri")
    fun imageUri(imageView: ImageView, uri: Uri?) {
        uri?.let {
            Picasso.Builder(imageView.context).build()
                    .load(uri)
                    .into(imageView)
        }
    }

}

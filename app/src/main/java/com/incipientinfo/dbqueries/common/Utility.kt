package com.incipientinfo.dbqueries.common

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.support.design.widget.Snackbar
import android.support.v4.content.ContextCompat
import android.util.Patterns
import android.view.View
import android.widget.Toast
import com.incipientinfo.dbqueries.R
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileOutputStream

class Utility {

    companion object {

        const val SPLASH_DELAY: Long = 2000

        const val SELECT_FILE: Int = 101

        const val REQUEST_CAMERA: Int = 100

        const val REQUESTPERMISSIONSREQUESTCODE = 100

        val PERMISSIONS = arrayOf(
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
        )

        fun redirectToActivity(
            yourActivity: Activity?,
            SecondActivity: Class<*>,
            context: Context,
            isfinish: Boolean,
            b: Bundle?
        ) {

            try {

                if (yourActivity != null) {
                    val intent = Intent(yourActivity, SecondActivity)

                    if (b != null)
                        intent.putExtras(b)

                    yourActivity.startActivity(intent)

                    yourActivity.overridePendingTransition(R.anim.anim_slide_in_left, R.anim.anim_slide_out_left)

                    if (isfinish) {

                        yourActivity.finish()
                    }
                } else {
                    val intent = Intent(context, SecondActivity)

                    val activity = context as Activity

                    if (b != null)
                        intent.putExtras(b)

                    activity.startActivity(intent)

                    activity.overridePendingTransition(R.anim.anim_slide_out_left, R.anim.anim_slide_in_left)

                    if (isfinish) {
                        activity.finish()
                    }
                }


            } catch (e: Exception) {

                Toast.makeText(context, e.toString(), Toast.LENGTH_LONG).show()
            }

        }


        fun showSnack(context: Context, view: View, msg: String) {
            try {
                val snackbar = Snackbar.make(view, msg, Snackbar.LENGTH_LONG)
                snackbar.view.setBackgroundColor(ContextCompat.getColor(context, R.color.colorPrimary))
                snackbar.show()
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }


        fun saveImage(thumbnail: Bitmap): Uri {

            var tempFile: File? = null

            try {
                var tempDir = Environment.getExternalStorageDirectory()
                val time = System.currentTimeMillis()
                tempDir = File(tempDir.absolutePath + "/Crud")
                if (!tempDir.exists())
                    tempDir.mkdirs()
                tempFile = File.createTempFile(time.toString(), ".jpg", tempDir)
                val bytes = ByteArrayOutputStream()
                thumbnail.compress(Bitmap.CompressFormat.JPEG, 100, bytes)
                val bitmapData = bytes.toByteArray()

                val fos = FileOutputStream(tempFile)
                fos.write(bitmapData)
                fos.flush()
                fos.close()
            } catch (e: Exception) {
                e.printStackTrace()
            }
            return Uri.fromFile(tempFile)


        }

        fun isValidEmail(target: CharSequence): Boolean {
            return Patterns.EMAIL_ADDRESS.matcher(target).matches()
        }

        fun isValidMobile(target: String): Boolean {
            return target.length in 6..16

        }

    }
}

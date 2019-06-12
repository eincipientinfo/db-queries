package com.incipientinfo.dbqueries.ui.home


import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.provider.Settings
import android.support.annotation.RequiresApi
import android.support.constraint.ConstraintLayout
import android.support.design.widget.Snackbar
import android.support.design.widget.TextInputEditText
import android.support.design.widget.TextInputLayout
import android.support.v4.app.ActivityCompat
import android.support.v4.app.Fragment
import android.support.v4.content.ContextCompat
import android.support.v7.app.AlertDialog
import android.text.Editable
import android.text.InputType
import android.text.TextUtils
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.RadioButton
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.incipientinfo.dbqueries.POJO.Queries
import com.incipientinfo.dbqueries.R
import com.incipientinfo.dbqueries.common.CircleImageView
import com.incipientinfo.dbqueries.common.Utility.Companion.PERMISSIONS
import com.incipientinfo.dbqueries.common.Utility.Companion.REQUESTPERMISSIONSREQUESTCODE
import com.incipientinfo.dbqueries.common.Utility.Companion.REQUEST_CAMERA
import com.incipientinfo.dbqueries.common.Utility.Companion.SELECT_FILE
import com.incipientinfo.dbqueries.common.Utility.Companion.isValidEmail
import com.incipientinfo.dbqueries.common.Utility.Companion.isValidMobile
import com.incipientinfo.dbqueries.common.Utility.Companion.saveImage
import com.incipientinfo.dbqueries.common.Utility.Companion.showSnack
import com.incipientinfo.dbqueries.database.Database
import com.incipientinfo.dbqueries.ui.main.MainActivity
import com.incipientinfo.dbqueries.ui.querieslist.QueriesListFragment
import kotlinx.android.synthetic.main.fragment_home.view.*


class HomeFragment : Fragment(), View.OnClickListener {


    private var rootView: View? = null

    private var imgUri: Uri? = null

    private var imgStr: String? = null

    private var userChoosenTask: String? = null

    private var snackbar: Snackbar? = null

    private var database: Database? = null

    private var name: String? = null

    private var email: String? = null

    private var phoneNum: String? = null

    private var gender: String? = null

    private var item: Queries? = null

    private var btnInsert: Button? = null

    private var imgEditPic: ImageView? = null

    private var img: CircleImageView? = null

    private var edName: TextInputEditText? = null

    private var edEmail: TextInputEditText? = null

    private var edNum: TextInputEditText? = null

    private var txtLayName: TextInputLayout? = null

    private var txtLayEmail: TextInputLayout? = null

    private var txtLayNum: TextInputLayout? = null

    private var id: String? = null

    private var rbMale: RadioButton? = null

    private var rbFemale: RadioButton? = null

    private var constraintMain: ConstraintLayout? = null

    private var fusedLocationClient: FusedLocationProviderClient? = null


    @RequiresApi(Build.VERSION_CODES.M)
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        rootView = inflater.inflate(R.layout.fragment_home, container, false)

        try {
            database = Database(context!!)

            fusedLocationClient = LocationServices.getFusedLocationProviderClient(activity!!)

            initUI()

            changeListener()

            clickListener()

            if (arguments != null) {

                item = Queries()

                id = arguments!!.getString(getString(R.string.bundle_id))

                if (!TextUtils.isEmpty(id)) {
                    item = database!!.getDataFromId(id!!.toInt())
                }

                editData(item!!)

            } else {

                img!!.setImageResource(R.drawable.ic_profile)

            }

        } catch (e: Exception) {
            e.printStackTrace()
        }


        return rootView
    }


    private fun initUI() {

        try {
            constraintMain = rootView!!.constraintMain

            imgEditPic = rootView!!.imgEditPic

            btnInsert = rootView!!.btnInsert

            img = rootView!!.img

            txtLayName = rootView!!.txtLayName

            txtLayEmail = rootView!!.txtLayEmail

            txtLayNum = rootView!!.txtLayNum

            edName = rootView!!.edName

            edName!!.inputType = InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_FLAG_CAP_SENTENCES

            edEmail = rootView!!.edEmail

            edNum = rootView!!.edNum

            rbMale = rootView!!.rbMale

            rbFemale = rootView!!.rbFemale


        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun editData(queriesData: Queries) {
        try {
            btnInsert!!.text = getString(R.string.update)

            val imgProfileStr = queriesData.imgUri

            if (!TextUtils.isEmpty(imgProfileStr)) {
                imgStr = imgProfileStr
                val uri = Uri.parse(imgProfileStr)
                img!!.setImageURI(uri)

            } else {
                img!!.setBackgroundResource(R.drawable.ic_profile)
            }

            val name = queriesData.name
            if (!TextUtils.isEmpty(name)) {
                edName!!.setText(name)
            }

            val email = queriesData.email
            if (!TextUtils.isEmpty(email)) {
                edEmail!!.setText(email)
            }

            val phone = queriesData.phoneNum
            if (!TextUtils.isEmpty(phone)) {
                edNum!!.setText(phone)
            }

            val radioButton = queriesData.gender
            if (!TextUtils.isEmpty(radioButton)) {
                if (radioButton == getString(R.string.male)) {
                    rbMale!!.isChecked = true
                    rbFemale!!.isChecked = false
                } else {
                    rbMale!!.isChecked = false
                    rbFemale!!.isChecked = true
                }
            }


        } catch (e: Exception) {
            e.printStackTrace()
        }

    }


    private fun changeListener() {
        try {
            edName!!.addTextChangedListener(object : TextWatcher {
                override fun afterTextChanged(s: Editable) {
                }

                override fun beforeTextChanged(
                    s: CharSequence, start: Int,
                    count: Int, after: Int
                ) {

                }

                override fun onTextChanged(
                    s: CharSequence, start: Int,
                    before: Int, count: Int
                ) {
                    val edName = edName!!.text!!.trim().toString()
                    if (TextUtils.isEmpty(edName)) {
                        txtLayName!!.error = getString(R.string.str_empty_name)
                    } else {
                        txtLayName!!.isErrorEnabled = false
                    }
                }
            })

            edEmail!!.addTextChangedListener(object : TextWatcher {
                override fun afterTextChanged(s: Editable) {

                }

                override fun beforeTextChanged(
                    s: CharSequence, start: Int,
                    count: Int, after: Int
                ) {

                }

                override fun onTextChanged(
                    s: CharSequence, start: Int,
                    before: Int, count: Int
                ) {

                    val edEmail = edEmail!!.text!!.trim().toString()
                    if (TextUtils.isEmpty(edEmail)) {
                        txtLayEmail!!.error = getString(R.string.str_email_req)
                    } else {
                        txtLayEmail!!.isErrorEnabled = false
                        if (!isValidEmail(s)) {
                            txtLayEmail!!.error = getString(R.string.str_empty_email)
                        }
                    }
                }
            })

            edNum!!.addTextChangedListener(object : TextWatcher {
                override fun afterTextChanged(s: Editable?) {

                }

                override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

                }

                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

                    val edNum = edNum!!.text!!.trim().toString()
                    if (TextUtils.isEmpty(edNum)) {
                        txtLayNum!!.error = getString(R.string.str_empty_phn)
                    } else {
                        if (!isValidMobile(edNum)) {
                            txtLayNum!!.error = getString(R.string.str_phone_valid)
                        } else {
                            txtLayNum!!.isErrorEnabled = false
                        }

                    }
                }

            })
        } catch (e: Exception) {
            e.printStackTrace()
        }


    }

    private fun clickListener() {
        try {
            btnInsert!!.setOnClickListener(this)
            imgEditPic!!.setOnClickListener(this)

        } catch (e: Exception) {
            e.printStackTrace()
        }

    }


    override fun onClick(v: View?) {
        when (v!!.id) {
            R.id.imgEditPic -> {

                try {
                    if (checkPermissions()) {
                        selectImage()
                    } else {
                        requestPermissions()
                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                }

            }

            R.id.btnInsert -> {

                try {
                    insertData()
                } catch (e: Exception) {
                    e.printStackTrace()
                }

            }
        }
    }


    private fun insertData() {

        try {
            name = edName!!.text!!.trim().toString()
            email = edEmail!!.text!!.trim().toString()
            phoneNum = edNum!!.text!!.trim().toString()

            gender = if (rbMale!!.isChecked) {
                getString(R.string.male)
            } else {
                getString(R.string.female)
            }


            when {
                name!!.isEmpty() && TextUtils.isEmpty(name) -> showSnack(
                    activity!!,
                    constraintMain!!,
                    "Name Required"
                )
                !isValidEmail(email!!) && TextUtils.isEmpty(email) -> showSnack(
                    activity!!,
                    constraintMain!!,
                    getString(R.string.str_empty_email)
                )
                !isValidMobile(phoneNum!!) && TextUtils.isEmpty(phoneNum) -> showSnack(
                    activity!!,
                    constraintMain!!,
                    getString(R.string.str_empty_phn)
                )
                TextUtils.isEmpty(gender) -> showSnack(
                    activity!!,
                    constraintMain!!,
                    getString(R.string.str_empty_gender)
                )

                TextUtils.isEmpty(imgStr) -> showSnack(
                    activity!!,
                    constraintMain!!,
                    getString(R.string.str_empty_img)
                )
                else -> {
                    val result: Boolean = if (arguments != null) {
                        database!!.updateData(
                            id!!.toInt(),
                            name!!,
                            email!!,
                            phoneNum!!,
                            gender!!,

                            imgStr!!
                        )
                    } else {
                        database!!.insertData(name!!, email!!, phoneNum!!, gender!!, imgStr!!)
                    }
                    if (result) {

                        loadFragment(QueriesListFragment())

                    }
                }
            }

        } catch (e: Exception) {
            e.printStackTrace()
        }


    }

    private fun loadFragment(fragment: Fragment?): Boolean {
        (activity as MainActivity).navigationTab(pos = 1)

        if (fragment != null) {
            activity!!.supportFragmentManager
                .beginTransaction()
                .replace(R.id.frgContainer, fragment)
                .addToBackStack(null)
                .commitAllowingStateLoss()
            return true
        }
        return false
    }

    private fun requestPermissions() {
        try {
            showRequestedPermission()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }


    private fun showRequestedPermission() {
        try {
            requestPermissions(PERMISSIONS, REQUESTPERMISSIONSREQUESTCODE)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun checkPermissions(): Boolean {

        return ActivityCompat.checkSelfPermission(activity!!, PERMISSIONS[0]) == PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(activity!!, PERMISSIONS[1]) == PackageManager.PERMISSION_GRANTED
    }

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        when (requestCode) {
            REQUESTPERMISSIONSREQUESTCODE -> {
                run {
                    var isGranted = true
                    try {
                        if (grantResults.isNotEmpty()) {
                            for (result in grantResults) {
                                if (result != PackageManager.PERMISSION_GRANTED) {
                                    isGranted = false
                                }
                            }

                            if (isGranted) {
                                selectImage()
                            } else {
                                if (isDoNotAskAgain()) {
                                    showAlertDailog()

                                } else {
                                    showRequestedPermission()
                                }
                            }
                        }
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
                }
                run { super.onRequestPermissionsResult(requestCode, permissions, grantResults) }
            }


            else -> {
                super.onRequestPermissionsResult(requestCode, permissions, grantResults)
            }
        }
    }


    private fun showAlertDailog() {
        try {
            snackbar = Snackbar.make(
                constraintMain!!,
                getString(R.string.str_allow_permission),
                Snackbar.LENGTH_INDEFINITE
            )
            snackbar!!.view.setBackgroundColor(ContextCompat.getColor(activity!!, R.color.colorPrimary))

            snackbar!!.setAction(getString(R.string.ok)) {
                val intent = Intent()
                intent.action = Settings.ACTION_APPLICATION_DETAILS_SETTINGS
                intent.addCategory(Intent.CATEGORY_DEFAULT)
                intent.data = Uri.parse("package:" + activity!!.packageName)
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                intent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY)
                intent.addFlags(Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS)
                startActivity(intent)
            }
                .show()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun isDoNotAskAgain(): Boolean {

        for (permission in PERMISSIONS) {
            if (!ActivityCompat.shouldShowRequestPermissionRationale(activity!!, permission)) {

                try {
                    if (ActivityCompat.checkSelfPermission(
                            activity!!,
                            permission
                        ) == PackageManager.PERMISSION_DENIED
                    ) {

                        return true
                    }

                } catch (e: Exception) {
                    e.printStackTrace()

                }

            }
        }


        return false
    }

    private fun selectImage() {

        val items = arrayOf<CharSequence>(
            getString(R.string.str_take_pic), getString(R.string.str_take_gallery), getString(
                R.string.cancel
            )
        )
        val builder = AlertDialog.Builder(activity!!)
        builder.setTitle("Add Photo!")
        builder.setItems(items) { dialog, item ->
            when {
                items[item] == getString(R.string.str_take_pic) -> {
                    userChoosenTask = getString(R.string.str_take_pic)
                    cameraIntent()
                }
                items[item] == getString(R.string.str_take_gallery) -> {
                    userChoosenTask = getString(R.string.str_take_gallery)
                    galleryIntent()
                }
                items[item] == getString(R.string.cancel) -> dialog.dismiss()
            }
        }
        builder.show()
    }

    private fun galleryIntent() {
        try {

            val intent = Intent()
            intent.type = "image/*"
            intent.action = Intent.ACTION_GET_CONTENT
            startActivityForResult(Intent.createChooser(intent, getString(R.string.str_select_file)), SELECT_FILE)

        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun cameraIntent() {

        try {
            val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
            startActivityForResult(intent, REQUEST_CAMERA)
        } catch (e: Exception) {
            e.printStackTrace()
        }

    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        try {
            if (resultCode == Activity.RESULT_OK) {
                if (requestCode == SELECT_FILE)
                    onSelectFromGalleryResult(data)
                else if (requestCode == REQUEST_CAMERA)
                    onCaptureImageResult(data!!)
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }


    private fun onSelectFromGalleryResult(data: Intent?) {
        try {
            var bm: Bitmap? = null

            if (data != null) {
                try {
                    bm = MediaStore.Images.Media.getBitmap(activity!!.applicationContext.contentResolver, data.data)
                    imgUri = saveImage(bm)
                    imgStr = imgUri.toString()

                } catch (e: Exception) {
                    e.printStackTrace()
                }

            }

            img!!.setImageBitmap(bm)

        } catch (e: Exception) {
            e.printStackTrace()
        }
    }


    private fun onCaptureImageResult(data: Intent) {
        try {

            val thumbnail = data.extras!!.get(getString(R.string.bundle_data)) as Bitmap

            imgUri = saveImage(thumbnail)

            imgStr = imgUri.toString()

            img!!.setImageBitmap(thumbnail)

        } catch (e: Exception) {
            e.printStackTrace()
        }

    }


}

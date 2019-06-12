package com.incipientinfo.dbqueries.database

import android.annotation.SuppressLint
import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import com.incipientinfo.dbqueries.POJO.Queries

class Database(context: Context) : SQLiteOpenHelper(context, dbName, null, dbVersion) {

    companion object {
        const val dbName: String = "DBQueries"
        const val dbVersion: Int = 1
        const val DBTABLE = "InsertData"
        const val ID = "id"
        const val NAME = "Name"
        const val EMAIL = "Email"
        const val PHONENUMBER = "PhoneNum"
        const val GENDER = "Gender"
        const val PROFILEIMAGE = "ProfileImage"


    }

    var context: Context? = context

    override fun onCreate(db: SQLiteDatabase?) {
        val createTable = "CREATE TABLE $DBTABLE ($ID INTEGER PRIMARY KEY AUTOINCREMENT," +
                "$NAME TEXT," +
                "$EMAIL TEXT," +
                "$PHONENUMBER TEXT," +
                "$GENDER TEXT," +
                "$PROFILEIMAGE TEXT)"

        db!!.execSQL(createTable)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        db!!.execSQL("Drop table IF EXISTS $DBTABLE")
    }

    @SuppressLint("SimpleDateFormat")
    fun insertData(
        name: String,
        email: String,
        phoneNum: String,
        gender: String,

        imgUri: String
    ): Boolean {
        val result = try {
            val db = this.writableDatabase
            val values = ContentValues()

            values.put(NAME, name)
            values.put(EMAIL, email)
            values.put(PHONENUMBER, phoneNum)
            values.put(GENDER, gender)

            values.put(PROFILEIMAGE, imgUri)
            db.insert(DBTABLE, null, values)
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return (Integer.parseInt("$result") != -1)
    }

    @SuppressLint("Recycle")
    fun getAllData(): ArrayList<Queries> {

        val getList = ArrayList<Queries>()
        try {
            val db = writableDatabase
            val selectQuery = "SELECT * FROM $DBTABLE"

            val cursor = db.rawQuery(selectQuery, null)
            if (cursor.moveToFirst()) {
                while (!cursor.isAfterLast) {
                    val item = Queries()
                    item.id = cursor.getInt(cursor.getColumnIndex(ID))
                    item.name = cursor.getString(cursor.getColumnIndex(NAME))
                    item.email = cursor.getString(cursor.getColumnIndex(EMAIL))
                    item.phoneNum = cursor.getString(cursor.getColumnIndex(PHONENUMBER))
                    item.gender = cursor.getString(cursor.getColumnIndex(GENDER))

                    item.imgUri = cursor.getString(cursor.getColumnIndex(PROFILEIMAGE))

                    getList.add(item)

                    cursor.moveToNext()

                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }

        return getList

    }


    fun deleteData(id: Int): Int? {
        val db = this.writableDatabase
        return db.delete(DBTABLE, "ID = ?", arrayOf(id.toString()))
    }


    @SuppressLint("Recycle")
    fun getDataFromId(id1: Int): Queries {
        val item = Queries()
        try {
            val db = writableDatabase
            val selectQuery = "SELECT * FROM $DBTABLE WHERE $ID=$id1"

            val cursor = db.rawQuery(selectQuery, null)
            if (cursor.moveToFirst()) {

                item.id = cursor.getInt(cursor.getColumnIndex(ID))
                item.name = cursor.getString(cursor.getColumnIndex(NAME))
                item.email = cursor.getString(cursor.getColumnIndex(EMAIL))
                item.phoneNum = cursor.getString(cursor.getColumnIndex(PHONENUMBER))
                item.gender = cursor.getString(cursor.getColumnIndex(GENDER))

                item.imgUri = cursor.getString(cursor.getColumnIndex(PROFILEIMAGE))
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }

        return item
    }


    fun updateData(
        id1: Int,
        name: String,
        email: String,
        phoneNum: String,
        gender: String,
        imgUri: String
    ): Boolean {
        val result = try {
            val db = this.writableDatabase
            val values = ContentValues()

            values.put(NAME, name)
            values.put(EMAIL, email)
            values.put(PHONENUMBER, phoneNum)
            values.put(GENDER, gender)
            values.put(PROFILEIMAGE, imgUri)

            db.update(DBTABLE, values, "$ID=$id1", null)
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return (Integer.parseInt("$result") != -1)
    }

}





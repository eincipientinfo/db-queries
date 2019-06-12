package com.incipientinfo.dbqueries.ui.querieslist


import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.Context
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.incipientinfo.dbqueries.POJO.Queries
import com.incipientinfo.dbqueries.R
import com.incipientinfo.dbqueries.common.OnItemClickListener
import com.incipientinfo.dbqueries.database.Database
import com.incipientinfo.dbqueries.ui.home.HomeFragment
import com.incipientinfo.dbqueries.ui.main.MainActivity
import kotlinx.android.synthetic.main.fragment_queries_list.view.*


class QueriesListFragment : Fragment(), OnItemClickListener {


    private var rootView: View? = null
    private lateinit var recyclerList: RecyclerView
    private var listAdapter: ListAdapter? = null
    private var queriesList: ArrayList<Queries>? = null
    private var txtNoData: TextView? = null

    private var database: Database? = null


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        rootView = inflater.inflate(R.layout.fragment_queries_list, container, false)


        try {

            init()
            getDbData()
            initAdapter()
            placeHolder()

        } catch (e: Exception) {
            e.printStackTrace()
        }

        return rootView
    }

    private fun placeHolder() {

        if (queriesList!!.size > 0) {
            recyclerList.visibility = View.VISIBLE
            txtNoData!!.visibility = View.GONE
        } else {
            txtNoData!!.visibility = View.VISIBLE
            recyclerList.visibility = View.GONE
        }
    }

    private fun init() {
        try {
            txtNoData = rootView!!.txtNoData
            recyclerList = rootView!!.recyclerList
            queriesList = ArrayList()
            database = Database(context!!)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun getDbData() {
        try {
            queriesList!!.clear()
            queriesList!!.addAll(database!!.getAllData())
            listAdapter?.notifyDataSetChanged()

        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun initAdapter() {
        recyclerList.layoutManager =
            LinearLayoutManager((this@QueriesListFragment.activity as Context?)!!, LinearLayoutManager.VERTICAL, false)
        listAdapter = ListAdapter(queriesList!!, this)
        recyclerList.adapter = listAdapter
    }


    override fun onResume() {
        super.onResume()
        try {
            if (listAdapter != null) {
                listAdapter!!.notifyDataSetChanged()
                placeHolder()
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }


    @SuppressLint("SetTextI18n")
    override fun onItemClick(view: View, queries: Any, pos: Int) {
        val queriesData = queries as Queries




        when (view.id) {
            R.id.imgDelete -> {

                try {
                    val builder = AlertDialog.Builder(activity!!)

                    builder.setTitle(getString(R.string.confirm))
                    builder.setMessage(getString(R.string.str_delete_mes))

                    builder.setPositiveButton(getString(R.string.yes)) { dialog, _ ->

                        database!!.deleteData(queriesData.id!!)
                        queriesList!!.removeAt(pos)
                        listAdapter!!.notifyDataSetChanged()
                        placeHolder()
                        dialog.dismiss()
                    }

                    builder.setNegativeButton(getString(R.string.no)) { dialog, _ ->

                        dialog.dismiss()
                    }

                    val alert = builder.create()
                    alert.show()
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }

            R.id.imgEdit -> {

                try {

                    (activity as MainActivity).navigationTab(0)
                    val fragment = HomeFragment()
                    val bundle = Bundle()
                    bundle.putString("id", queriesData.id.toString())
                    fragment.arguments = bundle
                    activity!!.supportFragmentManager
                        .beginTransaction()
                        .replace(R.id.frgContainer, fragment)
                        .addToBackStack(null)
                        .commitAllowingStateLoss()

                } catch (e: Exception) {
                    e.printStackTrace()
                }


            }
        }

    }

}

package hughjd.xyz.aperio.activity

import android.content.Context
import android.os.Bundle
import android.support.constraint.ConstraintLayout
import android.support.design.widget.FloatingActionButton
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.Toolbar
import android.view.LayoutInflater
import android.view.MenuItem
import android.widget.ListView
import android.widget.TextView
import hughjd.xyz.aperio.Development
import hughjd.xyz.aperio.R
import hughjd.xyz.aperio.password.Password
import hughjd.xyz.aperio.password.PasswordListAdapter
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

class PasswordList : AppCompatActivity() {

    private val passwordList = mutableListOf<Password>()

    private lateinit var listView: ListView

    private lateinit var clearSearch: MenuItem

    private lateinit var searchTextLayout: ConstraintLayout

    private lateinit var searchTextView: TextView

    private lateinit var adapter: PasswordListAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_password_list)
        title = "All Passwords"
        registerPasswordListener()

        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)

        val fabNewPassword = findViewById<FloatingActionButton>(R.id.fab_new_password)
        fabNewPassword.setOnClickListener { onNewPassword() }

        val fabSearch = findViewById<FloatingActionButton>(R.id.fab_search)
        fabSearch.setOnClickListener { onSearch() }

        listView = findViewById(R.id.passwords_view)

        searchTextLayout = layoutInflater.inflate(R.layout.search_text_view, null) as ConstraintLayout
        searchTextView = searchTextLayout.findViewById(R.id.search_text_view)
        listView.addHeaderView(searchTextLayout, null, false)

        adapter = PasswordListAdapter(getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater, passwordList)
        listView.adapter = adapter

        // todo remove
        injectTestData()
    }

    fun onNewPassword() {

    }

    fun onSearch() {

    }

    fun viewPassword() {

    }

    fun showPasswordOptions() {

    }

    private fun registerPasswordListener() {
        Aperio.db?.passwordDao()?.getAll()
            ?.subscribeOn(Schedulers.io())
            ?.observeOn(AndroidSchedulers.mainThread())
            ?.subscribe { listOfPasswords ->
                passwordList.clear()
                passwordList.addAll(listOfPasswords)
                adapter.notifyDataSetChanged()
            }
    }

    // todo remove before release!
    private fun injectTestData() {
        Single.fromCallable {
            Aperio.db?.passwordDao()?.deleteAll()
            Aperio.db?.passwordDao()?.insertAll(Development.testPasswords())
        }
        .subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread()).subscribe()

    }
}

package hughjd.xyz.aperio.activity

import android.app.Activity
import android.arch.persistence.room.Room
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.constraint.ConstraintLayout
import android.support.design.widget.Snackbar
import android.support.v7.widget.Toolbar
import android.text.InputType
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.ListView
import android.widget.TextView
import com.afollestad.materialdialogs.MaterialDialog
import com.getbase.floatingactionbutton.FloatingActionButton
import com.getbase.floatingactionbutton.FloatingActionsMenu
import hughjd.xyz.aperio.Development
import hughjd.xyz.aperio.R
import hughjd.xyz.aperio.password.Password
import hughjd.xyz.aperio.password.PasswordDb
import hughjd.xyz.aperio.password.PasswordListAdapter
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import timber.log.Timber

class PasswordList : AperioActivity() {

    private lateinit var listView: ListView
    private lateinit var clearSearch: MenuItem
    private lateinit var clearSearchFab: FloatingActionButton
    private lateinit var searchTextLayout: ConstraintLayout
    private lateinit var searchTextView: TextView
    private lateinit var adapter: PasswordListAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_password_list)
        title = "All Passwords"

        PasswordList.db =  Room.databaseBuilder(this, PasswordDb::class.java, "pdb").build()
        registerPasswordListener()
        Timber.plant(Timber.DebugTree())

        val toolbar = findViewById<Toolbar>(R.id.password_list_toolbar)
        setSupportActionBar(toolbar)

        // set fab click listeners
        val fabNewPassword = findViewById<FloatingActionButton>(R.id.password_list_fab_new)
        fabNewPassword.setOnClickListener { onNewPassword() }
        val fabSearch = findViewById<FloatingActionButton>(R.id.password_list_fab_search)
        fabSearch.setOnClickListener { onClickSearch() }
        clearSearchFab = findViewById(R.id.password_list_fab_clear_search)
        clearSearchFab.setOnClickListener { onClearSearch() }

        val fabMenu = findViewById<FloatingActionsMenu>(R.id.password_list_fab_menu)
        listView = findViewById(R.id.passwords_view)

        searchTextLayout = layoutInflater.inflate(R.layout.search_text_view, null) as ConstraintLayout
        searchTextView = searchTextLayout.findViewById(R.id.search_text_view)
        listView.addHeaderView(searchTextLayout, null, false)

        adapter = PasswordListAdapter(getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater)
        listView.adapter = adapter

        // subtract 1 from position to account for the search text headerview in the first position (even if not shown)
        listView.setOnItemClickListener { _, _, position, _ -> onViewPassword(adapter.getItem(position - 1)) }

        // todo remove
        injectTestData()
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_password_list, menu)
        clearSearch = menu.findItem(R.id.clear_search)
        clearSearch.setOnMenuItemClickListener { _ -> onClearSearch() }
        clearSearch.isEnabled = false
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val sort = when (item.itemId) {
            R.id.sort_alpha -> Password.SORT_ALPHA
            R.id.sort_zeta -> Password.SORT_ZETA
            R.id.sort_new -> Password.SORT_NEW
            R.id.sort_old -> Password.SORT_OLD
            else -> return super.onOptionsItemSelected(item)
        }

        adapter.sortPasswords(sort)
        return true
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == VIEW_PASSWORD_REQUEST && resultCode == Activity.RESULT_OK) {
            val deletedPassword = data?.extras?.getSerializable(Password.BUNDLE_KEY) as Password? ?: Password.empty()
            adapter.deletePassword(deletedPassword)

            Snackbar.make(listView, R.string.deleted_password, Snackbar.LENGTH_LONG)
                    .setAction(R.string.undo) { adapter.revertLastDelete() }
                    .show()
        }
    }

    private fun onClickSearch() {
        MaterialDialog.Builder(this)
                .title("Search")
                .content("Search for a password by partial name")
                .inputType(InputType.TYPE_CLASS_TEXT)
                .input("", "", { _, input -> onApplySearch(input) }).show()
    }

    private fun onApplySearch(input: CharSequence) {
        adapter.filterByName(input.toString())
        clearSearch.isEnabled = true
        searchTextView.text = getString(R.string.search_text_view_template, input)
        searchTextView.visibility = View.VISIBLE
        clearSearchFab.visibility = View.VISIBLE
    }

    private fun onClearSearch(): Boolean {
        adapter.clearFilters()
        clearSearch.isEnabled = false
        searchTextView.visibility = View.GONE
        clearSearchFab.visibility = View.GONE
        return true
    }

    private fun onViewPassword(password: Password) {
        val passwordViewIntent = Intent(this, PasswordView::class.java)
        passwordViewIntent.putExtra(Password.BUNDLE_KEY, password)
        startActivityForResult(passwordViewIntent, VIEW_PASSWORD_REQUEST)
    }

    private fun onNewPassword() {
        val newPasswordIntent = Intent(this, PasswordEdit::class.java)
        startActivity(newPasswordIntent)
    }

    fun showPasswordOptions() {

    }

    private fun registerPasswordListener() {
        PasswordList.db?.passwordDao()?.getAll()
            ?.subscribeOn(Schedulers.io())
            ?.observeOn(AndroidSchedulers.mainThread())
            ?.subscribe { listOfPasswords ->
                adapter.updatePasswords(listOfPasswords)
            }
    }

    // todo remove before release!
    private fun injectTestData() {
        Single.fromCallable {
            PasswordList.db?.passwordDao()?.deleteAll()
            PasswordList.db?.passwordDao()?.insertAll(Development.testPasswords())
        }
        .subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread()).subscribe()

    }

    companion object {

        private const val VIEW_PASSWORD_REQUEST = 1337

        var db: PasswordDb? = null
    }
}

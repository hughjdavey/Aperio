package hughjd.xyz.aperio.password

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView

class PasswordListAdapter(private val inflater: LayoutInflater) : BaseAdapter() {

    private var allPasswords = mutableListOf<Password>()

    private var shownPasswords = listOf<Password>()

    override fun getCount(): Int {
        return shownPasswords.size
    }

    override fun getItem(position: Int): Password {
        return shownPasswords[position]
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getView(position: Int, convertView: View?, container: ViewGroup): View {
        var view = convertView
        if (view == null) {
            view = inflater.inflate(android.R.layout.simple_list_item_1, container, false)
        }

        (view as TextView).text = getItem(position).name
        return view
    }

    fun updatePasswords(passwords: Collection<Password>) {
        allPasswords.clear()
        allPasswords.addAll(passwords)

        shownPasswords = allPasswords.toList()
        notifyDataSetChanged()
    }

    fun sortPasswords(sort: Comparator<Password>) {
        shownPasswords = allPasswords.sortedWith(sort)
        notifyDataSetChanged()
    }

    fun filterByName(partialName: String) {
        shownPasswords = allPasswords.filter { it.name.toLowerCase().contains(partialName.toLowerCase()) }
        notifyDataSetChanged()
    }

    fun clearFilters() {
        shownPasswords = allPasswords.toList()
        notifyDataSetChanged()
    }

    fun deletePassword(password: Password) {
        LAST_DELETED_PASSWORD = password
        LAST_DELETED_INDEX = allPasswords.indexOf(password)

        this.allPasswords.removeAt(LAST_DELETED_INDEX)
        clearFilters()
    }

    fun revertLastDelete() {
        this.allPasswords.add(LAST_DELETED_INDEX, LAST_DELETED_PASSWORD)
        clearFilters()
    }

    companion object {

        private var LAST_DELETED_INDEX = -1
        private var LAST_DELETED_PASSWORD = Password.empty()
    }
}

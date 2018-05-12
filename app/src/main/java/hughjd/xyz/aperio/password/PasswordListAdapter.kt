package hughjd.xyz.aperio.password

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView

class PasswordListAdapter(private val inflater: LayoutInflater, private val passwords: List<Password>) : BaseAdapter() {

    override fun getCount(): Int {
        return passwords.size
    }

    override fun getItem(position: Int): Password {
        return passwords[position]
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
}

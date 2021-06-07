package at.tugraz.ist.guessingwords.ui.multiplayer.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView
import at.tugraz.ist.guessingwords.R
import at.tugraz.ist.guessingwords.data.entity.Word

class HostAdapter(val context: Context, val joinedUser: List<String>) : BaseAdapter() {

    private val inflater: LayoutInflater = context.getSystemService(
            Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val view: View
        val viewHolder: ViewHolder

        if (convertView == null) {
            view = inflater.inflate(R.layout.list_item_joined_user, parent, false)

            viewHolder = ViewHolder()
            viewHolder.tvJoinedUser = view.findViewById<TextView>(R.id.li_joinedUser_text)
            view.tag = viewHolder
        } else {
            view = convertView
            viewHolder = convertView.tag as ViewHolder
        }

        viewHolder.tvJoinedUser.text = getItem(position) as String

        return view
    }

    override fun getItem(position: Int): Any {
        return joinedUser[position]
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getCount(): Int {
        return joinedUser.size
    }

    private class ViewHolder {
        lateinit var tvJoinedUser : TextView
    }
//
//    override fun getViewTypeCount(): Int {
//        return count
//    }
//
//    override fun getItemViewType(position: Int): Int {
//        return position
//    }
}
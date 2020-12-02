package ru.skillbranch.devintensive.ui.adapters

import android.content.ClipData
import android.graphics.Color
import android.util.Log
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.annotation.AttrRes
import androidx.annotation.ColorInt
import androidx.appcompat.view.menu.MenuView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.activity_profile.*
import kotlinx.android.synthetic.main.item_chat_single.*
import kotlinx.android.synthetic.main.item_chat_single.view.*
import ru.skillbranch.devintensive.R
import ru.skillbranch.devintensive.models.data.ChatItem
import ru.skillbranch.devintensive.ui.custom.AvatarImageView
import ru.skillbranch.devintensive.ui.custom.AvatarInitialsDrawable
import ru.skillbranch.devintensive.ui.custom.CircleImageView

class ChatAdapter(val listener: (ChatItem)-> Unit) : RecyclerView.Adapter<ChatAdapter.SingleViewHolder>() {
    var items: List<ChatItem> = listOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SingleViewHolder {
        var inflater = LayoutInflater.from(parent.context)
        val convertView  = inflater.inflate(R.layout.item_chat_single, parent, false)
//        Log.d("M_ChatAdapter","onCreateViewHolder")
        return SingleViewHolder(convertView)
    }

    override fun onBindViewHolder(holder: SingleViewHolder, position: Int) {
        Log.d("M_ChatAdapter","onBindViewHolder $position")
        holder.bind(items[position], listener)
    }

    override fun getItemCount(): Int = items.size

    fun updateData(data: List<ChatItem>){

        Log.d("M_ChatAdapter","update data adapter - new data ${data.size} hash: ${data.hashCode()}" +
            "old data ${items.size} hash: ${items.hashCode()}")

        val diffCallback = object: DiffUtil.Callback(){
            override fun getOldListSize(): Int = items.size

            override fun getNewListSize(): Int = data.size

            override fun areItemsTheSame(oldPos: Int, newPos: Int): Boolean = items[oldPos].id == data[newPos].id

            override fun areContentsTheSame(oldPos: Int, newPos: Int): Boolean = items[oldPos].hashCode() == data[newPos].hashCode()
        }

        val diffResult = DiffUtil.calculateDiff(diffCallback)

        items = data
        diffResult.dispatchUpdatesTo(this)
//        notifyDataSetChanged()
    }

    inner class SingleViewHolder(convertView: View): RecyclerView.ViewHolder(convertView),
            LayoutContainer, ItemTouchViewHolder{

        override fun onItemSelected() {
            itemView.setBackgroundColor(Color.LTGRAY)
        }

        override fun onItemCleared() {
            itemView.setBackgroundColor(Color.WHITE)
        }

        override val containerView: View?
        get() = itemView

        fun bind(item: ChatItem, listener: (ChatItem) -> Unit) {
            if(item.avatar == null) {
                iv_avatar_single.setInitials(item.initials)
            }else{
                // TODO: 11/30/20 set drawable 
            }

            sv_indicator.visibility = if (item.isOnline) View.VISIBLE else View.VISIBLE
            with(tv_date_single){
                visibility = if (item.lastMessageDate != null) View.VISIBLE else View.GONE
                text = item.lastMessageDate
            }

            with(tv_counter_single){
                visibility = if (item.messageCount > 0) View.VISIBLE else View.GONE
                text = item.messageCount.toString()
            }
            tv_title_single.text = item.title
            tv_message_single.text = item.shortDescription
            itemView.setOnClickListener{
                listener.invoke(item)
            }

        }
    }
}

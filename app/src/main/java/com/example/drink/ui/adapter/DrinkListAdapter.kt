package com.example.drink.ui.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.drink.R
import com.example.drink.data.model.Drink
import java.text.SimpleDateFormat
import java.util.*

class DrinkListAdapter internal constructor(context: Context) :
    RecyclerView.Adapter<DrinkListAdapter.EventViewHolder>() {
    private val formatter = SimpleDateFormat("HH:mm")
    private val inflater: LayoutInflater = LayoutInflater.from(context)
    private var drinks = emptyList<Drink>()
    private var isUnitMl = true

    var selecteds = arrayListOf<Drink>()
    var onItemClick: ((Drink) -> Unit)? = null
    var onItemLongClick: ((Drink) -> Unit)? = null

    inner class EventViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val dateTextView: TextView = itemView.findViewById(R.id.text_date)
        val amountTextView: TextView = itemView.findViewById(R.id.text_amount)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EventViewHolder {
        val itemView = inflater.inflate(R.layout.recyclerview_item_drink, parent, false)
        return EventViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: EventViewHolder, position: Int) {
        val current = drinks[position]
        holder.dateTextView.text = formatter.format(Date(current.date))
        holder.amountTextView.text =
            if (isUnitMl) current.getMlAmount().toString() + " ml" else current.getOzAmount()
                .toString() + " oz"

        if (selecteds.contains(current)) {
            holder.itemView.setBackgroundResource(R.drawable.recyclerview_item_shape_selected)
        } else {
            holder.itemView.setBackgroundResource(R.drawable.recyclerview_item_shape)
        }

        holder.itemView.setOnClickListener {
            if (selecteds.contains(current)) {
                selecteds.remove(current)
            } else {
                selecteds.add(current)
            }

            onItemClick?.invoke(current)
            notifyDataSetChanged()
        }

        holder.itemView.setOnLongClickListener {
            onItemLongClick?.invoke(current)
            true
        }
    }

    internal fun setDrinks(events: List<Drink>, isUnitMl: Boolean) {
        this.drinks = events
        this.isUnitMl = isUnitMl
        notifyDataSetChanged()
    }

    override fun getItemCount() = drinks.size
}
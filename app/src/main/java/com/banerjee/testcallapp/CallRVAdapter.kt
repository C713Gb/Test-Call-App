package com.banerjee.testcallapp

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.banerjee.testcallapp.model.CallDataModel
import kotlinx.android.synthetic.main.call_item.view.*

class CallRVAdapter(val context: Context, val callList: ArrayList<CallDataModel>) :
    RecyclerView.Adapter<CallRVAdapter.ViewHolder>() {

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(callDataModel: CallDataModel) {
            itemView.phone_txt.text = callDataModel.phone
            itemView.type_txt.text = callDataModel.type
            itemView.date_txt.text = callDataModel.date.toString()
            itemView.duration_txt.text = callDataModel.duration
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CallRVAdapter.ViewHolder {
        val view: View = LayoutInflater
            .from(context)
            .inflate(R.layout.call_item, parent, false)

        return CallRVAdapter.ViewHolder(view)
    }

    override fun onBindViewHolder(holder: CallRVAdapter.ViewHolder, position: Int) {
        holder.bind(callList[position])
    }

    override fun getItemCount(): Int {
        return callList.size
    }
}
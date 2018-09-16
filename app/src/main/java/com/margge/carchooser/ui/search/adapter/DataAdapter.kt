package com.margge.carchooser.ui.search.adapter

import android.arch.lifecycle.MutableLiveData
import android.support.v4.app.FragmentActivity
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import com.margge.carchooser.Event
import com.margge.carchooser.databinding.EvenItemLayoutBinding
import com.margge.carchooser.databinding.OddItemLayoutBinding

class DataAdapter(private val activity: FragmentActivity,
                  private val viewEvents: MutableLiveData<Event>)
    : RecyclerView.Adapter<RecyclerView.ViewHolder>(), Filterable {

    companion object {
        private const val EVEN = 0
        private const val ODD = 1
    }

    private var mDataFilter: DataFilter? = null
    private var mDataList: MutableList<Pair<String, String>> = mutableListOf()
    private var mOriginalDataList: MutableList<Pair<String, String>> = mutableListOf()

    override fun getItemViewType(position: Int): Int =
            if (position % 2 == 0) {
                EVEN
            } else {
                ODD
            }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        var viewHolder: RecyclerView.ViewHolder? = null

        when (viewType) {
            EVEN ->
                viewHolder = EvenRowViewHolder(EvenItemLayoutBinding.inflate(LayoutInflater.from(parent.context),
                        parent, false), activity, viewEvents)
            ODD ->
                viewHolder = OddRowViewHolder(OddItemLayoutBinding.inflate(LayoutInflater.from(parent.context),
                        parent, false), activity, viewEvents)
        }

        return viewHolder as RecyclerView.ViewHolder
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (getItemViewType(position)) {
            EVEN ->
                (holder as EvenRowViewHolder).bind(mDataList[position])
            ODD ->
                (holder as OddRowViewHolder).bind(mDataList[position])
        }
    }

    override fun getItemCount(): Int = mDataList.size

    fun addAll(dataList: List<Pair<String, String>>) {
        mDataList.addAll(dataList)
        mOriginalDataList.addAll(dataList)
        notifyDataSetChanged()
    }

    override fun getFilter(): Filter {
        if (mDataFilter == null) {
            mDataFilter = DataFilter()
        }
        return mDataFilter as DataFilter
    }

    private inner class DataFilter : Filter() {

        override fun performFiltering(constraint: CharSequence?): Filter.FilterResults {
            val results = Filter.FilterResults()

            if (constraint != null && constraint.isNotEmpty()) {

                val filteredList: MutableList<Pair<String, String>> = mutableListOf()

                for (country in mOriginalDataList) {
                    if (country.second.toLowerCase().startsWith(constraint.toString().toLowerCase())) {
                        filteredList.add(country)
                    }
                }
                results.count = filteredList.size
                results.values = filteredList
            } else {
                results.count = mOriginalDataList.size
                results.values = mOriginalDataList
            }
            return results
        }

        override fun publishResults(constraint: CharSequence, results: Filter.FilterResults) {
            mDataList = results.values as MutableList<Pair<String, String>>
            notifyDataSetChanged()
        }
    }

    class EvenRowViewHolder(private val binding: EvenItemLayoutBinding,
                            private val activity: FragmentActivity,
                            private val events: MutableLiveData<Event>) : RecyclerView.ViewHolder(binding.root) {

        fun bind(item: Pair<String, String>) {
            var currentViewModel = binding.dataAdapterViewModel

            currentViewModel ?: run {
                currentViewModel = DataAdapterViewModel(activity.application, events)
                binding.dataAdapterViewModel = currentViewModel
            }

            currentViewModel?.apply {
                bindData(item)
            }
            binding.executePendingBindings()
        }
    }

    class OddRowViewHolder(private val binding: OddItemLayoutBinding,
                           private val activity: FragmentActivity,
                           private val events: MutableLiveData<Event>) : RecyclerView.ViewHolder(binding.root) {

        fun bind(item: Pair<String, String>) {
            var currentViewModel = binding.dataAdapterViewModel

            currentViewModel ?: run {
                currentViewModel = DataAdapterViewModel(activity.application, events)
                binding.dataAdapterViewModel = currentViewModel
            }

            currentViewModel?.apply {
                bindData(item)
            }
            binding.executePendingBindings()
        }
    }
}
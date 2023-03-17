package com.test.waltester.ui.main

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.test.waltester.data.cache.model.CountryInfoEntity
import com.test.waltester.databinding.WtlistitemBinding

class WTCountriesRecyclerAdapter:
    RecyclerView.Adapter<WTCountriesRecyclerAdapter.ViewHolder>(){

    private lateinit var binding: WtlistitemBinding

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int):
            WTCountriesRecyclerAdapter.ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        binding = WtlistitemBinding.inflate(inflater, parent,false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: WTCountriesRecyclerAdapter.ViewHolder,
                                  position: Int) {
        holder.bindItem(differ.currentList[position])
    }

    override fun getItemCount() = differ.currentList.size

    inner class ViewHolder internal constructor(private var binding: WtlistitemBinding):
        RecyclerView.ViewHolder(binding.root) {
        fun bindItem(item: CountryInfoEntity?) {
            val name = item?.name + if(item?.region?.isEmpty() == true) "" else ", "
            binding.nameTextview.text = name
            binding.regionTextview.text = item?.region
            binding.codeTextview.text = item?.code
            binding.capitalTextview.text = item?.capital
        }
    }

    private val differCallback = object : DiffUtil.ItemCallback<CountryInfoEntity>(){
        override fun areItemsTheSame(oldItem: CountryInfoEntity,
                                     newItem: CountryInfoEntity): Boolean {
            return  oldItem == newItem
        }

        override fun areContentsTheSame(oldItem: CountryInfoEntity,
                                        newItem: CountryInfoEntity): Boolean {
            return oldItem.name == newItem.name
                    && oldItem.region == newItem.region
                    && oldItem.code == newItem.code
        }
    }

    val differ = AsyncListDiffer(this, differCallback)
}
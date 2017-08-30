package nz.co.trademe.mapme.util

import java.util.*

data class TestItem(val id: String = UUID.randomUUID().toString(), val startPosition: Int, val zIndex: Float = 0f)

class TestItemDiffCallback(private val mOldList: List<nz.co.trademe.mapme.util.TestItem>, private val mNewList: List<nz.co.trademe.mapme.util.TestItem>) : android.support.v7.util.DiffUtil.Callback() {

    override fun getOldListSize(): Int {
        return mOldList.size
    }

    override fun getNewListSize(): Int {
        return mNewList.size
    }

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return mNewList[newItemPosition].id == mOldList[oldItemPosition].id
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return mNewList[newItemPosition] == mOldList[oldItemPosition]
    }

}
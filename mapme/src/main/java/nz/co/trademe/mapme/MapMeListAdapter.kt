package nz.co.trademe.mapme

import android.content.Context
import androidx.recyclerview.widget.AsyncDifferConfig
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import nz.co.trademe.mapme.annotations.AnnotationFactory

abstract class MapMeListAdapter<T, MapType>(context: Context, factory: AnnotationFactory<MapType>, config: AsyncDifferConfig<T>)
    : MapMeAdapter<MapType>(context, factory) {

    @Suppress("LeakingThis")
    private val helper: AsyncListDiffer<T> = AsyncListDiffer(this, config)

    constructor(context: Context, factory: AnnotationFactory<MapType>, diffCallback: DiffUtil.ItemCallback<T>)
            : this(context, factory, AsyncDifferConfig.Builder(diffCallback).build())

    fun submitList(list: List<T>?) {
        helper.submitList(list)
    }

    protected fun getItem(position: Int): T = helper.currentList[position]

    override fun getItemCount(): Int = helper.currentList.size
}
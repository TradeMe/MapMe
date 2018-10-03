package nz.co.trademe.mapme

import android.annotation.SuppressLint
import android.content.Context
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import android.view.View
import com.nhaarman.mockito_kotlin.spy
import nz.co.trademe.mapme.util.TestAdapter
import nz.co.trademe.mapme.util.TestAnnotation
import nz.co.trademe.mapme.util.TestAnnotationFactory
import nz.co.trademe.mapme.util.TestItem
import nz.co.trademe.mapme.util.TestItemDiffCallback
import nz.co.trademe.mapme.util.TestMap
import nz.co.trademe.mapme.util.addItems
import org.junit.Before
import org.mockito.Mock
import org.mockito.MockitoAnnotations

@SuppressLint("NewApi")
abstract class BaseAdapterTest {

    @Mock
    lateinit var context: Context

    @Mock
    lateinit var mapView: View

    var items = arrayListOf<TestItem>()

    lateinit var adapter: TestAdapter
    var adapterObserver: RecyclerView.AdapterDataObserver = spy(TestDataObserver())

    @Before
    fun before() {
        MockitoAnnotations.initMocks(this)
        items.clear()
    }

    fun createAdapter(count: Int) {
        items = addItems(items, count)
        val factory = TestAnnotationFactory()

        adapter = TestAdapter(items, context, factory)
        resetVerification()
        adapter.attach(mapView, TestMap())
        adapter.notifyDataSetChanged()
        adapter.await()
        adapter.resetCounts()
    }

    /**
     * Resets the adapter observer and adapter update/create verifications
     */
    fun resetVerification() {
        adapter.unregisterObserver(adapterObserver)
        val observer = TestDataObserver()
        adapterObserver = spy(observer)
        adapter.registerObserver(adapterObserver)

        adapter.resetCounts()
    }

    /**
     * Waits for all asynchronous updates to complete
     */
    fun TestAdapter.await() = phaser.arriveAndAwaitAdvance()


    /**
     * Convenience method to find the annotation with a matching position
     */
    fun TestAdapter.annotationWithPosition(position: Int): TestAnnotation {
        return annotations.find { it.position == position } as TestAnnotation
    }

    open fun dispatchDiff(old: List<TestItem>, new: List<TestItem>, detectMoves: Boolean = false) : TestAdapter {
        val callback = TestItemDiffCallback(old, new)
        val diffResult = DiffUtil.calculateDiff(callback, detectMoves)

        items.clear()
        items.addAll(new)
        diffResult.dispatchUpdatesTo(adapter)
        return adapter
    }

    /**
     * A test observer used for verification
     */
    open class TestDataObserver : RecyclerView.AdapterDataObserver() {
        override fun onChanged() {
            println("onChanged")
        }

        override fun onItemRangeRemoved(positionStart: Int, itemCount: Int) {
            super.onItemRangeRemoved(positionStart, itemCount)
            println("onItemRangeRemoved positionStart: $positionStart, itemCount: $itemCount")
        }

        override fun onItemRangeMoved(fromPosition: Int, toPosition: Int, itemCount: Int) {
            super.onItemRangeMoved(fromPosition, toPosition, itemCount)
            println("onItemRangeMoved fromPosition: $fromPosition, toPosition: $toPosition, itemCount: $itemCount")
        }

        override fun onItemRangeInserted(positionStart: Int, itemCount: Int) {
            super.onItemRangeInserted(positionStart, itemCount)
            println("onItemRangeInserted positionStart: $positionStart, itemCount: $itemCount")
        }

        override fun onItemRangeChanged(positionStart: Int, itemCount: Int) {
            super.onItemRangeChanged(positionStart, itemCount)
            println("onItemRangeChanged positionStart: $positionStart, itemCount: $itemCount")
        }
    }

}

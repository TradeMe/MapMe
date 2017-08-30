package nz.co.trademe.mapme.simple

import nz.co.trademe.mapme.BaseAdapterTest
import nz.co.trademe.mapme.util.addItems
import nz.co.trademe.mapme.util.changeItem
import nz.co.trademe.mapme.util.moveItem
import nz.co.trademe.mapme.util.removeItems
import org.amshove.kluent.shouldEqual
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.Parameterized

/**
 * Tests simple change operations
 */
@android.annotation.SuppressLint("NewApi")
@RunWith(Parameterized::class)
class AdapterTestSimple(val detectMoves: Boolean) : BaseAdapterTest() {

    companion object {
        @JvmStatic
        @Parameterized.Parameters
        fun data(): Collection<Boolean> {
            return listOf(false, true)
        }
    }

    /**
     * Tests adding items to the list
     */
    @Test
    fun addition() {
        createAdapter(0).run { resetVerification() }

        dispatchDiff(items, addItems(items, 5), detectMoves).await()

        adapter.annotations.size shouldEqual 5

        adapter.createCount shouldEqual 5
        adapter.bindCount shouldEqual 5
    }

    /**
     * Tests removing items from the list
     */
    @Test
    fun removal() {
        createAdapter(10).run { resetVerification() }

        dispatchDiff(items, removeItems(items, 5), detectMoves).await()

        adapter.annotations.size shouldEqual 5

        adapter.createCount shouldEqual 0
        adapter.bindCount shouldEqual 0
    }

    /**
     * Tests changing (updating) an item in the list
     */
    @Test
    fun updating() {
        createAdapter(10).run { resetVerification() }

        dispatchDiff(items, changeItem(items, 1), detectMoves).await()

        adapter.annotations.size shouldEqual 10

        //ensure item and annotation was changed
        1f shouldEqual adapter.items[1].zIndex
        1f shouldEqual adapter.annotationWithPosition(1).zIndex

        adapter.createCount shouldEqual 0
        adapter.bindCount shouldEqual 1
    }

    /**
     * Tests moving an item in the list
     */
    @Test
    fun moving() {
        createAdapter(10).run { resetVerification() }

        dispatchDiff(items, moveItem(items, 0, 9), detectMoves).await()

        adapter.items[0].startPosition shouldEqual 1
        adapter.items[9].startPosition shouldEqual 0

        if (detectMoves) {
            adapter.createCount shouldEqual 0
            adapter.bindCount shouldEqual 0
        } else {
            adapter.createCount shouldEqual 1
            adapter.bindCount shouldEqual 1
        }
    }

}


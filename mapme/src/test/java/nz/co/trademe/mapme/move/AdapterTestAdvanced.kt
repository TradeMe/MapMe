package nz.co.trademe.mapme.move

import android.annotation.SuppressLint
import nz.co.trademe.mapme.BaseAdapterTest
import nz.co.trademe.mapme.util.addItems
import nz.co.trademe.mapme.util.moveAndChangeItem
import nz.co.trademe.mapme.util.moveItem
import nz.co.trademe.mapme.util.shuffleItems
import org.amshove.kluent.shouldBeGreaterThan
import org.amshove.kluent.shouldEqual
import org.amshove.kluent.shouldNotEqual
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.Parameterized
import java.util.*

/**
 * Tests advanced change operations
 */
@SuppressLint("NewApi")
@RunWith(Parameterized::class)
class AdapterTestAdvanced(val detectMoves: Boolean) : BaseAdapterTest() {

    companion object {
        @JvmStatic
        @Parameterized.Parameters
        fun data(): Collection<Boolean> {
            return listOf(false, true)
        }
    }

    /**
     * Tests shuffling the list, but making no changes
     */
    @Test
    fun shuffle() {
        createAdapter(10).run { resetVerification() }

        dispatchDiff(items, shuffleItems(items), detectMoves).await()

        if (detectMoves) {
            //when shuffling items, nothing should be created when detect moves is enabled
            adapter.createCount shouldEqual 0
            adapter.bindCount shouldEqual 0
        } else {
            //when shuffling without move detection, items should be recreated
            adapter.createCount shouldBeGreaterThan 0
            adapter.bindCount shouldBeGreaterThan 0
        }
    }

    /**
     * Tests moving an item and changing it at the same time
     */
    @Test
    fun move_change() {
        createAdapter(3).run { resetVerification() }

        ArrayList(items)
                .run { moveAndChangeItem(this, 0, 2) }
                .run { dispatchDiff(items, this, detectMoves).await() }

        if (detectMoves) {
            adapter.bindCount shouldEqual 1
            adapter.createCount shouldEqual 0
        } else {
            adapter.bindCount shouldEqual 1
            adapter.createCount shouldEqual 1
        }

        //ensure this item and its annotation both have had their value changed
        adapter.items[2].zIndex shouldNotEqual 0f
        adapter.annotationWithPosition(2).zIndex shouldNotEqual 0f
    }

    /**
     * Tests moving and changing an item, and moving another item
     */
    @Test
    fun move_change_advanced() {
        createAdapter(3)

        resetVerification()

        ArrayList(items)
                .run { moveAndChangeItem(this, 0, 2) }
                .run { moveItem(this, 1, 0) }
                .run { dispatchDiff(items, this, detectMoves).await() }

        if (detectMoves) {
            adapter.bindCount shouldEqual 1
        } else {
            adapter.bindCount shouldEqual 3
        }

        adapter.items[2].zIndex shouldEqual 1f
    }

    /**
     * Tests moving and changing an item, and moving another item on a larger list size
     */
    @Test
    fun move_change_advanced_large() {
        createAdapter(0)

        dispatchDiff(items, addItems(items, 10), detectMoves)
                .run { adapter.await() }
                .run { resetVerification() }

        //change item and move to end
        ArrayList(items)
                .run { moveAndChangeItem(this, 0, items.size - 1) }
                .run { moveItem(this, 1, 0) }
                .run { dispatchDiff(items, this, true).await() }

        adapter.bindCount shouldEqual 1
        adapter.items[items.size - 1].zIndex shouldEqual 1f
    }

}


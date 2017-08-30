package nz.co.trademe.mapme

import com.nhaarman.mockito_kotlin.any
import com.nhaarman.mockito_kotlin.eq
import com.nhaarman.mockito_kotlin.times
import com.nhaarman.mockito_kotlin.verify
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.MockitoAnnotations

class AdapterHelperTest {

    @Mock
    lateinit var callbacks: MapAdapterHelper.MapAdapterHelperCallback

    @Before
    fun before() {
        MockitoAnnotations.initMocks(this)
    }

    /**
     * Given the following list:
     *
     *      Item 1,
     *      Item 2,
     *      Item 3,
     *
     * And the following operations:
     *
     *      * Change value of field in Item at position 0
     *      * Move item from position 0 to position 2
     *
     * The following list of updates will be produced: (See AdapterTestMove#move_change_advanced)
     *
     *      update(0, 1)
     *      move(2, 0)
     *
     * The updates should be reordered and adjusted to give the correct output, which should be:
     *
     *      update(2, 0) (The update should have its position corrected to be the final position of the item)
     *      move(2, 0)
     */
    @Test
    fun testAdapterHelper() {
        val adapterHelper = MapAdapterHelper(callbacks, false)
        adapterHelper.onItemRangeChanged(0, 1, null)
        adapterHelper.onItemRangeMoved(2, 0, 1)

        adapterHelper.dispatch()


        verify(callbacks, times(1)).dispatchUpdate(eq(UpdateOp(UpdateOp.UPDATE, 0, 1, null)))
        verify(callbacks, times(1)).dispatchUpdate(eq(UpdateOp(UpdateOp.MOVE, 2, 0, null)))

        verify(callbacks, times(2)).dispatchUpdate(any())
    }
}


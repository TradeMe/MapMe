package nz.co.trademe.mapme

import android.util.Log

/**
 * Stores adapter updates, reorders them, and dispatches them to the adapter.
 *
 * NOTE: To void confusion between updates operations and the update operation type (an item changed),
 * we refer to the later as change operations.
 *
 * This is needed because updates from DiffUtil cannot be applied directly to the list.
 *
 * Updates need to be reordered - reordering ensures that 'move' updates are at the end of the list.
 *
 * The updates must then be applied in 2 passes.
 *
 * The first pass does not make any structural changes to the list. This allows change operations
 * to occur on the correct item.
 *
 * Consider the following update list:
 *
 *      ADD(0,1)
 *      CHANGE(1,1)
 *
 * This update list is the result of changing the first item in the list, and adding a new one at the start.
 * If all updates were applied sequentially, the change operation would occur on the 2nd item of the
 * original list, rather than the first.
 *
 * To solve this, updates are applied in the following sequence:
 *
 * 1. First pass - marks and annotations with changes as needing an update
 * 2. Second pass - structural updates such as REMOVE, and MOVE are applied, placeholders for ADD operations are created
 * 3. Third pass - annotations marked as needing updates are updated, placeholders are replaced with real annotations
 *
 */
class MapAdapterHelper(val callbacks: MapAdapterHelperCallback, val debug: Boolean = false) : OpReorderer.Callback {

    private val TAG = "MapAdapterHelper"

    internal val opReorder: OpReorderer by lazy {
        OpReorderer(this)
    }

    private val updates: ArrayList<UpdateOp> = ArrayList()

    /**
     * @return True if updates should be processed.
     */
    fun onItemRangeChanged(positionStart: Int, itemCount: Int, payload: Any?): Boolean {
        if (itemCount < 1) {
            return false
        }
        updates.add(UpdateOp(UpdateOp.UPDATE, positionStart, itemCount, payload))
        return updates.size == 1
    }

    /**
     * @return True if updates should be processed.
     */
    fun onItemRangeInserted(positionStart: Int, itemCount: Int): Boolean {
        if (itemCount < 1) {
            return false
        }
        updates.add(UpdateOp(UpdateOp.ADD, positionStart, itemCount, null))
        return updates.size == 1
    }

    /**
     * @return True if updates should be processed.
     */
    fun onItemRangeRemoved(positionStart: Int, itemCount: Int): Boolean {
        if (itemCount < 1) {
            return false
        }
        updates.add(UpdateOp(UpdateOp.REMOVE, positionStart, itemCount, null))
        return updates.size == 1
    }

    /**
     * @return True if updates should be processed.
     */
    fun onItemRangeMoved(from: Int, to: Int, itemCount: Int): Boolean {
        if (from == to) {
            return false
        }
        if (itemCount != 1) {
            throw IllegalArgumentException("Moving more than 1 item is not supported yet")
        }
        updates.add(UpdateOp(UpdateOp.MOVE, from, to, null))
        return updates.size == 1
    }

    /**
     * Reorders adapter updates, dispatches updates and offsets positions
     */
    fun dispatch() {
        opReorder.reorderOps(updates)

        if (this.debug) {
            Log.d(TAG, updates.toString())
        }

        dispatchFirstPass()
        dispatchSecondPass()

        updates.clear()
    }

    fun dispatchSecondPass() {
        updates.forEach {
            when (it.cmd) {
                UpdateOp.ADD -> {
                    callbacks.offsetPositionsForAdd(it.positionStart, it.itemCount)
                    callbacks.dispatchUpdate(it)
                }
                UpdateOp.REMOVE -> {
                    callbacks.dispatchUpdate(it)
                    callbacks.offsetPositionsForRemove(it.positionStart, it.itemCount)
                }
                UpdateOp.MOVE -> {
                    callbacks.dispatchUpdate(it)
                    callbacks.offsetPositionsForMove(it.positionStart, it.itemCount)
                }
                UpdateOp.UPDATE -> {
                    callbacks.dispatchUpdate(it)
                }
            }
        }
    }

    /**
     * Dispatching the first pass updates annotation state, rather than making structural changes
     */
    fun dispatchFirstPass() {
        updates.filter { it.cmd == UpdateOp.UPDATE }
                .forEach { op ->
                    callbacks.markAnnotationsUpdated(op.positionStart, op.itemCount)
                }
    }

    interface MapAdapterHelperCallback {
        fun offsetPositionsForRemove(positionStart: Int, itemCount: Int)
        fun dispatchUpdate(update: UpdateOp)
        fun offsetPositionsForAdd(positionStart: Int, itemCount: Int)
        fun offsetPositionsForMove(from: Int, to: Int)
        fun markAnnotationsUpdated(positionStart: Int, itemCount: Int)
    }

    override fun obtainUpdateOp(cmd: Int, startPosition: Int, itemCount: Int, payload: Any?): UpdateOp {
        return UpdateOp(cmd, startPosition, itemCount, payload)
    }

    override fun recycleUpdateOp(op: UpdateOp?) {
    }

    fun hasPendingUpdates(): Boolean {
        return this.updates.isNotEmpty()
    }
}


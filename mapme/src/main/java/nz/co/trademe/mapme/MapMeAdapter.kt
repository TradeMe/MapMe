package nz.co.trademe.mapme

import android.content.Context
import android.support.annotation.RestrictTo
import android.support.annotation.VisibleForTesting
import android.support.v7.util.ListUpdateCallback
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.View
import nz.co.trademe.mapme.annotations.*

const val TAG = "MapMeAdapter"
const val NO_POSITION = -1

abstract class MapMeAdapter<MapType>(var context: Context, var factory: AnnotationFactory<MapType>) : MapAdapterHelper.MapAdapterHelperCallback, ListUpdateCallback {

    var mapView: View? = null
    var map: MapType? = null
    var annotations: ArrayList<MapAnnotation> = ArrayList()
    var annotationClickListener: OnMapAnnotationClickListener? = null
    var infoWindowClickListener: OnInfoWindowClickListener? = null

    internal var debug = false

    internal val observer = MapMeDataObserver()
    private val registeredObservers = arrayListOf<RecyclerView.AdapterDataObserver>()

    internal val mapAdapterHelper: MapAdapterHelper by lazy {
        MapAdapterHelper(this, this.debug)
    }

    abstract fun onCreateAnnotation(factory: AnnotationFactory<@JvmSuppressWildcards MapType>, position: Int, annotationType: Int): MapAnnotation

    abstract fun onBindAnnotation(annotation: MapAnnotation, position: Int, payload: Any?)

    abstract fun getItemCount(): Int

    fun attach(mapView: View, map: MapType) {
        this.map = map
        this.mapView = mapView
        this.factory.setOnMarkerClickListener(map, { marker -> notifyAnnotatedMarkerClicked(marker) })
        this.factory.setOnInfoWindowClickListener(map, { marker -> notifyInfowWindowClicked(marker) })
    }

    //default implementation
    open fun getItemAnnotationType(position: Int): Int {
        return 0
    }

    fun enableDebugLogging() {
        this.debug = true
    }

    open fun setOnAnnotationClickListener(listener: OnMapAnnotationClickListener) {
        this.annotationClickListener = listener
    }

    open fun setOnInfoWindowClickListener(listener: OnInfoWindowClickListener) {
        this.infoWindowClickListener = listener
    }

    override fun onChanged(position: Int, count: Int, payload: Any?) {
        observer.onItemRangeChanged(position, count, payload)
    }

    override fun onMoved(fromPosition: Int, toPosition: Int) {
        observer.onItemRangeMoved(fromPosition, toPosition, 1)
    }

    override fun onInserted(position: Int, count: Int) {
        observer.onItemRangeInserted(position, count)
    }

    override fun onRemoved(position: Int, count: Int) {
        observer.onItemRangeRemoved(position, count)
    }

    fun registerObserver(observer: RecyclerView.AdapterDataObserver) {
        registeredObservers.add(observer)
    }

    fun unregisterObserver(observer: RecyclerView.AdapterDataObserver) {
        registeredObservers.remove(observer)
    }

    /**
     * Internally creates an annotation for the given position.
     */
    private fun createAnnotation(position: Int) {
        val annotationType = getItemAnnotationType(position)
        val annotation = onCreateAnnotation(this.factory, position, annotationType)
        annotation.position = position

        map?.let {
            annotation.addToMap(map!!, context)
            annotations.add(annotation)
            onBindAnnotation(annotation, position, null)

            onAnnotationAdded(annotation)
        }

    }

    open fun onAnnotationAdded(annotation: MapAnnotation) {

    }

    /**
     * Called after all DiffUtil updates have been dispatched.
     *
     * Removes any placeholders and replaces them with real annotations
     */
    private fun applyUpdates() {
        //update old annotations
        this.annotations.forEach {
            if (it.isDirty) {
                updateAnnotation(it.position, null)
                it.isDirty = false
            }
        }

        //create new annotations
        ArrayList(this.annotations).forEach {
            if (it.placeholder) {
                this.annotations.remove(it)
                createAnnotation(it.position)
            }
        }

    }

    /**
     * Removes an annotation at the given position
     */
    private fun removeAnnotation(position: Int) {
        val map = map ?: return
        val annotation = findAnnotationForPosition(position)

        if (annotation != null) {
            annotation.removeFromMap(map, context)
            annotations.remove(annotation)
        } else {
            Log.d(TAG, "annotation not found")
        }
    }

    /**
     * Updates an annotation at the given position
     */
    private fun updateAnnotation(position: Int, payload: Any?) {
        val annotation = findAnnotationForPosition(position)

        annotation?.let {
            onBindAnnotation(annotation, position, payload)
        }
    }

    internal fun findAnnotationForPosition(position: Int): MapAnnotation? {
        return annotations.find { it.position == position }
    }

    fun onItemsInserted(positionStart: Int, itemCount: Int) {
        for (position in positionStart until positionStart + itemCount) {
            this.annotations.add(Placeholder().apply { this.position = position })
        }
    }

    fun onItemsMoved(positionStart: Int, itemCount: Int, payload: Any?) {

    }

    fun onItemsRemoved(positionStart: Int, itemCount: Int) {
        for (position in positionStart until positionStart + itemCount) {
            removeAnnotation(position)
        }
    }

    fun onItemsChanged(positionStart: Int, itemCount: Int, payload: Any?) {
        //don't do anything here. Annotations have already been marked as updated,
        //and will be updated at the end of the 'layout'
    }

    /**
     * Notify any registered observers that the data set has changed.
     *
     * <p>There are two different classes of data change events, item changes and structural
     * changes. Item changes are when a single item has its data updated but no positional
     * changes have occurred. Structural changes are when items are inserted, removed or moved
     * within the data set.</p>
     *
     * <p>This event does not specify what about the data set has changed, forcing
     * any observers to assume that all existing items and structure may no longer be valid.
     * LayoutManagers will be forced to fully rebind and relayout all visible views.</p>
     *
     * <p><code>RecyclerView</code> will attempt to synthesize visible structural change events
     * for adapters that report that they have {@link #hasStableIds() stable IDs} when
     * this method is used. This can help for the purposes of animation and visual
     * object persistence but individual item views will still need to be rebound
     * and relaid out.</p>
     *
     * <p>If you are writing an adapter it will always be more efficient to use the more
     * specific change events if you can. Rely on <code>notifyDataSetChanged()</code>
     * as a last resort.</p>
     *
     * @see #notifyItemChanged(int)
     * @see #notifyItemInserted(int)
     * @see #notifyItemRemoved(int)
     * @see #notifyItemRangeChanged(int, int)
     * @see #notifyItemRangeInserted(int, int)
     * @see #notifyItemRangeRemoved(int, int)
     */
    fun notifyDataSetChanged() {
        val map = map ?: return

        onRemoved(0, annotations.size)

        factory.clear(map)
        annotations.clear()

        onInserted(0, getItemCount())
    }

    /**
     * Notify any registered observers that the item at <code>position</code> has changed.
     * Equivalent to calling <code>notifyItemChanged(position, null);</code>.
     *
     * <p>This is an item change event, not a structural change event. It indicates that any
     * reflection of the data at <code>position</code> is out of date and should be updated.
     * The item at <code>position</code> retains the same identity.</p>
     *
     * @param position Position of the item that has changed
     *
     * @see #notifyItemRangeChanged(int, int)
     */
    fun notifyItemChanged(position: Int) {
        notifyItemChanged(position, null)
    }

    /**
     * Notify any registered observers that the item at <code>position</code> has changed with an
     * optional payload object.
     *
     * <p>This is an item change event, not a structural change event. It indicates that any
     * reflection of the data at <code>position</code> is out of date and should be updated.
     * The item at <code>position</code> retains the same identity.
     * </p>
     *
     * <p>
     * Client can optionally pass a payload for partial change. These payloads will be merged
     * and may be passed to adapter's {@link #onBindViewHolder(ViewHolder, int, List)} if the
     * item is already represented by a ViewHolder and it will be rebound to the same
     * ViewHolder. A notifyItemRangeChanged() with null payload will clear all existing
     * payloads on that item and prevent future payload until
     * {@link #onBindViewHolder(ViewHolder, int, List)} is called. Adapter should not assume
     * that the payload will always be passed to onBindViewHolder(), e.g. when the view is not
     * attached, the payload will be simply dropped.
     *
     * @param position Position of the item that has changed
     * @param payload Optional parameter, use null to identify a "full" update
     *
     * @see #notifyItemRangeChanged(int, int)
     */
    fun notifyItemChanged(position: Int, payload: Any?) {
        observer.onItemRangeChanged(position, 1, payload)
    }

    /**
     * Notify any registered observers that the item reflected at <code>position</code>
     * has been newly inserted. The item previously at <code>position</code> is now at
     * position <code>position + 1</code>.
     *
     * <p>This is a structural change event. Representations of other existing items in the
     * data set are still considered up to date and will not be rebound, though their
     * positions may be altered.</p>
     *
     * @param position Position of the newly inserted item in the data set
     *
     * @see #notifyItemRangeInserted(int, int)
     */
    fun notifyItemInserted(position: Int) {
        observer.onItemRangeInserted(position, 1)
    }

    /**
     * Notify any registered observers that the item previously located at <code>position</code>
     * has been removed from the data set. The items previously located at and after
     * <code>position</code> may now be found at <code>oldPosition - 1</code>.
     *
     * <p>This is a structural change event. Representations of other existing items in the
     * data set are still considered up to date and will not be rebound, though their positions
     * may be altered.</p>
     *
     * @param position Position of the item that has now been removed
     *
     * @see #notifyItemRangeRemoved(int, int)
     */
    fun notifyItemRemoved(position: Int) {
        observer.onItemRangeRemoved(position, 1)
    }

    /**
     * Notify any registered observers that the currently reflected <code>itemCount</code>
     * items starting at <code>positionStart</code> have been newly inserted. The items
     * previously located at <code>positionStart</code> and beyond can now be found starting
     * at positionStart <code>positionStart + itemCount</code>.
     *
     * <p>This is a structural change event. Representations of other existing items in the
     * data set are still considered up to date and will not be rebound, though their positions
     * may be altered.</p>
     *
     * @param positionStart Position of the first item that was inserted
     * @param itemCount Number of items inserted
     *
     * @see #notifyItemInserted(int)
     */
    fun notifyItemRangeInserted(positionStart: Int, itemCount: Int) {
        for (position in positionStart until positionStart + itemCount) {
            notifyItemInserted(position)
        }
    }

    /**
     * Notify any registered observers that the item previously located at <code>positionStart</code>
     * has been removed from the data set. The items previously located at and after
     * <code>positionStart</code> may now be found at <code>oldPosition - 1</code>.
     *
     * <p>This is a structural change event. Representations of other existing items in the
     * data set are still considered up to date and will not be rebound, though their positions
     * may be altered.</p>
     *
     * @param positionStart Position of the item that has now been removed
     *
     * @see #notifyItemRangeRemoved(int, int)
     */
    fun notifyItemRangeRemoved(positionStart: Int, itemCount: Int) {
        for (position in positionStart + itemCount downTo positionStart) {
            notifyItemRemoved(position)
        }
    }


    /**
     * Notify any registered observers that the item at <code>positionStart</code> has changed.
     * Equivalent to calling <code>notifyItemChanged(positionStart, null);</code>.
     *
     * <p>This is an item change event, not a structural change event. It indicates that any
     * reflection of the data at <code>positionStart</code> is out of date and should be updated.
     * The item at <code>positionStart</code> retains the same identity.</p>
     *
     * @param positionStart Position of the item that has changed
     *
     * @see #notifyItemRangeChanged(int, int)
     */
    fun notifyItemRangeChanged(positionStart: Int, itemCount: Int) {
        notifyItemRangeChanged(positionStart, itemCount, null)
    }

    /**
     * Notify any registered observers that the item at <code>positionStart</code> has changed with an
     * optional payload object.
     *
     * <p>This is an item change event, not a structural change event. It indicates that any
     * reflection of the data at <code>positionStart</code> is out of date and should be updated.
     * The item at <code>positionStart</code> retains the same identity.
     * </p>
     *
     * <p>
     * Client can optionally pass a payload for partial change. These payloads will be merged
     * and may be passed to adapter's {@link #onBindViewHolder(ViewHolder, int, List)} if the
     * item is already represented by a ViewHolder and it will be rebound to the same
     * ViewHolder. A notifyItemRangeChanged() with null payload will clear all existing
     * payloads on that item and prevent future payload until
     * {@link #onBindViewHolder(ViewHolder, int, List)} is called. Adapter should not assume
     * that the payload will always be passed to onBindViewHolder(), e.g. when the view is not
     * attached, the payload will be simply dropped.
     *
     * @param positionStart Position of the item that has changed
     * @param payload Optional parameter, use null to identify a "full" update
     *
     * @see #notifyItemRangeChanged(int, int)
     */
    fun notifyItemRangeChanged(positionStart: Int, itemCount: Int, payload: Any?) {
        for (position in positionStart until positionStart + itemCount) {
            notifyItemChanged(position, payload)
        }
    }

    /**
     * Notify any registered observers that the item reflected at `fromPosition`
     * has been moved to `toPosition`.

     *
     * This is a structural change event. Representations of other existing items in the
     * data set are still considered up to date and will not be rebound, though their
     * positions may be altered.

     * @param fromPosition Previous position of the item.
     * *
     * @param toPosition New position of the item.
     */
    fun notifyItemMoved(fromPosition: Int, toPosition: Int) {
        observer.onItemRangeMoved(fromPosition, toPosition, 1)
        mapAdapterHelper.dispatch()
    }

    @RestrictTo(RestrictTo.Scope.LIBRARY_GROUP)
    fun notifyAnnotatedMarkerClicked(marker: Any): Boolean {
        val clickListener = annotationClickListener ?: return false

        val annotation = annotations.find { it.annotatesObject(marker) }

        if (annotation != null) {
            return clickListener.onMapAnnotationClick(annotation)
        } else {
            Log.e("MapMeAdapter", "Unable to find an annotation that annotates the marker")
        }
        return false
    }

    @RestrictTo(RestrictTo.Scope.LIBRARY_GROUP)
    fun notifyInfowWindowClicked(marker: Any): Boolean {
        val clickListener = infoWindowClickListener ?: return false

        val annotation = annotations.find { it.annotatesObject(marker) }

        if (annotation != null) {
            return clickListener.onInfoWindowClick(annotation)
        } else {
            Log.e("MapMeAdapter", "Unable to find an annotation that annotates the marker")
        }
        return false
    }

    /**
     * Marks an annotation as requiring an update (isDirty). The annotations will be updated
     * at the end of the 'layout pass'
     */
    override fun markAnnotationsUpdated(positionStart: Int, itemCount: Int) {
        annotations.forEach { annotation ->
            val position = annotation.position
            if (position >= positionStart && position < positionStart + itemCount) {
                annotation.isDirty = true
            }
        }
    }

    override fun offsetPositionsForAdd(positionStart: Int, itemCount: Int) {
        annotations.forEach { annotation ->
            val position = annotation.position

            if (position >= positionStart) {
                offsetPosition(annotation, itemCount)
            }
        }
    }

    override fun offsetPositionsForRemove(positionStart: Int, itemCount: Int) {
        val positionEnd = positionStart + itemCount

        annotations.forEach { annotation ->
            if (annotation.position >= positionEnd) {
                offsetPosition(annotation, -itemCount)
            } else if (annotation.position >= positionStart) {
                offsetPosition(annotation, -itemCount)
            }
        }
    }

    private fun offsetPosition(annotation: MapAnnotation, offset: Int) {
        annotation.position += offset
    }

    override fun offsetPositionsForMove(from: Int, to: Int) {
        val start: Int
        val end: Int
        val inBetweenOffset: Int
        if (from < to) {
            start = from
            end = to
            inBetweenOffset = -1
        } else {
            start = to
            end = from
            inBetweenOffset = 1
        }

        annotations
                .filterNot { annotation ->
                    annotation.position < start || annotation.position > end
                }
                .forEach { annotation ->
                    val position = annotation.position
                    if (position == from) {
                        offsetPosition(annotation, to - from)
                    } else {
                        offsetPosition(annotation, inBetweenOffset)
                    }
                }
    }

    internal val updateChildViewsRunnable: Runnable = Runnable {
        consumePendingUpdateOperations()
    }

    internal fun consumePendingUpdateOperations() {
        if (!mapAdapterHelper.hasPendingUpdates()) {
            return
        }

        mapAdapterHelper.dispatch()

        //after all updates have been dispatched, fill in any placeholder annotations
        //with new annotations
        applyUpdates()
    }

    @VisibleForTesting
    open internal fun triggerUpdateProcessor(runnable: Runnable) {
        mapView?.post(runnable)
    }

    inner class MapMeDataObserver : RecyclerView.AdapterDataObserver() {

        override fun onItemRangeChanged(positionStart: Int, itemCount: Int, payload: Any?) {
            if (mapAdapterHelper.onItemRangeChanged(positionStart, itemCount, payload)) {
                triggerUpdateProcessor(updateChildViewsRunnable)
            }

            registeredObservers.forEach { it.onItemRangeChanged(positionStart, itemCount, payload) }
        }

        override fun onItemRangeInserted(positionStart: Int, itemCount: Int) {
            if (mapAdapterHelper.onItemRangeInserted(positionStart, itemCount)) {
                triggerUpdateProcessor(updateChildViewsRunnable)
            }

            registeredObservers.forEach { it.onItemRangeInserted(positionStart, itemCount) }
        }

        override fun onItemRangeRemoved(positionStart: Int, itemCount: Int) {
            if (mapAdapterHelper.onItemRangeRemoved(positionStart, itemCount)) {
                triggerUpdateProcessor(updateChildViewsRunnable)
            }

            registeredObservers.forEach { it.onItemRangeRemoved(positionStart, itemCount) }
        }

        override fun onItemRangeMoved(fromPosition: Int, toPosition: Int, itemCount: Int) {
            if (mapAdapterHelper.onItemRangeMoved(fromPosition, toPosition, itemCount)) {
                triggerUpdateProcessor(updateChildViewsRunnable)
            }

            registeredObservers.forEach { it.onItemRangeMoved(fromPosition, toPosition, itemCount) }
        }
    }

    override fun dispatchUpdate(update: UpdateOp) {
        when (update.cmd) {
            UpdateOp.ADD -> onItemsInserted(update.positionStart, update.itemCount)
            UpdateOp.REMOVE -> onItemsRemoved(update.positionStart, update.itemCount)
            UpdateOp.UPDATE -> onItemsChanged(update.positionStart, update.itemCount, update.payload)
            UpdateOp.MOVE -> onItemsMoved(update.positionStart, update.itemCount, update.payload)
        }
    }
}

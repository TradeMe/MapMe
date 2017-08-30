package nz.co.trademe.mapme.util

import java.util.*

fun addItems(items: ArrayList<TestItem>, count: Int): ArrayList<TestItem> {
    val added = ArrayList(items)

    for (i in 0 until count) {
        added.add(TestItem(startPosition = items.size + i))
    }

    return added
}

fun removeItems(items: ArrayList<TestItem>, count: Int): ArrayList<TestItem> {
    val removed = ArrayList(items)

    for (i in (items.size - 1) downTo items.size - count) {
        removed.removeAt(i)
    }
    return removed
}

fun changeItem(items: ArrayList<TestItem>, position: Int): ArrayList<TestItem> {
    val changed = ArrayList(items)

    changed[position] = changed[position].copy(zIndex = 1f)

    return changed
}

fun moveItem(items: ArrayList<TestItem>, from: Int, to: Int): ArrayList<TestItem> {
    return ArrayList(items).apply {
        add(to, this.removeAt(from).copy())
    }
}

fun moveAndChangeItem(items: ArrayList<TestItem>, from: Int, to: Int): ArrayList<TestItem> {
    return ArrayList(items).apply {
        add(to, this.removeAt(from).copy(zIndex = 1f))
    }
}

fun shuffleItems(items: ArrayList<TestItem>): ArrayList<TestItem> {
    return ArrayList(items).apply {
        Collections.shuffle(this)
    }
}
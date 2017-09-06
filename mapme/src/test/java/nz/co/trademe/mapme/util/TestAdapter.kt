package nz.co.trademe.mapme.util

import android.annotation.SuppressLint
import android.content.Context
import nz.co.trademe.mapme.MapMeAdapter
import nz.co.trademe.mapme.annotations.MapAnnotation
import nz.co.trademe.mapme.annotations.AnnotationFactory
import nz.co.trademe.mapme.annotations.MarkerAnnotation
import java.util.*
import java.util.concurrent.Phaser

class TestMap

open class TestAdapter(val items: List<TestItem>, myContext: Context, myFactory: AnnotationFactory<TestMap>) : MapMeAdapter<TestMap>(myContext, myFactory) {

    var createCount: Int = 0
    var bindCount: Int = 0

    @SuppressLint("NewApi")
    val phaser = Phaser().apply { register() }

    override fun onCreateAnnotation(factory: AnnotationFactory<TestMap>, position: Int, viewType: Int): MapAnnotation {
        createCount++
        return TestAnnotation()
    }

    override fun onBindAnnotation(annotation: MapAnnotation, position: Int, payload: Any?) {
        val testData = items[position]
        (annotation as MarkerAnnotation).zIndex = testData.zIndex

        bindCount++
    }

    override fun getItemCount(): Int {
        return items.size
    }

    @SuppressLint("NewApi")
    override fun triggerUpdateProcessor(runnable: Runnable) {
        phaser.register()
        val task = object : TimerTask() {
            override fun run() {
                runnable.run()
                phaser.arriveAndDeregister()
            }
        }
        Timer().schedule(task, 10)
    }

    fun resetCounts() {
        createCount = 0
        bindCount = 0
    }
}


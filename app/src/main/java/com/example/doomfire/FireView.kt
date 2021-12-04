package com.example.doomfire

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Paint
import android.util.AttributeSet
import android.view.View
import java.util.*
import androidx.annotation.ColorInt
import kotlin.math.max
import kotlin.math.min


class FireView(context: Context, attrs: AttributeSet) : View(context, attrs) {
    private val firePalette = intArrayOf(
            -16316665,
            -8970489,
            -7395577,
            -6344953,
            -5292281,
            -4241657,
            -3717369,
            -2142457,
            -2140409,
            -2140409,
            -2662649,
            -2662649,
            -2660593,
            -3182833,
            -3180785,
            -3178737,
            -3176681,
            -3700969,
            -3698921,
            -3696865,
            -4219105,
            -4219105,
            -4217049,
            -4217049,
            -4214993,
            -4739281,
            -4737233,
            -4737225,
            -3158161,
            -2105441,
            -1052729,
            -1
    )

    private val paint = Paint()
    private val random = Random()

    private lateinit var firePixels: Array<IntArray>
    private var fireWidth: Int = 0
    private var fireHeight: Int = 0
    private lateinit var bitmap: Bitmap

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)

        val aspectRatio = w / h.toFloat()
        fireWidth = 150
        fireHeight = (fireWidth / aspectRatio).toInt()
        firePixels = Array(fireWidth) { IntArray(fireHeight) }

        for (x in 0 until fireWidth) {
            firePixels[x][fireHeight - 1] = firePalette.lastIndex
        }

        bitmap = Bitmap.createBitmap(fireWidth, fireHeight, Bitmap.Config.RGB_565);
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        spreadFire()
        drawFire(canvas)
        invalidate()
    }

    private fun drawFire(canvas: Canvas?) {
        for (x in 0 until fireWidth) {
            for (y in 0 until fireHeight) {

                var temperature = firePixels[x][y]

                if (temperature < 0) {
                    temperature = 0
                }

                if (temperature >= firePalette.size) {
                    temperature = firePalette.lastIndex
                }

                @ColorInt val color = firePalette[temperature]
                paint.color = color

                bitmap.setPixel(x, y, color)
            }
        }

        val scale = width.toFloat() / fireWidth
        canvas?.scale(scale, scale)
        canvas?.drawBitmap(bitmap, 0.0f, 0.0f, paint)
    }

    private fun spreadFire() {
        for (y in 0 until fireHeight - 1) {
            for (x in 0 until fireWidth) {
                val rand_x: Int = random.nextInt(3)
                val rand_y: Int = random.nextInt(6)
                val dst_x = min(fireWidth - 1, max(0, x + rand_x - 1))
                val dst_y = min(fireHeight - 1, y + rand_y)
                val deltaFire = -(rand_x and 1)
                firePixels[x][y] = max(0, firePixels[dst_x][dst_y] + deltaFire)
            }
        }
    }
}
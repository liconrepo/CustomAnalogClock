package com.licon.customanalogclocks

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.LinearGradient
import android.graphics.Paint
import android.graphics.Path
import android.graphics.RadialGradient
import android.graphics.RectF
import android.graphics.Shader
import android.graphics.SweepGradient
import android.graphics.Typeface
import android.util.AttributeSet
import android.view.View
import java.util.Calendar
import kotlin.math.PI
import kotlin.math.cos
import kotlin.math.sin

/**
 * An analog clock whose whole look is described by a [WatchFace].
 *
 * The view is always square, sized from its width, and redraws every frame so the second hand sweeps.
 */
class CustomAnalogClock @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0,
) : View(context, attrs, defStyleAttr) {

    /** The face to draw; assigning a new one redraws the clock immediately. */
    var watchFace: WatchFace = WatchFaces.all.first()
        set(value) {
            field = value
            rebuild()
            invalidate()
        }

    private val bezelPaint = Paint(Paint.ANTI_ALIAS_FLAG)
    private val flutePaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = Color.argb(140, 40, 25, 5)
        style = Paint.Style.STROKE
    }
    private val metalPaint = Paint(Paint.ANTI_ALIAS_FLAG)
    private val metalOutlinePaint = Paint(Paint.ANTI_ALIAS_FLAG).apply { style = Paint.Style.STROKE }
    private val dialPaint = Paint(Paint.ANTI_ALIAS_FLAG)
    private val gridPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = Color.argb(70, 0, 0, 0)
        style = Paint.Style.STROKE
    }
    private val subDialFillPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply { color = Color.argb(60, 0, 0, 0) }
    private val subDialStrokePaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        style = Paint.Style.STROKE
        strokeCap = Paint.Cap.ROUND
    }
    private val markerPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        textAlign = Paint.Align.CENTER
        typeface = Typeface.create(Typeface.SERIF, Typeface.BOLD)
    }
    private val handPaint = Paint(Paint.ANTI_ALIAS_FLAG)
    private val lumePaint = Paint(Paint.ANTI_ALIAS_FLAG)
    private val trackPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply { strokeCap = Paint.Cap.ROUND }
    private val secondHandPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply { strokeCap = Paint.Cap.ROUND }
    private val dateWindowPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply { color = Color.WHITE }
    private val dateTextPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = Color.BLACK
        textAlign = Paint.Align.CENTER
        typeface = Typeface.create(Typeface.SANS_SERIF, Typeface.BOLD)
    }
    private val brandPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        textAlign = Paint.Align.CENTER
        typeface = Typeface.create(Typeface.SERIF, Typeface.BOLD)
    }
    private val captionPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        textAlign = Paint.Align.CENTER
        typeface = Typeface.create(Typeface.SERIF, Typeface.NORMAL)
    }

    private val hourHand = Path()
    private val minuteHand = Path()
    private val octagon = Path()
    private val dialClip = Path()
    private val box = RectF()

    private var centerX = 0f
    private var centerY = 0f
    private var radius = 0f

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val side = getDefaultSize(suggestedMinimumWidth, widthMeasureSpec)
        setMeasuredDimension(side, side)
    }

    override fun onSizeChanged(width: Int, height: Int, oldWidth: Int, oldHeight: Int) {
        centerX = width / 2f
        centerY = height / 2f
        radius = minOf(width, height) / 2f * 0.96f
        rebuild()
    }

    /** Recomputes every size-dependent paint and shape; does nothing until the view has a size. */
    private fun rebuild() {
        if (radius == 0f) return
        buildPaints()
        buildHands()
        buildShapes()
    }

    private fun buildPaints() {
        val face = watchFace
        val metalColors = intArrayOf(face.metalLight, face.metalDark, face.metalLight, face.metalDark, face.metalLight)
        bezelPaint.shader = SweepGradient(centerX, centerY, metalColors, null)
        val metalGradient = LinearGradient(
            centerX - radius, centerY - radius, centerX + radius, centerY + radius,
            metalColors, null, Shader.TileMode.CLAMP,
        )
        metalPaint.shader = metalGradient
        metalOutlinePaint.shader = metalGradient
        metalOutlinePaint.strokeWidth = radius * 0.012f
        flutePaint.strokeWidth = radius * 0.012f

        dialPaint.shader = RadialGradient(
            centerX, centerY, radius * DIAL_RADIUS,
            intArrayOf(face.dialCenter, face.dialEdge), null, Shader.TileMode.CLAMP,
        )
        gridPaint.strokeWidth = radius * 0.006f

        applyColorOrMetal(markerPaint, face.markerColor, metalGradient)
        applyColorOrMetal(handPaint, face.handColor, metalGradient)
        markerPaint.textSize = radius * 0.15f

        lumePaint.color = face.lumeColor
        trackPaint.color = face.textColor
        subDialStrokePaint.color = face.textColor
        subDialStrokePaint.strokeWidth = radius * 0.008f
        secondHandPaint.color = face.secondHandColor
        secondHandPaint.strokeWidth = radius * 0.012f

        brandPaint.color = face.textColor
        brandPaint.textSize = radius * 0.11f
        brandPaint.letterSpacing = 0.15f
        captionPaint.color = face.textColor
        captionPaint.textSize = radius * 0.04f
        captionPaint.letterSpacing = 0.2f
        dateTextPaint.textSize = radius * 0.085f
    }

    private fun applyColorOrMetal(paint: Paint, color: Int?, metal: Shader) {
        paint.shader = if (color == null) metal else null
        if (color != null) paint.color = color
    }

    /** Builds both hands pointing straight up from the center. */
    private fun buildHands() {
        when (watchFace.handStyle) {
            HandStyle.SWORD -> {
                buildSwordHand(hourHand, length = radius * 0.5f, halfWidth = radius * 0.035f, tail = radius * 0.08f)
                buildSwordHand(minuteHand, length = radius * 0.74f, halfWidth = radius * 0.03f, tail = radius * 0.1f)
            }
            HandStyle.LEAF -> {
                buildLeafHand(hourHand, length = radius * 0.5f, halfWidth = radius * 0.045f, tail = radius * 0.08f)
                buildLeafHand(minuteHand, length = radius * 0.74f, halfWidth = radius * 0.035f, tail = radius * 0.1f)
            }
        }
    }

    private fun buildSwordHand(path: Path, length: Float, halfWidth: Float, tail: Float) {
        path.reset()
        path.moveTo(centerX - halfWidth, centerY + tail)
        path.lineTo(centerX - halfWidth, centerY - length * 0.8f)
        path.lineTo(centerX, centerY - length)
        path.lineTo(centerX + halfWidth, centerY - length * 0.8f)
        path.lineTo(centerX + halfWidth, centerY + tail)
        path.close()
    }

    private fun buildLeafHand(path: Path, length: Float, halfWidth: Float, tail: Float) {
        path.reset()
        path.moveTo(centerX, centerY + tail)
        path.lineTo(centerX - halfWidth * 0.4f, centerY)
        path.lineTo(centerX - halfWidth, centerY - length * 0.35f)
        path.lineTo(centerX, centerY - length)
        path.lineTo(centerX + halfWidth, centerY - length * 0.35f)
        path.lineTo(centerX + halfWidth * 0.4f, centerY)
        path.close()
    }

    private fun buildShapes() {
        octagon.reset()
        for (corner in 0 until 8) {
            val angle = PI / 4 * corner + PI / 8
            val x = centerX + (cos(angle) * radius).toFloat()
            val y = centerY + (sin(angle) * radius).toFloat()
            if (corner == 0) octagon.moveTo(x, y) else octagon.lineTo(x, y)
        }
        octagon.close()
        dialClip.reset()
        dialClip.addCircle(centerX, centerY, radius * DIAL_RADIUS, Path.Direction.CW)
    }

    override fun onDraw(canvas: Canvas) {
        val now = Calendar.getInstance()
        drawBezel(canvas)
        drawDial(canvas)
        drawMinuteTrack(canvas)
        drawHourMarkers(canvas)
        drawLabels(canvas)
        if (watchFace.hasDateWindow) drawDateWindow(canvas, now.get(Calendar.DAY_OF_MONTH))
        if (watchFace.hasSubDials) drawSubDials(canvas, now)
        drawHands(canvas, now)
        postInvalidateOnAnimation()
    }

    /** Draws the bezel in the face's [BezelStyle] and the ring where it meets the dial. */
    private fun drawBezel(canvas: Canvas) {
        when (watchFace.bezelStyle) {
            BezelStyle.FLUTED -> {
                canvas.drawCircle(centerX, centerY, radius, bezelPaint)
                forEachRotatedStep(FLUTE_COUNT, canvas) {
                    canvas.drawLine(centerX, centerY - radius, centerX, centerY - radius * 0.935f, flutePaint)
                }
            }
            BezelStyle.SMOOTH -> canvas.drawCircle(centerX, centerY, radius, bezelPaint)
            BezelStyle.OCTAGON -> {
                canvas.drawPath(octagon, bezelPaint)
                drawBezelScrews(canvas)
            }
        }
        canvas.drawCircle(centerX, centerY, radius * 0.9f, metalOutlinePaint)
    }

    private fun drawBezelScrews(canvas: Canvas) {
        for (screw in 0 until 8) {
            val angle = PI / 4 * screw + PI / 8
            val x = centerX + (cos(angle) * radius * 0.95f).toFloat()
            val y = centerY + (sin(angle) * radius * 0.95f).toFloat()
            canvas.drawCircle(x, y, radius * 0.018f, subDialFillPaint)
            canvas.drawCircle(x, y, radius * 0.012f, metalPaint)
        }
    }

    private fun drawDial(canvas: Canvas) {
        canvas.drawCircle(centerX, centerY, radius * DIAL_RADIUS, dialPaint)
        if (watchFace.dialPattern != DialPattern.GRID) return
        canvas.save()
        canvas.clipPath(dialClip)
        val step = radius * 0.09f
        var offset = -radius
        while (offset <= radius) {
            canvas.drawLine(centerX + offset, centerY - radius, centerX + offset, centerY + radius, gridPaint)
            canvas.drawLine(centerX - radius, centerY + offset, centerX + radius, centerY + offset, gridPaint)
            offset += step
        }
        canvas.restore()
    }

    private fun drawMinuteTrack(canvas: Canvas) {
        forEachRotatedStep(60, canvas, degreesPerStep = 6f) { minute ->
            val isFiveMinuteMark = minute % 5 == 0
            trackPaint.strokeWidth = radius * if (isFiveMinuteMark) 0.014f else 0.007f
            val length = radius * if (isFiveMinuteMark) 0.05f else 0.03f
            val outer = centerY - radius * 0.84f
            canvas.drawLine(centerX, outer, centerX, outer + length, trackPaint)
        }
    }

    /** Draws the twelve hour markers, leaving out positions taken by the date window or sub-dials. */
    private fun drawHourMarkers(canvas: Canvas) {
        val face = watchFace
        for (hour in 0 until 12) {
            val isDateWindowPosition = hour == 3 && face.hasDateWindow
            val isSubDialPosition = face.hasSubDials && hour % 3 == 0 && hour != 0
            if (isDateWindowPosition || isSubDialPosition && face.markerStyle != MarkerStyle.BATON) continue
            when (face.markerStyle) {
                MarkerStyle.BATON -> if (!isDateWindowPosition) drawBatonMarker(canvas, hour)
                MarkerStyle.DOT -> drawDotMarker(canvas, hour)
                MarkerStyle.NUMBERS -> drawNumeral(canvas, hour, (if (hour == 0) 12 else hour).toString())
                MarkerStyle.ROMAN -> drawNumeral(canvas, hour, ROMAN_NUMERALS[hour])
            }
        }
    }

    /** Draws the baton for [hour] (0 to 11, 0 being 12 o'clock). */
    private fun drawBatonMarker(canvas: Canvas, hour: Int) {
        val halfWidth = radius * 0.028f
        val outer = centerY - radius * 0.76f
        canvas.save()
        canvas.rotate(hour * 30f, centerX, centerY)
        if (hour == 0) {
            drawBaton(canvas, centerX - halfWidth * 1.4f, halfWidth, outer)
            drawBaton(canvas, centerX + halfWidth * 1.4f, halfWidth, outer)
        } else {
            drawBaton(canvas, centerX, halfWidth, outer)
        }
        canvas.restore()
    }

    private fun drawBaton(canvas: Canvas, x: Float, halfWidth: Float, top: Float) {
        box.set(x - halfWidth, top, x + halfWidth, top + radius * 0.13f)
        canvas.drawRoundRect(box, radius * 0.01f, radius * 0.01f, markerPaint)
        val inset = radius * 0.012f
        box.inset(inset, inset)
        canvas.drawRoundRect(box, radius * 0.005f, radius * 0.005f, lumePaint)
    }

    private fun drawDotMarker(canvas: Canvas, hour: Int) {
        val isQuarter = hour % 3 == 0
        val dotRadius = radius * if (isQuarter) 0.05f else 0.035f
        val (x, y) = pointOnDial(hour * 30f, radius * 0.7f)
        canvas.drawCircle(x, y, dotRadius, markerPaint)
        canvas.drawCircle(x, y, dotRadius * 0.6f, lumePaint)
    }

    private fun drawNumeral(canvas: Canvas, hour: Int, label: String) {
        val (x, y) = pointOnDial(hour * 30f, radius * 0.68f)
        canvas.drawText(label, x, y - (markerPaint.descent() + markerPaint.ascent()) / 2, markerPaint)
    }

    private fun drawLabels(canvas: Canvas) {
        val face = watchFace
        val brandY = centerY - radius * 0.38f
        canvas.drawText(face.brand, centerX, brandY, brandPaint)
        canvas.drawText(face.captionTop, centerX, centerY - radius * 0.27f, captionPaint)
        // the sub-dials take the space the bottom caption would use
        if (!face.hasSubDials) {
            canvas.drawText(face.captionBottom, centerX, centerY + radius * 0.42f, captionPaint)
        }
    }

    /** Draws the date window at 3 o'clock showing [day]. */
    private fun drawDateWindow(canvas: Canvas, day: Int) {
        val windowWidth = radius * 0.17f
        val windowHeight = radius * 0.13f
        val windowCenterX = centerX + radius * 0.64f
        box.set(
            windowCenterX - windowWidth / 2, centerY - windowHeight / 2,
            windowCenterX + windowWidth / 2, centerY + windowHeight / 2,
        )
        canvas.drawRect(box, metalPaint)
        box.inset(radius * 0.01f, radius * 0.01f)
        canvas.drawRect(box, dateWindowPaint)
        val textBaseline = centerY - (dateTextPaint.descent() + dateTextPaint.ascent()) / 2
        canvas.drawText(day.toString(), windowCenterX, textBaseline, dateTextPaint)
    }

    /** Small seconds at 9, minute counter at 3 and hour counter at 6. */
    private fun drawSubDials(canvas: Canvas, now: Calendar) {
        val seconds = now.get(Calendar.SECOND) + now.get(Calendar.MILLISECOND) / 1000f
        val minutes = now.get(Calendar.MINUTE) + seconds / 60f
        val hours = now.get(Calendar.HOUR) + minutes / 60f
        drawSubDial(canvas, 270f, seconds * 6f)
        drawSubDial(canvas, 90f, minutes * 6f)
        drawSubDial(canvas, 180f, hours * 30f)
    }

    /** Draws one sub-dial at [positionDegrees] around the dial, with its hand pointing at [handDegrees]. */
    private fun drawSubDial(canvas: Canvas, positionDegrees: Float, handDegrees: Float) {
        val (x, y) = pointOnDial(positionDegrees, radius * 0.4f)
        val subRadius = radius * 0.2f
        canvas.drawCircle(x, y, subRadius, subDialFillPaint)
        canvas.drawCircle(x, y, subRadius, subDialStrokePaint)
        for (tick in 0 until 12) {
            val inner = pointAround(x, y, tick * 30f, subRadius * 0.82f)
            val outer = pointAround(x, y, tick * 30f, subRadius * 0.95f)
            canvas.drawLine(inner.first, inner.second, outer.first, outer.second, subDialStrokePaint)
        }
        val tip = pointAround(x, y, handDegrees, subRadius * 0.8f)
        canvas.drawLine(x, y, tip.first, tip.second, secondHandPaint)
        canvas.drawCircle(x, y, radius * 0.015f, secondHandPaint)
    }

    /** Draws the hour, minute and second hands for [now], plus the center cap. */
    private fun drawHands(canvas: Canvas, now: Calendar) {
        val seconds = now.get(Calendar.SECOND) + now.get(Calendar.MILLISECOND) / 1000f
        val minutes = now.get(Calendar.MINUTE) + seconds / 60f
        val hours = now.get(Calendar.HOUR) + minutes / 60f

        drawRotated(canvas, hours * 30f) { drawHand(canvas, hourHand, radius * 0.31f) }
        drawRotated(canvas, minutes * 6f) { drawHand(canvas, minuteHand, radius * 0.48f) }
        drawRotated(canvas, seconds * 6f) { drawSecondHand(canvas) }

        canvas.drawCircle(centerX, centerY, radius * 0.035f, metalPaint)
        canvas.drawCircle(centerX, centerY, radius * 0.015f, dialPaint)
    }

    /** Runs [draw] with the canvas rotated by [degrees] around the center. */
    private inline fun drawRotated(canvas: Canvas, degrees: Float, draw: () -> Unit) {
        canvas.save()
        canvas.rotate(degrees, centerX, centerY)
        draw()
        canvas.restore()
    }

    /** Fills [hand] and, for sword hands, adds the luminous dot [lumeOffset] up from the center. */
    private fun drawHand(canvas: Canvas, hand: Path, lumeOffset: Float) {
        canvas.drawPath(hand, handPaint)
        if (watchFace.handStyle == HandStyle.SWORD) {
            canvas.drawCircle(centerX, centerY - lumeOffset, radius * 0.03f, lumePaint)
        }
    }

    private fun drawSecondHand(canvas: Canvas) {
        canvas.drawLine(centerX, centerY + radius * 0.2f, centerX, centerY - radius * 0.82f, secondHandPaint)
        canvas.drawCircle(centerX, centerY - radius * 0.62f, radius * 0.02f, secondHandPaint)
    }

    /** Runs [draw] once per step, rotating the canvas around the center between steps. */
    private inline fun forEachRotatedStep(count: Int, canvas: Canvas, degreesPerStep: Float = 360f / count, draw: (Int) -> Unit) {
        canvas.save()
        for (step in 0 until count) {
            draw(step)
            canvas.rotate(degreesPerStep, centerX, centerY)
        }
        canvas.restore()
    }

    /** Point at [distance] from the center in the clockwise direction [degrees] from 12 o'clock. */
    private fun pointOnDial(degrees: Float, distance: Float): Pair<Float, Float> =
        pointAround(centerX, centerY, degrees, distance)

    /** Point at [distance] from ([originX], [originY]) in the clockwise direction [degrees] from 12 o'clock. */
    private fun pointAround(originX: Float, originY: Float, degrees: Float, distance: Float): Pair<Float, Float> {
        val angle = Math.toRadians(degrees.toDouble())
        return Pair(originX + (sin(angle) * distance).toFloat(), originY - (cos(angle) * distance).toFloat())
    }

    private companion object {
        const val FLUTE_COUNT = 120
        const val DIAL_RADIUS = 0.88f
        val ROMAN_NUMERALS = listOf("XII", "I", "II", "III", "IIII", "V", "VI", "VII", "VIII", "IX", "X", "XI")
    }
}

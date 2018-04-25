package io.humanteq.graphcharttest

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.DashPathEffect
import android.graphics.Paint
import android.support.v4.content.ContextCompat
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.ScaleGestureDetector
import android.view.View
import com.google.gson.Gson
import io.humanteq.graphcharttest.models.CommunityModel2
import io.humanteq.graphcharttest.models.SocChartModel
import java.util.*
import kotlin.math.cos
import kotlin.math.sin


class SocChartView : View {
    private var ignoreEvents = false

    constructor(context: Context) : this(context, null)

    constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, 0)

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        initView()
    }

    private fun initView() {
        paintBgWhite.color = ContextCompat.getColor(context, R.color.dark)

        paint.color = ContextCompat.getColor(context, R.color.colorAccent)
        paint.strokeWidth = resources.getDimension(R.dimen.soc_chart_stroke_width)
        paint.style = Paint.Style.STROKE
        paint.strokeCap = Paint.Cap.ROUND

        paintText.textSize = resources.getDimension(R.dimen.soc_chart_text_size)
        paintText.style = Paint.Style.STROKE
        paintText.strokeCap = Paint.Cap.ROUND
        paintText.color = ContextCompat.getColor(context, R.color.very_white)

        paintTextLine.strokeWidth = resources.getDimension(R.dimen.soc_chart_cluster_name_stroke_width)
        paintTextLine.style = Paint.Style.STROKE
        paintTextLine.strokeCap = Paint.Cap.ROUND
        paintTextLine.color = ContextCompat.getColor(context, R.color.very_white)

        paintLine.color = ContextCompat.getColor(context, R.color.white)
        paintLine.strokeWidth = resources.getDimension(R.dimen.soc_chart_line_stroke_width)
        paintLine.style = Paint.Style.STROKE
        paintLine.strokeCap = Paint.Cap.ROUND

        paintLineDotted.color = ContextCompat.getColor(context, R.color.white)
        paintLineDotted.strokeWidth = resources.getDimension(R.dimen.soc_chart_line_stroke_width)
        paintLineDotted.style = Paint.Style.STROKE
        paintLine.strokeCap = Paint.Cap.ROUND
        paintLineDotted.pathEffect = DashPathEffect(floatArrayOf(context.fsp(3f), context.fsp(2f)), 0f)

        paintTextBg.color = ContextCompat.getColor(context, R.color.very_white)
        paintTextBg.textSize = paintText.textSize + 1
        paintTextBg.strokeWidth = resources.getDimension(R.dimen.soc_chart_text_stroke_width)
        paintTextBg.style = Paint.Style.STROKE
        paintTextBg.strokeCap = Paint.Cap.ROUND

        mScaleDetector = ScaleGestureDetector(context, ScaleListener())
    }

    val json2 = "{" +
            "  \"friends\": 153," +
            "  \"communities\": [" +
            "    {" +
            "      \"name\": \"imya\"," +
            "      \"size\": 14" +
            "    }," +
            "    {" +
            "      \"name\": \"imya2\"," +
            "      \"size\": 18" +
            "    }," +
            "    {" +
            "      \"name\": \"imya2\"," +
            "      \"size\": 5" +
            "    }," +
            "    {" +
            "      \"name\": \"imya2\"," +
            "      \"size\": 9" +
            "    }," +
            "    {" +
            "      \"name\": \"imya2\"," +
            "      \"size\": 14" +
            "    }," +
            "    {" +
            "      \"name\": \"imya2\"," +
            "      \"size\": 20" +
            "    }," +
            "    {" +
            "      \"name\": \"imya2\"," +
            "      \"size\": 18" +
            "    }," +
            "    {" +
            "      \"name\": \"imya2\"," +
            "      \"size\": 5" +
            "    }," +
            "    {" +
            "      \"name\": \"imya2\"," +
            "      \"size\": 9" +
            "    }," +
            "    {" +
            "      \"name\": \"imya2\"," +
            "      \"size\": 14" +
            "    }," +
            "    {" +
            "      \"name\": \"imya2\"," +
            "      \"size\": 20" +
            "    }," +
            "    {" +
            "      \"name\": \"imya2\"," +
            "      \"size\": 18" +
            "    }," +
            "    {" +
            "      \"name\": \"imya2\"," +
            "      \"size\": 5" +
            "    }," +
            "    {" +
            "      \"name\": \"imya2\"," +
            "      \"size\": 9" +
            "    }," +
            "    {" +
            "      \"name\": \"imya2\"," +
            "      \"size\": 14" +
            "    }," +
            "    {" +
            "      \"name\": \"imya2\"," +
            "      \"size\": 20" +
            "    }," +
            "  ]" +
            "}"

    private var mScaleFactor = 1f
    private var transX = 1f
    private var transY = 1f
    private var startX = 1f
    private var startY = 1f
    private var mScaleDetector: ScaleGestureDetector? = null
    private var chartWidth = 0
    private var chartHeight = 0
    private var screenCenterX = measuredWidth / 2f
    private var screenCenterY = measuredHeight / 2f
    private var scalePivotX = screenCenterX
    private var scalePivotY = screenCenterY
    private val initialRadius = resources.getDimension(R.dimen.initial_radius)
    private val clusterNameRadiusOffset = resources.getDimension(R.dimen.soc_chart_cluster_name_radius_offset)
    private val gson = Gson()
    private val rand = Random()
    private val paint = Paint(Paint.ANTI_ALIAS_FLAG)
    private val paintText = Paint(Paint.ANTI_ALIAS_FLAG)
    private val paintTextBg = Paint(Paint.ANTI_ALIAS_FLAG)
    private val paintLine = Paint(Paint.ANTI_ALIAS_FLAG)
    private val paintLineDotted = Paint(Paint.ANTI_ALIAS_FLAG)
    private val paintTextLine = Paint(Paint.ANTI_ALIAS_FLAG)
    private val paintBgWhite = Paint(Paint.ANTI_ALIAS_FLAG)

    private val pointList = arrayListOf<SocPoint>()
    private val lineList = arrayListOf<SocLine>()
    private val textList = arrayListOf<SocText>()

    data class SocPoint(val x: Float, val y: Float, val color: Int)
    data class SocLine(val x1: Float, val y1: Float, val x2: Float, val y2: Float, val paint: Paint)
    data class SocText(val text: String, val textX: Float, val textY: Float,
                       val x1: Float, val y1: Float, val x2: Float, val y2: Float,
                       val paintText: Paint,
                       val paintLine: Paint)

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        if (chartHeight == 0 || chartWidth == 0)
            return
        val model = gson.fromJson<SocChartModel>(json2, SocChartModel::class.java) ?: return

        canvas?.save()
        canvas?.scale(mScaleFactor, mScaleFactor, screenCenterX, screenCenterY)
//        canvas?.scale(mScaleFactor, mScaleFactor, scalePivotX, scalePivotY)
        canvas?.translate(transX, transY)

        canvas?.drawPaint(paintBgWhite)

        if (pointList.isEmpty())
            buildMainPointAndFiends(model, canvas)
        else
            drawMainPointAndFiends(canvas)

        canvas?.restore()
    }

    private fun drawMainPointAndFiends(canvas: Canvas?) {
        lineList.forEach {
            canvas?.drawLine(it.x1, it.y1, it.x2, it.y2, it.paint)
        }
        pointList.forEach {
            paint.color = it.color
            canvas?.drawPoint(it.x, it.y, paint)
        }
        textList.forEach {
            canvas?.drawLine(it.x1, it.y1, it.x2, it.y2, it.paintLine)
            canvas?.drawText(it.text, it.textX, it.textY, it.paintText)
        }
    }

    private fun buildMainPointAndFiends(model: SocChartModel, canvas: Canvas?) {
        // Draw central point and friends around it
        paint.color = ContextCompat.getColor(context, R.color.colorAccent)
        pointList.add(SocPoint(screenCenterX, screenCenterY, paint.color))

        val angleStep = (190 + rand.nextInt(170)) / model.friends
        val radius = initialRadius + rand.nextInt(60)
        var prevX = screenCenterX
        var prevY = screenCenterY
        val randomRadianOffset = rand.nextInt(10)
        for (angle in 0..model.friends) {
            val radian = (angle * angleStep) * 0.0174532925
            val randomOffsetX = rand.nextInt(30)
            val randomOffsetY = rand.nextInt(30)
            val x = radius * cos(radian + randomRadianOffset) + if (randomOffsetX % 2 == 0) randomOffsetX else randomOffsetX * -1
            val y = radius * sin(radian + randomRadianOffset) + if (randomOffsetY % 2 == 0) randomOffsetY else randomOffsetY * -1

            val xC = (screenCenterX + x).toFloat()
            val yC = (screenCenterY + y).toFloat()
            pointList.add(SocPoint(xC, yC, paint.color))
            lineList.add(SocLine(prevX, prevY, xC, yC, paintLine))
            prevX = xC
            prevY = yC
        }

        buildClusterCenterPoints(model.communities, canvas)
    }

    private fun buildClusterCenterPoints(clusters: List<CommunityModel2>, canvas: Canvas?) {
        // Draw clusters around center point
        val randomRadianOffset = rand.nextInt(10)
        var startAngle = 190
        if (clusters.size > 7)
            startAngle = 360
        val randomAngleOffset = 360 - startAngle
        val angleStep = (startAngle + if (randomAngleOffset == 0) 0 else rand.nextInt(randomAngleOffset)) / clusters.size
        for (angle in 0 until clusters.size) {
            if (clusters[angle] == null)
                continue

            val radius = initialRadius + 150 + rand.nextInt(170)
            val color = Color.argb(200 + rand.nextInt(55),
                    50 + rand.nextInt(205),
                    50 + rand.nextInt(205),
                    50 + rand.nextInt(205))
            paint.color = color
            // Calculate center point of community
            val radian = (angle * angleStep) * 0.0174532925
            val randomOffsetX = rand.nextInt(30)
            val randomOffsetY = rand.nextInt(30)
            val x = radius * cos(radian + randomRadianOffset) + if (randomOffsetX % 2 == 0) randomOffsetX else randomOffsetX * -1
            val y = radius * sin(radian + randomRadianOffset) + if (randomOffsetY % 2 == 0) randomOffsetY else randomOffsetY * -1

            // Draw central point of cluster
            val xC = (screenCenterX + x).toFloat()
            val yC = (screenCenterY + y).toFloat()
            pointList.add(SocPoint(xC, yC, paint.color))
            lineList.add(SocLine(screenCenterX, screenCenterY, xC, yC, paintLine))

            //Draw name of cluster
            val text = clusters[angle].name
            val textWidth = paintText.measureText(text)
            val textRadius = clusterNameRadiusOffset
            val textX: Float = (xC + textRadius * cos(radian + randomRadianOffset + .2f)).toFloat()
            val textY: Float = (yC + textRadius * sin(radian + randomRadianOffset + .3f)).toFloat()
            var fixedTextX = textX - textWidth / 2f
            if (fixedTextX < 0)
                fixedTextX = 0f
            else if (fixedTextX > chartWidth)
                fixedTextX = chartWidth - textWidth

            textList.add(SocText(text, fixedTextX, textY, textX, textY, xC, yC, paintText, paintTextLine))

            buildCluster(xC, yC, clusters[angle], color)
        }

        drawMainPointAndFiends(canvas)
    }

    private fun buildCluster(parentCentralX: Float, parentCentralY: Float, cluster: CommunityModel2, color: Int) {
        if (cluster.size == 0)
            return

        // Draw clusters around center point
        val angleStep = (100 + rand.nextInt(260)) / cluster.size
        var prevX = parentCentralX
        var prevY = parentCentralY
        val randomRadianOffset = rand.nextInt(5)
        for (angle in 0 until cluster.size) {
            val radius = initialRadius + rand.nextInt(60)
            paint.color = color

            // Calculate center point of community
            val radian = (angle * angleStep) * 0.0174532925
            val randomOffsetX = rand.nextInt(10 + angle)
            val randomOffsetY = rand.nextInt(10 + angle)
            val x = radius * cos(radian + randomRadianOffset) + if (randomOffsetX % 3 == 0) randomOffsetX else randomOffsetX * -1
            val y = radius * sin(radian + randomRadianOffset) + if (randomOffsetY % 3 == 0) randomOffsetY else randomOffsetY * -1

            // Draw central point of cluster
            val xC = (parentCentralX + x).toFloat()
            val yC = (parentCentralY + y).toFloat()
            val drawLineRandom = rand.nextInt(100)
            if (drawLineRandom % 7 == 0)
                lineList.add(SocLine(parentCentralX, parentCentralY, xC, yC, paintLineDotted))
            pointList.add(SocPoint(xC, yC, paint.color))
            lineList.add(SocLine(prevX, prevY, xC, yC, paintLineDotted))
            prevX = xC
            prevY = yC
        }
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        chartWidth = measuredWidth
        chartHeight = measuredHeight

        screenCenterX = measuredWidth / 2f
        screenCenterY = measuredHeight / 2f
    }

    private inner class ScaleListener : ScaleGestureDetector.SimpleOnScaleGestureListener() {
        override fun onScale(detector: ScaleGestureDetector): Boolean {
            mScaleFactor *= detector.scaleFactor

            scalePivotX = detector.focusX
            scalePivotY = detector.focusY

            // Don't let the object get too small or too large.
            mScaleFactor = Math.max(1f, Math.min(mScaleFactor, 4.0f))

            invalidate()
            return true
        }
    }

    override fun onTouchEvent(ev: MotionEvent): Boolean {
        mScaleDetector?.onTouchEvent(ev)

        if (ev.pointerCount == 1) {
            when {
                ev.action == MotionEvent.ACTION_DOWN -> {
                    startX = ev.rawX
                    startY = ev.rawY
                }
                ev.action == MotionEvent.ACTION_MOVE -> {
                    if (ignoreEvents)
                        return true

                    transX += (ev.rawX - startX) / mScaleFactor
                    transY += (ev.rawY - startY) / mScaleFactor

                    startX = ev.rawX
                    startY = ev.rawY
                    invalidate()
                }
                ev.action == MotionEvent.ACTION_UP -> {
                    ignoreEvents = false
                }
            }
        } else {
            ignoreEvents = true
        }

        return true
    }
}

fun Context.sp(value: Int): Int = (value * resources.displayMetrics.scaledDensity).toInt()
fun Context.sp(value: Float): Int = (value * resources.displayMetrics.scaledDensity).toInt()
fun Context.fsp(value: Int): Float = sp(value).toFloat()
fun Context.fsp(value: Float): Float = sp(value).toFloat()
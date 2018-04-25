package io.humanteq.graphcharttest

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.support.v4.content.ContextCompat
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.ScaleGestureDetector
import android.view.View
import com.google.gson.Gson
import io.humanteq.graphcharttest.models.CommunityModel
import io.humanteq.graphcharttest.models.SocChartModel
import java.util.*
import kotlin.math.cos
import kotlin.math.sin


class SocChartView : View {
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

        paintTextBg.color = ContextCompat.getColor(context, R.color.very_white)
        paintTextBg.textSize = paintText.textSize + 1
        paintTextBg.strokeWidth = resources.getDimension(R.dimen.soc_chart_text_stroke_width)
        paintTextBg.style = Paint.Style.STROKE
        paintTextBg.strokeCap = Paint.Cap.ROUND

        mScaleDetector = ScaleGestureDetector(context, ScaleListener())
    }

    val json = "{" +
            "  \"friends\": 13," +
            "  \"communities\": [" +
            "    {" +
            "      \"name\": \"друзья\"," +
            "      \"data\": [" +
            "        762881," +
            "        63579140," +
            "        22512645," +
            "        444772," +
            "        5253131," +
            "        23634971," +
            "        6872604," +
            "        2013189," +
            "        2587680," +
            "        19613731," +
            "        511020" +
            "      ]" +
            "    }," +
            "    {" +
            "      \"name\": \"друзья2\"," +
            "      \"data\": [" +
            "        762881," +
            "        63579140," +
            "        22512645," +
            "        444772," +
            "        5253131," +
            "        23634971," +
            "        6872604," +
            "        2013189," +
            "        2587680," +
            "        19613731," +
            "        511020" +
            "      ]" +
            "    }," +
            "    {" +
            "      \"name\": \"друзья3\"," +
            "      \"data\": [" +
            "        762881," +
            "        63579140," +
            "        22512645," +
            "        444772," +
            "        5253131," +
            "        23634971," +
            "        6872604," +
            "        2013189," +
            "        2587680," +
            "        19613731," +
            "        511020" +
            "      ]" +
            "    }," +
            "    {" +
            "      \"name\": \"друзья4\"," +
            "      \"data\": [" +
            "        762881," +
            "        63579140," +
            "        22512645," +
            "        444772," +
            "        5253131," +
            "        23634971," +
            "        6872604," +
            "        2013189," +
            "        2587680," +
            "        19613731," +
            "        511020" +
            "      ]" +
            "    }," +
            "    {" +
            "      \"name\": \"друзья5\"," +
            "      \"data\": [" +
            "        762881," +
            "        63579140," +
            "        22512645," +
            "        444772," +
            "        5253131," +
            "        23634971," +
            "        6872604," +
            "        2013189," +
            "        2587680," +
            "        19613731," +
            "        511020" +
            "      ]" +
            "    }," +
            "    {" +
            "      \"name\": \"друзья5\"," +
            "      \"data\": [" +
            "        762881," +
            "        63579140," +
            "        22512645," +
            "        444772," +
            "        5253131," +
            "        23634971," +
            "        6872604," +
            "        2013189," +
            "        2587680," +
            "        19613731," +
            "        511020" +
            "      ]" +
            "    }," +
            "    {" +
            "      \"name\": \"друзья5\"," +
            "      \"data\": [" +
            "        762881," +
            "        63579140," +
            "        22512645," +
            "        444772," +
            "        5253131," +
            "        23634971," +
            "        6872604," +
            "        2013189," +
            "        2587680," +
            "        19613731," +
            "        511020" +
            "      ]" +
            "    }," +
            "    {" +
            "      \"name\": \"друзья5\"," +
            "      \"data\": [" +
            "        762881," +
            "        63579140," +
            "        22512645," +
            "        444772," +
            "        5253131," +
            "        23634971," +
            "        6872604," +
            "        2013189," +
            "        2587680," +
            "        19613731," +
            "        511020" +
            "      ]" +
            "    }," +
            "    {" +
            "      \"name\": \"друзья5\"," +
            "      \"data\": [" +
            "        762881," +
            "        63579140," +
            "        22512645," +
            "        444772," +
            "        5253131," +
            "        23634971," +
            "        6872604," +
            "        2013189," +
            "        2587680," +
            "        19613731," +
            "        511020" +
            "      ]" +
            "    }," +
            "    {" +
            "      \"name\": \"друзья5\"," +
            "      \"data\": [" +
            "        762881," +
            "        63579140," +
            "        22512645," +
            "        444772," +
            "        5253131," +
            "        23634971," +
            "        6872604," +
            "        2013189," +
            "        2587680," +
            "        19613731," +
            "        511020" +
            "      ]" +
            "    }," +
            "    {" +
            "      \"name\": \"друзья5\"," +
            "      \"data\": [" +
            "        762881," +
            "        63579140," +
            "        22512645," +
            "        444772," +
            "        5253131," +
            "        23634971," +
            "        6872604," +
            "        2013189," +
            "        2587680," +
            "        19613731," +
            "        511020" +
            "      ]" +
            "    }," +
            "    {" +
            "      \"name\": \"друзья5\"," +
            "      \"data\": [" +
            "        762881," +
            "        63579140," +
            "        22512645," +
            "        444772," +
            "        5253131," +
            "        23634971," +
            "        6872604," +
            "        2013189," +
            "        2587680," +
            "        19613731," +
            "        511020" +
            "      ]" +
            "    }," +
            "    {" +
            "      \"name\": \"друзья5\"," +
            "      \"data\": [" +
            "        762881," +
            "        63579140," +
            "        22512645," +
            "        444772," +
            "        5253131," +
            "        23634971," +
            "        6872604," +
            "        2013189," +
            "        2587680," +
            "        19613731," +
            "        511020" +
            "      ]" +
            "    }" +
            "  ]" +
            "}"

    private var chartWidth = 0
    private var chartHeight = 0
    private var screenCenterX = measuredWidth / 2f
    private var screenCenterY = measuredHeight / 2f
    private val initialRadius = resources.getDimension(R.dimen.initial_radius)
    private val clusterNameRadiusOffset = resources.getDimension(R.dimen.soc_chart_cluster_name_radius_offset)
    private val gson = Gson()
    private val rand = Random()
    private val paint = Paint(Paint.ANTI_ALIAS_FLAG)
    private val paintText = Paint(Paint.ANTI_ALIAS_FLAG)
    private val paintTextBg = Paint(Paint.ANTI_ALIAS_FLAG)
    private val paintLine = Paint(Paint.ANTI_ALIAS_FLAG)
    private val paintTextLine = Paint(Paint.ANTI_ALIAS_FLAG)
    private val paintBgWhite = Paint(Paint.ANTI_ALIAS_FLAG)

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        if (chartHeight == 0 || chartWidth == 0)
            return
        val model = gson.fromJson<SocChartModel>(json, SocChartModel::class.java) ?: return

        canvas?.save()
        canvas?.scale(mScaleFactor, mScaleFactor, screenCenterX, screenCenterY)
        canvas?.translate(transX, transY)

        canvas?.drawPaint(paintBgWhite)

        drawMainPointAndFiends(model, canvas)

        canvas?.restore()
    }

    private val pointList = arrayListOf<SocPoint>()
    private val lineList = arrayListOf<SocLine>()

    data class SocPoint(val x: Float, val y: Float, val color: Int)
    data class SocLine(val x1: Float, val y1: Float, val x2: Float, val y2: Float, val color: Int)

    private fun drawMainPointAndFiends(model: SocChartModel, canvas: Canvas?) {
        // Draw central point and friends around it
        paint.color = ContextCompat.getColor(context, R.color.colorAccent)
        canvas?.drawPoint(screenCenterX, screenCenterY, paint)
        val angleStep = (100 + rand.nextInt(260)) / model.friends
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
            canvas?.drawLine(prevX, prevY, xC, yC, paintLine)
            canvas?.drawPoint(xC, yC, paint)
            prevX = xC
            prevY = yC
        }

        drawClusterCenterPoints(model.communities, canvas)
    }

    private fun drawClusterCenterPoints(clusters: List<CommunityModel>, canvas: Canvas?) {
        // Draw clusters around center point
        val randomRadianOffset = rand.nextInt(10)
        var startAngle = 150
        if (clusters.size > 7)
            startAngle = 360
        val randomAngleOffset = 360 - startAngle
        val angleStep = (startAngle + if (randomAngleOffset == 0) 0 else rand.nextInt(randomAngleOffset)) / clusters.size
        for (angle in 0 until clusters.size) {
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
//            val x = radius * cos(radian + randomRadianOffset)
//            val y = radius * sin(radian + randomRadianOffset)

            // Draw central point of cluster
            val xC = (screenCenterX + x).toFloat()
            val yC = (screenCenterY + y).toFloat()
            canvas?.drawLine(screenCenterX, screenCenterY, xC, yC, paintLine)
            canvas?.drawPoint(xC, yC, paint)
            val text = clusters[angle].name
            val textWidth = paintText.measureText(text)
//            val textWidth2 = paintTextBg.measureText(text)
////            paintText.color = color
//            val textX = xC - textWidth / 2f
//            val textX2 = xC - textWidth2 / 2f
////            canvas?.drawText(text, textX2, yC, paintTextBg)
//            canvas?.drawText(text, textX, yC, paintText)

            //Draw name of cluster
            val textRadius = clusterNameRadiusOffset
            val textX: Float = (xC + textRadius * cos(radian + randomRadianOffset + .2f)).toFloat()
            val textY: Float = (yC + textRadius * sin(radian + randomRadianOffset + .3f)).toFloat()
            var fixedTextX = textX - textWidth / 2f
            if (fixedTextX < 0)
                fixedTextX = 0f
            else if (fixedTextX > chartWidth)
                fixedTextX = chartWidth - textWidth

            canvas?.drawLine(textX, textY, xC, yC, paintTextLine)
            canvas?.drawText(text, fixedTextX, textY, paintText)

            drawCluster(xC, yC, clusters[angle], color, canvas)
        }
    }

    private fun drawCluster(parentCentralX: Float, parentCentralY: Float, cluster: CommunityModel, color: Int, canvas: Canvas?) {
        // Draw clusters around center point
        val angleStep = (100 + rand.nextInt(260)) / cluster.data.size
        var prevX = parentCentralX
        var prevY = parentCentralY
        val randomRadianOffset = rand.nextInt(5)
        for (angle in 0 until cluster.data.size) {
            val radius = initialRadius + rand.nextInt(60)
            paint.color = color

            // Calculate center point of community
            val radian = (angle * angleStep) * 0.0174532925
            val randomOffsetX = rand.nextInt(20 + angle)
            val randomOffsetY = rand.nextInt(20 + angle)
            val x = radius * cos(radian + randomRadianOffset) + if (randomOffsetX % 3 == 0) randomOffsetX else randomOffsetX * -1
            val y = radius * sin(radian + randomRadianOffset) + if (randomOffsetY % 3 == 0) randomOffsetY else randomOffsetY * -1

            // Draw central point of cluster
            val xC = (parentCentralX + x).toFloat()
            val yC = (parentCentralY + y).toFloat()
            val drawLineRandom = rand.nextInt(100)
            if (drawLineRandom % 7 == 0)
                canvas?.drawLine(parentCentralX, parentCentralY, xC, yC, paintLine)
            canvas?.drawLine(prevX, prevY, xC, yC, paintLine)
            canvas?.drawPoint(xC, yC, paint)
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

    private var mScaleFactor = 1f
    private var transX = 1f
    private var transY = 1f
    private var startX = 1f
    private var startY = 1f
    private var mScaleDetector: ScaleGestureDetector? = null

    private inner class ScaleListener : ScaleGestureDetector.SimpleOnScaleGestureListener() {
        override fun onScale(detector: ScaleGestureDetector): Boolean {
            mScaleFactor *= detector.scaleFactor

            // Don't let the object get too small or too large.
            mScaleFactor = Math.max(0.1f, Math.min(mScaleFactor, 5.0f))

            invalidate()
            return true
        }
    }

    override fun onTouchEvent(ev: MotionEvent): Boolean {
        // Let the ScaleGestureDetector inspect all events.
        mScaleDetector?.onTouchEvent(ev)

        if (ev.actionMasked == MotionEvent.ACTION_DOWN) {
            startX = ev.x
            startY = ev.y
        } else if (ev.actionMasked == MotionEvent.ACTION_MOVE) {
            transX = ev.x - startX
            transY = ev.y - startY
            invalidate()
        }

        return true
    }
}
package io.humanteq.graphcharttest

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.support.v4.content.ContextCompat
import android.util.AttributeSet
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
        paint.strokeWidth = 10f
        paint.style = Paint.Style.STROKE
        paint.strokeCap = Paint.Cap.ROUND

        paintText.textSize = 30f

        paintLine.color = ContextCompat.getColor(context, R.color.white)
        paintLine.strokeWidth = 1f
        paintLine.style = Paint.Style.STROKE
        paintLine.strokeCap = Paint.Cap.ROUND
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
            "    }" +
            "  ]" +
            "}"

    private var chartWidth = 0
    private var chartHeight = 0
    private var screenCenterX = measuredWidth / 2f
    private var screenCenterY = measuredHeight / 2f
    val gson = Gson()
    val rand = Random()
    private val paint = Paint(Paint.ANTI_ALIAS_FLAG)
    private val paintText = Paint(Paint.ANTI_ALIAS_FLAG)
    private val paintLine = Paint(Paint.ANTI_ALIAS_FLAG)
    private val paintBgWhite = Paint(Paint.ANTI_ALIAS_FLAG)

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        if (chartHeight == 0 || chartWidth == 0)
            return

        val model = gson.fromJson<SocChartModel>(json, SocChartModel::class.java)

        canvas?.drawPaint(paintBgWhite)

        // Draw central point and friends around it
        paint.color = ContextCompat.getColor(context, R.color.colorAccent)
        canvas?.drawPoint(screenCenterX, screenCenterY, paint)
        var angleStep = 360 / model.friends
        var radius = 70f + rand.nextInt(60)
        var prevX = screenCenterX
        var prevY = screenCenterY
        val randomRadianOffset = rand.nextInt(10)
        for (angle in 0..model.friends) {
            val radian = (angle * angleStep) * 0.0174532925
            val randomOffsetX = rand.nextInt(50)
            val randomOffsetY = rand.nextInt(60)
            val x = radius * cos(radian + randomRadianOffset) + if (randomOffsetX % 2 == 0) randomOffsetX else randomOffsetX * -1
            val y = radius * sin(radian + randomRadianOffset) + if (randomOffsetY % 2 == 0) randomOffsetY else randomOffsetY * -1

            val xC = (screenCenterX + x).toFloat()
            val yC = (screenCenterY + y).toFloat()
            canvas?.drawLine(prevX, prevY, xC, yC, paintLine)
            canvas?.drawPoint(xC, yC, paint)
            prevX = xC
            prevY = yC
        }

        // Draw clusters around center point
        val clusters = model.communities
        angleStep = 360 / clusters.size
        for (angle in 0 until clusters.size) {
            val externalRadius = radius + 140 + rand.nextInt(170)
            val color = Color.argb(200, rand.nextInt(), rand.nextInt(), rand.nextInt())
            paint.color = color
            // Calculate center point of community
            val radian = (angle * angleStep) * 0.0174532925
            val randomOffsetX = rand.nextInt(30)
            val randomOffsetY = rand.nextInt(30)
            val x = externalRadius * cos(radian + randomRadianOffset) + if (randomOffsetX % 2 == 0) randomOffsetX else randomOffsetX * -1
            val y = externalRadius * sin(radian + randomRadianOffset) + if (randomOffsetY % 2 == 0) randomOffsetY else randomOffsetY * -1

            // Draw central point of cluster
            val xC = (screenCenterX + x).toFloat()
            val yC = (screenCenterY + y).toFloat()
            canvas?.drawLine(screenCenterX, screenCenterY, xC, yC, paintLine)
            canvas?.drawPoint(xC, yC, paint)
            val text = clusters[angle].name
            val textWidth = paintText.measureText(text)
            paintText.color = color
            canvas?.drawText(text, xC - textWidth / 2f, yC, paintText)

            drawCluster(xC, yC, clusters[angle], color, canvas)
        }
    }

    private fun drawCluster(parentCentralX: Float, parentCentralY: Float, cluster: CommunityModel, color: Int, canvas: Canvas?) {
        // Draw clusters around center point
        val angleStep = 360 / cluster.data.size
        val radius = 60 + rand.nextInt(60)
        var prevX = parentCentralX
        var prevY = parentCentralY
        val randomRadianOffset = rand.nextInt(5)
        for (angle in 0 until cluster.data.size) {
            paint.color = color

            // Calculate center point of community
            val radian = (angle * angleStep) * 0.0174532925
            val randomOffsetX = rand.nextInt(50)
            val randomOffsetY = rand.nextInt(50)
            val x = radius * cos(radian + randomRadianOffset) + if (randomOffsetX % 2 == 0) randomOffsetX else randomOffsetX * -1
            val y = radius * sin(radian + randomRadianOffset) + if (randomOffsetY % 2 == 0) randomOffsetY else randomOffsetY * -1

            // Draw central point of cluster
            val xC = (parentCentralX + x).toFloat()
            val yC = (parentCentralY + y).toFloat()
            val drawLineRandom = rand.nextInt(100)
            if (drawLineRandom % 5 == 0)
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
}

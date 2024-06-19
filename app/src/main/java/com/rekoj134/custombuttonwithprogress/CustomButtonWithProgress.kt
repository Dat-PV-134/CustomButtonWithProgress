package com.rekoj134.custombuttonwithprogress

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.LinearGradient
import android.graphics.Paint
import android.graphics.Path
import android.graphics.Shader
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import androidx.core.content.ContextCompat

class CustomButtonWithProgress : View {
    private var startColorProgress = 0
    private var endColorProgress = 0
    private var startColorTrack = 0
    private var endColorTrack = 0
    private var centerImage = 0
    private var centerImageDrawable: Drawable? = null
    private var centerDrawablePadding = 0f

    private var radiusRipple = 0f
    private var isPressing = false
    private val ripplePaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = Color.parseColor("#4DFFFFFF")
        style = Paint.Style.FILL
        strokeCap = Paint.Cap.ROUND
    }

    private val paint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        style = Paint.Style.STROKE
        strokeCap = Paint.Cap.ROUND
    }
    private var pathProgress = Path()
    private var pathTrack = Path()

    private var totalStep = 3f
    private var currentStep = 1f
    private var tempCurrentStep = 1f
    private var isGoPrevious = false
    private var isProgressing = false

    constructor(context: Context) : super(context) {
        initView(null)
    }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        initView(attrs)
    }

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    ) {
        initView(attrs)
    }

    private fun initView(attrs: AttributeSet?) {
        attrs?.let {
            val obtainStyledAttributes = context.theme.obtainStyledAttributes(
                attrs,
                R.styleable.CustomButtonWithProgress,
                0, 0
            )
            try {
                startColorProgress = obtainStyledAttributes.getInt(
                    R.styleable.CustomButtonWithProgress_start_color_progress,
                    0
                )
                endColorProgress = obtainStyledAttributes.getInteger(
                    R.styleable.CustomButtonWithProgress_end_color_progress,
                    startColorProgress
                )

                startColorTrack = obtainStyledAttributes.getInt(
                    R.styleable.CustomButtonWithProgress_start_color_track,
                    0
                )
                endColorTrack = obtainStyledAttributes.getInt(
                    R.styleable.CustomButtonWithProgress_end_color_track,
                    startColorTrack
                )

                centerImage = obtainStyledAttributes.getResourceId(
                    R.styleable.CustomButtonWithProgress_src_center,
                    0
                )

                centerImageDrawable = ContextCompat.getDrawable(context, centerImage)

                centerDrawablePadding = obtainStyledAttributes.getDimension(
                    R.styleable.CustomButtonWithProgress_src_center_padding,
                    0f
                )

                totalStep = obtainStyledAttributes.getFloat(
                    R.styleable.CustomButtonWithProgress_total_step,
                    3f
                )
            } finally {
                obtainStyledAttributes.recycle()
            }
        }
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        drawView(canvas)
    }

    private fun drawView(canvas: Canvas) {
        paint.setShader(
            LinearGradient(
                0f + width * 0.18f,
                0f + height * 0.18f,
                width.toFloat() - width * 0.18f,
                height.toFloat() - height * 0.18f,
                intArrayOf(
                    startColorProgress,
                    endColorProgress
                ),
                null,
                Shader.TileMode.CLAMP
            )
        )

        paint.style = Paint.Style.FILL
        canvas.drawCircle(width/2f, height/2f, (width - width * 0.38f)/2, paint)

        paint.style = Paint.Style.STROKE
        centerImageDrawable?.setBounds(
            (0f + width * 0.3f + centerDrawablePadding).toInt(),
            (0f + height * 0.3f + centerDrawablePadding).toInt(),
            (width.toFloat() - width * 0.3f - centerDrawablePadding).toInt(),
            (height.toFloat() - height * 0.3f - centerDrawablePadding).toInt()
        )
        centerImageDrawable?.draw(canvas)

        pathTrack.reset()
        pathTrack.arcTo(
            0f + width * 0.1f / 2,
            0f + height * 0.1f / 2,
            width.toFloat() - width * 0.1f / 2,
            height.toFloat() - height * 0.1f / 2,
            0f,
            359f,
            false
        )

        paint.setShader(
            LinearGradient(
                0f + width * 0.1f / 2,
                0f + height * 0.1f / 2,
                width.toFloat() - width * 0.1f / 2,
                height.toFloat() - height * 0.1f / 2,
                intArrayOf(
                    startColorTrack,
                    endColorTrack
                ),
                null,
                Shader.TileMode.CLAMP
            )
        )

        paint.strokeWidth = width * 0.1f / 3

        canvas.drawPath(pathTrack, paint)

        pathProgress.reset()
        var swipeProgress = 360f * currentStep / totalStep
        if (swipeProgress >= 360f) swipeProgress = 359f
        pathProgress.arcTo(
            0f + width * 0.1f / 2,
            0f + height * 0.1f / 2,
            width.toFloat() - width * 0.1f / 2,
            height.toFloat() - height * 0.1f / 2,
            -90f,
            swipeProgress,
            false
        )

        paint.setShader(
            LinearGradient(
                0f + width * 0.1f / 2,
                0f + height * 0.1f / 2,
                width.toFloat() - width * 0.1f / 2,
                height.toFloat() - height * 0.1f / 2,
                intArrayOf(
                    startColorProgress,
                    endColorProgress
                ),
                null,
                Shader.TileMode.CLAMP
            )
        )

        canvas.drawPath(pathProgress, paint)

        if (isPressing) {
            canvas.drawCircle(width / 2f, height / 2f, radiusRipple, ripplePaint)

            if (radiusRipple <= width / 2f - width * 0.18f) {
                radiusRipple += width * 0.1f
                invalidate()
            } else {
                radiusRipple = 0f
                isPressing = false
                invalidate()
            }
        }

        if (isGoPrevious) {
            isProgressing = true
            if (currentStep > tempCurrentStep) {
                currentStep -= 0.04f
                invalidate()
            } else {
                currentStep = tempCurrentStep
                isGoPrevious = false
                isProgressing = false
            }
        } else {
            isProgressing = true
            if (currentStep < tempCurrentStep) {
                currentStep += 0.04f
                invalidate()
            } else {
                currentStep = tempCurrentStep
                isProgressing = false
            }
        }
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onTouchEvent(event: MotionEvent?): Boolean {
        if (isPressing) return true

        event?.let {
            // get pointer index from the event object
//            val pointerIndex = event.actionIndex

            // get pointer ID
//            val pointerId = event.getPointerId(pointerIndex)

            // get masked (not specific to a pointer) action
            val maskedAction = event.actionMasked

            when (maskedAction) {
                MotionEvent.ACTION_DOWN, MotionEvent.ACTION_POINTER_DOWN -> {
                    if (currentStep < totalStep) {
                        tempCurrentStep = currentStep + 1
                    }
                    isPressing = true
                }

                MotionEvent.ACTION_MOVE -> {

                }

                MotionEvent.ACTION_UP, MotionEvent.ACTION_POINTER_UP, MotionEvent.ACTION_CANCEL -> {

                }
            }

            invalidate()
        }
        return true
    }

    fun nextStep() {
        if (currentStep < totalStep && !isPressing && !isProgressing) {
            tempCurrentStep = currentStep + 1
            invalidate()
        }
    }

    fun previousStep() {
        if (currentStep > 1 && !isPressing && !isProgressing) {
            tempCurrentStep = currentStep - 1
            isGoPrevious = true
            invalidate()
        }
    }
}
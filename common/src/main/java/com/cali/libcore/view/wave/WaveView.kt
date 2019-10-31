/*
 *  Copyright (C) 2015, gelitenight(gelitenight@gmail.com).
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */
package com.cali.libcore.view.wave

import android.content.Context
import android.graphics.*
import android.graphics.Paint.Style
import android.util.AttributeSet
import android.view.View
import com.cali.common.R

class WaveView : View {

    // if true, the shader will display the wave
    var isShowWave: Boolean = false

    // shader containing repeated waves
    private var mWaveShader: BitmapShader? = null
    // shader matrix
    private val mShaderMatrix: Matrix by lazy {
        Matrix()
    }
    // paint to draw wave
    private val mViewPaint: Paint by lazy {
        Paint().apply {
            isAntiAlias = true
        }
    }
    // paint to draw border
    private val mBorderPaint: Paint by lazy {
        Paint().apply {
            isAntiAlias = true
            style = Style.STROKE
        }
    }

    private var mDefaultAmplitude: Float = 0f
    private var mDefaultWaterLevel: Float = 0f
    private var mDefaultWaveLength: Float = 0f
    private var mDefaultAngularFrequency: Double = 0.0

    private var mAmplitudeRatio = DEFAULT_AMPLITUDE_RATIO
    /**
     * Set horizontal size of wave according to `waveLengthRatio`
     *
     * Ratio of wave length to width of WaveView.
     */
    private var waveLengthRatio = DEFAULT_WAVE_LENGTH_RATIO
    private var mWaterLevelRatio = DEFAULT_WATER_LEVEL_RATIO
    private var mWaveShiftRatio = DEFAULT_WAVE_SHIFT_RATIO

    private var mBehindWaveColor = DEFAULT_BEHIND_WAVE_COLOR
    private var mFrontWaveColor = DEFAULT_FRONT_WAVE_COLOR
    private var mShapeType = DEFAULT_WAVE_SHAPE

    /**
     * Shift the wave horizontally according to `waveShiftRatio`.
     *
     * Result of waveShiftRatio multiples width of WaveView is the length to shift.
     */
    var waveShiftRatio: Float
        get() = mWaveShiftRatio
        set(waveShiftRatio) {
            if (mWaveShiftRatio != waveShiftRatio) {
                mWaveShiftRatio = waveShiftRatio
                invalidate()
            }
        }

    /**
     * Set water level according to `waterLevelRatio`.
     *
     * Ratio of water level to WaveView height.
     */
    var waterLevelRatio: Float
        get() = mWaterLevelRatio
        set(waterLevelRatio) {
            if (mWaterLevelRatio != waterLevelRatio) {
                mWaterLevelRatio = waterLevelRatio
                invalidate()
            }
        }

    /**
     * Set vertical size of wave according to `amplitudeRatio`
     *
     * Ratio of amplitude to height of WaveView.
     */
    var amplitudeRatio: Float
        get() = mAmplitudeRatio
        set(amplitudeRatio) {
            if (mAmplitudeRatio != amplitudeRatio) {
                mAmplitudeRatio = amplitudeRatio
                invalidate()
            }
        }

    enum class ShapeType {
        CIRCLE,
        SQUARE
    }


    constructor(context: Context) : super(context)

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        init(attrs)
    }

    constructor(context: Context, attrs: AttributeSet, defStyle: Int) : super(context, attrs, defStyle) {
        init(attrs)
    }

    private fun init(attrs: AttributeSet) {

        val typedArray = context.theme.obtainStyledAttributes(
            attrs,
            R.styleable.WaveView, 0, 0
        )

        mAmplitudeRatio = typedArray.getFloat(R.styleable.WaveView_amplitudeRatio, DEFAULT_AMPLITUDE_RATIO)
        mWaterLevelRatio = typedArray.getFloat(R.styleable.WaveView_waveWaterLevel, DEFAULT_WATER_LEVEL_RATIO)
        waveLengthRatio = typedArray.getFloat(R.styleable.WaveView_waveLengthRatio, DEFAULT_WAVE_LENGTH_RATIO)
        mWaveShiftRatio = typedArray.getFloat(R.styleable.WaveView_waveShiftRatio, DEFAULT_WAVE_SHIFT_RATIO)
        mFrontWaveColor = typedArray.getColor(R.styleable.WaveView_frontWaveColor, DEFAULT_FRONT_WAVE_COLOR)
        mBehindWaveColor = typedArray.getColor(R.styleable.WaveView_behindWaveColor, DEFAULT_BEHIND_WAVE_COLOR)
        mShapeType =
            if (typedArray.getInt(R.styleable.WaveView_waveShape, 0) == 0) ShapeType.CIRCLE else ShapeType.SQUARE
        isShowWave = typedArray.getBoolean(R.styleable.WaveView_showWave, true)

        typedArray.recycle()

    }

    fun setBorder(width: Int, color: Int) {
        mBorderPaint.color = color
        mBorderPaint.strokeWidth = width.toFloat()
        invalidate()
    }

    fun setWaveColor(behindWaveColor: Int, frontWaveColor: Int) {
        mBehindWaveColor = behindWaveColor
        mFrontWaveColor = frontWaveColor

        if (width > 0 && height > 0) {
            // need to recreate shader when color changed
            mWaveShader = null
            createShader()
            invalidate()
        }
    }

    fun setShapeType(shapeType: ShapeType) {
        mShapeType = shapeType
        invalidate()
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)

        createShader()
    }

    /**
     * Create the shader with default waves which repeat horizontally, and clamp vertically
     */
    private fun createShader() {
        mDefaultAngularFrequency = 2.0f * Math.PI / DEFAULT_WAVE_LENGTH_RATIO.toDouble() / width.toDouble()
        mDefaultAmplitude = height * DEFAULT_AMPLITUDE_RATIO
        mDefaultWaterLevel = height * DEFAULT_WATER_LEVEL_RATIO
        mDefaultWaveLength = width.toFloat()

        val bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(bitmap)

        val wavePaint = Paint()
        wavePaint.strokeWidth = 2f
        wavePaint.isAntiAlias = true

        // Draw default waves into the bitmap
        // y=Asin(ωx+φ)+h
        val endX = width + 1
        val endY = height + 1

        val waveY = FloatArray(endX)

        wavePaint.color = mBehindWaveColor
        for (beginX in 0 until endX) {
            val wx = beginX * mDefaultAngularFrequency
            val beginY = (mDefaultWaterLevel + mDefaultAmplitude * Math.sin(wx)).toFloat()
            canvas.drawLine(beginX.toFloat(), beginY, beginX.toFloat(), endY.toFloat(), wavePaint)

            waveY[beginX] = beginY
        }

        wavePaint.color = mFrontWaveColor
        val wave2Shift = (mDefaultWaveLength / 4).toInt()
        for (beginX in 0 until endX) {
            canvas.drawLine(
                beginX.toFloat(),
                waveY[(beginX + wave2Shift) % endX],
                beginX.toFloat(),
                endY.toFloat(),
                wavePaint
            )
        }

        // use the bitamp to create the shader
        // https://blog.csdn.net/harvic880925/article/details/52039081
        mWaveShader = BitmapShader(bitmap, Shader.TileMode.REPEAT, Shader.TileMode.CLAMP)
        mViewPaint.shader = mWaveShader
    }

    override fun onDraw(canvas: Canvas) {
        // modify paint shader according to mShowWave state
        if (isShowWave && mWaveShader != null) {
            // first call after mShowWave, assign it to our paint
            if (mViewPaint.shader == null) {
                mViewPaint.shader = mWaveShader
            }

            // sacle shader according to mWaveLengthRatio and mAmplitudeRatio
            // this decides the size(mWaveLengthRatio for width, mAmplitudeRatio for height) of waves
            mShaderMatrix.setScale(
                waveLengthRatio / DEFAULT_WAVE_LENGTH_RATIO,
                mAmplitudeRatio / DEFAULT_AMPLITUDE_RATIO,
                0f,
                mDefaultWaterLevel
            )
            // translate shader according to mWaveShiftRatio and mWaterLevelRatio
            // this decides the start position(mWaveShiftRatio for x, mWaterLevelRatio for y) of waves
            mShaderMatrix.postTranslate(
                mWaveShiftRatio * width,
                (DEFAULT_WATER_LEVEL_RATIO - mWaterLevelRatio) * height
            )

            // assign matrix to invalidate the shader
            mWaveShader?.setLocalMatrix(mShaderMatrix)

            val borderWidth = mBorderPaint.strokeWidth
            when (mShapeType) {
               ShapeType.CIRCLE -> {
                   //外部圆
                    if (borderWidth > 0) {
                        canvas.drawCircle(width / 2f, height / 2f,
                            (width - borderWidth) / 2f - 1f,
                            mBorderPaint)
                    }
                   //波纹
                    val radius = width / 2f - borderWidth
                    canvas.drawCircle(width / 2f, height / 2f,
                        radius,
                        mViewPaint)
                }
                ShapeType.SQUARE -> {
                    if (borderWidth > 0) {
                        canvas.drawRect(
                            borderWidth / 2f,
                            borderWidth / 2f,
                            width.toFloat() - borderWidth / 2f - 0.5f,
                            height.toFloat() - borderWidth / 2f - 0.5f,
                            mBorderPaint
                        )
                    }
                    canvas.drawRect(
                        borderWidth, borderWidth, width - borderWidth,
                        height - borderWidth, mViewPaint
                    )
                }
            }
        } else {
            mViewPaint.shader = null
        }
    }

    companion object {
        /**
         * +------------------------+
         * |<--wave length->        |______
         * |   /\          |   /\   |  |
         * |  /  \         |  /  \  | amplitude
         * | /    \        | /    \ |  |
         * |/      \       |/      \|__|____
         * |        \      /        |  |
         * |         \    /         |  |
         * |          \  /          |  |
         * |           \/           | water level
         * |                        |  |
         * |                        |  |
         * +------------------------+__|____
         */
        private const val DEFAULT_AMPLITUDE_RATIO = 0.05f
        private const val DEFAULT_WATER_LEVEL_RATIO = 0.5f
        private const val DEFAULT_WAVE_LENGTH_RATIO = 1.0f
        private const val DEFAULT_WAVE_SHIFT_RATIO = 0.0f

        val DEFAULT_BEHIND_WAVE_COLOR = Color.parseColor("#28FFFFFF")
        val DEFAULT_FRONT_WAVE_COLOR = Color.parseColor("#3CFFFFFF")
        val DEFAULT_WAVE_SHAPE = ShapeType.CIRCLE
    }
}

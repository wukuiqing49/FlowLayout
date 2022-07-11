package com.wkq.flow

import android.content.Context
import android.graphics.drawable.GradientDrawable
import android.text.TextUtils
import android.util.AttributeSet
import android.view.Gravity
import android.view.ViewGroup
import android.widget.TextView
import com.wkq.flow.util.PixelsUtil


/**
 * @author wkq
 *
 * @date 2022年07月08日 10:12
 *
 *@des
 *
 */

class FlowLayout @JvmOverloads constructor(
        context: Context,
        attrs: AttributeSet? = null,
        defStyleAttr: Int? = 0
) :
        ViewGroup(context, attrs, defStyleAttr!!, 0) {

    //背景颜色
    var bgcolor: Int = 0

    //文字颜色
    var textColor: Int = 0

    //padding
    var mLeftAndRightPadding = 10
    var mTopAndBottomPadding = 5

    //边距
    var leftAndRightMargin = 1
    var topAndBottomMargin = 1
    //角度
    var flAngleSize = 1f
    //点击监听
    var listener: FlowLayoutLabelslListener? = null

    //内容集合
    var textList = ArrayList<String>()

    init {
        var style = context.obtainStyledAttributes(attrs, R.styleable.FlowLayoutStyle)
        bgcolor = style.getColor(  R.styleable.FlowLayoutStyle_flBackgroundColor,context.resources.getColor(R.color.bg_color))
        textColor = style.getColor( R.styleable.FlowLayoutStyle_flTextColor,  context.resources.getColor(R.color.text_color) )
        mLeftAndRightPadding = style.getInt(R.styleable.FlowLayoutStyle_flPaddingLeftAndRight, 10)
        mTopAndBottomPadding = style.getInt(R.styleable.FlowLayoutStyle_flPaddingTopAndBottom, 5)
        leftAndRightMargin = style.getInt(R.styleable.FlowLayoutStyle_flMarginLeftAndRight, 10)
        topAndBottomMargin = style.getInt(R.styleable.FlowLayoutStyle_flMarginTopAndBottom, 5)
        flAngleSize = style.getFloat(R.styleable.FlowLayoutStyle_flAngleSize, 10f)
    }

    /**
     * 设置点击监听
     */
    fun addLabelsListener(listener: FlowLayoutLabelslListener) {
        this.listener = listener
    }

    override fun generateLayoutParams(attrs: AttributeSet?): LayoutParams? {
        return MarginLayoutParams(context, attrs)
    }

    //宽度
    var lineHight = 0
    var mRealWidth = 0
    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        measureChildren(widthMeasureSpec, heightMeasureSpec);
        //获取测量模式
        var widthSize = MeasureSpec.getSize(widthMeasureSpec)
        var heightSize = MeasureSpec.getSize(heightMeasureSpec)
        // kotlin for循环中 .. 包含最后一个数  until  不包含最后一次
        var groupHeight = 0
        var maxWidth = 0
        var curWidth = 0;
        groupHeight = getChildAt(0).measuredHeight + topAndBottomMargin
        for (index in 0 until childCount) {
            val currentView = getChildAt(index);
            //测量后才能获取宽高
            lineHight = (currentView.measuredHeight + topAndBottomMargin)
            var viewWidth = (currentView.measuredWidth + leftAndRightMargin)
            if ((viewWidth + curWidth) > (widthSize - leftAndRightMargin)) {
                // 一行不满足需要换行
                groupHeight += lineHight
                curWidth = 0
            } else {
                curWidth += (viewWidth)
            }
            maxWidth = Math.max(maxWidth, curWidth)
        }
        groupHeight = Math.max(groupHeight, lineHight)

        //作为一个ViewGroup,它自己也是一个View，它的大小也要根据它的父View给他提供的宽高来度量
        val widthMode = MeasureSpec.getMode(widthMeasureSpec)
        val heigthMode = MeasureSpec.getMode(heightMeasureSpec)
        //如果是实际大小，则直接使用设置的具体值
        val realWidth = if (widthMode == MeasureSpec.EXACTLY) widthSize else (maxWidth + leftAndRightMargin)
        val realHeight = if (heigthMode == MeasureSpec.EXACTLY) heightSize else (groupHeight + topAndBottomMargin)
        //保存测量的大小
        setMeasuredDimension(realWidth, realHeight)
        mRealWidth = realWidth
    }

    var linNum = 1
    override fun onLayout(changed: Boolean, l: Int, t: Int, r: Int, b: Int) {
        // 布局的位置
        var left: Int = 0
        var top: Int = 0
        var right: Int = 0
        var bottom: Int = 0

        var curLeft = 0
        var curTop = 0
        for (index in 0 until childCount) {
            var view = getChildAt(index)
            //一行的高度
            lineHight = view.measuredHeight + topAndBottomMargin

            if ((right + view.measuredWidth) > mRealWidth) {
                //需要换行
                linNum += 1
                left = leftAndRightMargin
                top = lineHight * (linNum - 1) + topAndBottomMargin
                right = left + view.measuredWidth
                bottom = top + view.measuredHeight
                view.layout(left, top, right, bottom)
                curLeft = right
            } else {
                left = leftAndRightMargin + curLeft
                top = (linNum - 1) * view.measuredHeight + linNum * topAndBottomMargin
                right = left + view.measuredWidth
                bottom = top + view.measuredHeight
                view.layout(left, top, right, bottom)
                curLeft = right
            }

        }


    }

    /**
     * 设置标签数据
     */
    fun addLabels(labels: ArrayList<String>, isAdd: Boolean = true) {
        if (labels == null || labels.size == 0) return
        if (isAdd) {
            textList.addAll(labels)
        } else {
            textList = labels
        }
        textList.forEach {
            if (it != null && !TextUtils.isEmpty(it.toString()))
                addTextView(it)
        }
    }

    /**
     * 添加单个标签
     */
    fun addLabels(textContent: String, isAdd: Boolean = true) {
        if (textContent != null && TextUtils.isEmpty(textContent.toString())) return
        if (isAdd) {
            textList.add(textContent)
        } else {
            textList.clear()
            textList.add(textContent)
        }

        addTextView(textContent)

    }

    private fun addTextView(textContent: String) {
        var textDrawable = GradientDrawable()
        textDrawable.setColor(bgcolor)
        textDrawable.cornerRadius = flAngleSize
        var textView = TextView(context)
        textView.text = textContent
        textView.setBackground(textDrawable)
        textView.setTextColor(textColor)
        textView.setGravity(Gravity.CENTER);
        textView.setPadding(mLeftAndRightPadding, mTopAndBottomPadding, mLeftAndRightPadding, mTopAndBottomPadding)
        textView.setOnClickListener {
            if (listener != null) listener!!.onLabelsClick(textContent)
        }
        addView(textView)
    }

    interface FlowLayoutLabelslListener {

        fun onLabelsClick(content: Any);

    }
}
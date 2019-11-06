package com.ch.yoon.kakao.pay.imagesearch.presentation.common.customview.searchview

import android.annotation.SuppressLint
import android.content.Context
import android.text.Editable
import android.text.TextWatcher
import android.util.AttributeSet
import android.util.TypedValue
import android.view.KeyEvent
import android.view.MotionEvent
import android.view.View
import android.widget.EditText
import android.widget.ImageView
import androidx.annotation.DrawableRes
import androidx.appcompat.widget.LinearLayoutCompat
import androidx.core.content.ContextCompat
import com.ch.yoon.kakao.pay.imagesearch.R
import kotlinx.android.synthetic.main.custom_clear_search_view.view.*

/**
 * Creator : ch-yoon
 * Date : 2019-11-06.
 */
class ClearSearchView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyle: Int = 0
): LinearLayoutCompat(context, attrs, defStyle) {

    companion object {
        private const val DEFAULT_TEXT_SIZE_PX = 50f
    }

    private val searchViewLayout: LinearLayoutCompat
    private val searchViewEditText: EditText
    private val searchViewClearButton: ImageView
    private val searchViewSearchButton: ImageView

    private var onTextChangeListener: OnTextChangeListener? = null
    private var onSearchButtonClickListener: OnSearchButtonClickListener? = null
    private var onSearchViewEditTextClickListener: OnSearchViewEditTextClickListener? = null

    init {
        val view = inflate(context, R.layout.custom_clear_search_view, this)
        with(view) {
            searchViewLayout = root
            searchViewEditText = editText
            searchViewClearButton = clearButton
            searchViewSearchButton = searchButton
        }

        initSearchViewEditText()
        initSearchViewClearButton()
        initSearchViewSearchButton()
        initAttrs(attrs, defStyle)
    }

    @SuppressLint("ClickableViewAccessibility")
    private fun initSearchViewEditText() {
        refreshClearButtonVisibility(searchViewEditText.text.length)

        searchViewEditText.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {}

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                val textLength = s?.length ?: 0
                refreshClearButtonVisibility(textLength)
                onTextChangeListener?.onTextChange(s.toString())
            }
        })

        searchViewEditText.setOnEditorActionListener{ _, actionId, _ ->
            if (actionId == KeyEvent.KEYCODE_ENDCALL) {
                onSearchButtonClickListener?.onClick(searchViewEditText.text.toString())
                true
            } else {
                false
            }
        }

        searchViewEditText.setOnTouchListener { _, event ->
            if (MotionEvent.ACTION_UP == event.action) {
                onSearchViewEditTextClickListener?.onClick(searchViewEditText)
            }
            false
        }
    }

    private fun initSearchViewClearButton() {
        searchViewClearButton.setOnClickListener {
            searchViewEditText.setText("")
        }
    }

    private fun initSearchViewSearchButton() {
        searchViewSearchButton.setOnClickListener {
            val inputtedText = searchViewEditText.text.toString()
            onSearchButtonClickListener?.onClick(inputtedText)
        }
    }

    @SuppressLint("Recycle")
    private fun initAttrs(attrs: AttributeSet?, defStyle: Int) {
        attrs?.let { attributeSet ->
            val typedArray = context.obtainStyledAttributes(attributeSet, R.styleable.ClearSearchView, defStyle, 0)

            with(typedArray) {
                val backgroundResId = getResourceId(R.styleable.ClearSearchView_background, R.color.colorBasicGray)
                setBackground(backgroundResId)

                val text = getString(R.styleable.ClearSearchView_text) ?: ""
                setText(text)

                val textSize = getDimension(R.styleable.ClearSearchView_textSize, DEFAULT_TEXT_SIZE_PX)
                setTextSize(textSize)

                val textColor = getColor(R.styleable.ClearSearchView_textColor, ContextCompat.getColor(context, android.R.color.black))
                setTextColor(textColor)

                val clearButtonIconResId = getResourceId(R.styleable.ClearSearchView_clearButtonIcon, R.drawable.ic_action_cancel)
                setClearButtonIconResource(clearButtonIconResId)

                val searchButtonIconResId = getResourceId(R.styleable.ClearSearchView_searchButtonIcon, R.drawable.ic_action_search)
                setSearchButtonIconResource(searchButtonIconResId)
            }

            typedArray.recycle()
        }
    }

    fun setBackground(@DrawableRes drawableResId: Int) {
        searchViewLayout.setBackgroundResource(drawableResId)
    }

    fun setText(text: String?) {
        text?.let { inputtedText ->
            with(searchViewEditText) {
                val beforeText = searchViewEditText.text.toString()
                if(inputtedText != beforeText) {
                    setText(inputtedText)
                }
            }
        }
    }

    fun setTextSize(textSize: Float) {
        searchViewEditText.setTextSize(TypedValue.COMPLEX_UNIT_PX, textSize)
    }

    fun setTextColor(textColor: Int) {
        searchViewEditText.setTextColor(textColor)
    }

    fun setClearButtonIconResource(@DrawableRes drawableResId: Int) {
        searchViewClearButton.setImageResource(drawableResId)
    }

    fun setSearchButtonIconResource(@DrawableRes drawableResId: Int) {
        searchViewSearchButton.setImageResource(drawableResId)
    }

    fun setOnTextChangeListener(onTextChangeListener: OnTextChangeListener?) {
        this.onTextChangeListener = onTextChangeListener
    }

    fun setOnSearchButtonClickListener(onSearchButtonClickListener: OnSearchButtonClickListener?) {
        this.onSearchButtonClickListener = onSearchButtonClickListener
    }

    fun setOnSearchViewEditTextClickListener(onSearchViewEditTextClickListener: OnSearchViewEditTextClickListener?) {
        this.onSearchViewEditTextClickListener = onSearchViewEditTextClickListener
    }

    private fun refreshClearButtonVisibility(textLength: Int) {
        val visibility = if(textLength > 0) {
            View.VISIBLE
        } else {
            View.GONE
        }

        if(searchViewClearButton.visibility != visibility) {
            searchViewClearButton.visibility = visibility
        }
    }

}
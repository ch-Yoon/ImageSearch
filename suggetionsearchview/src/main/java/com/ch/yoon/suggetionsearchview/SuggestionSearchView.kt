package com.ch.yoon.suggetionsearchview

import android.content.Context
import android.os.Parcel
import android.os.Parcelable
import android.text.Editable
import android.text.TextWatcher
import android.util.AttributeSet
import android.util.TypedValue
import android.view.MotionEvent
import android.view.View
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.ImageView
import androidx.annotation.DrawableRes
import androidx.appcompat.widget.LinearLayoutCompat
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.suggestion_search_view.view.*

/**
 * Creator : ch-yoon
 * Date : 2019-11-06.
 */
class SuggestionSearchView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyle: Int = 0
): LinearLayoutCompat(context, attrs, defStyle) {

    enum class State {
        OPEN, CLOSE
    }

    private val suggestionSearchViewContainer: LinearLayoutCompat

    private val searchViewContainer: LinearLayoutCompat
    private val closeButton: ImageView
    private val inputEditText: EditText
    private val clearButton: ImageView

    private val suggestionViewContainer: LinearLayoutCompat
    private val suggestionRecyclerView: RecyclerView

    private var onTextChangeListener: OnTextChangeListener? = null
    private var onSearchButtonClickListener: OnSearchButtonClickListener? = null
    private var onStateChangeListener: OnStateChangeListener? = null

    init {
        inflate(context, R.layout.suggestion_search_view, this)

        suggestionSearchViewContainer = this
        searchViewContainer = search_view_container
        closeButton = close_button
        inputEditText = input_edit_text
        clearButton = clear_button
        suggestionViewContainer = suggestion_view_container
        suggestionRecyclerView = suggestion_recyclerview

        initAttrs(attrs, defStyle)
        initCloseButton()
        initInputEditText()
        initClearButton()
        initSuggestionContainer()
    }

    private fun initCloseButton() {
        closeButton.setOnClickListener {
            hideSuggestionSearchView()
        }
    }

    private fun initInputEditText() {
        with(inputEditText) {
            refreshClearButtonVisibility(text.length)

            addTextChangedListener(object : TextWatcher {
                override fun afterTextChanged(s: Editable?) {}

                override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                    (s?.toString())?.let { changedText ->
                        refreshClearButtonVisibility(changedText.length)
                        onTextChangeListener?.onTextChange(changedText)
                    }
                }
            })

            setOnEditorActionListener{ _, actionId, _ ->
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    confirmSearch()
                    true
                } else {
                    false
                }
            }

            setOnFocusChangeListener { v, hasFocus ->
                if(hasFocus) {
                    v.showKeyboard()
                } else {
                    v.hideKeyboard()
                }
            }

            setOnTouchListener { _, event ->
                if (MotionEvent.ACTION_UP == event.action) {
                    showSuggestionView()
                }
                false
            }
        }
    }

    private fun initClearButton() {
        clearButton.setOnClickListener {
            inputEditText.setText("")
            inputEditText.requestFocus()
            suggestionViewContainer.show()
        }
    }

    private fun initSuggestionContainer() {
        suggestionViewContainer.setOnClickListener {
            hideSuggestionView()
        }
    }

    private fun initAttrs(attrs: AttributeSet?, defStyle: Int) {
        attrs?.let { attributeSet ->
            val typedArray = context.obtainStyledAttributes(attributeSet, R.styleable.SuggestionSearchView, defStyle, 0)

            with(typedArray) {
                val text = getString(R.styleable.SuggestionSearchView_text) ?: ""
                setText(text)

                val hint = getString(R.styleable.SuggestionSearchView_hint) ?: ""
                setHint(hint)

                val textSize = getDimension(R.styleable.SuggestionSearchView_textSize, 50f)
                setTextSize(textSize)

                val textColor = getColor(R.styleable.SuggestionSearchView_textColor, ContextCompat.getColor(context, android.R.color.black))
                setTextColor(textColor)

                val clearButtonIconResId = getResourceId(R.styleable.SuggestionSearchView_clearButtonIcon, R.drawable.ic_action_cancel)
                setClearButtonIconResource(clearButtonIconResId)

                val closeButtonIconResId = getResourceId(R.styleable.SuggestionSearchView_closeButtonIcon, R.drawable.ic_action_back)
                setCloseButtonIconResource(closeButtonIconResId)
            }

            typedArray.recycle()
        }
    }

    fun show() {
        showSuggestionSearchView()
    }

    fun hide() {
        hideSuggestionSearchView()
    }

    fun showSuggestions() {
        showSuggestionView()
    }

    fun hideSuggestions() {
        hideSuggestionView()
    }

    fun setText(text: String?) {
        text?.let { inputtedText ->
            val beforeText = inputEditText.text.toString()
            if(beforeText != inputtedText) {
                inputEditText.setText(inputtedText)
            }
        }
    }

    fun setTextSize(textSize: Float) {
        inputEditText.setTextSize(TypedValue.COMPLEX_UNIT_PX, textSize)
    }

    fun setHint(hint: String) {
        inputEditText.hint = hint
    }

    fun setTextColor(textColor: Int) {
        inputEditText.setTextColor(textColor)
    }

    fun setClearButtonIconResource(@DrawableRes drawableResId: Int) {
        clearButton.setImageResource(drawableResId)
    }

    fun setCloseButtonIconResource(@DrawableRes drawableResId: Int) {
        closeButton.setImageResource(drawableResId)
    }

    fun setOnTextChangeListener(onTextChangeListener: OnTextChangeListener?) {
        this.onTextChangeListener = onTextChangeListener
    }

    fun setOnSearchButtonClickListener(onSearchButtonClickListener: OnSearchButtonClickListener?) {
        this.onSearchButtonClickListener = onSearchButtonClickListener
    }

    fun setOnStateChangeListener(onStateChangeListener: OnStateChangeListener?) {
        this.onStateChangeListener = onStateChangeListener
    }

    fun setAdapter(adapter: RecyclerView.Adapter<*>) {
        suggestionRecyclerView.adapter = adapter
    }

    fun getAdapter(): RecyclerView.Adapter<*>? {
        return suggestionRecyclerView.adapter
    }

    private fun refreshClearButtonVisibility(textLength: Int) {
        if(0 < textLength) {
            clearButton.show()
        } else {
            clearButton.hide()
        }
    }

    private fun confirmSearch() {
        hideSuggestionView()
        onSearchButtonClickListener?.onClick(inputEditText.text.toString())
    }

    private fun showSuggestionSearchView() {
        suggestionSearchViewContainer.show()
        showSuggestionView()
        onStateChangeListener?.onChange(State.OPEN)
    }

    private fun hideSuggestionSearchView() {
        suggestionSearchViewContainer.hide()
        inputEditText.setText("")
        hideSuggestionView()
        onStateChangeListener?.onChange(State.CLOSE)
    }

    private fun showSuggestionView() {
        inputEditText.requestFocus()
        suggestionViewContainer.show()
    }

    private fun hideSuggestionView() {
        inputEditText.clearFocus()
        suggestionViewContainer.hide()
    }

    private fun View.showKeyboard() {
        val inputMethodManager = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        inputMethodManager.showSoftInput(this, 0)
    }

    private fun View.hideKeyboard() {
        val inputMethodManager = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        inputMethodManager.hideSoftInputFromWindow(windowToken, 0)
    }

    private fun View.show() {
        visibility = View.VISIBLE
    }

    private fun View.hide() {
        visibility = View.INVISIBLE
    }

    override fun onSaveInstanceState(): Parcelable? {
        return SavedState(super.onSaveInstanceState()).apply {
            suggestionSearchViewContainerVisibility = suggestionSearchViewContainer.visibility
            suggestionViewContainer.visibility = suggestionViewContainer.visibility
        }
    }

    override fun onRestoreInstanceState(state: Parcelable?) {
        super.onRestoreInstanceState(state)
        if(state is SavedState) {
            suggestionSearchViewContainer.visibility = state.suggestionSearchViewContainerVisibility
            suggestionViewContainer.visibility = state.suggestionViewContainerVisibility
        }
    }

    private class SavedState : BaseSavedState {

        var suggestionSearchViewContainerVisibility: Int = 0
        var suggestionViewContainerVisibility: Int = 0

        constructor(parcelable: Parcelable?) : super(parcelable)

        constructor(source: Parcel) : super(source) {
            suggestionSearchViewContainerVisibility = source.readInt()
            suggestionViewContainerVisibility = source.readInt()
        }

        override fun describeContents() = 0

        override fun writeToParcel(dest: Parcel, flags: Int) {
            super.writeToParcel(dest, flags)
            with(dest) {
                writeInt(suggestionSearchViewContainerVisibility)
                writeInt(suggestionViewContainerVisibility)
            }
        }

        companion object {
            @JvmField
            val CREATOR: Parcelable.Creator<SavedState> = object : Parcelable.Creator<SavedState> {
                override fun createFromParcel(source: Parcel): SavedState = SavedState(source)
                override fun newArray(size: Int): Array<SavedState?> = arrayOfNulls(size)
            }
        }
    }
}
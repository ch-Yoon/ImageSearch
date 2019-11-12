package com.ch.yoon.suggetionsearchview

import android.content.Context
import android.os.Parcel
import android.os.Parcelable
import android.text.Editable
import android.text.TextWatcher
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.widget.EditText
import android.widget.ImageView
import androidx.annotation.DrawableRes
import androidx.appcompat.widget.LinearLayoutCompat
import androidx.cardview.widget.CardView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.ch.yoon.suggetionsearchview.adapter.DefaultSuggestionAdapter
import com.ch.yoon.suggetionsearchview.adapter.SuggestionAdapter
import com.ch.yoon.suggetionsearchview.extention.*
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

    private val root: LinearLayoutCompat

    private val backgroundView: View
    private val suggestionSearchViewContainer: CardView
    private val searchViewContainer: LinearLayoutCompat
    private val closeButton: ImageView
    private val inputEditText: EditText
    private val clearButton: ImageView
    private val suggestionViewContainer: LinearLayoutCompat
    private val suggestionRecyclerView: RecyclerView

    private var onTextChangeListener: OnTextChangeListener? = null
    private var onSearchButtonClickListener: OnSearchButtonClickListener? = null
    private var onStateChangeListener: OnStateChangeListener? = null
    private var onSuggestionItemClickListener: OnSuggestionItemClickListener? = null
    private var onSuggestionSubButtonClickListener: OnSuggestionSubButtonClickListener? = null
    private var onSuggestionFooterClickListener: OnSuggestionFooterClickListener? = null

    init {
        inflate(context, R.layout.suggestion_search_view, this)

        root = this
        backgroundView = background_view
        suggestionSearchViewContainer = suggestion_search_view_container
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
        initBackgroundView()
        initDefaultAdapter()
    }

    private fun initAttrs(attrs: AttributeSet?, defStyle: Int) {
        attrs?.let { attributeSet ->
            val typedArray = context.obtainStyledAttributes(attributeSet, R.styleable.SuggestionSearchView, defStyle, 0)

            with(typedArray) {
                val radius = getDimension(R.styleable.SuggestionSearchView_radius, 0f)
                setRadius(radius)

                val margin = getDimension(R.styleable.SuggestionSearchView_margin, 0f)
                setMargin(margin)

                val searchViewBackgroundColor = getColor(R.styleable.SuggestionSearchView_searchViewBackgroundColor, ContextCompat.getColor(context, R.color.colorLightGray))
                setSearchViewBackgroundColor(searchViewBackgroundColor)

                val text = getString(R.styleable.SuggestionSearchView_text) ?: ""
                setText(text)

                val textSize = getDimension(R.styleable.SuggestionSearchView_textSize, resources.getDimension(R.dimen.suggestion_search_input_edit_text_default_text_size))
                setTextSize(textSize)

                val textColor = getColor(R.styleable.SuggestionSearchView_textColor, ContextCompat.getColor(context, R.color.colorBlack))
                setTextColor(textColor)

                val hint = getString(R.styleable.SuggestionSearchView_hint) ?: ""
                setHint(hint)

                val clearButtonIconResId = getResourceId(R.styleable.SuggestionSearchView_clearButtonIcon, R.drawable.ic_action_cancel)
                setClearButtonIconResource(clearButtonIconResId)

                val closeButtonIconResId = getResourceId(R.styleable.SuggestionSearchView_closeButtonIcon, R.drawable.ic_action_back)
                setCloseButtonIconResource(closeButtonIconResId)
            }

            typedArray.recycle()
        }
    }

    private fun initCloseButton() {
        closeButton.setOnClickListener {
            hide()
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
                    showSuggestions()
                }
                false
            }
        }
    }

    private fun initClearButton() {
        clearButton.setOnClickListener {
            inputEditText.setText("")
            showSuggestions()
        }
    }

    private fun initBackgroundView() {
        backgroundView.setOnClickListener {
            hide()
        }
    }

    private fun initDefaultAdapter() {
        setAdapter(DefaultSuggestionAdapter())
    }

    fun show() {
        backgroundView.visible()
        root.visible()
        showSuggestions()
        onStateChangeListener?.onChange(State.OPEN)
    }

    fun hide() {
        backgroundView.gone()
        root.gone()
        inputEditText.setText("")
        hideSuggestions()
        onStateChangeListener?.onChange(State.CLOSE)
    }

    fun showSuggestions() {
        backgroundView.visible()
        inputEditText.requestFocus()
        suggestionViewContainer.visible()
    }

    fun hideSuggestions() {
        backgroundView.gone()
        inputEditText.clearFocus()
        suggestionViewContainer.gone()
    }

    fun setText(text: String?) {
        text?.let { inputtedText ->
            val beforeText = inputEditText.text.toString()
            if(beforeText != inputtedText) {
                inputEditText.setText(inputtedText)
            }
        }
    }

    fun setRadius(pixel: Float) {
        suggestionSearchViewContainer.radius = pixel.toDP()
    }

    fun setMargin(pixel: Float) {
        with(pixel.toInt()) {
            val layoutParams = (suggestionSearchViewContainer.layoutParams as ViewGroup.MarginLayoutParams)
            layoutParams.setMargins(this, this, this, this)
            suggestionSearchViewContainer.layoutParams = layoutParams
        }
    }

    fun setSearchViewBackgroundColor(color: Int) {
        searchViewContainer.setBackgroundColor(color)
    }

    fun setTextSize(pixel: Float) {
        inputEditText.textSize = pixel.toDP()
    }

    fun setTextColor(color: Int) {
        inputEditText.setTextColor(color)
    }

    fun setHint(hint: String) {
        inputEditText.hint = hint
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

    fun setOnSuggestionItemClickListener(onSuggestionItemClickListener: OnSuggestionItemClickListener?) {
        this.onSuggestionItemClickListener = onSuggestionItemClickListener
    }

    fun setOnSuggestionDeleteClickListener(onSuggestionSubButtonClickListener: OnSuggestionSubButtonClickListener?) {
        this.onSuggestionSubButtonClickListener = onSuggestionSubButtonClickListener
    }

    fun setOnSuggestionFooterClickListener(onSuggestionFooterClickListener: OnSuggestionFooterClickListener?) {
        this.onSuggestionFooterClickListener = onSuggestionFooterClickListener
    }

    fun setAdapter(adapter: SuggestionAdapter<*, *>) {
        suggestionRecyclerView.adapter = adapter.apply {
            onSuggestionItemClick = { text, position ->
                onSuggestionItemClickListener?.onClick(text, position)
                inputEditText.setText(text)
                hideSuggestions()
            }
            onSuggestionSubButtonClick = { text, position ->
                onSuggestionSubButtonClickListener?.onClick(text, position)
            }
            onSuggestionFooterClick = {
                onSuggestionFooterClickListener?.onClick()
            }
        }
    }

    fun getAdapter(): SuggestionAdapter<*, *>? {
        return suggestionRecyclerView.adapter as SuggestionAdapter<*, *>
    }

    private fun refreshClearButtonVisibility(textLength: Int) {
        if(0 < textLength) {
            clearButton.visible()
        } else {
            clearButton.gone()
        }
    }

    private fun confirmSearch() {
        hideSuggestions()
        onSearchButtonClickListener?.onClick(inputEditText.text.toString())
    }

    override fun onSaveInstanceState(): Parcelable? {
        return SavedState(super.onSaveInstanceState()).apply {
            suggestionSearchViewContainerVisibility = root.visibility
            suggestionViewContainer.visibility = suggestionViewContainer.visibility
        }
    }

    override fun onRestoreInstanceState(state: Parcelable?) {
        super.onRestoreInstanceState(state)
        if(state is SavedState) {
            root.visibility = state.suggestionSearchViewContainerVisibility
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
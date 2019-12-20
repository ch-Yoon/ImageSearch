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
import androidx.appcompat.widget.LinearLayoutCompat
import androidx.cardview.widget.CardView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
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
    private val searchViewDivisionLine: View
    private val suggestionViewContainer: LinearLayoutCompat
    private val suggestionRecyclerView: RecyclerView

    private var searchViewText: String? = ""
    private var radius: Float = -1f
    private var margin: Float = -1f
    private var searchViewBackgroundColor: Int = -1
    private var textSize: Float = -1f
    private var textColor: Int = -1
    private var hint: String = ""
    private var clearButtonIconResId: Int = -1
    private var closeButtonResId: Int = -1
    private var searchViewDivisionLineColor: Int = -1
    private var suggestionBackgroundColor: Int = -1

    var onTextChangeListener: OnTextChangeListener? = null
    var onSearchButtonClickListener: OnSearchButtonClickListener? = null
    var onStateChangeListener: OnStateChangeListener? = null

    init {
        inflate(context, R.layout.suggestion_search_view, this)

        root = this
        backgroundView = background_view
        suggestionSearchViewContainer = suggestion_search_view_container
        searchViewContainer = search_view_container
        closeButton = close_button
        inputEditText = input_edit_text
        clearButton = clear_button
        searchViewDivisionLine = search_view_division_line
        suggestionViewContainer = suggestion_view_container
        suggestionRecyclerView = suggestion_recyclerview

        initCloseButton()
        initInputEditText()
        initClearButton()
        initBackgroundView()
        initAttrs(attrs, defStyle)
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

    private fun initAttrs(attrs: AttributeSet?, defStyle: Int) {
        attrs?.let { attributeSet ->
            val typedArray = context.obtainStyledAttributes(attributeSet, R.styleable.SuggestionSearchView, defStyle, 0)

            with(typedArray) {
                val radius = getDimension(R.styleable.SuggestionSearchView_radius, 0f)
                setRadius(radius)

                val margin = getDimension(R.styleable.SuggestionSearchView_margin, 0f)
                setMargin(margin)

                val searchViewBackgroundColor = getColor(R.styleable.SuggestionSearchView_searchViewBackgroundColor, -1)
                setSearchViewBackgroundColor(searchViewBackgroundColor)

                val text = getString(R.styleable.SuggestionSearchView_text) ?: ""
                setText(text)

                val textSize = getDimension(R.styleable.SuggestionSearchView_textSize, -1f)
                setTextSize(textSize)

                val textColor = getColor(R.styleable.SuggestionSearchView_textColor, -1)
                setTextColor(textColor)

                val hint = getString(R.styleable.SuggestionSearchView_hint) ?: ""
                setHint(hint)

                val clearButtonIconResId = getResourceId(R.styleable.SuggestionSearchView_clearButtonIcon, -1)
                setClearButtonIconResource(clearButtonIconResId)

                val closeButtonIconResId = getResourceId(R.styleable.SuggestionSearchView_closeButtonIcon, -1)
                setCloseButtonIconResource(closeButtonIconResId)

                val searchViewDivisionLineColor = getColor(R.styleable.SuggestionSearchView_searchViewDivisionLineColor, -1)
                setSearchViewDivisionLineColor(searchViewDivisionLineColor)

                val suggestionBackgroundColor = getColor(R.styleable.SuggestionSearchView_suggestionBackgroundColor, -1)
                setSuggestionBackgroundColor(suggestionBackgroundColor)
            }

            typedArray.recycle()
        }
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
        searchViewText = text
        val beforeText = inputEditText.text.toString()
        if(text != beforeText) {
            inputEditText.setText(text)
        }
    }

    fun setRadius(pixel: Float) {
        radius = pixel
        suggestionSearchViewContainer.radius = radius.toDP()
    }

    fun setMargin(pixel: Float) {
        margin = pixel
        with(margin.toInt()) {
            val layoutParams = (suggestionSearchViewContainer.layoutParams as ViewGroup.MarginLayoutParams)
            layoutParams.setMargins(this, this, this, this)
            suggestionSearchViewContainer.layoutParams = layoutParams
        }
    }

    fun setSearchViewBackgroundColor(color: Int) {
        searchViewBackgroundColor = if(color == -1) {
            ContextCompat.getColor(context, R.color.colorLightGray)
        } else {
            color
        }
        searchViewContainer.setBackgroundColor(searchViewBackgroundColor)
    }

    fun setTextSize(pixel: Float) {
        textSize = if(pixel == -1f) {
            resources.getDimension(R.dimen.suggestion_search_input_edit_text_default_text_size)
        } else {
            pixel
        }
        inputEditText.textSize = textSize.toDP()
    }

    fun setTextColor(color: Int) {
        textColor = if(color == -1) {
            ContextCompat.getColor(context, R.color.colorBlack)
        } else {
            color
        }
        inputEditText.setTextColor(textColor)
    }

    fun setHint(text: String) {
        hint = text
        inputEditText.hint = text
    }

    fun setClearButtonIconResource(drawableResId: Int) {
        clearButtonIconResId = if(drawableResId == -1) {
            R.drawable.ic_action_cancel
        } else {
            drawableResId
        }
        clearButton.setImageResource(clearButtonIconResId)
    }

    fun setCloseButtonIconResource(drawableResId: Int) {
        closeButtonResId = if(drawableResId == -1) {
            R.drawable.ic_action_back
        } else {
            drawableResId
        }
        closeButton.setImageResource(closeButtonResId)
    }

    fun setSearchViewDivisionLineColor(color: Int) {
        searchViewDivisionLineColor = if(color == -1) {
            ContextCompat.getColor(context, R.color.colorBasicGray)
        } else {
            color
        }
        searchViewDivisionLine.setBackgroundColor(searchViewDivisionLineColor)
    }

    fun setSuggestionBackgroundColor(color: Int) {
        suggestionBackgroundColor = if(color == -1) {
            ContextCompat.getColor(context, R.color.colorLightGray)
        } else {
            color
        }
        suggestionRecyclerView.setBackgroundColor(suggestionBackgroundColor)
    }

    fun setAdapter(adapter: ListAdapter<*, *>) {
        suggestionRecyclerView.adapter = adapter
    }

    fun getAdapter(): ListAdapter<*, *>? {
        return suggestionRecyclerView.adapter as? ListAdapter<*, *>
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
        val state = SavedState(super.onSaveInstanceState())
        state.searchViewText = searchViewText
        state.suggestionSearchViewContainerVisibility = root.visibility
        state.suggestionViewContainerVisibility = suggestionViewContainer.visibility
        state.radius = radius
        state.margin = margin
        state.searchViewBackgroundColor = searchViewBackgroundColor
        state.textSize = textSize
        state.textColor = textColor
        state.hint = hint
        state.clearButtonIconResId = clearButtonIconResId
        state.closeButtonResId = closeButtonResId
        state.searchViewDivisionLineColor = searchViewDivisionLineColor
        state.suggestionBackgroundColor = suggestionBackgroundColor
        return state
    }

    override fun onRestoreInstanceState(state: Parcelable?) {
        super.onRestoreInstanceState(state)
        if(state is SavedState) {
            if(state.suggestionSearchViewContainerVisibility == View.VISIBLE) {
                show()
            } else {
                hide()
            }

            if(state.suggestionViewContainerVisibility == View.VISIBLE) {
                showSuggestions()
            } else {
                hideSuggestions()
            }

            setRadius(state.radius)
            setMargin(state.margin)
            setSearchViewBackgroundColor(state.searchViewBackgroundColor)
            setTextSize(state.textSize)
            setTextColor(state.textColor)
            setHint(state.hint)
            setClearButtonIconResource(state.clearButtonIconResId)
            setCloseButtonIconResource(state.closeButtonResId)
            setSearchViewDivisionLineColor(state.searchViewDivisionLineColor)
            setSuggestionBackgroundColor(state.suggestionBackgroundColor)
        }
    }

    private class SavedState : BaseSavedState {

        var suggestionSearchViewContainerVisibility: Int = 0
        var suggestionViewContainerVisibility: Int = 0
        var searchViewText: String? = ""
        var radius: Float = -1f
        var margin: Float = -1f
        var searchViewBackgroundColor: Int = -1
        var textSize: Float = -1f
        var textColor: Int = -1
        var hint: String = ""
        var clearButtonIconResId: Int = -1
        var closeButtonResId: Int = -1
        var searchViewDivisionLineColor: Int = -1
        var suggestionBackgroundColor: Int = -1

        constructor(parcelable: Parcelable?) : super(parcelable)

        constructor(source: Parcel) : super(source) {
            with(source) {
                suggestionSearchViewContainerVisibility = readInt()
                suggestionViewContainerVisibility = readInt()
                searchViewText = readString() ?: ""
                radius = readFloat()
                margin = readFloat()
                searchViewBackgroundColor = readInt()
                textSize = readFloat()
                textColor = readInt()
                hint = readString() ?: ""
                clearButtonIconResId = readInt()
                closeButtonResId = readInt()
                searchViewDivisionLineColor = readInt()
                suggestionBackgroundColor = readInt()
            }
        }

        override fun describeContents() = 0

        override fun writeToParcel(dest: Parcel, flags: Int) {
            super.writeToParcel(dest, flags)
            with(dest) {
                writeInt(suggestionSearchViewContainerVisibility)
                writeInt(suggestionViewContainerVisibility)
                writeString(searchViewText ?: "")
                writeFloat(radius)
                writeFloat(margin)
                writeInt(searchViewBackgroundColor)
                writeFloat(textSize)
                writeInt(textColor)
                writeString(hint)
                writeInt(clearButtonIconResId)
                writeInt(closeButtonResId)
                writeInt(searchViewDivisionLineColor)
                writeInt(suggestionBackgroundColor)
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
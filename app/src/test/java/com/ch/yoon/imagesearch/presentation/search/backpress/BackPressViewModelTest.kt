package com.ch.yoon.imagesearch.presentation.search.backpress

import android.app.Application
import android.util.Log
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.belongings.bag.belongingsbag.RxSchedulerRule
import com.ch.yoon.imagesearch.R
import io.mockk.MockKAnnotations
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.mockkStatic
import junit.framework.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test

/**
 * Creator : ch-yoon
 * Date : 2019-12-21
 */
class BackPressViewModelTest {

    companion object {
        private const val BACK_PRESS_GUIDE_MESSAGE = "뒤로 버튼을 한번 더 누르면 종료됩니다"
    }

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()
    @get:Rule
    val rxSchedulerRule = RxSchedulerRule()

    @MockK
    lateinit var mockApplication: Application

    private lateinit var backPressViewModel: BackPressViewModel

    @Before
    fun init() {
        MockKAnnotations.init(this, relaxUnitFun = true)
        initApplication()
        initUtils()
        initBackPressViewModel()
    }

    private fun initApplication() {
        every { mockApplication.getString(R.string.back_press_guide_message) } returns BACK_PRESS_GUIDE_MESSAGE
    }

    private fun initUtils() {
        mockkStatic(Log::class)
        every { Log.d(any(), any()) } returns -1
    }

    private fun initBackPressViewModel() {
        backPressViewModel = BackPressViewModel(mockApplication)
    }

    @Test
    fun `뒤로가기 버튼 한번 클릭 시 가이드 메시지 이벤트가 호출되는지 테스트`() {
        // when
        backPressViewModel.onBackPress()

        // then
        var guideMessage: String? = null
        backPressViewModel.showMessageEvent.observeForever { guideMessage = it }
        assertEquals(BACK_PRESS_GUIDE_MESSAGE, guideMessage)
    }

    @Test
    fun `뒤로가기 버튼 연속 두번 클릭 시 종료 이벤트가 호출되는지 테스트`() {
        // when
        backPressViewModel.onBackPress()
        backPressViewModel.onBackPress()

        // then
        var finishCount = 0
        backPressViewModel.finishEvent.observeForever { finishCount++ }
        assertEquals(1, finishCount)
    }
}
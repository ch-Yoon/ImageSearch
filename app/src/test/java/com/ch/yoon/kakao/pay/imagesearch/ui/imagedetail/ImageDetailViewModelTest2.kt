package com.ch.yoon.kakao.pay.imagesearch.ui.imagedetail

import android.app.Application
import android.util.Log
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.belongings.bag.belongingsbag.RxSchedulerRule
import com.ch.yoon.kakao.pay.imagesearch.R
import io.mockk.MockKAnnotations
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.mockkStatic
import org.junit.Before
import org.junit.Rule
import org.mockito.Mock
import org.mockito.MockitoAnnotations
import android.text.TextUtils
import org.mockito.Mockito.`when`
import android.R



/**
 * Creator : ch-yoon
 * Date : 2019-11-02.
 */
class ImageDetailViewModelTest2 {

    companion object {
        private const val UNKNOWN_ERROR_MESSAGE = "알 수 없는 에러가 발생했습니다"
        private const val NONEXISTENT_URL_MESSAGE = "연결되는 사이트가 없습니다"
    }

    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()
    @get:Rule
    val rxSchedulerRule = RxSchedulerRule()

    @MockK
    private lateinit var mockApplication: Application

    private lateinit var imageDetailViewModel: ImageDetailViewModel

    @Before
    fun init() {
        MockKAnnotations.init(this, relaxUnitFun = true)

        initApplication()
        initImageDetailViewModel()
        initUtils()
    }

    private fun initApplication() {
        every { mockApplication.getString(com.ch.yoon.kakao.pay.imagesearch.R.string.unknown_error)) } returns (UNKNOWN_ERROR_MESSAGE)
        every { mockApplication.getString(com.ch.yoon.kakao.pay.imagesearch.R.string.non_existent_url_error)) } returns (NONEXISTENT_URL_MESSAGE)
    }

    private fun initImageDetailViewModel() {
        imageDetailViewModel = ImageDetailViewModel(mockApplication)
    }

    private fun initUtils() {
        mockkStatic(Log::class)
        every { Log.d(any(), any()) } returns -1

    }
}
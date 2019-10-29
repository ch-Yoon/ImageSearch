package com.ch.yoon.kakao.pay.imagesearch.ui.imagedetail;

import android.app.Application;
import android.util.Log;

import androidx.arch.core.executor.testing.InstantTaskExecutorRule;
import androidx.lifecycle.Observer;

import com.ch.yoon.kakao.pay.imagesearch.R;
import com.ch.yoon.kakao.pay.imagesearch.RxSchedulerRule;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.powermock.api.mockito.PowerMockito.mockStatic;

/**
 * Creator : ch-yoon
 * Date : 2019-08-13.
 */
@RunWith(PowerMockRunner.class)
@PrepareForTest(Log.class)
public class ImageDetailViewModelTest {

    private static final String UNKNOWN_ERROR_MESSAGE = "알 수 없는 에러가 발생했습니다";
    private static final String NONEXISTENT_URL_MESSAGE = "연결되는 사이트가 없습니다";
    @Rule
    public InstantTaskExecutorRule instantTaskExecutorRule = new InstantTaskExecutorRule();
    @Rule
    public RxSchedulerRule rxSchedulerRule = new RxSchedulerRule();

    @Mock
    private Application mockApplication;
    @Mock
    private Observer<Void> mockVoidObserver;
    @Mock
    private Observer<String> mockStringObserver;

    private ImageDetailViewModel imageDetailViewModel;

    @Before
    public void init() {
        MockitoAnnotations.initMocks(this);

        initApplication();
        initImageDetailViewModel();
        initUtils();
    }

    private void initApplication() {
        when(mockApplication.getString(R.string.error_unknown_error)).thenReturn(UNKNOWN_ERROR_MESSAGE);
        when(mockApplication.getString(R.string.error_nonexistent_url)).thenReturn(NONEXISTENT_URL_MESSAGE);
    }

    private void initImageDetailViewModel() {
        imageDetailViewModel = new ImageDetailViewModel(mockApplication);
    }

    private void initUtils() {
        mockStatic(Log.class);
        when(Log.d(any(String.class), any(String.class))).thenReturn(0);
    }

    @Test
    public void 이미지_상세정보_값이_null일때_에러메시지가_반영되는지_테스트() {
        // when
        imageDetailViewModel.showImageDetailInfo(null);

        // then
        assertEquals(imageDetailViewModel.observeShowMessage().getValue(), UNKNOWN_ERROR_MESSAGE);
    }

    @Test
    public void 이미지_상세정보_null일때_종료_이벤트가_호출되는지_테스트() {
        // given
        imageDetailViewModel.observeFinishEvent().observeForever(mockVoidObserver);

        // when
        imageDetailViewModel.showImageDetailInfo(null);

        // then
        verify(mockVoidObserver, times(1)).onChanged(any());
    }

    @Test
    public void 이미지_상세정보의_imageUrl이_반영되는지_테스트() {
        // given
        ImageDocument imageDocument = createVirtualImageDocument(1);

        // when
        imageDetailViewModel.showImageDetailInfo(imageDocument);

        // then
        assertEquals(imageDocument.getImageUrl(), imageDetailViewModel.observeImageUrl().getValue());
    }

    @Test
    public void 뒤로가기_버튼_클릭시_종료_이벤트가_호출되는지_테스트() {
        // given
        imageDetailViewModel.observeFinishEvent().observeForever(mockVoidObserver);

        // when
        imageDetailViewModel.onClickBackPress();

        // then
        verify(mockVoidObserver, times(1)).onChanged(any());
    }

    @Test
    public void 웹_이동_버튼_클릭시_웹_이동_이벤트가_발생되는지_테스트() {
        // given
        ImageDocument imageDocument = createVirtualImageDocument(1);
        imageDetailViewModel.showImageDetailInfo(imageDocument);

        // when
        imageDetailViewModel.observeMoveWebEvent().observeForever(mockStringObserver);
        imageDetailViewModel.onClickWebButton();

        // then
        verify(mockStringObserver, times(1)).onChanged(imageDocument.getDocUrl());
    }

    @Test
    public void 웹_이동_버튼_클릭시_웹_주소가_없다면_에러메시지가_반영되는지_테스트() {
        // given
        ImageDocument imageDocument = createVirtualImageDocumentWithDocUrlNull(1);
        imageDetailViewModel.showImageDetailInfo(imageDocument);

        imageDetailViewModel.observeShowMessage().observeForever(mockStringObserver);

        // when
        imageDetailViewModel.showImageDetailInfo(imageDocument);
        imageDetailViewModel.onClickWebButton();

        // then
        verify(mockStringObserver, times(1)).onChanged(NONEXISTENT_URL_MESSAGE);
    }

    private ImageDocument createVirtualImageDocument(int id) {
        return new ImageDocument(
            "collection" + id,
            "thumbnailUrl" + id,
            "imageUrl" + id,
            id,
            id,
            "displaySiteName" + id,
            "docUrl" + id,
            "dateTime" + id
        );
    }

    private ImageDocument createVirtualImageDocumentWithDocUrlNull(int id) {
        return new ImageDocument(
            "collection" + id,
            "thumbnailUrl" + id,
            "imageUrl" + id,
            id,
            id,
            "displaySiteName" + id,
            null,
            "dateTime" + id
        );
    }

}

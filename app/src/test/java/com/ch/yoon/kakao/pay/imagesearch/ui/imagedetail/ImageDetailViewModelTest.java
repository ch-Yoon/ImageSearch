package com.ch.yoon.kakao.pay.imagesearch.ui.imagedetail;

import android.app.Application;
import android.util.Log;

import androidx.arch.core.executor.testing.InstantTaskExecutorRule;
import androidx.lifecycle.Observer;

import com.ch.yoon.kakao.pay.imagesearch.R;
import com.ch.yoon.kakao.pay.imagesearch.RxSchedulerRule;
import com.ch.yoon.kakao.pay.imagesearch.repository.ImageRepository;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import io.reactivex.Single;

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
    private ImageRepository mockImageRepository;
    @Mock
    private Observer<Void> mockVoidObserver;
    @Mock
    private Observer<String> mockStringObserver;

    private ImageDetailViewModel imageDetailViewModel;

//    @Before
//    public void init() {
//        MockitoAnnotations.initMocks(this);
//
//        initApplication();
//        initImageDetailViewModel();
//        initUtils();
//    }
//
//    private void initApplication() {
//        when(mockApplication.getString(R.string.error_unknown_error)).thenReturn(UNKNOWN_ERROR_MESSAGE);
//        when(mockApplication.getString(R.string.error_nonexistent_url)).thenReturn(NONEXISTENT_URL_MESSAGE);
//    }
//
//    private void initImageDetailViewModel() {
//        imageDetailViewModel = new ImageDetailViewModel(mockApplication, mockImageRepository);
//    }
//
//    private void initUtils() {
//        mockStatic(Log.class);
//        when(Log.d(any(String.class), any(String.class))).thenReturn(0);
//    }
//
//    @Test
//    public void id값이_null일때_에러메시지가_반영되는지_테스트() {
//        // when
//        imageDetailViewModel.showImageDetailInfo(null);
//
//        // then
//        assertEquals(imageDetailViewModel.observeShowMessage().getValue(), UNKNOWN_ERROR_MESSAGE);
//    }
//
//    @Test
//    public void id값이_null일때_종료_이벤트가_호출되는지_테스트() {
//        // given
//        imageDetailViewModel.observeFinishEvent().observeForever(mockVoidObserver);
//
//        // when
//        imageDetailViewModel.showImageDetailInfo(null);
//
//        // then
//        verify(mockVoidObserver, times(1)).onChanged(any());
//    }
//
//    @Test
//    public void 뒤로가기_버튼_클릭시_종료_이벤트가_호출되는지_테스트() {
//        // given
//        imageDetailViewModel.observeFinishEvent().observeForever(mockVoidObserver);
//
//        // when
//        imageDetailViewModel.onClickBackPress();
//
//        // then
//        verify(mockVoidObserver, times(1)).onChanged(any());
//    }
//
//    @Test
//    public void 수신한_id_값으로_레파지토리에_이미지_상세정보를_요청하는지_테스트() {
//        // given
//        DetailImageInfo detailImageInfo = createDetailImageInfo("테스트");
//        when(mockImageRepository.requestImageDetailInfo("테스트")).
//            thenReturn(Single.just(detailImageInfo));
//
//        // when
//        imageDetailViewModel.showImageDetailInfo("테스트");
//
//        // then
//        verify(mockImageRepository, times(1)).requestImageDetailInfo("테스트");
//    }
//
//    @Test
//    public void 레파지토리에_상세정보_요청했을때_에러발생시_에러메시지가_반영되는지_테스트() {
//        // given
//        when(mockImageRepository.requestImageDetailInfo("테스트")).
//            thenReturn(Single.error(new Throwable()));
//
//        // when
//        imageDetailViewModel.showImageDetailInfo("테스트");
//
//        // then
//        assertEquals(imageDetailViewModel.observeShowMessage().getValue(), UNKNOWN_ERROR_MESSAGE);
//    }
//
//    @Test
//    public void 레파지토리에_상세정보_요청했을때_에러발생시_종료_이벤트가_호출되는지_테스트() {
//        // given
//        when(mockImageRepository.requestImageDetailInfo("테스트")).
//            thenReturn(Single.error(new Throwable()));
//
//        imageDetailViewModel.observeFinishEvent().observeForever(mockVoidObserver);
//
//        // when
//        imageDetailViewModel.showImageDetailInfo("테스트");
//
//        // then
//        verify(mockVoidObserver, times(1)).onChanged(any());
//    }
//
//    @Test
//    public void 이미지_상세정보_수신시_imageUrl이_반영되는지_테스트() {
//        // given
//        DetailImageInfo detailImageInfo = createDetailImageInfo("테스트");
//        when(mockImageRepository.requestImageDetailInfo("테스트")).
//            thenReturn(Single.just(detailImageInfo));
//
//        // when
//        imageDetailViewModel.showImageDetailInfo("테스트");
//
//        // then
//        assertEquals(detailImageInfo.getImageUrl(), imageDetailViewModel.observeImageUrl().getValue());
//    }
//
//    @Test
//    public void 웹_이동_버튼_클릭시_웹_이동_이벤트가_발생되는지_테스트() {
//        // given
//        DetailImageInfo detailImageInfo = createDetailImageInfo("테스트");
//        when(mockImageRepository.requestImageDetailInfo("테스트")).
//            thenReturn(Single.just(detailImageInfo));
//
//        imageDetailViewModel.observeMoveWebEvent().observeForever(mockStringObserver);
//
//        // when
//        imageDetailViewModel.showImageDetailInfo("테스트");
//        imageDetailViewModel.onClickWebButton();
//
//        // then
//        verify(mockStringObserver, times(1)).onChanged(detailImageInfo.getDocUrl());
//    }
//
//    @Test
//    public void 웹_이동_버튼_클릭시_웹_주소가_없다면_에러메시지가_반영되는지_테스트() {
//        // given
//        DetailImageInfo detailImageInfo = createDetailImageInfoWithDocUrlNull("테스트");
//        when(mockImageRepository.requestImageDetailInfo("테스트")).
//            thenReturn(Single.just(detailImageInfo));
//
//        imageDetailViewModel.observeShowMessage().observeForever(mockStringObserver);
//
//        // when
//        imageDetailViewModel.showImageDetailInfo("테스트");
//        imageDetailViewModel.onClickWebButton();
//
//        // then
//        verify(mockStringObserver, times(1)).onChanged(NONEXISTENT_URL_MESSAGE);
//    }
//
//    @Test
//    public void 웹_이동_버튼_클릭시_상세_정보가_존재하지_않다면_에러메시지가_반영되는지_테스트() {
//        // given
//        imageDetailViewModel.observeShowMessage().observeForever(mockStringObserver);
//
//        // when
//        imageDetailViewModel.onClickWebButton();
//
//        // then
//        verify(mockStringObserver, times(1)).onChanged(UNKNOWN_ERROR_MESSAGE);
//    }
//
//    @Test
//    public void 웹_이동_버튼_클릭시_상세_정보가_존재하지_않다면_종료_이벤트가_발생되는지_테스트() {
//        // given
//        imageDetailViewModel.observeFinishEvent().observeForever(mockVoidObserver);
//
//        // when
//        imageDetailViewModel.onClickWebButton();
//
//        // then
//        verify(mockVoidObserver, times(1)).onChanged(any());
//    }
//
//   private DetailImageInfo createDetailImageInfo(String id) {
//        return new DetailImageInfo(
//            id + "-url",
//            id + "-siteName",
//            id + "-docUrl",
//            id + "-dateTime",
//            100,
//            200
//        );
//    }
//
//    private DetailImageInfo createDetailImageInfoWithDocUrlNull(String id) {
//        return new DetailImageInfo(
//            id + "-url",
//            id + "-siteName",
//            null,
//            id + "-dateTime",
//            100,
//            200
//        );
//    }

    /*

    @NonNull
    public LiveData<String> observeImageUrl() {
        return imageUrlLiveData;
    }

    @NonNull
    public LiveData<String> observeMoveWebEvent() {
        return docUrlLiveEvent;
    }

    @NonNull
    public LiveData<String> observeShowMessage() {
        return showMessageLiveEvent;
    }

    public void showImageDetailInfo(@NonNull String id) {
        registerDisposable(
            imageRepository.requestImageDetailInfo(id)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                    this::updateImageDetailInfo,
                    throwable -> Log.d(TAG, throwable.getMessage())
                )
        );
    }

    private void updateImageDetailInfo(DetailImageInfo detailImageInfo) {
        this.detailImageInfo = detailImageInfo;
        imageUrlLiveData.setValue(detailImageInfo.getImageUrl());
    }

    public void onClickWebButton() {
        if(detailImageInfo != null) {
            final String docUrl = detailImageInfo.getDocUrl();
            docUrlLiveEvent.setValue(docUrl);
        } else {
            docUrlLiveEvent.setValue(getString(R.string.error_unknown_error));
        }
    }
     */

}

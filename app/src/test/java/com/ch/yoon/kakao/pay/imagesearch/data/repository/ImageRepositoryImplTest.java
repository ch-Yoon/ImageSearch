package com.ch.yoon.kakao.pay.imagesearch.data.repository;

import com.ch.yoon.kakao.pay.imagesearch.RxSchedulerRule;
import com.ch.yoon.kakao.pay.imagesearch.data.local.room.ImageSearchLocalDataSource;
import com.ch.yoon.kakao.pay.imagesearch.data.local.room.entity.SearchLog;
import com.ch.yoon.kakao.pay.imagesearch.data.model.imagesearch.request.ImageSearchRequest;
import com.ch.yoon.kakao.pay.imagesearch.data.model.imagesearch.request.ImageSortType;
import com.ch.yoon.kakao.pay.imagesearch.data.model.imagesearch.response.ImageSearchResponse;
import com.ch.yoon.kakao.pay.imagesearch.data.model.imagesearch.response.SearchMetaInfo;
import com.ch.yoon.kakao.pay.imagesearch.data.remote.kakao.ImageRemoteDataSource;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Completable;
import io.reactivex.Single;
import io.reactivex.disposables.CompositeDisposable;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class ImageRepositoryImplTest {

    @Rule
    public RxSchedulerRule rxSchedulerRule = new RxSchedulerRule();

    @Mock
    private ImageRemoteDataSource mockImageRemoteDataSource;
    @Mock
    private ImageSearchLocalDataSource mockImageLocalDataSource;

    private ImageRepository imageRepository;
    private CompositeDisposable compositeDisposable;

    @Before
    public void init() {
        MockitoAnnotations.initMocks(this);
        initImageRepository();
        initCompositeDisposable();
    }

    @After
    public void clear() {
        compositeDisposable.clear();
        ImageRepositoryImpl.destroyInstanceForTesting();
    }

    private void initImageRepository() {
        imageRepository = ImageRepositoryImpl.getInstance(mockImageLocalDataSource, mockImageRemoteDataSource);
    }

    private void initCompositeDisposable() {
        compositeDisposable = new CompositeDisposable();
    }

    @Test
    public void 리모트_데이터소스에_이미지_요청을_전달하는지_테스트() {
        // given
        when(mockImageRemoteDataSource.requestImageList(any(ImageSearchRequest.class)))
            .thenReturn(Single.just(emptyImageSearchResponse()));

        // when
        imageRepository.requestImageList(emptyImageSearchRequest());

        // then
        verify(mockImageRemoteDataSource, times(1)).requestImageList(any(ImageSearchRequest.class));
    }

    @Test
    public void 로컬_데이터소스에_키워드_업데이트_요청을_전달하는지_테스트() {
        // given
        when(mockImageLocalDataSource.insertOrUpdateSearchLog("테스트"))
            .thenReturn(Single.just(emptySearchLog()));

        // when
        imageRepository.insertOrUpdateSearchLog("테스트");

        // then
        verify(mockImageLocalDataSource, times(1)).insertOrUpdateSearchLog("테스트");
    }

    @Test
    public void 로컬_데이터소스에_검색_목록_요청을_전달하는지_테스트() {
        // given
        when(mockImageLocalDataSource.selectAllSearchLog())
            .thenReturn(Single.just(emptySearchLogList()));

        // when
        imageRepository.requestSearchLogList();

        // then
        verify(mockImageLocalDataSource, times(1)).selectAllSearchLog();
    }

    @Test
    public void 로컬_데이터소스에_키워드_데이터_삭제_요청을_전달하는지_테스트() {
        // given
        when(mockImageLocalDataSource.deleteSearchLog("테스트"))
            .thenReturn(Completable.complete());

        // when
        imageRepository.deleteSearchLog("테스트");

        // then
        verify(mockImageLocalDataSource, times(1)).deleteSearchLog("테스트");
    }

    private ImageSearchResponse emptyImageSearchResponse() {
        SearchMetaInfo emptySearchMetaInfo = new SearchMetaInfo(false);
        return new ImageSearchResponse(emptySearchMetaInfo, new ArrayList<>());
    }

    private ImageSearchRequest emptyImageSearchRequest() {
        return new ImageSearchRequest("", ImageSortType.ACCURACY, 0, 0, true);
    }

    private List<SearchLog> emptySearchLogList() {
        return new ArrayList<>();
    }

    private SearchLog emptySearchLog() {
        return new SearchLog("", 0);
    }

}

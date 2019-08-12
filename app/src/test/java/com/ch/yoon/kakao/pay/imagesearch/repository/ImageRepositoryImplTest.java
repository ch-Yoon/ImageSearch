package com.ch.yoon.kakao.pay.imagesearch.repository;

import com.ch.yoon.kakao.pay.imagesearch.RxSchedulerRule;
import com.ch.yoon.kakao.pay.imagesearch.repository.local.room.ImageLocalDataSource;
import com.ch.yoon.kakao.pay.imagesearch.repository.local.room.entity.LocalImageDocument;
import com.ch.yoon.kakao.pay.imagesearch.repository.local.room.entity.SearchLog;
import com.ch.yoon.kakao.pay.imagesearch.repository.model.imagesearch.ImageInfoConverter;
import com.ch.yoon.kakao.pay.imagesearch.repository.model.imagesearch.request.ImageSearchRequest;
import com.ch.yoon.kakao.pay.imagesearch.repository.model.imagesearch.request.ImageSortType;
import com.ch.yoon.kakao.pay.imagesearch.repository.model.imagesearch.response.DetailImageInfo;
import com.ch.yoon.kakao.pay.imagesearch.repository.model.imagesearch.response.ImageSearchResult;
import com.ch.yoon.kakao.pay.imagesearch.repository.model.imagesearch.response.ResultMeta;
import com.ch.yoon.kakao.pay.imagesearch.repository.model.imagesearch.response.SimpleImageInfo;
import com.ch.yoon.kakao.pay.imagesearch.repository.model.imagesearch.response.error.ImageSearchError;
import com.ch.yoon.kakao.pay.imagesearch.repository.model.imagesearch.response.error.ImageSearchException;
import com.ch.yoon.kakao.pay.imagesearch.repository.remote.kakao.ImageRemoteDataSource;
import com.ch.yoon.kakao.pay.imagesearch.repository.remote.kakao.model.ImageDocument;
import com.ch.yoon.kakao.pay.imagesearch.repository.remote.kakao.model.ImageSearchResponse;
import com.ch.yoon.kakao.pay.imagesearch.repository.remote.kakao.model.SearchMetaInfo;
import com.ch.yoon.kakao.pay.imagesearch.utils.NetworkUtil;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Completable;
import io.reactivex.Single;
import io.reactivex.disposables.CompositeDisposable;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.powermock.api.mockito.PowerMockito.mockStatic;

@RunWith(PowerMockRunner.class)
@PrepareForTest({NetworkUtil.class, ImageInfoConverter.class})
public class ImageRepositoryImplTest {

    @Rule
    public RxSchedulerRule rxSchedulerRule = new RxSchedulerRule();

    @Mock
    private ImageRemoteDataSource mockImageRemoteDataSource;
    @Mock
    private ImageLocalDataSource mockImageLocalDataSource;

    private ImageRepository imageRepository;
    private CompositeDisposable compositeDisposable;

    @Before
    public void init() {
        MockitoAnnotations.initMocks(this);

        initImageRepository();
        initUtils();
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

    private void initUtils() {
        mockStatic(NetworkUtil.class);
        mockStatic(ImageInfoConverter.class);
    }

    private void initCompositeDisposable() {
        compositeDisposable = new CompositeDisposable();
    }

    @Test
    public void 로컬_데이터소스에_키워드_업데이트_요청을_전달하는지_테스트() {
        // given
        when(mockImageLocalDataSource.updateSearchLog("테스트"))
            .thenReturn(Single.just(emptySearchLog()));

        // when
        imageRepository.updateSearchLog("테스트");

        // then
        verify(mockImageLocalDataSource, times(1)).updateSearchLog("테스트");
    }

    @Test
    public void 로컬_데이터소스에_검색_목록_요청을_전달하는지_테스트() {
        // given
        when(mockImageLocalDataSource.getSearchLogList())
            .thenReturn(Single.just(emptySearchLogList()));

        // when
        imageRepository.requestSearchLogList();

        // then
        verify(mockImageLocalDataSource, times(1)).getSearchLogList();
    }

    @Test
    public void 로컬_데이터소스에_키워드_데이터_삭제_요청을_전달하는지_테스트() {
        // given
        when(mockImageLocalDataSource.deleteAllByKeyword("테스트"))
            .thenReturn(Completable.complete());

        // when
        imageRepository.deleteAllByKeyword("테스트");

        // then
        verify(mockImageLocalDataSource, times(1)).deleteAllByKeyword("테스트");
    }

    @Test
    public void 로컬_데이터소스에_이미지_상세_정보_요청을_전달하는지_테스트() {
        // given
        when(mockImageLocalDataSource.getImageDetailInfo("id"))
            .thenReturn(Single.just(emptyDetailImageInfo()));

        // when
        imageRepository.requestImageDetailInfo("id");

        // then
        verify(mockImageLocalDataSource, times(1)).getImageDetailInfo("id");
    }

    @Test
    public void 네트워크가_연결되어있다면_리모트_데이터소스에_이미지_목록_요청을_전달하는지_테스트() {
        // given
        when(NetworkUtil.isNetworkConnecting()).thenReturn(true);

        when(mockImageRemoteDataSource.requestImageList(any(ImageSearchRequest.class)))
                .thenReturn(Single.just(emptyImageSearchResponse()));

        // when
        imageRepository.requestImageList(emptyImageSearchRequest());

        // then
        verify(mockImageRemoteDataSource, times(1)).requestImageList(any(ImageSearchRequest.class));
    }

    @Test
    public void 네트워크가_연결되어있다면_수신한_이미지_목록을_로컬_데이터소스에_캐싱요청하는지_테스트() {
        // given
        when(NetworkUtil.isNetworkConnecting()).thenReturn(true);

        when(mockImageRemoteDataSource.requestImageList(any(ImageSearchRequest.class)))
                .thenReturn(Single.just(emptyImageSearchResponse()));

        when(ImageInfoConverter.toLocalImageDocumentList(any(ImageSearchRequest.class), any()))
                .thenReturn(emptyLocalImageDocumentList());

        when(ImageInfoConverter.toImageSearchResult(any(ImageSearchRequest.class), any(SearchMetaInfo.class), any()))
                .thenReturn(emptyImageSearchResult());

        // when
        compositeDisposable.add(
            imageRepository.requestImageList(emptyImageSearchRequest()).subscribe()
        );

        // then
        verify(mockImageLocalDataSource, times(1))
            .saveLocalImageDocumentList(any());
    }

    @Test
    public void 네트워크가_연결되어있지_않다면_로컬_데이터소스에_이미지목록_요청을_하는지_테스트() {
        // given
        when(NetworkUtil.isNetworkConnecting()).thenReturn(false);

        when(mockImageLocalDataSource.getImageSearchList(any(ImageSearchRequest.class)))
            .thenReturn(Single.just(emptySimpleImageInfoList()));

        // when
        imageRepository.requestImageList(emptyImageSearchRequest());

        // then
        verify(mockImageLocalDataSource, times(1)).getImageSearchList(any(ImageSearchRequest.class));
    }

    @Test
    public void 네트워크가_연결되어있지_않을때_로컬데이터소스로부터_수신한_리스트_사이즈가_0이_아니면_정상_return_테스트() {
        // given
        ImageSearchResult notEmptyResult = notEmptyImageSearchResult();

        when(NetworkUtil.isNetworkConnecting()).thenReturn(false);

        when(mockImageLocalDataSource.getImageSearchList(any(ImageSearchRequest.class)))
            .thenReturn(Single.just(notEmptySimpleImageInfoList()));

        when(ImageInfoConverter.toImageSearchResult(any(ImageSearchRequest.class), any()))
            .thenReturn(notEmptyResult);

        // when && then
        compositeDisposable.add(
            imageRepository.requestImageList(emptyImageSearchRequest())
                .subscribe(
                    imageSearchResult -> assertEquals(imageSearchResult, notEmptyResult),
                    throwable -> fail()
                )
        );
    }

    @Test
    public void 네트워크가_연결되어있지_않을때_로컬데이터소스로부터_수신한_리스트_사이즈가_0이면_네트워크_에러_발생_테스트() {
        // given
        when(NetworkUtil.isNetworkConnecting()).thenReturn(false);

        when(mockImageLocalDataSource.getImageSearchList(any(ImageSearchRequest.class)))
            .thenReturn(Single.just(emptySimpleImageInfoList()));

        when(ImageInfoConverter.toImageSearchResult(any(ImageSearchRequest.class), any()))
            .thenReturn(emptyImageSearchResult());

        // when && then
        compositeDisposable.add(
            imageRepository.requestImageList(emptyImageSearchRequest())
                .subscribe(
                    imageSearchResult -> fail(),
                    throwable -> {
                        assertTrue(throwable instanceof ImageSearchException);

                        ImageSearchException exception = (ImageSearchException)throwable;
                        assertEquals(exception.getImageSearchError(), ImageSearchError.NETWORK_NOT_CONNECTING_ERROR);
                    }
                )
        );
    }

    private List<LocalImageDocument> emptyLocalImageDocumentList() {
        return new ArrayList<>();
    }

    private ImageSearchResult emptyImageSearchResult() {
        ResultMeta emptyResultMeta = new ResultMeta(false, "", ImageSortType.ACCURACY);
        List<SimpleImageInfo> emptyList = new ArrayList<>();
        return new ImageSearchResult(emptyResultMeta, emptyList);
    }

    private ImageSearchResult notEmptyImageSearchResult() {
        ResultMeta emptyResultMeta = new ResultMeta(false, "", ImageSortType.ACCURACY);
        List<SimpleImageInfo> imageInfoList = new ArrayList<>();
        imageInfoList.add(new SimpleImageInfo("", ""));
        return new ImageSearchResult(emptyResultMeta, imageInfoList);
    }

    private ImageSearchRequest emptyImageSearchRequest() {
        return new ImageSearchRequest("", ImageSortType.ACCURACY, 0, 0);
    }

    private ImageSearchResponse emptyImageSearchResponse() {
        SearchMetaInfo emptySearchMetaInfo = new SearchMetaInfo(true);
        List<ImageDocument> emptyDocumentList = new ArrayList<>();
        return new ImageSearchResponse(emptySearchMetaInfo, emptyDocumentList);
    }

    private List<SearchLog> emptySearchLogList() {
        return new ArrayList<>();
    }

    private SearchLog emptySearchLog() {
        return new SearchLog("", 0);
    }

    private DetailImageInfo emptyDetailImageInfo() {
        return new DetailImageInfo("", "", "", "", 0, 0);
    }

    private List<SimpleImageInfo> emptySimpleImageInfoList() {
        return new ArrayList<>();
    }

    private List<SimpleImageInfo> notEmptySimpleImageInfoList() {
        List<SimpleImageInfo> simpleImageInfoList = new ArrayList<>();
        simpleImageInfoList.add(new SimpleImageInfo("", ""));

        return simpleImageInfoList;
    }

}

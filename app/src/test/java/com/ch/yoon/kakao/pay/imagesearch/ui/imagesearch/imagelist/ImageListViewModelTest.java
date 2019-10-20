package com.ch.yoon.kakao.pay.imagesearch.ui.imagesearch.imagelist;



import android.app.Application;
import android.util.Log;

import androidx.arch.core.executor.testing.InstantTaskExecutorRule;

import com.ch.yoon.kakao.pay.imagesearch.R;
import com.ch.yoon.kakao.pay.imagesearch.RxSchedulerRule;
import com.ch.yoon.kakao.pay.imagesearch.data.repository.ImageRepository;
import com.ch.yoon.kakao.pay.imagesearch.data.model.imagesearch.request.ImageSearchRequest;
import com.ch.yoon.kakao.pay.imagesearch.data.model.imagesearch.response.ImageDocument;
import com.ch.yoon.kakao.pay.imagesearch.data.model.imagesearch.response.ImageSearchResponse;
import com.ch.yoon.kakao.pay.imagesearch.data.model.imagesearch.response.SearchMetaInfo;
import com.ch.yoon.kakao.pay.imagesearch.data.model.imagesearch.response.error.ImageSearchError;
import com.ch.yoon.kakao.pay.imagesearch.data.model.imagesearch.response.error.ImageSearchException;
import com.ch.yoon.kakao.pay.imagesearch.ui.imagesearch.imagelist.helper.ImageSearchInspector;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.stubbing.Answer;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Single;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.powermock.api.mockito.PowerMockito.mockStatic;

/**
 * Creator : ch-yoon
 * Date : 2019-08-12.
 */
@RunWith(PowerMockRunner.class)
@PrepareForTest(Log.class)
public class ImageListViewModelTest {

    private static final int START_PAGE_NUMBER = 1;
    private static final int LAST_PAGE_NUMBER = 5;
    private static final int REQUIRED_DATA_SIZE = 10;
    private static final int PRELOAD_ALLOW_LINE_MUTLPLE = 3;

    private static final String SUCCESS_NO_RESULT = "검색 결과가 없습니다";
    private static final String SUCCESS_LAST_DATA = "마지막 검색 페이지입니다";
    private static final String ERROR_WRONG_REQUEST = "잘못된 요청입니다";
    private static final String ERROR_AUTHENTICATION_ERROR = "카카오 서버 인증 실패로 인해 요청에 실패했습니다";
    private static final String ERROR_PERMISSION_ERROR = "카카오 서버 권한 오류로 인해 요청에 실패했습니다";
    private static final String ERROR_SERVER_ERROR = "카카오 서버에서 에러가 발생했습니다";
    private static final String ERROR_NETWORK = "인터넷 연결을 확인해주세요";
    private static final String ERROR_UNKNOWN_ERROR = "알 수 없는 에러가 발생했습니다";

    @Rule
    public InstantTaskExecutorRule instantTaskExecutorRule = new InstantTaskExecutorRule();
    @Rule
    public RxSchedulerRule rxSchedulerRule = new RxSchedulerRule();

    @Mock
    private Application mockApplication;
    @Mock
    private ImageRepository mockImageRepository;

    private ImageSearchInspector imageSearchInspector;
    private ImageListViewModel imageListViewModel;

    @Before
    public void init() {
        MockitoAnnotations.initMocks(this);

        initApplication();
        initImageSearchInspector();
        initImageListViewModel();
        initUtils();
    }

    private void initApplication() {
        when(mockApplication.getString(R.string.success_image_search_no_result)).thenReturn(SUCCESS_NO_RESULT);
        when(mockApplication.getString(R.string.success_image_search_last_data)).thenReturn(SUCCESS_LAST_DATA);
        when(mockApplication.getString(R.string.error_image_search_wrong_request)).thenReturn(ERROR_WRONG_REQUEST);
        when(mockApplication.getString(R.string.error_image_search_authentication_error)).thenReturn(ERROR_AUTHENTICATION_ERROR);
        when(mockApplication.getString(R.string.error_image_search_permission_error)).thenReturn(ERROR_PERMISSION_ERROR);
        when(mockApplication.getString(R.string.error_image_search_kakao_server_error)).thenReturn(ERROR_SERVER_ERROR);
        when(mockApplication.getString(R.string.error_network_not_connecting_error)).thenReturn(ERROR_NETWORK);
        when(mockApplication.getString(R.string.error_unknown_error)).thenReturn(ERROR_UNKNOWN_ERROR);
    }

    private void initImageSearchInspector() {
        imageSearchInspector = new ImageSearchInspector(
            START_PAGE_NUMBER,
            LAST_PAGE_NUMBER,
            REQUIRED_DATA_SIZE,
            PRELOAD_ALLOW_LINE_MUTLPLE
        );
    }

    private void initImageListViewModel() {
        imageListViewModel = new ImageListViewModel(
            mockApplication,
            mockImageRepository,
            imageSearchInspector
        );
    }

    private void initUtils() {
        mockStatic(Log.class);
        when(Log.d(any(String.class), any(String.class))).thenReturn(0);
    }

    @Test
    public void 한줄에_표현되는_아이템의_갯수를_변경하면_반영되는지_테스트() {
        // when && then
        imageListViewModel.changeCountOfItemInLine(2);
        assertEquals(new Integer(2), imageListViewModel.observeCountOfItemInLine().getValue());

        imageListViewModel.changeCountOfItemInLine(3);
        assertEquals(new Integer(3), imageListViewModel.observeCountOfItemInLine().getValue());

        imageListViewModel.changeCountOfItemInLine(4);
        assertEquals(new Integer(4), imageListViewModel.observeCountOfItemInLine().getValue());
    }

    @Test
    public void 최초_이미지_목록_요청시_수신한_이미지_목록이_반영되는지_테스트() {
        // given
        ArrayList<ImageDocument> expectedList = new ArrayList<>();
        when(mockImageRepository.requestImageList(any(ImageSearchRequest.class)))
            .thenAnswer((Answer<Single<ImageSearchResponse>>) invocation -> {
                ImageSearchRequest request = invocation.getArgument(0);
                ImageSearchResponse result = createVirtualImageSearchResult(request, false);
                expectedList.addAll(result.getImageDocumentList());
                return Single.fromCallable(() -> result);
            });

        // when
        imageListViewModel.loadImageList("테스트");

        // then
        List<ImageDocument> targetList = imageListViewModel.observeImageInfoList().getValue();
        assertEquals(targetList, expectedList);
    }

    @Test
    public void 최초_이미지_목록_요청시_수신한_리스트의_사이즈가_0이어도_반영되는지_테스트() {
        // given
        when(mockImageRepository.requestImageList(any(ImageSearchRequest.class)))
            .thenReturn(Single.just(createEmptyImageSearchResult()));

        // when
        imageListViewModel.loadImageList("테스트");

        // then
        List<ImageDocument> targetList = imageListViewModel.observeImageInfoList().getValue();
        assertEquals(targetList.size(), 0);
    }

    @Test
    public void 최초_이미지_목록_요청시_수신한_리스트의_사이즈가_0이라면_검색결과가_없다는_메시지가_반영되는지_테스트() {
        // given
        when(mockImageRepository.requestImageList(any(ImageSearchRequest.class)))
            .thenReturn(Single.just(createEmptyImageSearchResult()));

        // when
        imageListViewModel.loadImageList("테스트");

        // then
        String message = imageListViewModel.observeShowMessage().getValue();
        assertEquals(message, SUCCESS_NO_RESULT);
    }

    @Test
    public void 최초_이미지_목록_요청시_에러가_수신된다면_에러_메시지가_반영되는지_테스트() {
        // given
        when(mockImageRepository.requestImageList(any(ImageSearchRequest.class)))
            .thenAnswer((Answer<Single<ImageSearchResponse>>) invocation ->
                Single.create(emitter -> emitter.onError(new ImageSearchException("network error", ImageSearchError.NETWORK_NOT_CONNECTING_ERROR)))
            );

        // when
        imageListViewModel.loadImageList("테스트");

        // then
        String message = imageListViewModel.observeShowMessage().getValue();
        assertEquals(message, ERROR_NETWORK);
    }

    @Test
    public void 최초_이미지_목록_요청시_마지막_데이터라면_마지막을_뜻하는_메시지가_반영되는지_테스트() {
        // given
        when(mockImageRepository.requestImageList(any(ImageSearchRequest.class)))
            .thenAnswer((Answer<Single<ImageSearchResponse>>) invocation -> {
                ImageSearchRequest request = invocation.getArgument(0);
                ImageSearchResponse result = createVirtualImageSearchResult(request, true);
                return Single.fromCallable(() -> result);
            });

        // when
        imageListViewModel.loadImageList("테스트");

        // then
        String message = imageListViewModel.observeShowMessage().getValue();
        assertEquals(message, SUCCESS_LAST_DATA);
    }

    @Test
    public void 추가_이미지_목록_요청시_수신된_추가_이미지_목록이_반영되는지_테스트() {
        // given
        ArrayList<ImageDocument> expectedList = new ArrayList<>();
        when(mockImageRepository.requestImageList(any(ImageSearchRequest.class)))
            .thenAnswer((Answer<Single<ImageSearchResponse>>) invocation -> {
                ImageSearchRequest request = invocation.getArgument(0);
                ImageSearchResponse result = createVirtualImageSearchResult(request, false);
                expectedList.addAll(result.getImageDocumentList());
                return Single.fromCallable(() -> result);
            });

        // when
        imageListViewModel.loadImageList("테스트");
        int dataSize = imageListViewModel.observeImageInfoList().getValue().size();
        imageListViewModel.loadMoreImageListIfPossible(dataSize - 3);

        // then
        List<ImageDocument> targetList = imageListViewModel.observeImageInfoList().getValue();
        assertEquals(targetList, expectedList);
    }

    @Test
    public void 추가_이미지_목록_요청시_마지막_데이터였다면_레파지토리에_요청을_안하는지_테스트() {
        // given
        when(mockImageRepository.requestImageList(any(ImageSearchRequest.class)))
            .thenAnswer((Answer<Single<ImageSearchResponse>>) invocation -> {
                ImageSearchRequest request = invocation.getArgument(0);
                ImageSearchResponse result = createVirtualImageSearchResult(request, true);
                return Single.fromCallable(() -> result);
            });

        // when
        imageListViewModel.loadImageList("테스트");

        int dataSize = imageListViewModel.observeImageInfoList().getValue().size();
        imageListViewModel.loadMoreImageListIfPossible(dataSize - 1);
        imageListViewModel.loadMoreImageListIfPossible(dataSize - 1);
        imageListViewModel.loadMoreImageListIfPossible(dataSize - 1);

        // then
        verify(mockImageRepository, times(1))
            .requestImageList(any(ImageSearchRequest.class));
    }

    @Test
    public void 재시도_요청시_레파지토리에_요청을_하는지_테스트() {
        // given
        when(mockImageRepository.requestImageList(any(ImageSearchRequest.class)))
            .thenAnswer((Answer<Single<ImageSearchResponse>>) invocation -> {
                ImageSearchRequest request = invocation.getArgument(0);
                ImageSearchResponse result = createVirtualImageSearchResult(request, true);
                return Single.fromCallable(() -> result);
            });

        // when
        imageListViewModel.loadImageList("테스트");
        imageListViewModel.retryLoadMoreImageList();

        //then
        verify(mockImageRepository, times(2))
            .requestImageList(any(ImageSearchRequest.class));
    }

    private ImageSearchResponse createEmptyImageSearchResult() {
        ArrayList<ImageDocument> simpleImageInfoList = new ArrayList<>();
        SearchMetaInfo searchMetaInfo = new SearchMetaInfo(true);

        return new ImageSearchResponse(searchMetaInfo, simpleImageInfoList);
    }

    private ImageSearchResponse createVirtualImageSearchResult(ImageSearchRequest imageSearchRequest,
                                                               boolean isLastData) {
        int pageNumber = imageSearchRequest.getPageNumber();
        int requiredSize = imageSearchRequest.getRequiredSize();
        int startNumber = ((pageNumber-1) * requiredSize) + 1;

        int listSize = imageSearchRequest.getRequiredSize();
        ArrayList<ImageDocument> simpleImageInfoList = new ArrayList<>();
        for(int i=0; i<listSize; i++) {
            int itemNumber = startNumber + i;
            simpleImageInfoList.add(createVirtualImageDocument(itemNumber));
        }


        SearchMetaInfo searchMetaInfo = new SearchMetaInfo(isLastData);

        return new ImageSearchResponse(searchMetaInfo, simpleImageInfoList);
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

}

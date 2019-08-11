package com.ch.yoon.kakao.pay.imagesearch.ui.imagesearch.imagelist;



import android.app.Application;
import android.util.Log;

import androidx.arch.core.executor.testing.InstantTaskExecutorRule;

import com.ch.yoon.kakao.pay.imagesearch.R;
import com.ch.yoon.kakao.pay.imagesearch.RxSchedulerRule;
import com.ch.yoon.kakao.pay.imagesearch.repository.ImageRepository;
import com.ch.yoon.kakao.pay.imagesearch.repository.model.imagesearch.request.ImageSearchRequest;
import com.ch.yoon.kakao.pay.imagesearch.repository.model.imagesearch.request.ImageSortType;
import com.ch.yoon.kakao.pay.imagesearch.repository.model.imagesearch.response.ImageSearchResult;
import com.ch.yoon.kakao.pay.imagesearch.repository.model.imagesearch.response.RequestMeta;
import com.ch.yoon.kakao.pay.imagesearch.repository.model.imagesearch.response.SimpleImageInfo;
import com.ch.yoon.kakao.pay.imagesearch.repository.model.imagesearch.response.error.ImageSearchError;
import com.ch.yoon.kakao.pay.imagesearch.repository.model.imagesearch.response.error.ImageSearchException;
import com.ch.yoon.kakao.pay.imagesearch.ui.imagesearch.imagelist.helper.ImageSearchInspector;
import com.ch.yoon.kakao.pay.imagesearch.utils.NetworkUtil;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Single;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.powermock.api.mockito.PowerMockito.mockStatic;

/**
 * Creator : ch-yoon
 * Date : 2019-08-12.
 */
@RunWith(PowerMockRunner.class)
@PrepareForTest(Log.class)
public class ImageListViewModelTest {

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
        mockApplication = mock(Application.class);
        when(mockApplication.getApplicationContext()).thenReturn(mockApplication);
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
        imageSearchInspector = new ImageSearchInspector(1, 5, 10, 10);
    }

    private void initImageListViewModel() {
        imageListViewModel = new ImageListViewModel(mockApplication, mockImageRepository, imageSearchInspector);
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
        ArrayList<ImageSearchResult> searchResultList = new ArrayList<>();
        when(mockImageRepository.requestImageList(any(ImageSearchRequest.class)))
            .thenAnswer((Answer<Single<ImageSearchResult>>) invocation -> {
                ImageSearchRequest request = invocation.getArgument(0);
                ImageSearchResult result = createVirtualImageSearchResult(request, true);
                searchResultList.add(result);
                return Single.fromCallable(() -> result);
            });

        // when
        imageListViewModel.loadImageList("설현");

        // then
        List<SimpleImageInfo> expectedList = searchResultList.get(searchResultList.size() - 1).getSimpleImageInfoList();
        List<SimpleImageInfo> targetList = imageListViewModel.observeImageInfoList().getValue();
        assertTrue(isSame(targetList, expectedList));
    }

    @Test
    public void 최초_이미지_목록_요청시_수신한_리스트의_사이즈가_0이라면_0으로_반영되는지_테스트() {
        // given
        when(mockImageRepository.requestImageList(any(ImageSearchRequest.class)))
            .thenAnswer((Answer<Single<ImageSearchResult>>) invocation -> {
                ImageSearchRequest request = invocation.getArgument(0);
                ImageSearchResult result = createEmptyImageSearchResult(request);
                return Single.fromCallable(() -> result);
            });

        // when
        imageListViewModel.loadImageList("설현");

        // then
        List<SimpleImageInfo> targetList = imageListViewModel.observeImageInfoList().getValue();
        assertEquals(targetList.size(), 0);
    }

    @Test
    public void 최초_이미지_목록_요청시_수신한_리스트의_사이즈가_0이라면_검색결과가_없다는_메시지가_반영되는지_테스트() {
        // given
        when(mockImageRepository.requestImageList(any(ImageSearchRequest.class)))
            .thenAnswer((Answer<Single<ImageSearchResult>>) invocation -> {
                ImageSearchRequest request = invocation.getArgument(0);
                ImageSearchResult result = createEmptyImageSearchResult(request);
                return Single.fromCallable(() -> result);
            });

        // when
        imageListViewModel.loadImageList("설현");

        // then
        String message = imageListViewModel.observeShowMessage().getValue();
        assertEquals(message, SUCCESS_NO_RESULT);
    }

    @Test
    public void 최초_이미지_목록_요청시_네트워크가_끊겼다면_네트워크를_확인하라는_메시지가_반영되는지_테스트() {
        // given
        when(mockImageRepository.requestImageList(any(ImageSearchRequest.class)))
            .thenAnswer((Answer<Single<ImageSearchResult>>) invocation ->
                Single.create(emitter -> emitter.onError(new ImageSearchException("network error", ImageSearchError.NETWORK_NOT_CONNECTING_ERROR)))
            );

        // when
        imageListViewModel.loadImageList("설현");

        // then
        String message = imageListViewModel.observeShowMessage().getValue();
        assertEquals(message, ERROR_NETWORK);
    }

    private ImageSearchResult createEmptyImageSearchResult(ImageSearchRequest imageSearchRequest) {
        String keyword = imageSearchRequest.getKeyword();
        ImageSortType imageSortType = imageSearchRequest.getImageSortType();

        ArrayList<SimpleImageInfo> simpleImageInfoList = new ArrayList<>();
        RequestMeta requestMeta = new RequestMeta(true, keyword, imageSortType);

        return new ImageSearchResult(requestMeta, simpleImageInfoList);
    }

    private ImageSearchResult createVirtualImageSearchResult(ImageSearchRequest imageSearchRequest, boolean isLastData) {
        String keyword = imageSearchRequest.getKeyword();
        ImageSortType imageSortType = imageSearchRequest.getImageSortType();
        int pageNumber = imageSearchRequest.getPageNumber();
        int requiredSize = imageSearchRequest.getRequiredSize();
        int startNumber = ((pageNumber-1) * requiredSize) + 1;

        int listSize = imageSearchRequest.getRequiredSize();
        ArrayList<SimpleImageInfo> simpleImageInfoList = new ArrayList<>();
        for(int i=0; i<listSize; i++) {
            int itemNumber = startNumber + i;
            String id = keyword + imageSortType + itemNumber;
            String url = "url" + itemNumber;
            simpleImageInfoList.add(new SimpleImageInfo(id, url));
        }


        RequestMeta requestMeta = new RequestMeta(isLastData, keyword, imageSortType);

        return new ImageSearchResult(requestMeta, simpleImageInfoList);
    }

    private boolean isSame(List<SimpleImageInfo> base, List<SimpleImageInfo> target) {
        if(base == null || target == null) {
            return false;
        }

        if(base.size() != target.size()) {
            return false;
        }

        for(int i=0; i<base.size(); i++) {
            if(!base.get(i).equals(target.get(i))) {
                return false;
            }
        }

        return true;
    }

    /**
     *
     *     @NonNull
     *     public LiveData<List<SimpleImageInfo>> observeImageInfoList() {
     *         return imageInfoListLiveData;
     *     }
     *
     *     @NonNull
     *     public LiveData<String> observeShowMessage() {
     *         return showMessageLiveEvent;
     *     }
     *
     *     @NonNull
     *     public LiveData<ImageSearchState> observeImageSearchState() {
     *         return imageSearchStateLiveData;
     *     }
     *
     *     public void loadImageList(@NonNull String keyword) {
     *         imageInfoListLiveData.setValue(null);
     *         imageSearchInspector.submitFirstImageSearchRequest(keyword, DEFAULT_IMAGE_SORT_TYPE);
     *     }
     *
     *     public void retryLoadMoreImageList() {
     *         imageSearchInspector.submitRetryRequest(DEFAULT_IMAGE_SORT_TYPE);
     *     }
     *
     *     public void loadMoreImageListIfPossible(int displayPosition) {
     *         if(remainingMoreData()) {
     *             final List<SimpleImageInfo> imageDocumentList = imageInfoListLiveData.getValue();
     *             if (imageDocumentList != null) {
     *                 imageSearchInspector.submitPreloadRequest(
     *                     displayPosition,
     *                     imageDocumentList.size(),
     *                     DEFAULT_IMAGE_SORT_TYPE,
     *                     getCountOfItemInLine()
     *                 );
     *             }
     *         }
     *     }
     */
}

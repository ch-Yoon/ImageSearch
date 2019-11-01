//package com.ch.yoon.kakao.pay.imagesearch.ui.imagesearch.imagelist;
//
//
//
//import android.app.Application;
//import android.util.Log;
//
//import androidx.arch.core.executor.testing.InstantTaskExecutorRule;
//
//import com.ch.yoon.kakao.pay.imagesearch.R;
//import com.ch.yoon.kakao.pay.imagesearch.RxSchedulerRule;
//import com.ch.yoon.kakao.pay.imagesearch.data.repository.ImageRepository;
//import com.ch.yoon.kakao.pay.imagesearch.ui.common.pageload.PageLoadHelper;
//
//import org.junit.Before;
//import org.junit.Rule;
//import org.junit.Test;
//import org.junit.runner.RunWith;
//import org.mockito.Mock;
//import org.mockito.MockitoAnnotations;
//import org.mockito.stubbing.Answer;
//import org.powermock.api.mockito.PowerMockito;
//import org.powermock.core.classloader.annotations.PrepareForTest;
//import org.powermock.modules.junit4.PowerMockRunner;
//
//import static org.junit.Assert.assertEquals;
//import static org.mockito.ArgumentMatchers.any;
//import static org.powermock.api.mockito.PowerMockito.mockStatic;
//import static org.powermock.api.mockito.PowerMockito.when;
//
///**
// * Creator : ch-yoon
// * Date : 2019-08-12.
// */
//@RunWith(PowerMockRunner.class)
//@PrepareForTest({Log.class})
//public class ImageListViewModelTest {
//
//    private static final String SUCCESS_NO_RESULT = "검색 결과가 없습니다";
//    private static final String SUCCESS_LAST_DATA = "마지막 검색 페이지입니다";
//    private static final String ERROR_NETWORK = "인터넷 연결을 확인해주세요";
//    private static final String ERROR_UNKNOWN_ERROR = "알 수 없는 에러가 발생했습니다";
//
//    @Rule
//    public InstantTaskExecutorRule instantTaskExecutorRule = new InstantTaskExecutorRule();
//    @Rule
//    public RxSchedulerRule rxSchedulerRule = new RxSchedulerRule();
//
//    @Mock
//    private Application mockApplication;
//    @Mock
//    private ImageRepository mockImageSearchRepository;
//    @Mock
//    private PageLoadHelper<String> mockPageLoadHelper;
//
//    private ImageListViewModel imageListViewModel;
//
//    @Before
//    public void init() {
//        MockitoAnnotations.initMocks(this);
//
////        initApplication();
////        initUtils();
////
////        initImageListViewModel();
//    }
//
//    private void initApplication() {
//        when(mockApplication.getString(R.string.success_image_search_no_result)).thenReturn(SUCCESS_NO_RESULT);
//        when(mockApplication.getString(R.string.success_image_search_last_data)).thenReturn(SUCCESS_LAST_DATA);
//        when(mockApplication.getString(R.string.network_not_connecting_error)).thenReturn(ERROR_NETWORK);
//        when(mockApplication.getString(R.string.unknown_error)).thenReturn(ERROR_UNKNOWN_ERROR);
//    }
//
//    private void initImageListViewModel() {
//        imageListViewModel = new ImageListViewModel(
//            mockApplication,
//            mockImageSearchRepository,
//            mockPageLoadHelper
//        );
//    }
//
//    private void initUtils() {
//        mockStatic(Log.class);
//        when(Log.d(any(String.class), any(String.class))).thenReturn(0);
//    }
//
//    @Test
//    public void 한줄에_표현되는_아이템의_갯수를_변경하면_반영되는지_테스트() {
//        // when && then
//        imageListViewModel.changeCountOfItemInLine(2);
//        assertEquals(new Integer(2), imageListViewModel.getCountOfItemInLine().getValue());
//
//        imageListViewModel.changeCountOfItemInLine(3);
//        assertEquals(new Integer(3), imageListViewModel.getCountOfItemInLine().getValue());
//
//        imageListViewModel.changeCountOfItemInLine(4);
//        assertEquals(new Integer(4), imageListViewModel.getCountOfItemInLine().getValue());
//    }
////
////    @Test
////    public void 최초_이미지_목록_요청시_수신한_이미지_목록이_반영되는지_테스트() {
////        // given
////        ArrayList<ImageDocument> expectedList = new ArrayList<>();
////        when(mockImageSearchRepository.requestImageList(any(ImageSearchRequest.class)))
////            .thenAnswer((Answer<Single<ImageSearchResult>>) invocation -> {
////                ImageSearchRequest request = invocation.getArgument(0);
////                ImageSearchResult result = createVirtualImageSearchResult(request, false);
////                expectedList.addAll(result.getImageDocumentList());
////                return Single.just(result);
////            });
////
////        // when
////        imageListViewModel.loadImageList("테스트");
////
////        // then
////        List<ImageDocument> targetList = imageListViewModel.observeImageDocumentList().getValue();
////        assertEquals(targetList, expectedList);
////    }
////
////    @Test
////    public void 최초_이미지_목록_요청시_수신한_리스트의_사이즈가_0이어도_반영되는지_테스트() {
////        // given
////        when(mockImageSearchRepository.requestImageList(any(ImageSearchRequest.class)))
////            .thenReturn(Single.just(createEmptyImageSearchResult(true)));
////
////        // when
////        imageListViewModel.loadImageList("테스트");
////
////        // then
////        List<ImageDocument> targetList = imageListViewModel.observeImageDocumentList().getValue();
////        assertEquals(targetList.size(), 0);
////    }
////
////    @Test
////    public void 최초_이미지_목록_요청시_수신한_리스트의_사이즈가_0이라면_검색결과가_없다는_메시지가_반영되는지_테스트() {
////        // given
////        when(mockImageSearchRepository.requestImageList(any(ImageSearchRequest.class)))
////            .thenReturn(Single.just(createEmptyImageSearchResult(true)));
////
////        // when
////        imageListViewModel.loadImageList("테스트");
////
////        // then
////        String message = imageListViewModel.observeShowMessage().getValue();
////        assertEquals(message, SUCCESS_NO_RESULT);
////    }
////
////    @Test
////    public void 최초_이미지_목록_요청시_에러가_수신된다면_에러_메시지가_반영되는지_테스트() {
////        // given
////        when(mockImageSearchRepository.requestImageList(any(ImageSearchRequest.class)))
////            .thenReturn(Single.error(new ImageSearchException("network error", ImageSearchError.NETWORK_NOT_CONNECTING_ERROR)));
////
////        // when
////        imageListViewModel.loadImageList("테스트");
////
////        // then
////        String message = imageListViewModel.observeShowMessage().getValue();
////        assertEquals(message, ERROR_NETWORK);
////    }
////
////    @Test
////    public void 최초_이미지_목록_요청시_마지막_데이터라면_마지막을_뜻하는_메시지가_반영되는지_테스트() {
////        // given
////        when(mockImageSearchRepository.requestImageList(any(ImageSearchRequest.class)))
////            .thenAnswer((Answer<Single<ImageSearchResult>>) invocation -> {
////                ImageSearchRequest request = invocation.getArgument(0);
////                return Single.just(createVirtualImageSearchResult(request, true));
////            });
////
////        // when
////        imageListViewModel.loadImageList("테스트");
////
////        // then
////        String message = imageListViewModel.observeShowMessage().getValue();
////        assertEquals(message, SUCCESS_LAST_DATA);
////    }
////
////    @Test
////    public void 추가_이미지_목록_요청시_수신된_추가_이미지_목록이_반영되는지_테스트() {
////        // given
////        ArrayList<ImageDocument> expectedList = new ArrayList<>();
////        when(mockImageSearchRepository.requestImageList(any(ImageSearchRequest.class)))
////            .thenAnswer((Answer<Single<ImageSearchResult>>) invocation -> {
////                ImageSearchRequest request = invocation.getArgument(0);
////                ImageSearchResult result = createVirtualImageSearchResult(request, false);
////                expectedList.addAll(result.getImageDocumentList());
////                return Single.just(result);
////            });
////
////        // when
////        imageListViewModel.loadImageList("테스트");
////        int dataSize = imageListViewModel.observeImageDocumentList().getValue().size();
////        imageListViewModel.loadMoreImageListIfPossible(dataSize - 3);
////
////        // then
////        List<ImageDocument> targetList = imageListViewModel.observeImageDocumentList().getValue();
////        assertEquals(targetList, expectedList);
////    }
////
////    @Test
////    public void 추가_이미지_목록_요청시_마지막_데이터였다면_레파지토리에_요청을_안하는지_테스트() {
////        // given
////        when(mockImageSearchRepository.requestImageList(any(ImageSearchRequest.class)))
////            .thenAnswer((Answer<Single<ImageSearchResult>>) invocation -> {
////                ImageSearchRequest request = invocation.getArgument(0);
////                return Single.just(createVirtualImageSearchResult(request, true));
////            });
////
////        // when
////        imageListViewModel.loadImageList("테스트");
////
////        int dataSize = imageListViewModel.observeImageDocumentList().getValue().size();
////        imageListViewModel.loadMoreImageListIfPossible(dataSize - 1);
////        imageListViewModel.loadMoreImageListIfPossible(dataSize - 1);
////        imageListViewModel.loadMoreImageListIfPossible(dataSize - 1);
////
////        // then
////        verify(mockImageSearchRepository, times(1))
////            .requestImageList(any(ImageSearchRequest.class));
////    }
////
////    @Test
////    public void 재시도_요청시_레파지토리에_요청을_하는지_테스트() {
////        // given
////        when(mockImageSearchRepository.requestImageList(any(ImageSearchRequest.class)))
////            .thenAnswer((Answer<Single<ImageSearchResult>>) invocation -> {
////                ImageSearchRequest request = invocation.getArgument(0);
////                return Single.just(createVirtualImageSearchResult(request, true));
////            });
////
////        // when
////        imageListViewModel.loadImageList("테스트");
////        imageListViewModel.retryLoadMoreImageList();
////
////        //then
////        verify(mockImageSearchRepository, times(2))
////            .requestImageList(any(ImageSearchRequest.class));
////    }
////
////    @Test
////    public void 이미지_검색_타입_변경시_반영되는지_테스트() {
////        // given
////        when(mockImageSearchRepository.requestImageList(any(ImageSearchRequest.class)))
////            .thenReturn(Single.just(createEmptyImageSearchResult(true)));
////
////        // when
////        imageListViewModel.changeImageSortType(ImageSortType.ACCURACY);
////
////        // then
////        assertEquals(ImageSortType.ACCURACY, imageListViewModel.observeImageSortType().getValue());
////
////        // when
////        imageListViewModel.changeImageSortType(ImageSortType.RECENCY);
////
////        // then
////        assertEquals(ImageSortType.RECENCY, imageListViewModel.observeImageSortType().getValue());
////    }
////
////    @Test
////    public void 최초_이미지_검색_후_이미지_검색_타입_변경시_기존의_이미지_목록이_삭제되는지_테스트() {
////        // given
////        ArrayList<ImageDocument> expectedList = new ArrayList<>();
////        when(mockImageSearchRepository.requestImageList(any(ImageSearchRequest.class)))
////            .thenAnswer((Answer<Single<ImageSearchResult>>) invocation -> {
////                ImageSearchRequest request = invocation.getArgument(0);
////                ImageSearchResult result = createVirtualImageSearchResult(request, false);
////                expectedList.addAll(result.getImageDocumentList());
////                return Single.just(result);
////            });
////
////        // when
////        imageListViewModel.changeImageSortType(ImageSortType.ACCURACY);
////        imageListViewModel.loadImageList("테스트");
////
////        //then
////        assertEquals(expectedList.size(), imageListViewModel.observeImageDocumentList().getValue().size());
////
////        //when
////        expectedList.clear();
////        imageListViewModel.changeImageSortType(ImageSortType.RECENCY);
////
////        // then
////        assertTrue(expectedList.size() != 0);
////        assertEquals(expectedList.size(), imageListViewModel.observeImageDocumentList().getValue().size());
////    }
////
////    private ImageSearchResult createEmptyImageSearchResult(boolean isLastData) {
////        ArrayList<ImageDocument> simpleImageInfoList = new ArrayList<>();
////        KakaoImageSearchMetaInfo kakaoImageSearchMetaInfo = new KakaoImageSearchMetaInfo(true);
////        ImageSearchRequest imageSearchRequest = new ImageSearchRequest("", ImageSortType.ACCURACY, 0, 0, isLastData);
////        return new ImageSearchResult(imageSearchRequest, kakaoImageSearchMetaInfo, simpleImageInfoList);
////    }
////
////    private ImageSearchResult createVirtualImageSearchResult(ImageSearchRequest imageSearchRequest,
////                                                               boolean isLastData) {
////        int pageNumber = imageSearchRequest.getPageNumber();
////        int requiredSize = imageSearchRequest.getRequiredSize();
////        int startNumber = ((pageNumber-1) * requiredSize) + 1;
////
////        int listSize = imageSearchRequest.getRequiredSize();
////        ArrayList<ImageDocument> simpleImageInfoList = new ArrayList<>();
////        for(int i=0; i<listSize; i++) {
////            int itemNumber = startNumber + i;
////            simpleImageInfoList.add(createVirtualImageDocument(itemNumber));
////        }
////
////        KakaoImageSearchMetaInfo kakaoImageSearchMetaInfo = new KakaoImageSearchMetaInfo(isLastData);
////        return new ImageSearchResult(imageSearchRequest, kakaoImageSearchMetaInfo, simpleImageInfoList);
////    }
////
////    private ImageDocument createVirtualImageDocument(int id) {
////        return new ImageDocument(
////            "collection" + id,
////            "thumbnailUrl" + id,
////            "imageUrlInfo" + id,
////            id,
////            id,
////            "displaySiteName" + id,
////            "docUrl" + id,
////            "dateTime" + id
////        );
////    }
//
//}

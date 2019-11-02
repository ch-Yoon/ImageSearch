package com.ch.yoon.kakao.pay.imagesearch.data.remote.error

import com.ch.yoon.kakao.pay.imagesearch.data.remote.kakao.error.KakaoSearchExceptionMapper
import com.ch.yoon.kakao.pay.imagesearch.data.repository.error.RepositoryException
import io.mockk.every
import io.mockk.mockk
import junit.framework.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import retrofit2.HttpException
import java.net.UnknownHostException

/**
 * Creator : ch-yoon
 * Date : 2019-11-02
 **/
class KakaoSearchExceptionMapperTest {

    @Before
    fun init() {

    }

    @Test
    fun `400 에러 발생 시 WrongRequestException으로 맵핑하는지 테스트`() {
        // given
        val exception: HttpException = mockk()
        every { exception.code() } returns 400
        every { exception.message } returns ""

        // when
        val convertedException = KakaoSearchExceptionMapper.toRepositoryException(exception)

        // then
        assertEquals(true, convertedException is RepositoryException.WrongRequestException)
    }

    @Test
    fun `401 에러 발생 시 AuthenticationException 맵핑하는지 테스트`() {
        // given
        val exception: HttpException = mockk()
        every { exception.code() } returns 401
        every { exception.message } returns ""

        // when
        val convertedException = KakaoSearchExceptionMapper.toRepositoryException(exception)

        // then
        assertEquals(true, convertedException is RepositoryException.AuthenticationException)
    }

    @Test
    fun `403 에러 발생 시 AuthenticationException 맵핑하는지 테스트`() {
        // given
        val exception: HttpException = mockk()
        every { exception.code() } returns 403
        every { exception.message } returns ""

        // when
        val convertedException = KakaoSearchExceptionMapper.toRepositoryException(exception)

        // then
        assertEquals(true, convertedException is RepositoryException.PermissionException)
    }

    @Test
    fun `500 에러 발생 시 ServerSystemException 맵핑하는지 테스트`() {
        // given
        val exception: HttpException = mockk()
        every { exception.code() } returns 500
        every { exception.message } returns ""

        // when
        val convertedException = KakaoSearchExceptionMapper.toRepositoryException(exception)

        // then
        assertEquals(true, convertedException is RepositoryException.ServerSystemException)
    }

    @Test
    fun `502 에러 발생 시 ServerSystemException 맵핑하는지 테스트`() {
        // given
        val exception: HttpException = mockk()
        every { exception.code() } returns 502
        every { exception.message } returns ""

        // when
        val convertedException = KakaoSearchExceptionMapper.toRepositoryException(exception)

        // then
        assertEquals(true, convertedException is RepositoryException.ServerSystemException)
    }

    @Test
    fun `503 에러 발생 시 ServerSystemException 맵핑하는지 테스트`() {
        // given
        val exception: HttpException = mockk()
        every { exception.code() } returns 503
        every { exception.message } returns ""

        // when
        val convertedException = KakaoSearchExceptionMapper.toRepositoryException(exception)

        // then
        assertEquals(true, convertedException is RepositoryException.ServerSystemException)
    }

    @Test
    fun `Htpp Exception이지만 알 수 없는 코드인 경우 NetworkUnknownException 맵핑하는지 테스트`() {
        // given
        val exception: HttpException = mockk()
        every { exception.code() } returns 1354
        every { exception.message } returns ""

        // when
        val convertedException = KakaoSearchExceptionMapper.toRepositoryException(exception)

        // then
        assertEquals(true, convertedException is RepositoryException.NetworkUnknownException)
    }

    @Test
    fun `UnknownHostException 인 경우 NotworkNotConnectingException 맵핑하는지 테스트`() {
        // given
        val exception: UnknownHostException = mockk()
        every { exception.message } returns ""

        // when
        val convertedException = KakaoSearchExceptionMapper.toRepositoryException(exception)

        // then
        assertEquals(true, convertedException is RepositoryException.NetworkNotConnectingException)
    }

    @Test
    fun `알 수 없는 exception인 경우, UnknownException 맵핑하는지 테스트`() {
        // given
        val exception: Exception = mockk()
        every { exception.message } returns ""

        // when
        val convertedException = KakaoSearchExceptionMapper.toRepositoryException(exception)

        // then
        assertEquals(true, convertedException is RepositoryException.UnknownException)
    }
}
package com.ch.yoon.kakao.pay.imagesearch.data.local.error

import androidx.room.EmptyResultSetException
import com.ch.yoon.kakao.pay.imagesearch.data.local.room.error.RoomExceptionMapper
import com.ch.yoon.kakao.pay.imagesearch.data.repository.error.RepositoryException
import io.mockk.every
import io.mockk.mockk
import junit.framework.Assert.assertEquals
import org.junit.Test

/**
 * Creator : ch-yoon
 * Date : 2019-11-02
 **/
class RoomExceptionMapperTest {

    @Test
    fun `EmptyResultSetException 발생 시 NotFoundException 맵핑하는지 테스트`() {
        // given
        val exception: EmptyResultSetException = mockk()
        every { exception.message } returns ""

        // when
        val convertedException = RoomExceptionMapper.toRepositoryException(exception)

        // then
        assertEquals(true, convertedException is RepositoryException.NotFoundException)
    }

    @Test
    fun `알 수 없는 Exception 인 경우, DatabaseUnknownException 맵핑하는지 테스트`() {
        // given
        val exception: Exception = mockk()
        every { exception.message } returns ""

        // when
        val convertedException = RoomExceptionMapper.toRepositoryException(exception)

        // then
        assertEquals(true, convertedException is RepositoryException.DatabaseUnknownException)
    }
}
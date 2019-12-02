package com.ch.yoon.imagesearch.data.local.room.transformer.error.mapper

import androidx.room.EmptyResultSetException
import com.ch.yoon.imagesearch.data.local.room.transformer.error.RoomExceptionConverter
import com.ch.yoon.imagesearch.data.repository.error.RepositoryException
import io.mockk.every
import io.mockk.mockk
import junit.framework.Assert.assertEquals
import org.junit.Test

/**
 * Creator : ch-yoon
 * Date : 2019-11-02
 **/
class RoomExceptionConverterTest {

    @Test
    fun `EmptyResultSetException 발생 시 NotFoundException 맵핑하는지 테스트`() {
        // given
        val exception: EmptyResultSetException = mockk()
        every { exception.message } returns ""

        // when
        val convertedException = RoomExceptionConverter.toRepositoryException(exception)

        // then
        assertEquals(true, convertedException is RepositoryException.NotFoundException)
    }

    @Test
    fun `알 수 없는 Exception 인 경우, DatabaseUnknownException 맵핑하는지 테스트`() {
        // given
        val exception: Exception = mockk()
        every { exception.message } returns ""

        // when
        val convertedException = RoomExceptionConverter.toRepositoryException(exception)

        // then
        assertEquals(true, convertedException is RepositoryException.DatabaseUnknownException)
    }
}
package com.ch.yoon.data.error

import java.lang.Exception

/**
 * Creator : ch-yoon
 * Date : 2019-10-30
 **/
sealed class RepositoryException(errorMessage: String) : Exception(errorMessage) {

    // 찾을 수 없는 오류
    class NotFoundException(errorMessage: String): RepositoryException(errorMessage)

    // 잘못된 요청 오류(잘못된 파라미터)
    class WrongRequestException(errorMessage: String) : RepositoryException(errorMessage)

    // 사용자 인증 오류
    class AuthenticationException(errorMessage: String): RepositoryException(errorMessage)

    // 권한/퍼미션 오류
    class PermissionException(errorMessage: String): RepositoryException(errorMessage)

    // 서버 시스템 오류
    class ServerSystemException(errorMessage: String): RepositoryException(errorMessage)

    // 네트워크 미 연결 오류
    class NetworkNotConnectingException(errorMessage: String): RepositoryException(errorMessage)

    // 네트워크 알 수 없는 오류
    class NetworkUnknownException(errorMessage: String): RepositoryException(errorMessage)

    // 데이터베이스 알 수 없는 오류
    class DatabaseUnknownException(errorMessage: String): RepositoryException(errorMessage)

    // 알 수 없는 오류
    class UnknownException(errorMessage: String): RepositoryException(errorMessage)
}
package com.ch.yoon.remote.kakao.model.response.mapper

/**
 * Creator : ch-yoon
 * Date : 2019-12-23.
 */
/**
 * @param <R> the remote model input type
 * @param <E> the entity model output type
 */
interface EntityMapper<R, E> {

    fun toEntity(remoteModel: R): E
}
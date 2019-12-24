package com.ch.yoon.remote.mapper

/**
 * Creator : ch-yoon
 * Date : 2019-12-23.
 *
 * @param <R> the remote model input type
 * @param <E> the entity model output type
 */
interface EntityMapper<R, E> {

    fun fromRemote(model: R): E

}
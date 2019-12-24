package com.ch.yoon.local.mapper

/**
 * Creator : ch-yoon
 * Date : 2019-12-24.
 */

/**
 * @param <L> the local layer model input type
 * @param <E> the data layer entity output type
 */
interface EntityMapper<L, E> {

    fun fromLocal(model: L): E

    fun fromLocal(models: List<L>): List<E>

    fun toLocal(model: E): L

    fun toLocal(models: List<E>): List<L>
}
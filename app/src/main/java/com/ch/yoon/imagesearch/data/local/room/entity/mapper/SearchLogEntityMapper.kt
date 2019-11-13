package com.ch.yoon.imagesearch.data.local.room.entity.mapper

import com.ch.yoon.imagesearch.data.local.room.entity.SearchLogEntity
import com.ch.yoon.imagesearch.data.repository.searchlog.model.SearchLog

/**
 * Creator : ch-yoon
 * Date : 2019-10-30
 **/
object SearchLogEntityMapper {

    fun fromEntity(searchLogEntity: SearchLogEntity): SearchLog = searchLogEntity.run { SearchLog(keyword, time) }

    fun fromEntityList(searchLogEntityList: List<SearchLogEntity>) = searchLogEntityList.map { SearchLog(it.keyword, it.time) }

}
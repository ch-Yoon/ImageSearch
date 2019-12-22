package com.ch.yoon.local.room.model.mapper

import com.ch.yoon.local.room.model.SearchLogEntity
import com.ch.yoon.data.model.searchlog.SearchLog

/**
 * Creator : ch-yoon
 * Date : 2019-10-30
 **/
object SearchLogEntityMapper {

    fun fromEntity(searchLogEntity: SearchLogEntity): com.ch.yoon.data.model.searchlog.SearchLog = searchLogEntity.run { com.ch.yoon.data.model.searchlog.SearchLog(keyword, time) }

    fun fromEntityList(searchLogEntityList: List<SearchLogEntity>) = searchLogEntityList.map { com.ch.yoon.data.model.searchlog.SearchLog(it.keyword, it.time) }

}
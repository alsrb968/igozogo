package io.jacob.igozogo.core.data.datasource.local

import io.jacob.igozogo.core.data.db.StoryRemoteKeyDao
import io.jacob.igozogo.core.data.model.local.odii.StoryRemoteKey
import javax.inject.Inject

interface StoryRemoteKeyDataSource {
    suspend fun insertRemoteKeys(keys: List<StoryRemoteKey>)
    suspend fun getRemoteKey(id: Int, queryType: String): StoryRemoteKey?
    suspend fun deleteRemoteKeysByQueryType(queryType: String)
    suspend fun deleteRemoteKeys()
}

class StoryRemoteKeyDataSourceImpl @Inject constructor(
    private val dao: StoryRemoteKeyDao,
) : StoryRemoteKeyDataSource {
    override suspend fun insertRemoteKeys(keys: List<StoryRemoteKey>) {
        return dao.insertRemoteKeys(keys)
    }

    override suspend fun getRemoteKey(id: Int, queryType: String): StoryRemoteKey? {
        return dao.getRemoteKey(id, queryType)
    }

    override suspend fun deleteRemoteKeysByQueryType(queryType: String) {
        return dao.deleteRemoteKeysByQueryType(queryType)
    }

    override suspend fun deleteRemoteKeys() {
        return dao.deleteRemoteKeys()
    }
}
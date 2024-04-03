package net.schnall.compose.repo.cache

import androidx.datastore.preferences.core.longPreferencesKey
import kotlinx.datetime.Clock

class ApiCacheHelper(private val apiDataStore: ApiDataStore, private val clock: Clock) {
    suspend fun isExpired(key: String) =
        apiDataStore.getFirstPreference(longPreferencesKey(key), clock.now().epochSeconds) - clock.now().epochSeconds <= 0

    suspend fun updateFetchTime(key: String, timeout: Long = TIMEOUT) =
        apiDataStore.putPreference(longPreferencesKey(key), clock.now().epochSeconds + timeout)

    companion object {
        const val TIMEOUT: Long = 60 * 60 // 1 hour in seconds
    }
}
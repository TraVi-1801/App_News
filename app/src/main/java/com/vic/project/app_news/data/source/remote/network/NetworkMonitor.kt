package com.vic.project.app_news.data.source.remote.network

import kotlinx.coroutines.flow.Flow

/**
 * Utility for reporting app connectivity status
 */
interface NetworkMonitor {
    val isOnline: Flow<Boolean>
}

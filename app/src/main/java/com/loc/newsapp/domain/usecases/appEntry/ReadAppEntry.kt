package com.loc.newsapp.domain.usecases.appEntry

import com.loc.newsapp.domain.managers.LocalUserManager
import kotlinx.coroutines.flow.Flow

class ReadAppEntry(private val localUserManager: LocalUserManager) {

    operator fun invoke(): Flow<Boolean> {
        return localUserManager.readAppEntry()
    }

}
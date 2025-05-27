package io.jacob.igozogo.core.domain.usecase

import io.jacob.igozogo.core.domain.repository.OdiiRepository
import javax.inject.Inject

class SyncThemesUseCase @Inject constructor(
    private val repository: OdiiRepository
) {
    suspend operator fun invoke() {
        repository.syncThemes()
    }
}
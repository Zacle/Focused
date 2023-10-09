package com.zn.apps.feature.tags

import android.content.Context
import com.zn.apps.domain.tag.SearchTagUseCase
import com.zn.apps.model.R
import com.zn.apps.model.usecase.UseCaseException
import com.zn.apps.ui_common.state.CommonResultConverter
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

class TagsUiConverter @Inject constructor(
    @ApplicationContext private val context: Context
): CommonResultConverter<SearchTagUseCase.Response, TagsUiModel>() {
    override fun convertSuccess(data: SearchTagUseCase.Response): TagsUiModel =
        TagsUiModel(tags = data.tags)

    override fun convertError(useCaseException: UseCaseException): String =
        context.getString(R.string.unknown_error)
}
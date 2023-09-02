package com.zn.apps.di

import com.zn.apps.domain.UseCase
import com.zn.apps.domain.repository.TagRepository
import com.zn.apps.domain.tag.DeleteTagUseCase
import com.zn.apps.domain.tag.GetTagUseCase
import com.zn.apps.domain.tag.GetTagsUseCase
import com.zn.apps.domain.tag.SearchTagUseCase
import com.zn.apps.domain.tag.UpsertTagUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
object TagUseCaseModule {

    @Provides
    fun providesDeleteTagUseCase(
        configuration: UseCase.Configuration,
        tagRepository: TagRepository
    ): DeleteTagUseCase = DeleteTagUseCase(configuration, tagRepository)

    @Provides
    fun providesGetTagsUseCase(
        configuration: UseCase.Configuration,
        tagRepository: TagRepository
    ): GetTagsUseCase = GetTagsUseCase(configuration, tagRepository)

    @Provides
    fun providesGetTagUseCase(
        configuration: UseCase.Configuration,
        tagRepository: TagRepository
    ): GetTagUseCase = GetTagUseCase(configuration, tagRepository)

    @Provides
    fun providesSearchUseCase(
        configuration: UseCase.Configuration,
        tagRepository: TagRepository
    ): SearchTagUseCase = SearchTagUseCase(configuration, tagRepository)

    @Provides
    fun providesUpsertUseCase(
        configuration: UseCase.Configuration,
        tagRepository: TagRepository
    ): UpsertTagUseCase = UpsertTagUseCase(configuration, tagRepository)
}
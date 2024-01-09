package com.example.allahs99names.data

import com.example.allahs99names.R
import com.example.allahs99names.core.ResourceManager
import com.example.allahs99names.domain.model.FullBlessedNameEntity
import com.example.allahs99names.domain.repository.NamesListRepository
import javax.inject.Inject

class NamesListRepositoryImpl @Inject constructor(private val resourceManager: ResourceManager) :
    NamesListRepository {

    override fun getAllNames(): ArrayList<FullBlessedNameEntity> {
        val arabicBlessedNamesArray =
            resourceManager.getStringArray(R.array.blessed_names_arabic_version)
        val transliterationBlessedNamesArray =
            resourceManager.getStringArray(R.array.blessed_names_transliteration_version)
        val russianTranslationBlessedNamesArray =
            resourceManager.getStringArray(R.array.blessed_names_russian_translation)
        val russianMeaningBlessedNamesArray =
            resourceManager.getStringArray(R.array.blessed_names_russian_meaning)

        return arrayListOf<FullBlessedNameEntity>().apply {
            for (i in 0..BLESSED_NAMES_LIST_SIZE.dec()) {
                add(
                    FullBlessedNameEntity(
                        nameCount = i.inc(),
                        arabicVersion = arabicBlessedNamesArray[i],
                        transliteration = transliterationBlessedNamesArray[i],
                        russianVersion = russianTranslationBlessedNamesArray[i],
                        russianMeaning = russianMeaningBlessedNamesArray[i]
                    )
                )
            }
        }
    }

    companion object {
        private const val BLESSED_NAMES_LIST_SIZE = 99
    }
}
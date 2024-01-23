package com.example.allahs99names.data

import com.example.allahs99names.R
import com.example.allahs99names.core.ResourceManager
import com.example.allahs99names.domain.model.FullBlessedNameEntity
import com.example.allahs99names.domain.repository.NamesListRepository
import com.example.allahs99names.local.BlessedNameDao
import com.example.allahs99names.local.BlessedNameEntity
import javax.inject.Inject

class NamesListRepositoryImpl @Inject constructor(
    private val resourceManager: ResourceManager,
    private val dao: BlessedNameDao
) : NamesListRepository {

    override suspend fun getAllNames(): ArrayList<FullBlessedNameEntity> {
        val learnedNames = dao.getAllLearnedNames().map { it.arabicName }

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
                        russianMeaning = russianMeaningBlessedNamesArray[i],
                        isLearned = learnedNames.contains(arabicBlessedNamesArray[i])
                    )
                )
            }
        }
    }

    override suspend fun saveLearnedName(arabicName: String) {
        dao.insertLearnedName(BlessedNameEntity(arabicName))
    }

    override suspend fun removeLearnedName(arabicName: String) {
        dao.removeSingleName(arabicName)
    }

    companion object {
        private const val BLESSED_NAMES_LIST_SIZE = 99
    }
}
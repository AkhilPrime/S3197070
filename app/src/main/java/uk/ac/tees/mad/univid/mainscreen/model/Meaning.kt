package uk.ac.tees.mad.univid.mainscreen.model

data class Meaning(
    val antonyms: List<Any>,
    val definitions: List<Definition>,
    val partOfSpeech: String,
    val synonyms: List<Any>
)
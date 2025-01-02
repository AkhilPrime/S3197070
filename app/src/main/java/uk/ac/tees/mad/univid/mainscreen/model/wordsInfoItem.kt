package uk.ac.tees.mad.univid.mainscreen.model

data class wordsInfoItem(
    val meanings: List<Meaning>,
    val phonetics: List<Phonetic>,
    val word: String
)
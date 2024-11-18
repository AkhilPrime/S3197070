package uk.ac.tees.mad.univid.authentication.model

data class CurrentUser(
    val name: String,
    val email: String,
    val profileImgUrl: String
)

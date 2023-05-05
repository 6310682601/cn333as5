package com.example.phonebook.domain.model


const val NEW_NOTE_ID = -1L

data class BookModel(
    val id: Long = NEW_NOTE_ID,
    val name: String = "",
    val phoneNumber: String = "",
    val address: String = "",
    val work: String = "",
    val color: ColorModel = ColorModel.DEFAULT
)
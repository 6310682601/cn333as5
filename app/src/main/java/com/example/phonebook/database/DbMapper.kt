package com.example.phonebook.database

import com.example.phonebook.domain.model.BookModel
import com.example.phonebook.domain.model.ColorModel
import com.example.phonebook.domain.model.NEW_NOTE_ID

class DbMapper {
    fun mapBooks(
        bookDbModels: List<BookDbModel>,
        colorDbModels: Map<Long, ColorDbModel>
    ): List<BookModel> = bookDbModels.map {
        val colorDbModel = colorDbModels[it.colorId]
            ?: throw RuntimeException("Color for colorId: ${it.colorId} was not found. Make sure that all colors are passed to this method")

        mapBook(it, colorDbModel)
    }

    fun mapBook(bookDbModel: BookDbModel, colorDbModel: ColorDbModel): BookModel {
        val color = mapColor(colorDbModel)
        return with(bookDbModel) { BookModel(id, name, phoneNumber, address, work, color) }
    }

    fun mapColors(colorDbModels: List<ColorDbModel>): List<ColorModel> =
        colorDbModels.map { mapColor(it) }

    fun mapColor(colorDbModel: ColorDbModel): ColorModel =
        with(colorDbModel) { ColorModel(id, name, hex) }

    fun mapDbBook(note: BookModel): BookDbModel =
        with(note) {
            if (id == NEW_NOTE_ID)
                BookDbModel(
                    name = name,
                    phoneNumber = phoneNumber,
                    address = address,
                    work = work,
                    colorId = color.id,
                    isInTrash = false
                )
            else
                BookDbModel(id, name, phoneNumber, address, work, color.id, false)
        }
}
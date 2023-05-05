package com.example.phonebook.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class BookDbModel(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    @ColumnInfo(name = "name") val name: String,
    @ColumnInfo(name = "phone_number") val phoneNumber: String,
    @ColumnInfo(name = "address") val address: String,
    @ColumnInfo(name = "work") val work: String,
    @ColumnInfo(name = "color_id") val colorId: Long,
    @ColumnInfo(name = "in_trash") val isInTrash: Boolean
) {
    companion object {
        val DEFAULT_BOOK = listOf(
            BookDbModel(1, "Harry Potter", "0818826266", "Earth", "Wizard", 1, false),
            BookDbModel(2, "Johnny Depp", "0997633321",  "Earth", "Actor", 2, false),
            BookDbModel(3, "Johnny English", "1123624145",   "Earth", "Spy", 3, false),
            BookDbModel(4, "Mr.Bean", "6500082116",   "Earth", "Actor", 4, false),
            BookDbModel(5, "Ronaldo", "3364413298",   "Earth", "Football player", 5, false)
        )
    }
}

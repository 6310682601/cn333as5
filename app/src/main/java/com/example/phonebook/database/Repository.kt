package com.example.phonebook.database

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import com.example.phonebook.domain.model.BookModel
import com.example.phonebook.domain.model.ColorModel
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class Repository (
    private val bookDao: BookDao,
    private val colorDao: ColorDao,
    private val dbMapper: DbMapper
    ) {
    private val booksNotInTrashLiveData: MutableLiveData<List<BookModel>> by lazy {
        MutableLiveData<List<BookModel>>()
    }

    fun getAllBooksNotInTrash(): LiveData<List<BookModel>> = booksNotInTrashLiveData

    private val booksInTrashLiveData: MutableLiveData<List<BookModel>> by lazy {
        MutableLiveData<List<BookModel>>()
    }

    fun getAllBooksInTrash(): LiveData<List<BookModel>> = booksInTrashLiveData

    init {
        initDatabase(this::updateBooksLiveData)
    }

    private fun initDatabase(postInitAction: () -> Unit) {
        GlobalScope.launch {
            val colors = ColorDbModel.DEFAULT_COLORS.toTypedArray()
            val dbColors = colorDao.getAllSync()
            if (dbColors.isNullOrEmpty()) {
                colorDao.insertAll(*colors)
            }

            val books = BookDbModel.DEFAULT_BOOK.toTypedArray()
            val dbBooks = bookDao.getAllSync()
            if (dbBooks.isNullOrEmpty()) {
                bookDao.insertAll(*books)
            }

            postInitAction.invoke()
        }
    }

    private fun getAllBooksDependingOnTrashStateSync(inTrash: Boolean): List<BookModel> {
        val colorDbModels: Map<Long, ColorDbModel> = colorDao.getAllSync().map { it.id to it }.toMap()
        val dbBooks: List<BookDbModel> =
            bookDao.getAllSync().filter { it.isInTrash == inTrash }
        return dbMapper.mapBooks(dbBooks, colorDbModels)
    }

    fun insertBook(note: BookModel) {
        bookDao.insert(dbMapper.mapDbBook(note))
        updateBooksLiveData()
    }

    fun deleteBooks(bookIds: List<Long>) {
        bookDao.delete(bookIds)
        updateBooksLiveData()
    }

    fun moveBookToTrash(bookId: Long) {
        val dbBook = bookDao.findByIdSync(bookId)
        val newDbBook = dbBook.copy(isInTrash = true)
        bookDao.insert(newDbBook)
        updateBooksLiveData()
    }

    fun restoreBooksFromTrash(bookIds: List<Long>) {
        val dbBooksInTrash = bookDao.getBooksByIdsSync(bookIds)
        dbBooksInTrash.forEach {
            val newDbBook = it.copy(isInTrash = false)
            bookDao.insert(newDbBook)
        }
        updateBooksLiveData()
    }

    fun getAllColors(): LiveData<List<ColorModel>> =
        Transformations.map(colorDao.getAll()) { dbMapper.mapColors(it) }

    private fun updateBooksLiveData() {
        booksNotInTrashLiveData.postValue(getAllBooksDependingOnTrashStateSync(false))
        booksInTrashLiveData.postValue(getAllBooksDependingOnTrashStateSync(true))
    }
}
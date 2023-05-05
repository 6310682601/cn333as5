package com.example.phonebook.viewmodel

import android.app.Application
import androidx.compose.ui.graphics.BlendMode
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.phonebook.database.DbMapper
import com.example.phonebook.database.Repository
import com.example.phonebook.database.AppDatabase
import com.example.phonebook.domain.model.BookModel
import com.example.phonebook.domain.model.ColorModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import com.example.phonebook.routing.PhoneBooksRouter
import com.example.phonebook.routing.Screen

class MainViewModel (application: Application) : ViewModel() {
    val booksNotInTrash: LiveData<List<BookModel>> by lazy {
        repository.getAllBooksNotInTrash()
    }

    private var _bookEntry = MutableLiveData(BookModel())

    val bookEntry: LiveData<BookModel> = _bookEntry

    val colors: LiveData<List<ColorModel>> by lazy {
        repository.getAllColors()
    }

    val booksInTrash by lazy { repository.getAllBooksInTrash() }

    private var _selectedBooks = MutableLiveData<List<BookModel>>(listOf())

    val selectedBooks: LiveData<List<BookModel>> = _selectedBooks

    private val repository: Repository

    init {
        val db = AppDatabase.getInstance(application)
        repository = Repository(db.bookDao(), db.colorDao(), DbMapper())
    }

    fun onCreateNewBookClick() {
        _bookEntry.value = BookModel()
        PhoneBooksRouter.navigateTo(Screen.SaveBook)
    }

    fun onBookClick(book: BookModel) {
        _bookEntry.value = book
        PhoneBooksRouter.navigateTo(Screen.SaveBook)
    }

    fun onBookCheckedChange(book: BookModel) {
        viewModelScope.launch(Dispatchers.Default) {
            repository.insertBook(book)
        }
    }

    fun onBookSelected(book: BookModel) {
        _selectedBooks.value = _selectedBooks.value!!.toMutableList().apply {
            if (contains(book)) {
                remove(book)
            } else {
                add(book)
            }
        }
    }

    fun restoreBooks(books: List<BookModel>) {
        viewModelScope.launch(Dispatchers.Default) {
            repository.restoreBooksFromTrash(books.map { it.id })
            withContext(Dispatchers.Main) {
                _selectedBooks.value = listOf()
            }
        }
    }

    fun permanentlyDeleteBooks(books: List<BookModel>) {
        viewModelScope.launch(Dispatchers.Default) {
            repository.deleteBooks(books.map { it.id })
            withContext(Dispatchers.Main) {
                _selectedBooks.value = listOf()
            }
        }
    }

    fun onBookEntryChange(book: BookModel) {
        _bookEntry.value = book
    }

    fun saveBook(book: BookModel) {
        viewModelScope.launch(Dispatchers.Default) {
            repository.insertBook(book)

            withContext(Dispatchers.Main) {
                PhoneBooksRouter.navigateTo(Screen.Books)

                _bookEntry.value = BookModel()
            }
        }
    }

    fun moveBookToTrash(book: BookModel) {
        viewModelScope.launch(Dispatchers.Default) {
            repository.moveBookToTrash(book.id)

            withContext(Dispatchers.Main) {
                PhoneBooksRouter.navigateTo(Screen.Books)
            }
        }
    }
}
package com.example.phonebook.screens

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.phonebook.domain.model.BookModel
import com.example.phonebook.ui.components.Book
import com.example.phonebook.viewmodel.MainViewModel
import kotlinx.coroutines.launch
import com.example.phonebook.routing.Screen
import com.example.phonebook.ui.components.AppDrawer


@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BooksScreen(viewModel: MainViewModel) {
    val books by viewModel.booksNotInTrash.observeAsState(listOf())
    val snackbarHostState = remember { SnackbarHostState() }
    val coroutineScope = rememberCoroutineScope()

    Column {
        Box(
            Modifier
                .fillMaxWidth()
                .background(color = Color(0xFF4A148C))
        ) {
            Row() {
                Text(
                    text = "Phone book",
                    fontSize = 20.sp,
                    color = Color.White,
                    modifier = Modifier.padding(12.dp)
                )
                Spacer(modifier = Modifier.width(220.dp))
                IconButton(onClick = { viewModel.onCreateNewBookClick() }) {
                    Icon(
                        imageVector = Icons.Filled.Add,
                        contentDescription = "Add Phone Number Button",
                        tint = MaterialTheme.colorScheme.onPrimary
                    )
                }
            }
        }
        if (books.isNotEmpty()) {
            BooksList(
                books = books,
                onBookCheckedChange = {
                    viewModel.onBookCheckedChange(it)
                },
                onBookClick = { viewModel.onBookClick(it) }
            )
        }
//        Button(onClick = {viewModel.onCreateNewBookClick() }) {
//
//        }


        }
    }


//    Scaffold(
//        snackbarHostState = { SnackbarHost(snackbarHostState)},
//        topBar = {

//                navigationIcon = {
//                    IconButton(onClick = {
//                        coroutineScope.launch { scaffoldState.drawerState.open() }
//                    }) {
//                        Icon(
//                            imageVector = Icons.Filled.List,
//                            contentDescription = "Drawer Button"
//                        )
//                    }
//                }
//            )
//        },
//        drawerContent = {
//            AppDrawer(
//                currentScreen = Screen.Books,
//                closeDrawerAction = {
//                    coroutineScope.launch {
//                        scaffoldState.drawerState.close()
//                    }
//                }
//            )
//         },
//        floatingActionButtonPosition = FabPosition.End,
//        floatingActionButton = {}
//    ) {
//        if (books.isNotEmpty()) {
//            BooksList(
//                books = books,
//                onBookCheckedChange = {
//                    viewModel.onBookCheckedChange(it)
//                },
//                onBookClick = { viewModel.onBookClick(it) }
//            )
//        }
//    }
//}

    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    fun BooksList(
        books: List<BookModel>,
        onBookCheckedChange: (BookModel) -> Unit,
        onBookClick: (BookModel) -> Unit
    ) {
        LazyColumn {
            items(count = books.size) { bookIndex ->
                val book = books[bookIndex]
                Book(
                    book = book,
                    onBookClick = onBookClick,
                    onBookCheckedChange = onBookCheckedChange,
                )
            }
        }
    }


//@OptIn(ExperimentalMaterial3Api::class)
//@Preview
//@Composable
//fun NotesListPreview() {
//    BooksList(
//        books = listOf(
//            BookModel(1, "Note 1", "Content 1"),
//            BookModel(2, "Note 2", "Content 2"),
//            BookModel(3, "Note 3", "Content 3")
//        ),
//        onBookCheckedChange = {},
//        onBookClick = {}
//    )
//}


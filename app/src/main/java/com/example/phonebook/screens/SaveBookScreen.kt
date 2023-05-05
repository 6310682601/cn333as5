package com.example.phonebook.screens

import android.annotation.SuppressLint
import android.text.style.BackgroundColorSpan
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.phonebook.domain.model.BookModel
import com.example.phonebook.domain.model.ColorModel
import com.example.phonebook.domain.model.NEW_NOTE_ID
import com.example.phonebook.routing.Screen
import com.example.phonebook.viewmodel.MainViewModel
import com.example.phonebook.routing.PhoneBooksRouter
import com.example.phonebook.ui.components.BookColor
import com.example.phonebook.utill.fromHex

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SaveBookScreen (viewModel: MainViewModel){
    val noteEntry by viewModel.bookEntry.observeAsState(BookModel())

    val colors: List<ColorModel> by viewModel.colors.observeAsState(listOf())

    val coroutineScope = rememberCoroutineScope()

    val moveBookToTrashDialogShownState = rememberSaveable { mutableStateOf(false) }

    BackHandler {
        PhoneBooksRouter.navigateTo(Screen.Books)
    }

        Scaffold(
            topBar = {
                val isEditingMode: Boolean = noteEntry.id != NEW_NOTE_ID
                SaveBookTopAppBar(
                    isEditingMode = isEditingMode,
                    onBackClick = { PhoneBooksRouter.navigateTo(Screen.Books) },
                    onSaveBookClick = {
                        viewModel.saveBook(noteEntry)
                    },
                    onDeleteBookClick = {
                        moveBookToTrashDialogShownState.value = true
                    }
                )
            }
        ) {
            SaveBookContent(
                book = noteEntry,
                onBookChange = { updateBookEntry ->
                    viewModel.onBookEntryChange(updateBookEntry)
                }
            )

            if (moveBookToTrashDialogShownState.value) {
                AlertDialog(
                    onDismissRequest = {
                        moveBookToTrashDialogShownState.value = false
                    },
                    title = {
                        Text("Move this phone number to the trash?")
                    },
                    text = {
                        Text(
                            "Are you sure you want to " +
                                    "move this phone number to the trash?"
                        )
                    },
                    confirmButton = {
                        TextButton(onClick = {
                            viewModel.moveBookToTrash(noteEntry)
                        }) {
                            Text(text = "Confirm")
                        }
                    },
                    dismissButton = {
                        TextButton(onClick = {
                            moveBookToTrashDialogShownState.value = false
                        }) {
                            Text("Dismiss")
                        }
                    }
                )
            }
        }
    }


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SaveBookTopAppBar(
    isEditingMode: Boolean,
    onBackClick: () -> Unit,
    onSaveBookClick: () -> Unit,
    onDeleteBookClick: () -> Unit
) {
    Box (
        Modifier
            .fillMaxWidth()
            .background(color = Color(0xFF4A148C))
        ) {
        Row() {
            IconButton(onClick = onBackClick) {
                Icon(
                    imageVector = Icons.Default.ArrowBack,
                    contentDescription = "Back Button",
                    tint = MaterialTheme.colorScheme.onPrimary
                )
            }
            
            Text(
                text = "Save Phone number",
                fontSize = 20.sp,
                color = Color.White,
                modifier = Modifier.padding(12.dp)
            )
            Spacer(modifier = Modifier.width(30.dp))
            IconButton(onClick = onSaveBookClick) {
                Icon(
                    imageVector = Icons.Default.Check,
                    contentDescription = "Save phone number Button",
                    tint = MaterialTheme.colorScheme.onPrimary
                )
            }

            if (isEditingMode) {
                IconButton(onClick = onDeleteBookClick) {
                    Icon(
                        imageVector = Icons.Default.Delete,
                        contentDescription = "Delete phone number Button",
                        tint = MaterialTheme.colorScheme.onPrimary
                    )
                }
            }
        }
    }

//        navigationIcon = {
//            IconButton(onClick = onBackClick) {
//                Icon(
//                    imageVector = Icons.Default.ArrowBack,
//                    contentDescription = "Back Button",
//                    tint = MaterialTheme.colorScheme.onPrimary
//                )
//            }
//        },
//        actions = {
//            IconButton(onClick = onSaveBookClick) {
//                Icon(
//                    imageVector = Icons.Default.Check,
//                    contentDescription = "Save phone number Button",
//                    tint = MaterialTheme.colorScheme.onPrimary
//                )
//            }

//            if (isEditingMode) {
//                IconButton(onClick = onDeleteBookClick) {
//                    Icon(
//                        imageVector = Icons.Default.Delete,
//                        contentDescription = "Delete phone number Button",
//                        tint = MaterialTheme.colorScheme.onPrimary
//                    )
//                }
//            }
        }




@Composable
private fun SaveBookContent(
    book: BookModel,
    onBookChange: (BookModel) -> Unit
) {
    Column(modifier = Modifier.fillMaxSize()) {
        Spacer(modifier = Modifier.height(60.dp))
        ContentTextField(
            modifier = Modifier
                .heightIn(max = 240.dp)
                .padding(top = 16.dp),
            label = "Name",
            text = book.name,
            onTextChange = { newName ->
                onBookChange.invoke(book.copy(name = newName))
            }
        )

        ContentTextField(
            modifier = Modifier
                .heightIn(max = 240.dp)
                .padding(top = 16.dp),
            label = "Phone Number",
            text = book.phoneNumber,
            onTextChange = { newPhoneNumber ->
                onBookChange.invoke(book.copy(phoneNumber = newPhoneNumber))
            }
        )

        ContentTextField(
            modifier = Modifier
                .heightIn(max = 240.dp)
                .padding(top = 16.dp),
            label = "Address",
            text = book.address,
            onTextChange = { newAddress ->
                onBookChange.invoke(book.copy(address = newAddress))
            }
        )

        ContentTextField(
            modifier = Modifier
                .heightIn(max = 240.dp)
                .padding(top = 16.dp),
            label = "Work",
            text = book.work,
            onTextChange = { newWork ->
                onBookChange.invoke(book.copy(work = newWork))
            }
        )
//        PickedColor(color = book.color)
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun ContentTextField(
    modifier: Modifier = Modifier,
    label: String,
    text: String,
    onTextChange: (String) -> Unit
) {
    TextField(
        value = text,
        onValueChange = onTextChange,
        label = { Text(label) },
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 8.dp),
//        colors = TextFieldDefaults.textFieldColors(
//            backgroundColor = MaterialTheme.colorScheme.surface
//        )
        colors = TextFieldDefaults.textFieldColors(Color.Black)
    )
}

//@Composable
//private fun BookCheckOption(
//    isChecked: Boolean,
//    onCheckedChange: (Boolean) -> Unit
//) {
//    Row(
//        Modifier
//            .padding(8.dp)
//            .padding(top = 16.dp)
//    ) {
//        Text(
//            text = "Can note be checked off?",
//            modifier = Modifier.weight(1f)
//        )
//        Switch(
//            checked = isChecked,
//            onCheckedChange = onCheckedChange,
//            modifier = Modifier.padding(start = 8.dp)
//        )
//    }
//}

//@Composable
//private fun PickedColor(color: ColorModel) {
//    Row(
//        Modifier
//            .padding(8.dp)
//            .padding(top = 16.dp)
//    ) {
//        Text(
//            text = "Picked color",
//            modifier = Modifier
//                .weight(1f)
//                .align(Alignment.CenterVertically)
//        )
//        BookColor(
//            color = Color.fromHex(color.hex),
//            size = 40.dp,
//            border = 1.dp,
//            modifier = Modifier.padding(4.dp)
//        )
//    }
//}

//@Composable
//private fun ColorPicker(
//    colors: List<ColorModel>,
//    onColorSelect: (ColorModel) -> Unit
//) {
//    Column(modifier = Modifier.fillMaxWidth()) {
//        Text(
//            text = "Color picker",
//            fontSize = 18.sp,
//            fontWeight = FontWeight.Bold,
//            modifier = Modifier.padding(8.dp)
//        )
//        LazyColumn(modifier = Modifier.fillMaxWidth()) {
//            items(colors.size) { itemIndex ->
//                val color = colors[itemIndex]
//                ColorItem(
//                    color = color,
//                    onColorSelect = onColorSelect
//                )
//            }
//        }
//    }
//}

//@Composable
//fun ColorItem(
//    color: ColorModel,
//    onColorSelect: (ColorModel) -> Unit
//) {
//    Row(
//        modifier = Modifier
//            .fillMaxWidth()
//            .clickable(
//                onClick = {
//                    onColorSelect(color)
//                }
//            )
//    ) {
//        BookColor(
//            modifier = Modifier.padding(10.dp),
//            color = Color.fromHex(color.hex),
//            size = 80.dp,
//            border = 2.dp
//        )
//        Text(
//            text = color.name,
//            fontSize = 22.sp,
//            modifier = Modifier
//                .padding(horizontal = 16.dp)
//                .align(Alignment.CenterVertically)
//        )
//    }
//}

//@Preview
//@Composable
//fun ColorItemPreview() {
//    ColorItem(ColorModel.DEFAULT) {}
//}
//
//@Preview
//@Composable
//fun ColorPickerPreview() {
//    ColorPicker(
//        colors = listOf(
//            ColorModel.DEFAULT,
//            ColorModel.DEFAULT,
//            ColorModel.DEFAULT
//        )
//    ) { }
//}
//
//@Preview
//@Composable
//fun PickedColorPreview() {
//    PickedColor(ColorModel.DEFAULT)
//}



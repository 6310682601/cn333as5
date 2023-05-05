package com.example.phonebook.ui.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.phonebook.domain.model.BookModel
import com.example.phonebook.utill.fromHex

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Book(
    modifier: Modifier = Modifier,
    book: BookModel,
    onBookClick: (BookModel) -> Unit = {},
    onBookCheckedChange: (BookModel) -> Unit = {},
) {
    Card(
        shape = RoundedCornerShape(4.dp),
        modifier = modifier
            .padding(8.dp)
            .fillMaxWidth(),
    ) {
        ListItem(
            headlineText = { Text(text = book.name, maxLines = 1) },
            supportingText = { Text(text = book.phoneNumber, maxLines = 1) },
            modifier = Modifier.clickable {
                onBookClick.invoke(book)
            }
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Preview
@Composable
private fun BookPreview() {
    Book(book = BookModel(1, "Harry Potter", "0810000000"))
}
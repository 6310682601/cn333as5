package com.example.phonebook

import android.app.Application
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.phonebook.routing.PhoneBooksRouter
import com.example.phonebook.routing.Screen
import com.example.phonebook.screens.BooksScreen
import com.example.phonebook.screens.SaveBookScreen
import com.example.phonebook.screens.TrashScreen
import com.example.phonebook.ui.theme.PhoneBookTheme
import com.example.phonebook.viewmodel.MainViewModel
import com.example.phonebook.viewmodel.MainViewModelFactory

class MainActivity : ComponentActivity() {
    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            PhoneBookTheme {
                    val viewModel: MainViewModel = viewModel(
                        factory = MainViewModelFactory(LocalContext.current.applicationContext as Application)
                    )
                MainActivityScreen(viewModel)
                }
            }
        }
    }

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainActivityScreen(viewModel: MainViewModel) {
    Surface {
        when (PhoneBooksRouter.currentScreen) {
            is Screen.Books -> BooksScreen(viewModel)
            is Screen.SaveBook -> SaveBookScreen(viewModel)
            is Screen.Trash -> TrashScreen(viewModel)
        }
    }
}

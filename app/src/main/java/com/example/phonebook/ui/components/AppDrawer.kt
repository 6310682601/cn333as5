package com.example.phonebook.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.*
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.BlendMode.Companion.Screen
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.phonebook.routing.Screen
import com.example.phonebook.ui.theme.PhoneBookTheme
import com.example.phonebook.ui.theme.PhoneBookThemeSettings
import com.example.phonebook.routing.PhoneBooksRouter

@Composable
private fun AppDrawerHeader() {
    Row(modifier = Modifier.fillMaxWidth()) {
        Image(
            imageVector = Icons.Filled.Menu,
            contentDescription = "Drawer Header Icon",
            colorFilter = ColorFilter
                .tint(MaterialTheme.colorScheme.onSurface),
            modifier = Modifier.padding(16.dp)
        )
        Text(
            text = "Phone Book",
            modifier = Modifier
                .align(alignment = Alignment.CenterVertically)
        )
    }
}


@Preview
@Composable
fun AppDrawerHeaderPreview() {
    PhoneBookTheme {
        AppDrawerHeader()
    }
}

@Composable
private fun ScreenNavigationButton(
    icon: ImageVector,
    label: String,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    val colors = MaterialTheme.colorScheme

    val imageAlpha = if (isSelected) {
        1f
    } else {
        0.6f
    }

    val textColor = if (isSelected) {
        colors.primary
    } else {
        colors.onSurface.copy(alpha = 0.6f)
    }

    val backgroundColor = if (isSelected) {
        colors.primary.copy(alpha = 0.12f)
    } else {
        colors.surface
    }

    Surface( // 1
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 8.dp, end = 8.dp, top = 8.dp),
        color = backgroundColor,
        shape = MaterialTheme.shapes.small
    ) {
        Row( // 2
            horizontalArrangement = Arrangement.Start,
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .clickable(onClick = onClick)
                .fillMaxWidth()
                .padding(4.dp)
        ) {
            Image(
                imageVector = icon,
                contentDescription = "Screen Navigation Button",
                colorFilter = ColorFilter.tint(textColor),
                alpha = imageAlpha
            )
            Spacer(Modifier.width(16.dp)) // 3
            Text(
                text = label,
                style = MaterialTheme.typography.bodyMedium,
                color = textColor,
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}

@Preview
@Composable
fun ScreenNavigationButtonPreview() {
    PhoneBookTheme {
        ScreenNavigationButton(
            icon = Icons.Filled.Home,
            label = "Books",
            isSelected = false,
            onClick = { }
        )
    }
}

@Composable
private fun LightDarkThemeItem() {
    Row(
        Modifier
            .padding(8.dp)
    ) {
        Text(
            text = "Turn on dark theme",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f),
            modifier = Modifier
                .weight(1f)
                .padding(start = 8.dp, top = 8.dp, end = 8.dp, bottom = 8.dp)
                .align(alignment = Alignment.CenterVertically)
        )
        Switch(
            checked = PhoneBookThemeSettings.isDarkThemeEnabled,
            onCheckedChange = { PhoneBookThemeSettings.isDarkThemeEnabled = it },
            modifier = Modifier
                .padding(start = 8.dp, end = 8.dp)
                .align(alignment = Alignment.CenterVertically)
        )
    }
}

@Preview
@Composable
fun LightDarkThemeItemPreview() {
    PhoneBookTheme {
        LightDarkThemeItem()
    }
}

@Composable
fun AppDrawer(
    currentScreen: Screen,
    closeDrawerAction: () -> Unit
) {
    Column(modifier = Modifier.fillMaxSize()) {
        AppDrawerHeader()

        Divider(color = MaterialTheme.colorScheme.onSurface.copy(alpha = .2f))

        ScreenNavigationButton(
            icon = Icons.Filled.Home,
            label = "Notes",
            isSelected = currentScreen == com.example.phonebook.routing.Screen.Books,
            onClick = {
                PhoneBooksRouter.navigateTo(com.example.phonebook.routing.Screen.Books)
                closeDrawerAction()
            }
        )
        ScreenNavigationButton(
            icon = Icons.Filled.Delete,
            label = "Trash",
            isSelected = currentScreen == com.example.phonebook.routing.Screen.Trash,
            onClick = {
                PhoneBooksRouter.navigateTo(com.example.phonebook.routing.Screen.Trash)
                closeDrawerAction()
            }
        )
        LightDarkThemeItem()
    }
}

@Preview
@Composable
fun AppDrawerPreview() {
    PhoneBookTheme {
        AppDrawer(com.example.phonebook.routing.Screen.Books, {})
    }
}
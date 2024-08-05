package com.loc.newsapp.presentation.common

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import com.loc.newsapp.ui.theme.WhiteGray

@Composable
fun NewsTextButton(
    title: String,
    onClick: () -> Unit
) {
    TextButton(onClick = onClick) {
        Text(
            text = title,
            style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.SemiBold),
            color = WhiteGray
        )
    }
}
package com.vic.project.app_news.presentation.screen.home.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.vic.project.app_news.R
import com.vic.project.app_news.utils.ModifierExtension.clickableSingle

@Composable
fun EmptyPage() {
    Column (
        modifier = Modifier
            .fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ){
        Spacer(Modifier.height(42.dp))
        Image(
            painter = painterResource(R.drawable.img_error_page),
            contentDescription = "",
            modifier = Modifier
                .fillMaxWidth(0.35f),
            contentScale = ContentScale.FillWidth
        )
        Text(
            text = stringResource(R.string.empty_message),
            style = MaterialTheme.typography.bodyLarge,
            fontWeight = FontWeight.W400,
            textAlign = TextAlign.Center,
        )
    }
}
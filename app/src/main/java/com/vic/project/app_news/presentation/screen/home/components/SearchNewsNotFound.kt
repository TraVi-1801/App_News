package com.vic.project.app_news.presentation.screen.home.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.vic.project.app_news.R
import com.vic.project.app_news.utils.AuthModel
import com.vic.project.app_news.utils.ContextExtension.getLocalizedString
import com.vic.project.app_news.utils.ModifierExtension.clickableSingle

@Composable
fun SearchNewsNotFound(
    onBackHome: () -> Unit,
) {
    Column(
        modifier = Modifier
            .fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(Modifier.height(32.dp))
        Image(
            painter = painterResource(R.drawable.img_not_found),
            contentDescription = "",
            modifier = Modifier
                .fillMaxWidth(0.65f)
                .aspectRatio(1f)
        )
        Spacer(Modifier.height(16.dp))
        Text(
            text = getLocalizedString(R.string.not_found),
            style = MaterialTheme.typography.labelLarge,
            fontWeight = FontWeight.W600,
            textAlign = TextAlign.Center
        )
        Spacer(Modifier.height(8.dp))
        Text(
            text = getLocalizedString(R.string.not_found_description),
            style = MaterialTheme.typography.bodySmall,
            fontWeight = FontWeight.W400,
            textAlign = TextAlign.Center
        )
        Spacer(Modifier.height(8.dp))
        Text(
            text = getLocalizedString(R.string.back_to_home),
            style = MaterialTheme.typography.labelLarge,
            fontWeight = FontWeight.W500,
            textAlign = TextAlign.Center,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier
                .background(MaterialTheme.colorScheme.primaryContainer, RoundedCornerShape(16.dp))
                .clickableSingle {
                    onBackHome.invoke()
                }
                .padding(16.dp)
        )
    }
}
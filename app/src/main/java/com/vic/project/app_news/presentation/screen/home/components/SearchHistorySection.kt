package com.vic.project.app_news.presentation.screen.home.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.vic.project.app_news.R
import com.vic.project.app_news.utils.AuthModel
import com.vic.project.app_news.utils.ContextExtension.getLocalizedString
import com.vic.project.app_news.utils.ModifierExtension.clickableSingle
import com.vic.project.app_news.utils.ModifierExtension.shadowCustom

@Composable
fun SearchHistorySection(
    visible: Boolean,
    history: List<String>,
    modifier: Modifier = Modifier,
    onSearchItemClick: (String) -> Unit
) {
    AnimatedVisibility(
        visible = visible,
        modifier = modifier
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .shadowCustom(
                    color = MaterialTheme.colorScheme.onSurface.copy(0.3f),
                    offsetY = 0.dp,
                    offsetX = 3.dp,
                    spread = 0.dp,
                    blurRadius = 4.dp,
                    borderRadius = 8.dp
                )
                .background(MaterialTheme.colorScheme.background, RoundedCornerShape(8.dp))
                .padding(8.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(4.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = getLocalizedString(R.string.search_history),
                    style = MaterialTheme.typography.labelMedium,
                    fontWeight = FontWeight.W500,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )
                Icon(
                    imageVector = Icons.Default.Refresh,
                    contentDescription = "",
                    tint = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.size(18.dp)
                )
            }
            history.forEach {
                Text(
                    text = it,
                    style = MaterialTheme.typography.bodySmall,
                    fontWeight = FontWeight.W500,
                    maxLines = 1,
                    overflow = TextOverflow.Clip,
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickableSingle {
                            onSearchItemClick.invoke(it)
                        }
                )
            }
        }
    }
}
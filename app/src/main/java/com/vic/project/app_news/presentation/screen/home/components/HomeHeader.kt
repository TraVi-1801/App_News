package com.vic.project.app_news.presentation.screen.home.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusState
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import com.vic.project.app_news.R
import com.vic.project.app_news.presentation.components.InputText
import com.vic.project.app_news.presentation.navigation.AppScreens
import com.vic.project.app_news.presentation.navigation.currentComposeNavigator
import com.vic.project.app_news.utils.ModifierExtension.shadowCustom

@Composable
fun HomeHeader(
    search: String,
    modifier: Modifier = Modifier,
    onSearch: () -> Unit,
    onFocusChanged: (FocusState) -> Unit,
    onSearchValueChanged: (String) -> Unit
) {

    val composeNavigator = currentComposeNavigator

    Column(
        modifier = modifier
            .fillMaxWidth()
            .shadowCustom(
                color = MaterialTheme.colorScheme.onSurface.copy(0.3f),
                offsetY = 0.dp,
                offsetX = 3.dp,
                spread = 0.dp,
                blurRadius = 4.dp,
                borderRadius = 16.dp
            )
            .background(
                MaterialTheme.colorScheme.background,
                RoundedCornerShape(bottomEnd = 16.dp, bottomStart = 16.dp)
            )
            .padding(horizontal = 16.dp, vertical = 8.dp),
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .statusBarsPadding(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = stringResource(id = R.string.title_home),
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.W600,
            )
            IconButton(
                onClick = {composeNavigator.navigate(AppScreens.Setting)}
            ) {
                Icon(
                    painter = painterResource(R.drawable.ic_globe_earth),
                    contentDescription = "",
                    tint = MaterialTheme.colorScheme.onSurface,
                )
            }
        }
        Spacer(Modifier.height(12.dp))
        InputText(
            string = search,
            textHint = stringResource(R.string.search_news),
            imgTrailing = R.drawable.ic_search,
            imeAction = ImeAction.Search,
            onSearch = onSearch,
            onFocusChanged = onFocusChanged,
            onValueChange = onSearchValueChanged
        )
        Spacer(Modifier.height(8.dp))
    }
}
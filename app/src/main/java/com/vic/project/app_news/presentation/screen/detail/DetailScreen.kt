package com.vic.project.app_news.presentation.screen.detail

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.ClickableText
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.withAnnotation
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.vic.project.app_news.R
import com.vic.project.app_news.presentation.components.ImageLoad
import com.vic.project.app_news.presentation.navigation.AppScreens
import com.vic.project.app_news.presentation.navigation.currentComposeNavigator
import com.vic.project.app_news.presentation.viewmodel.NewsDetailViewModel
import com.vic.project.app_news.utils.ModifierExtension.shadowCustom
import com.vic.project.app_news.utils.StringExtension.orNullWithHolder

@Composable
fun DetailScreen(
    viewModel: NewsDetailViewModel = hiltViewModel()
) {
    val composeNavigator = currentComposeNavigator
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()


    Column (
        modifier = Modifier
            .fillMaxSize()
    ){
        Column(
            modifier = Modifier
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
                .padding(16.dp),
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .statusBarsPadding(),
                horizontalArrangement = Arrangement.spacedBy(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(
                    onClick = {
                        composeNavigator.navigateUp()
                    }
                ) {
                    Icon(
                        painter = painterResource(R.drawable.ic_arrow_left),
                        contentDescription = "",
                        tint = MaterialTheme.colorScheme.onSurface,
                    )
                }

                Text(
                    text = stringResource(id = R.string.title_home),
                    style = MaterialTheme.typography.headlineMedium,
                    fontWeight = FontWeight.W600,
                )

            }
        }

        LazyColumn (
            modifier = Modifier
                .weight(1f),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ){
            item {
                ImageLoad(
                    url = uiState.data?.urlToImage.orEmpty(),
                    contentScale = ContentScale.FillWidth,
                    contentDescription = "",
                    modifier = Modifier
                        .fillMaxWidth()
                )
            }
            item {
                Text(
                    text = uiState.data?.title.orNullWithHolder(),
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.W600,
                    modifier = Modifier.padding(horizontal = 16.dp)
                )
            }
            item {
                Text(
                    text = uiState.data?.content.orNullWithHolder(),
                    style = MaterialTheme.typography.bodyMedium,
                    fontWeight = FontWeight.W400,
                    modifier = Modifier.padding(horizontal = 16.dp)
                )
            }
            item {
                Text(
                    text = "${stringResource(R.string.author)}: ${uiState.data?.author.orNullWithHolder()}",
                    style = MaterialTheme.typography.bodyMedium,
                    fontWeight = FontWeight.W400,
                    modifier = Modifier.padding(horizontal = 16.dp)
                )
            }
            item {
                Text(
                    text = "${stringResource(R.string.published_at)}: ${uiState.data?.publishedAt.orNullWithHolder()}",
                    style = MaterialTheme.typography.bodyMedium,
                    fontWeight = FontWeight.W400,
                    modifier = Modifier.padding(horizontal = 16.dp)
                )
            }
            item {
                val annotatedText = buildAnnotatedString {
                    append("${stringResource(R.string.read_detail)}: ")
                    pushStringAnnotation(
                        tag = "NAVIGATE",
                        annotation = "detail_screen"
                    )
                    withStyle(
                        style = SpanStyle(
                            color = MaterialTheme.colorScheme.primary,
                            fontWeight = FontWeight.W500,
                            textDecoration = TextDecoration.Underline
                        )
                    ) {
                        append(uiState.data?.url.orNullWithHolder())
                    }
                    pop()
                }
                ClickableText(
                    text = annotatedText,
                    style = MaterialTheme.typography.bodyMedium.copy(
                        fontWeight = FontWeight.W400,
                        color = MaterialTheme.colorScheme.onSurface
                    ),
                    modifier = Modifier.padding(horizontal = 16.dp),
                    onClick = { offset ->
                        annotatedText.getStringAnnotations(
                            tag = "NAVIGATE", start = offset, end = offset
                        ).firstOrNull()?.let { annotation ->
                            if (uiState.data?.url.isNullOrBlank().not()){
                                composeNavigator.navigate(AppScreens.ReadMore(uiState.data?.url.orEmpty()))
                            }
                        }
                    }
                )
            }

            item {
                Spacer(Modifier.height(16.dp))
            }
        }
    }
}
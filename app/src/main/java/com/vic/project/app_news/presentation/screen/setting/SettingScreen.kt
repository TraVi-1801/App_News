package com.vic.project.app_news.presentation.screen.setting

import android.annotation.SuppressLint
import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
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
import androidx.compose.runtime.key
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.core.app.ActivityCompat.recreate
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.vic.project.app_news.R
import com.vic.project.app_news.data.model.EnumLanguage
import com.vic.project.app_news.presentation.components.ImageLoad
import com.vic.project.app_news.presentation.navigation.AppScreens
import com.vic.project.app_news.presentation.navigation.currentComposeNavigator
import com.vic.project.app_news.presentation.viewmodel.SettingEvent
import com.vic.project.app_news.presentation.viewmodel.SettingViewModel
import com.vic.project.app_news.utils.AuthModel
import com.vic.project.app_news.utils.AuthModel.updateStateContext
import com.vic.project.app_news.utils.ContextExtension.findActivity
import com.vic.project.app_news.utils.ContextExtension.getLocalizedString
import com.vic.project.app_news.utils.ContextExtension.updateLocale
import com.vic.project.app_news.utils.ModifierExtension.clickableSingle
import com.vic.project.app_news.utils.ModifierExtension.shadowCustom
import com.vic.project.app_news.utils.StringExtension
import com.vic.project.app_news.utils.StringExtension.orNullWithHolder

@SuppressLint("LocalContextConfigurationRead")
@Composable
fun SettingScreen(
    viewModel: SettingViewModel = hiltViewModel()
) {
    val context = LocalContext.current
    val orientation = context.resources.configuration.orientation
    val composeNavigator = currentComposeNavigator
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    var key by remember (
        orientation,
        EnumLanguage.fromLocale(uiState.currentLanguage).language
    ){
        val newContext = context.updateLocale(EnumLanguage.fromLocale(uiState.currentLanguage).localDefault)
        updateStateContext(newContext)
        mutableStateOf(StringExtension.randomUUID())
    }

    key (
        key
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
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
                        text = getLocalizedString( R.string.language),
                        style = MaterialTheme.typography.headlineMedium,
                        fontWeight = FontWeight.W600,
                    )

                }
            }

            LazyColumn(
                modifier = Modifier
                    .weight(1f)
                    .padding(horizontal = 16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {

                item {
                    Spacer(Modifier.height(16.dp))
                }

                item {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(
                                MaterialTheme.colorScheme.primaryContainer,
                                RoundedCornerShape(16.dp)
                            )
                            .padding(16.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        Image(
                            painter = painterResource(R.drawable.img_language_skill),
                            contentDescription = "",
                            modifier = Modifier.size(100.dp)
                        )
                        Text(
                            text = buildAnnotatedString {
                                append(
                                    "${
                                        getLocalizedString(
                                            R.string.current_language
                                        )
                                    } \n"
                                )
                                withStyle(
                                    style = SpanStyle(
                                        color = MaterialTheme.colorScheme.primary,
                                        fontWeight = FontWeight.W600,
                                    )
                                ) {
                                    append(EnumLanguage.fromLocale(uiState.currentLanguage).language)
                                }
                            },
                            style = MaterialTheme.typography.bodyLarge,
                            fontWeight = FontWeight.W500,
                            textAlign = TextAlign.Center,
                            modifier = Modifier.weight(1f)
                        )
                    }
                }

                item {
                    Column(
                        modifier = Modifier
                            .padding(16.dp)
                            .fillMaxWidth(),
                        verticalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        Text(
                            text = getLocalizedString(
                                R.string.title_language
                            ),
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.W600,
                        )

                        Text(
                            text = getLocalizedString(
                                R.string.description_language
                            ),
                            style = MaterialTheme.typography.bodyMedium,
                            fontWeight = FontWeight.W400,
                        )
                    }

                }

                item {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        ItemLanguage(
                            modifier = Modifier
                                .weight(1f)
                                .aspectRatio(1f),
                            current = uiState.currentLanguage,
                            language = EnumLanguage.ENGLISH.language,
                        ) {
                            viewModel.handleEvent(
                                SettingEvent.UpdateLanguage(EnumLanguage.ENGLISH.locale.language)
                            )
                        }
                        ItemLanguage(
                            modifier = Modifier
                                .weight(1f)
                                .aspectRatio(1f),
                            current = uiState.currentLanguage,
                            language = EnumLanguage.VIETNAMESE.language,
                        ) {
                            viewModel.handleEvent(
                                SettingEvent.UpdateLanguage(EnumLanguage.VIETNAMESE.locale.language)
                            )
                        }
                    }
                }

                item {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        ItemLanguage(
                            modifier = Modifier
                                .weight(1f)
                                .aspectRatio(1f),
                            current = uiState.currentLanguage,
                            language = EnumLanguage.CHINESE_TW.language,
                        ) {
                            viewModel.handleEvent(
                                SettingEvent.UpdateLanguage(EnumLanguage.CHINESE_TW.locale.language)
                            )
                        }
                        Spacer(Modifier.weight(1f))
                    }
                }

                item {
                    Spacer(Modifier.height(16.dp))
                }
            }
        }
    }
}

@Composable
fun ItemLanguage(
    modifier: Modifier = Modifier,
    current: String,
    language: String,
    onClick: () -> Unit
) {
    Box(
        modifier = modifier
            .background(
                if (current == language) MaterialTheme.colorScheme.primaryContainer else MaterialTheme.colorScheme.surfaceContainerLow,
                RoundedCornerShape(16.dp)
            )
            .border(1.dp, MaterialTheme.colorScheme.onSurfaceVariant, RoundedCornerShape(16.dp))
            .clickableSingle(enabled = current != language) {
                onClick.invoke()
            },
        contentAlignment = Alignment.Center
    ){
        Text(
            text = language,
            style = MaterialTheme.typography.bodyLarge,
            fontWeight = FontWeight.W500,
        )
    }

}
package com.zn.apps.feature.onboarding

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.spring
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.zn.apps.feature.onboarding.data.OnboardingPage
import com.zn.apps.ui_common.state.CommonScreen
import com.zn.apps.ui_common.state.UiState
import com.zn.apps.ui_design.component.FocusedAppBackground
import com.zn.apps.ui_design.component.ThemePreviews
import com.zn.apps.ui_design.icon.FAIcons
import com.zn.apps.ui_design.theme.FocusedAppTheme
import kotlinx.coroutines.launch

@Composable
fun OnboardingRoute(
    navigateToHome: () -> Unit,
    viewModel: OnboardingViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiStateFlow.collectAsStateWithLifecycle()

    FocusedAppBackground {
        OnboardingScreen(
            uiState = uiState,
            onboardingCompleted = {
                viewModel.submitAction(OnboardingUiAction.SetOnboarded)
                navigateToHome()
            }
        )
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun OnboardingScreen(
    uiState: UiState<OnboardedPageModel>,
    onboardingCompleted: () -> Unit
) {
    CommonScreen(state = uiState) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .navigationBarsPadding()
        ) {
            val scope = rememberCoroutineScope()
            val pagerState = rememberPagerState(
                initialPage = 0,
                initialPageOffsetFraction = 0f
            ) {
                it.pages.size
            }
            HorizontalPager(
                modifier = Modifier.weight(10f),
                state = pagerState
            ) { page ->
                PagerScreen(onboardingPage = it.pages[page])
            }
            Box(
                modifier = Modifier
                    .fillMaxWidth(),
                contentAlignment = Alignment.Center
            ) {
                Indicators(
                    size = it.pages.size,
                    currentPage = pagerState.currentPage
                )
            }
            BottomSection(
                currentPage = pagerState.currentPage,
                size = it.pages.size,
                onSkip = {
                    if (pagerState.currentPage < it.pages.size - 1) {
                        scope.launch {
                            pagerState.animateScrollToPage(it.pages.size - 1)
                        }
                    }
                },
                onNext = {
                    if (pagerState.currentPage < it.pages.size - 1) {
                        scope.launch {
                            pagerState.animateScrollToPage(pagerState.currentPage + 1)
                        }
                    }
                },
                onFinished = onboardingCompleted
            )
        }
    }
}

@Composable
fun PagerScreen(
    onboardingPage: OnboardingPage
) {
    Column(
        modifier = Modifier
            .fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top
    ) {
        Image(
            modifier = Modifier
                .weight(2f)
                .fillMaxWidth(0.5f)
                .fillMaxHeight(0.5f),
            painter = painterResource(id = onboardingPage.image.id),
            contentDescription = stringResource(id = onboardingPage.title)
        )
        Box(
            Modifier.weight(1f)
        ) {
            Column {
                Text(
                    text = stringResource(id = onboardingPage.title),
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.size(8.dp))
                Text(
                    text = stringResource(id = onboardingPage.description),
                    style = MaterialTheme.typography.bodyMedium,
                    fontWeight = FontWeight.Medium,
                    textAlign = TextAlign.Center,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 20.dp)
                        .padding(top = 20.dp)
                )
            }
        }
    }
}

@Composable
fun BottomSection(
    currentPage: Int,
    size: Int,
    onSkip: () -> Unit,
    onNext: () -> Unit,
    onFinished: () -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 24.dp)
    ) {
        SkipOrNextButtons(
            currentPage = currentPage,
            size = size,
            onSkip = onSkip,
            onNext = onNext
        )
        GetStarted(
            currentPage = currentPage,
            size = size,
            onFinished = onFinished
        )
    }
}

@Composable
fun SkipOrNextButtons(
    currentPage: Int,
    size: Int,
    onSkip: () -> Unit,
    onNext: () -> Unit,
) {
    val density = LocalDensity.current
    AnimatedVisibility(
        visible = currentPage < size - 1,
        enter = slideInVertically {
            // Slide in from 40 dp from the top.
            with(density) { -40.dp.roundToPx() }
        } + expandVertically(
            // Expand from the top.
            expandFrom = Alignment.Top
        ) + fadeIn(
            // Fade in with the initial alpha of 0.3f.
            initialAlpha = 0.3f
        ),
        exit = slideOutVertically() + shrinkVertically() + fadeOut()
    ) {
        Box(
            Modifier.fillMaxWidth()
        ) {
            TextButton(
                onClick = onSkip,
                modifier = Modifier
                    .align(Alignment.CenterStart)
            ) {
                Text(
                    text = stringResource(id = R.string.skip)
                )
            }
            Box(
                modifier = Modifier
                    .clip(MaterialTheme.shapes.large)
                    .background(MaterialTheme.colorScheme.primary)
                    .align(Alignment.CenterEnd)
                    .clickable { onNext() }
            ) {
                Row(
                    horizontalArrangement = Arrangement.spacedBy(4.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp)
                ) {
                    Text(
                        text = stringResource(id = R.string.next),
                        color = MaterialTheme.colorScheme.onPrimary
                    )
                    Icon(
                        imageVector = FAIcons.KeyboardArrowRight,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.onPrimary
                    )
                }
            }
        }
    }
}

@Composable
fun GetStarted(
    currentPage: Int,
    size: Int,
    onFinished: () -> Unit
) {
    val density = LocalDensity.current
    AnimatedVisibility(
        visible = currentPage == size - 1,
        modifier = Modifier
            .fillMaxWidth(),
        enter = slideInVertically {
            // Slide in from 40 dp from the top.
            with(density) { -40.dp.roundToPx() }
        } + expandVertically(
            // Expand from the top.
            expandFrom = Alignment.Top
        ) + fadeIn(
            // Fade in with the initial alpha of 0.3f.
            initialAlpha = 0.3f
        ),
        exit = slideOutVertically() + shrinkVertically() + fadeOut()
    ) {
        Button(onClick = onFinished) {
            Text(
                text = stringResource(id = R.string.get_started)
            )
        }
    }
}

@Composable
fun Indicators(size: Int, currentPage: Int, modifier: Modifier = Modifier) {
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        repeat(size) {
            Indicator(selected = it == currentPage)
        }
    }
}

@Composable
fun Indicator(selected: Boolean) {
    val width = animateDpAsState(
        targetValue = if (selected) 25.dp else 10.dp,
        label = "Onboarding indicator",
        animationSpec = spring(dampingRatio = Spring.DampingRatioMediumBouncy)
    )

    Box(
        modifier = Modifier
            .height(10.dp)
            .width(width.value)
            .clip(CircleShape)
            .background(
                if (selected) MaterialTheme.colorScheme.primary else Color.Gray
            )
    )
}

@ThemePreviews
@Composable
fun OnboardingScreenPreview() {
    FocusedAppTheme {
        FocusedAppBackground {
            OnboardingScreen(
                uiState = UiState.Success(
                    OnboardedPageModel(
                        pages = listOf(
                            OnboardingPage.ScheduleTasks,
                            OnboardingPage.RunTasks,
                            OnboardingPage.OrganizeTasks
                        )
                    )
                )
            ) {}
        }
    }
}
@file:OptIn(ExperimentalFoundationApi::class)

package bv.dev.onboarding

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import bv.dev.onboarding.ui.theme.ObjPrimary
import bv.dev.onboarding.ui.theme.ObjSecondary
import bv.dev.onboarding.ui.theme.OnboardingTheme
import bv.dev.onboarding.ui.theme.Typography
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            OnboardingTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    MainUI()
                }
            }
        }
    }
}

@Composable
fun MainUI() {
    val showSplash = rememberSaveable { mutableStateOf(true) }

    if (showSplash.value) {
        SplashScreenUI(showSplash)
    } else {
        OnboardingUI()
    }
}

@Composable
fun SplashScreenUI(showSplash: MutableState<Boolean>) {
    val modifier = Modifier

    Column(
        modifier
            .fillMaxSize()
            .clickable {
                showSplash.value = false
            },
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Row(
            modifier
                .padding(123.dp, 0.dp)
                .fillMaxWidth()
                .align(Alignment.CenterHorizontally),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Image(
                painter = painterResource(R.drawable.logo),
                contentDescription = "Logo",
                modifier = modifier.fillMaxWidth()
            )
        }
    }

    val coroutineScope = rememberCoroutineScope()

    LaunchedEffect(showSplash) {
        coroutineScope.launch {
            delay(5000)
            if (showSplash.value) {
                showSplash.value = false
            }
        }
    }
}

@Composable
fun OnboardingUI() {
    val modifier: Modifier = Modifier

    Column(modifier = modifier.verticalScroll(rememberScrollState())
    ) {
        Row(
            modifier = modifier.padding(24.dp, 12.dp).fillMaxWidth(),
            horizontalArrangement = Arrangement.End, verticalAlignment = Alignment.Top
        ) {
            Text(
                text = LocalContext.current.getString(R.string.text_skip),
                modifier = modifier,
                style = Typography.labelSmall,
                textAlign = TextAlign.End
            )
        }
        Column {
            val pageCount = 3
            val pagerState = rememberPagerState(0)
            HorizontalPager(
                state = pagerState,
                pageCount = pageCount
            ) { page ->
                var image = 0
                var title = ""
                var text = ""

                when (page) {
                    0 -> {
                        image = R.drawable.illu
                        title = LocalContext.current.getString(R.string.text_title_welcome)
                        text = LocalContext.current.getString(R.string.text_main)
                    }
                    1 -> {
                        image = R.drawable.illu_1
                        title = LocalContext.current.getString(R.string.text_title_watch)
                        text = LocalContext.current.getString(R.string.text_main)
                    }
                    2 -> {
                        image = R.drawable.illu_2
                        title = LocalContext.current.getString(R.string.text_title_easy)
                        text = LocalContext.current.getString(R.string.text_main)
                    }
                }

                Column {
                    Image(
                        painter = painterResource(id = image),
                        contentDescription = title,
                        modifier = modifier
                            .padding(35.dp, 41.dp)
                            .fillMaxWidth()
                    )

                    Text(
                        text = title,
                        modifier = Modifier
                            .padding(33.dp, 9.dp)
                            .fillMaxWidth()
                            .align(Alignment.CenterHorizontally),
                        style = Typography.titleLarge,
                        textAlign = TextAlign.Center
                    )

                    Text(
                        text = text,
                        modifier = Modifier
                            .padding(33.dp, 11.dp)
                            .fillMaxWidth()
                            .align(Alignment.CenterHorizontally),
                        style = Typography.bodySmall,
                        textAlign = TextAlign.Center
                    )
                }
            }

            val coroutineScope = rememberCoroutineScope()

            Row(
                modifier
                    .padding(0.dp, 61.dp)
                    .fillMaxWidth()
                    .align(Alignment.CenterHorizontally),
                horizontalArrangement = Arrangement.Center
            ) {
                repeat(pageCount) { iteration ->
                    val color =
                        if (pagerState.currentPage == iteration) ObjPrimary else ObjSecondary

                    Box(
                        modifier = Modifier
                            .padding(4.dp)
                            .clip(CircleShape)
                            .background(color)
                            .size(8.dp)
                            .clickable {
                                coroutineScope.launch {
                                    pagerState.animateScrollToPage(iteration)
                                }
                            }
                    )
                }
            }
        }

        Row(
            modifier
                .padding(28.dp, 14.dp, 28.dp, 80.dp)
                .fillMaxWidth()
                .align(Alignment.CenterHorizontally),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Image(
                painter = painterResource(R.drawable.button),
                contentDescription = LocalContext.current.getString(R.string.text_continue),
                modifier = modifier.fillMaxWidth()
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun UIPreview() {
    OnboardingTheme {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colorScheme.background
        ) {
            MainUI()
        }
    }
}

@Preview(showBackground = true)
@Composable
fun UIPreviewOnboarding() {
    OnboardingTheme {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colorScheme.background
        ) {
            OnboardingUI()
        }
    }
}
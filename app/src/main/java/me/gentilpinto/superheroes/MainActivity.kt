package me.gentilpinto.superheroes

import android.content.res.Configuration
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.compose.animation.*
import androidx.compose.animation.core.MutableTransitionState
import androidx.compose.animation.core.Spring.DampingRatioLowBouncy
import androidx.compose.animation.core.Spring.StiffnessVeryLow
import androidx.compose.animation.core.spring
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import me.gentilpinto.superheroes.data.HeroesDataSource
import me.gentilpinto.superheroes.model.Hero
import me.gentilpinto.superheroes.ui.theme.SuperheroesTheme

@ExperimentalAnimationApi
@ExperimentalMaterial3Api
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            SuperheroesTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background
                ) {
                    HeroApp()
                }
            }
        }
    }
}

@ExperimentalAnimationApi
@ExperimentalMaterial3Api
@Preview(name = "Light Theme", showBackground = true, showSystemUi = true, apiLevel = 23)
@Preview(
    name = "Dark Theme",
    showBackground = true,
    showSystemUi = true,
    uiMode = Configuration.UI_MODE_NIGHT_YES,
    apiLevel = 23
)
@Composable
fun AppPreview() {
    SuperheroesTheme {
        Surface(
            modifier = Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background
        ) {
            HeroApp()
        }
    }
}

@ExperimentalAnimationApi
@ExperimentalMaterial3Api
@Composable
fun HeroApp() {
    val visibilityState = remember {
        MutableTransitionState(false).apply {
            targetState = true // start animation imediately
        }
    }

    Scaffold(topBar = {
        AppTopBar()
    }) { paddingValues ->
        AnimatedVisibility(
            visibleState = visibilityState, enter = fadeIn(
                animationSpec = spring(dampingRatio = DampingRatioLowBouncy)
            ), exit = fadeOut()
        ) {
            LazyColumn(
                modifier = Modifier.padding(paddingValues)
            ) {
                itemsIndexed(HeroesDataSource.heroes) { index, hero ->
                    HeroItem(
                        hero, modifier = Modifier
                            .padding(16.dp, 8.dp)
                            .animateEnterExit(
                                enter = slideInVertically(animationSpec = spring(
                                    stiffness = StiffnessVeryLow,
                                    dampingRatio = DampingRatioLowBouncy
                                ), initialOffsetY = { it * (index + 1) })
                            )
                    )
                }
            }
        }
    }
}

@Composable
fun AppTopBar(
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .sizeIn(minHeight = 56.dp)
            .fillMaxWidth(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = stringResource(R.string.app_name), style = MaterialTheme.typography.headlineLarge
        )
    }
}

@Composable
fun HeroItem(
    hero: Hero, modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier,
        elevation = CardDefaults.cardElevation(2.dp),
        shape = MaterialTheme.shapes.medium,
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface,
            contentColor = MaterialTheme.colorScheme.onSurface,
        )
    ) {
        Row(
            modifier = modifier
                .fillMaxWidth()
                .padding(16.dp)
                .sizeIn(minHeight = 72.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            HeroInformation(
                nameRes = hero.nameRes,
                descriptionRes = hero.descriptionRes,
                modifier = Modifier.weight(1f)
            )
            Spacer(modifier = Modifier.width(16.dp))
            HeroImage(hero.imageRes)
        }
    }
}

@Composable
fun HeroInformation(
    @StringRes nameRes: Int, @StringRes descriptionRes: Int, modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
    ) {
        Text(
            text = stringResource(nameRes),
            style = MaterialTheme.typography.headlineSmall,
            modifier = Modifier,
        )
        Text(
            text = stringResource(descriptionRes),
            style = MaterialTheme.typography.bodyLarge,
            modifier = Modifier,
        )
    }
}

@Composable
fun HeroImage(
    @DrawableRes imageRes: Int, modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .size(72.dp)
            .clip(RoundedCornerShape(8.dp))

    ) {
        Image(
            painter = painterResource(imageRes),
            contentDescription = stringResource(R.string.hero1),
            alignment = Alignment.TopCenter,
            contentScale = ContentScale.FillWidth,
        )
    }
}
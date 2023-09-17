package com.zn.apps.ui_design.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.TileMode
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.zn.apps.model.data.tag.Tag
import com.zn.apps.ui_design.icon.Icon
import com.zn.apps.ui_design.icon.Icon.DrawableResourceIcon
import com.zn.apps.ui_design.icon.Icon.ImageVectorIcon
import com.zn.apps.ui_design.theme.FocusedAppTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FATopAppBar(
    titleName: String,
    navigationIcon: Icon,
    onNavigationIconClicked: () -> Unit,
    modifier: Modifier = Modifier,
    actions: @Composable RowScope.() -> Unit = {},
    scrollBehavior: TopAppBarScrollBehavior = TopAppBarDefaults.pinnedScrollBehavior(rememberTopAppBarState()),
    content: @Composable () -> Unit
) {
    Surface(
        modifier = modifier
            .clip(
                shape = RoundedCornerShape(
                    bottomStart = 20.dp,
                    bottomEnd = 20.dp
                )
            )
            .background(FATopAppBarDefaults.appBarColor())
            .statusBarsPadding()
            .fillMaxWidth(),
        color = Color.Transparent,
        contentColor = FATopAppBarDefaults.appBarContentColor()
    ) {
        Column(
            modifier = Modifier.padding(bottom = 16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            TopAppBar(
                title = {
                    Text(
                        text = titleName,
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Medium
                    )
                },
                navigationIcon = {
                    when(navigationIcon) {
                        is DrawableResourceIcon -> {
                            IconButton(onClick = onNavigationIconClicked) {
                                Icon(
                                    painter = painterResource(id = navigationIcon.id),
                                    contentDescription = null,
                                    tint = FATopAppBarDefaults.appBarContentColor(),
                                    modifier = Modifier.size(24.dp)
                                )
                            }
                        }
                        is ImageVectorIcon -> {
                            IconButton(onClick = onNavigationIconClicked) {
                                Icon(
                                    imageVector = navigationIcon.imageVector,
                                    contentDescription = null,
                                    tint = FATopAppBarDefaults.appBarContentColor()
                                )
                            }
                        }
                    }
                },
                actions = actions,
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color.Transparent,
                    navigationIconContentColor = FATopAppBarDefaults.appBarContentColor(),
                    titleContentColor = FATopAppBarDefaults.appBarContentColor(),
                    actionIconContentColor = FATopAppBarDefaults.appBarContentColor()
                ),
                scrollBehavior = scrollBehavior
            )
            content()
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@ThemePreviews
@Composable
fun FocusedAppBarPreview() {
    FocusedAppTheme {
        val selectedTag = Tag(name = "work")
        FATopAppBar(
            titleName = "Tasks",
            navigationIcon = ImageVectorIcon(Icons.Default.Menu),
            onNavigationIconClicked = {}
        ) {
            TagChips(
                tags = listOf(
                    Tag(name = "all"),
                    selectedTag,
                    Tag(name = "health and fitness"),
                    Tag(name = "Technology"),
                    Tag(name = "sport")
                ),
                selectedTagId = selectedTag.id,
                onTagSelected = {},
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
            )
        }
    }
}

/**
 * Focused App top app bar defaults
 */
object FATopAppBarDefaults {

    @Composable
    fun appBarColor() = Brush.linearGradient(
        listOf(
            MaterialTheme.colorScheme.surfaceVariant,
            MaterialTheme.colorScheme.secondaryContainer
        ),
        tileMode = TileMode.Clamp
    )

    @Composable
    fun appBarContentColor() = MaterialTheme.colorScheme.onSecondaryContainer
}
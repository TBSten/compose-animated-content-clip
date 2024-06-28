package me.tbsten.prac.animation

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.ContentTransform
import androidx.compose.animation.SizeTransform
import androidx.compose.animation.core.snap
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import me.tbsten.prac.animation.ui.theme.AnimationPracTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            AnimationPracTheme {
                var animState by remember {
                    mutableStateOf(AnimState.Visible)
                }
                val updateNextAnimState = remember {
                    {animState = animState.next()}
                }
                Row(Modifier.fillMaxSize(), verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.Center) {
                    AnimatedContent(
                        targetState = animState,
                        transitionSpec = {
                            when(targetState) {
                                AnimState.Visible -> {
                                    fadeIn(tween(1000)) togetherWith fadeOut(tween(1000)) using
                                            SizeTransform(
                                                clip = false,
                                                {_,_,->snap()},
                                            )
                                }
                                AnimState.Invisible -> {
                                    fadeIn(tween(1000)) togetherWith fadeOut(tween(1000)) using
                                            SizeTransform(
                                                clip = false,
                                                {_,_,->snap(delayMillis = 1000)},
                                            )
                                }
                            }
                        },
                    ) { animState ->
                        Box(Modifier.shadow().clickable(onClick = updateNextAnimState)) {
                            Text(text = "$animState")
                        }
                    }
                }
            }
        }
    }
}

fun Modifier.shadow() =
    this
        .drawWithContent {
            val shadowSize = 100.dp.toPx()
            drawRect(
                Color.Red,
                topLeft = Offset(-shadowSize, -shadowSize),
                size = Size(
                    size.width + (shadowSize*2),
                    size.height + (shadowSize*2),
                ),
            )
            drawContent()
        }

enum class AnimState(val next:()->AnimState) {
    Invisible({ Visible }),
    Visible({Invisible}),
    ;
}
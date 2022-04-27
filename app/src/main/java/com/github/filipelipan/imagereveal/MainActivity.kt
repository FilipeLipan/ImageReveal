package com.github.filipelipan.imagereveal

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect

import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.*
import androidx.compose.ui.graphics.drawscope.clipPath
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.imageResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import com.github.filipelipan.imagereveal.ui.theme.ImageRevealTheme
import java.lang.Math.sqrt
import kotlin.math.pow

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ImageRevealTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.background
                ) {
                    ImageReveal(
                        radius = 30.dp,
                    )
                }
            }
        }
    }
}

@Composable
fun ImageReveal(
    radius: Dp,
) {
    val kermit = ImageBitmap.imageResource(id = R.drawable.kermit)
    var ballPosition by remember { mutableStateOf(Offset(300f, 300f)) }

    BoxWithConstraints(modifier = Modifier.fillMaxSize()) {
        val width = this.maxWidth

        Canvas(modifier = Modifier
            .fillMaxSize()
            .pointerInput(true) {
                detectDragGestures { change, _ ->
                    ballPosition = change.position
                }
            }
        ) {
            val radiusInPx = radius.toPx()
            val widthInPx = width.toPx()

            val imageHeight = widthInPx * kermit.height.toFloat() / kermit.width.toFloat()
            val imageOffSet = IntOffset(0, 0)
            val imageSize = IntSize(width = width.toPx().toInt(), height = imageHeight.toInt())

            drawImage(
                image = kermit,
                dstOffset = imageOffSet,
                dstSize = imageSize,
                colorFilter = ColorFilter.tint(Color.Gray, BlendMode.Color)
            )

            val arcPath = Path().apply {
                addArc(
                    oval = Rect(ballPosition, radiusInPx),
                    startAngleDegrees = 0f,
                    sweepAngleDegrees = 360f
                )
            }

            clipPath(
                path = arcPath,
                clipOp = ClipOp.Intersect
            ) {
                drawImage(
                    image = kermit,
                    dstOffset = imageOffSet,
                    dstSize = imageSize,
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    ImageRevealTheme {
        ImageReveal(
            radius = 30.dp,
        )
    }
}
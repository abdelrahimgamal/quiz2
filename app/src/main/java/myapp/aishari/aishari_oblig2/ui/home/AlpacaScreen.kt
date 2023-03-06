package myapp.aishari.aishari_oblig2.ui.home

import android.util.Log
import android.view.WindowInsets
import androidx.compose.animation.core.*
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Color.Companion.Blue
import androidx.compose.ui.graphics.Color.Companion.Gray
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.stringArrayResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.*
import androidx.core.graphics.toColorInt
import coil.compose.rememberImagePainter
import myapp.aishari.aishari_oblig2.R
import myapp.aishari.aishari_oblig2.data.model.AlpacaParty
import myapp.aishari.aishari_oblig2.data.model.Vote
import myapp.aishari.aishari_oblig2.ui.theme.Purple40

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AlpacaScreen(modifier: Modifier, viewModel: AlpacaViewModel) {

    val homeUiState = viewModel.parties.collectAsState()
    val votesD3 = viewModel.votesD3.collectAsState()
    val votesCount = viewModel.totalVotes.collectAsState()
    Surface(
        modifier = modifier,
        color = MaterialTheme.colorScheme.background
    ) {
        when (homeUiState.value) {

            is AlpacaUiState.Loading -> {
                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier
                        .fillMaxSize()
                        .background(Color.White)
                ) {
                    Indicator()
                }

            }
            is AlpacaUiState.Error -> {
                Text("Error")

            }
            is AlpacaUiState.Successful<*> -> {
                var expanded by remember { mutableStateOf(false) }
                val disctricts = stringArrayResource(R.array.districts)
                var selectedText by remember { mutableStateOf("District 1") }
                var dropDownWidth by remember { mutableStateOf(0) }
                val list =
                    (homeUiState.value as AlpacaUiState.Successful<*>).list as List<AlpacaParty>

                when (votesD3.value) {
                    is AlpacaUiState.Successful<*> -> {
                        Column() {

                            Column(Modifier.padding(8.dp, 0.dp)) {
                                OutlinedTextField(
                                    value = selectedText,
                                    onValueChange = {
                                        selectedText = it

                                    },
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .onSizeChanged {
                                            dropDownWidth = it.width
                                        },
                                    label = { Text("Select District") },
                                    trailingIcon = {
                                        Icon(
                                            Icons.Filled.ArrowDropDown,
                                            "contentDescription",
                                            Modifier.clickable { expanded = !expanded })
                                    }
                                )
                                DropdownMenu(
                                    expanded = expanded,
                                    onDismissRequest = { expanded = false },
                                    modifier = Modifier
                                        .width(with(LocalDensity.current) { dropDownWidth.toDp() })
                                ) {
                                    disctricts.forEach { label ->
                                        DropdownMenuItem(onClick = {
                                            val id = label.substring(label.length - 1).toInt()
                                            viewModel.getVotes(id)
                                            selectedText = label
                                            expanded = false
                                        }, text = { Text(text = label) })
                                    }
                                }
                            }

                            LazyColumn {
                                val votes =
                                    votesCount.value
                                items(list.size) { index ->
                                    val item = list[index]
                                    val itemVote =
                                        votes?.find { it.index == (index + 1) }
                                    val textVote =
                                        "Votes: ${itemVote?.value} - ${itemVote?.percent} %"
                                    val myComposeColorInt = Color(item.color.toColorInt())
                                    Column(
                                        modifier.padding(8.dp),
                                        verticalArrangement = Arrangement.Center,
                                        horizontalAlignment = Alignment.CenterHorizontally
                                    ) {
                                        Spacer(
                                            modifier = Modifier
                                                .fillMaxWidth()
                                                .height(30.dp)
                                                .background(myComposeColorInt)
                                        )
                                        Spacer(modifier = Modifier.height(4.dp))
                                        Text(
                                            text = item.name,
                                            color = Color.Gray,
                                            fontWeight = FontWeight.SemiBold
                                        )

                                        Spacer(modifier = Modifier.height(4.dp))
                                        Image(
                                            painter = rememberImagePainter(item.img),
                                            contentDescription = "avatar",
                                            contentScale = ContentScale.Crop,            // crop the image if it's not a square
                                            modifier = Modifier
                                                .size(64.dp)
                                                .clip(CircleShape)
                                        )
                                        Text(
                                            text = "Leader : ${item.leader}",
                                            color = Color.Gray,
                                            fontWeight = FontWeight.Light
                                        )
                                        Spacer(modifier = Modifier.height(4.dp))
                                        Text(
                                            text = textVote,
                                            color = Color.Gray,
                                            fontWeight = FontWeight.SemiBold
                                        )

                                    }
                                }
                            }

                        }
                    }
                    is AlpacaUiState.Loading -> {
                        Box(
                            contentAlignment = Alignment.Center,
                            modifier = Modifier
                                .fillMaxSize()
                                .background(Color.White)
                        ) {
                            Indicator()
                        }
                    }
                    is AlpacaUiState.Error -> {
                        Text("Error")

                    }
                    else -> {}
                }


            }
            else -> {
                Text(text = "Else")
            }
        }
    }

}


@Composable
fun Indicator(
    size: Dp = 32.dp, // indicator size
    sweepAngle: Float = 90f, // angle (lenght) of indicator arc
    color: Color = Purple40, // color of indicator arc line
    strokeWidth: Dp = 2.dp //width of cicle and ar lines
) {
    ////// animation //////

    // docs recomend use transition animation for infinite loops
    // https://developer.android.com/jetpack/compose/animation
    val transition = rememberInfiniteTransition()

    // define the changing value from 0 to 360.
    // This is the angle of the beginning of indicator arc
    // this value will change over time from 0 to 360 and repeat indefinitely.
    // it changes starting position of the indicator arc and the animation is obtained
    val currentArcStartAngle by transition.animateValue(
        0,
        360,
        Int.VectorConverter,
        infiniteRepeatable(
            animation = tween(
                durationMillis = 1100,
                easing = LinearEasing
            )
        )
    )

    ////// draw /////

    // define stroke with given width and arc ends type considering device DPI
    val stroke = with(LocalDensity.current) {
        Stroke(width = strokeWidth.toPx(), cap = StrokeCap.Square)
    }

    // draw on canvas
    Canvas(
        Modifier
            .progressSemantics() // (optional) for Accessibility services
            .size(size) // canvas size
            .padding(strokeWidth / 2) //padding. otherwise, not the whole circle will fit in the canvas
    ) {
        // draw "background" (gray) circle with defined stroke.
        // without explicit center and radius it fit canvas bounds
        drawCircle(Color.LightGray, style = stroke)

        // draw arc with the same stroke
        drawArc(
            color,
            // arc start angle
            // -90 shifts the start position towards the y-axis
            startAngle = currentArcStartAngle.toFloat() - 90,
            sweepAngle = sweepAngle,
            useCenter = false,
            style = stroke
        )
    }
}



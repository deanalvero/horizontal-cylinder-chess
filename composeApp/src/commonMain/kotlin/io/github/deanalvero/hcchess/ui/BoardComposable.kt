package io.github.deanalvero.hcchess.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.animateScrollBy
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.gestures.scrollBy
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.North
import androidx.compose.material.icons.filled.South
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import io.github.deanalvero.hcchess.model.Position
import io.github.deanalvero.hcchess.utils.floorMod
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BoardComposable(
    viewModel: GameViewModel,
    modifier: Modifier = Modifier
) {
    val totalVirtualRows = 80085
    val centerIndex = totalVirtualRows / 2
    val listState = rememberLazyListState(initialFirstVisibleItemIndex = centerIndex - 7)
    val coroutineScope = rememberCoroutineScope()

    Column(modifier) {
        LetterRowComposable {
            IconButton(
                onClick = {
                    coroutineScope.launch {
                        listState.animateScrollBy(-300f)
                    }
                },
                modifier = Modifier.size(24.dp)
            ) {
                Icon(Icons.Default.North, "Up")
            }
        }
        InternalBoardComposable(
            viewModel = viewModel,
            listState = listState,
            totalVirtualRows = totalVirtualRows,
            centerIndex = centerIndex,
            modifier = Modifier.weight(1f)
        )
        LetterRowComposable {
            IconButton(
                onClick = {
                    coroutineScope.launch {
                        listState.animateScrollBy(300f)
                    }
                },
                modifier = Modifier.size(24.dp)
            ) {
                Icon(Icons.Default.South, "Down")
            }
        }
    }
}

@Composable
fun InternalBoardComposable(
    viewModel: GameViewModel,
    listState: LazyListState,
    totalVirtualRows: Int,
    centerIndex: Int,
    modifier: Modifier = Modifier
) {
    val coroutineScope = rememberCoroutineScope()
    BoxWithConstraints(modifier.background(Color.Gray)) {
        val squareSize = maxWidth / 8
        LazyColumn(
            state = listState,
            modifier = Modifier.fillMaxWidth()
                .pointerInput(Unit) {
                    detectDragGestures { change, dragAmount ->
                        change.consume()
                        coroutineScope.launch {
                            listState.scrollBy(-dragAmount.y)
                        }
                    }
                }
        ) {
            items(totalVirtualRows) { index ->
                val rank = floorMod(centerIndex - index, 14)

                Row(Modifier.fillMaxWidth().height(squareSize)) {
                    Box(
                        Modifier.width(24.dp).fillMaxHeight(),
                        contentAlignment = Alignment.Center
                    ) {
                        Text("${rank + 1}", fontSize = 12.sp, fontWeight = FontWeight.Bold)
                    }
                    for (file in 0..7) {
                        val pos = Position(file, rank)
                        val piece = viewModel.board.getPieceAt(pos)

                        SquareComposable(
                            position = pos,
                            piece = piece,
                            isSelected = viewModel.selectedPosition == pos,
                            isPossibleMove = pos in viewModel.validMoves,
                            onClick = { viewModel.onSquareClicked(it) },
                            modifier = Modifier.weight(1f).fillMaxHeight()
                        )
                    }
                    Box(
                        Modifier.width(24.dp).fillMaxHeight(),
                        contentAlignment = Alignment.Center
                    ) {
                        Text("${rank + 1}", fontSize = 12.sp, fontWeight = FontWeight.Bold)
                    }
                }
            }
        }
    }
}

@Composable
private fun LetterRowComposable(arrowComposable: @Composable () -> Unit) {
    Row(modifier = Modifier.fillMaxWidth().background(Color.Gray)) {
        arrowComposable.invoke()
        for (letter in 'A'..'H') {
            Box(
                Modifier.weight(1f).height(24.dp),
                contentAlignment = Alignment.Center
            ) {
                Text("$letter", fontSize = 12.sp, fontWeight = FontWeight.Bold)
            }
        }
        arrowComposable.invoke()
    }
}

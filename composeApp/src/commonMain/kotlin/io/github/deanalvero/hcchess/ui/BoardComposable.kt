package io.github.deanalvero.hcchess.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import io.github.deanalvero.hcchess.model.Position
import io.github.deanalvero.hcchess.utils.floorMod

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BoardComposable(viewModel: GameViewModel) {
    val totalVirtualRows = Int.MAX_VALUE
    val centerIndex = totalVirtualRows / 2

    val listState = rememberLazyListState(initialFirstVisibleItemIndex = centerIndex)

    Column(Modifier.fillMaxSize().background(Color.Gray)) {
        TopAppBar(
            title = { Text("Horizontal Cylinder Chess") }
        )

        LazyColumn(
            state = listState,
            modifier = Modifier.weight(1f).fillMaxWidth()
        ) {
            items(totalVirtualRows) { index ->
                val rank = floorMod(centerIndex - index, 14)

                Row(Modifier.fillMaxWidth().height(48.dp)) {
                    Box(Modifier.width(24.dp).fillMaxHeight(), contentAlignment = Alignment.Center) {
                        Text("${rank + 1}", fontSize = 10.sp)
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
                }
            }
        }

        Row(
            Modifier.fillMaxWidth().background(Color.White).padding(8.dp),
            horizontalArrangement = Arrangement.Center
        ) {
            Text("Turn: ${viewModel.currentPlayer.id}")
        }
    }
}

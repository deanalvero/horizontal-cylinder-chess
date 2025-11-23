package io.github.deanalvero.hcchess.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import io.github.deanalvero.hcchess.model.Move
import io.github.deanalvero.hcchess.utils.toDrawableResource
import org.jetbrains.compose.resources.painterResource

@Composable
fun MoveHistoryComposable(moves: List<Move>, modifier: Modifier = Modifier) {
    Column(modifier.background(Color(0xFFB58863)).fillMaxHeight().padding(16.dp)) {
        Text("Move History", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
        LazyColumn(
            modifier = Modifier.fillMaxWidth().weight(1f)
        ) {
            val pairs = moves.chunked(2)
            itemsIndexed(pairs) { index, pair ->
                val whiteMove = pair[0]
                val blackMove = pair.getOrNull(1)

                Row(
                    Modifier.fillMaxWidth().padding(vertical = 4.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text("${index + 1}.", fontWeight = FontWeight.Bold)
                    Icon(
                        painter = painterResource(whiteMove.piece.type.toDrawableResource()),
                        contentDescription = null,
                        tint = Color.White,
                        modifier = Modifier.size(24.dp)
                    )
                    Text(
                        "${whiteMove.from}-${whiteMove.to}",
                        modifier = Modifier.weight(1f)
                    )
                    if (blackMove != null) {
                        Icon(
                            painter = painterResource(blackMove.piece.type.toDrawableResource()),
                            contentDescription = null,
                            tint = Color.Black,
                            modifier = Modifier.size(24.dp)
                        )
                        Text(
                            "${blackMove.from}-${blackMove.to}",
                            modifier = Modifier.weight(1f)
                        )
                    } else {
                        Spacer(Modifier.weight(1f))
                    }
                }
            }
        }
    }
}

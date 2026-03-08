package com.juraj.gamebrowser.shared.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.juraj.gamebrowser.shared.theme.GameBrowserTheme
import com.juraj.gamebrowser.shared.theme.RatingGold
import com.juraj.gamebrowser.shared.theme.spacing

@Composable
fun RatingBadge(
    rating: Double,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .clip(RoundedCornerShape(6.dp))
            .background(RatingGold.copy(alpha = 0.15f))
            .padding(horizontal = MaterialTheme.spacing.small, vertical = MaterialTheme.spacing.xSmall),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = Icons.Default.Star,
            contentDescription = null,
            tint = RatingGold,
            modifier = Modifier.size(14.dp)
        )
        Text(
            text = " %.1f".format(rating),
            style = MaterialTheme.typography.labelMedium,
            color = RatingGold
        )
    }
}

@Preview(showBackground = true, backgroundColor = 0xFF0F0F23)
@Composable
private fun RatingBadgePreview() {
    GameBrowserTheme {
        RatingBadge(rating = 4.47)
    }
}

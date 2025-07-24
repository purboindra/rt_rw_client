package org.purboyndradev.rt_rw.features.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import compose.icons.FeatherIcons
import compose.icons.feathericons.Send

@Composable
fun CommentInputActivity(
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier.background(Color.LightGray)
            .padding(horizontal = 12.dp).systemBarsPadding(),
        contentAlignment = Alignment.Center
    ) {
        Row(
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            verticalAlignment = Alignment.CenterVertically,
            modifier = modifier      .height(48.dp)
                .padding(bottom = 6.dp)
        ) {
            Box(
                modifier = modifier
                    .weight(1f)
              
                    .background(Color.Transparent)
                    .border(1.dp, Color.Gray, RoundedCornerShape(12.dp)),
                contentAlignment = Alignment.CenterStart
            ) {
                BasicTextField(
                    value = "",
                    onValueChange = { newText ->
                    
                    },
                    textStyle = TextStyle(
                        color = MaterialTheme.colorScheme.onSurface,
                        fontSize = 14.sp
                    ),
                    singleLine = true,
                    modifier = modifier
                        .fillMaxWidth()
                        .padding(start = 16.dp),
                    decorationBox = { innerTextField ->
                        Row(
                            modifier = modifier
                                .fillMaxSize()
                                .padding(start = 16.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            innerTextField()
                        }
                    }
                )
            }
            Box(
                modifier = Modifier.height(38.dp).width(38.dp).clip(
                    CircleShape
                ).background(Color.Blue).padding(4.dp),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    FeatherIcons.Send,
                    contentDescription = "Send",
                    modifier = Modifier.size(24.dp),
                    tint = Color.White
                )
            }
        }
    }
}
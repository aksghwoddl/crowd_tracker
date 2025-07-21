package com.lee.crowdtracker.libray.design.component

import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.lee.crowdtracker.libray.design.theme.CDTheme

@Composable
fun CdInputBox(
    text: String,
    onTextChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    isShowTrailingIcon: Boolean = false,
    trailingIcon: ImageVector? = null,
    trailingIconDescription: String = "",
    onTrailingIconClick: () -> Unit = {},
    placeholder: String = "지역명을 입력하세요."
) {
    val focusRequester = remember { FocusRequester() }
    var isFocused by remember { mutableStateOf(false) }

    val borderColor by animateColorAsState(
        if (isFocused) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.tertiaryContainer
    )

    Box(
        modifier = modifier
            .fillMaxWidth()
            .border(
                width = 2.dp,
                color = borderColor,
                shape = RoundedCornerShape(8.dp)
            )
            .padding(horizontal = 12.dp, vertical = 8.dp)
    ) {
        BasicTextField(
            value = text,
            onValueChange = onTextChange,
            modifier = Modifier
                .fillMaxWidth()
                .onFocusChanged { focusState ->
                    isFocused = focusState.isFocused
                },
            singleLine = true,
            textStyle = TextStyle(
                color = Color.Black,
                fontSize = 16.sp,
                fontWeight = FontWeight.Medium
            ),
            cursorBrush = SolidColor(MaterialTheme.colorScheme.tertiaryContainer),
            decorationBox = { innerTextField ->
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Box(
                        modifier = Modifier
                            .weight(1f)
                    ) {
                        if (text.isEmpty()) {
                            Text(
                                text = placeholder,
                                style = TextStyle(
                                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                                    fontSize = 16.sp
                                ),
                                modifier = Modifier.alpha(0.6f)
                            )
                        }
                        innerTextField()
                    }

                    if (isShowTrailingIcon) {
                        IconButton(
                            onClick = onTrailingIconClick,
                            modifier = Modifier.size(32.dp)
                        ) {
                            trailingIcon?.let { icon ->
                                Icon(
                                    imageVector = icon,
                                    contentDescription = trailingIconDescription,
                                    tint = if (text.isEmpty()) MaterialTheme.colorScheme.onSurfaceVariant else MaterialTheme.colorScheme.primary
                                )
                            }
                        }
                    }
                }
            }
        )
    }
}

@Composable
@Preview
private fun CdInputBoxPreview() {
    CDTheme {
        CdInputBox(
            text = "",
            onTextChange = {},
            isShowTrailingIcon = true,
            trailingIcon = Icons.Default.Search
        )
    }
}

package com.arissantas.drill.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Checkbox
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuAnchorType
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.OffsetMapping
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.input.TransformedText
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.text.isDigitsOnly
import com.arissantas.drill.applySuggestion
import com.arissantas.drill.model.Drill
import com.arissantas.drill.normalizeDrill

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DrillEditor(
    drill: Drill,
    done: Boolean,
    checkAction: (Drill) -> Unit,
    update: (Drill) -> Unit,
    delete: (Drill) -> Unit,
    dragModifier: Modifier = Modifier,
    onDescNext: () -> Unit = {},
    descFocusRequester: FocusRequester = FocusRequester.Default,
    suggest: (String) -> List<String>,
    clearFocus: () -> Unit,
) {
  Row(
      verticalAlignment = Alignment.Top,
      modifier =
          Modifier.fillMaxWidth()
              .height(IntrinsicSize.Min)
              .border(1.dp, MaterialTheme.colorScheme.primaryContainer),
  ) {
    Row(
        modifier =
            dragModifier.fillMaxHeight().background(MaterialTheme.colorScheme.secondaryContainer)
    ) {
      Checkbox(
          modifier = Modifier.size(32.dp).padding(start = 4.dp, top = 4.dp),
          checked = done,
          onCheckedChange = {
            checkAction(drill)
            clearFocus()
          },
      )
      val minutesValue =
          remember(drill.createdAt) { // prevents cursor jumps on update
            mutableStateOf(TextFieldValue(drill.minutesStr, TextRange(drill.minutesStr.length)))
          }
      CompactTextField(
          value = minutesValue.value,
          onValueChange = { v ->
            val newMin = v.text.filter { it.isDigit() }
            minutesValue.value = v.copy(newMin)
            update(drill.copy(minutesStr = newMin))
          },
          textAlign = TextAlign.End,
          modifier = Modifier.width(48.dp).padding(top = 8.dp),
          isError = drill.minutesStr.isNotEmpty() && !drill.minutesStr.isDigitsOnly(),
          suffix = { Text("m") },
          keyboardOptions =
              KeyboardOptions(keyboardType = KeyboardType.Number, imeAction = ImeAction.Next),
      )
    }
    Row {
      val dropDownExpanded = remember { mutableStateOf(false) }
      ExposedDropdownMenuBox(
          expanded = dropDownExpanded.value,
          onExpandedChange = { dropDownExpanded.value = it },
          modifier = Modifier.weight(1f),
      ) {
        val textValue =
            remember(drill.createdAt) { // prevents cursor jumps on update
              mutableStateOf(TextFieldValue(drill.description, TextRange(drill.description.length)))
            }
        val suggestions =
            remember(dropDownExpanded.value, drill.description) {
              val cursor = textValue.value.selection.start
              val nextComma = textValue.value.text.indexOf(",", startIndex = cursor)
              if (dropDownExpanded.value)
                  suggest(
                      if (nextComma == -1) textValue.value.text
                      else textValue.value.text.take(nextComma)
                  )
              else listOf()
            }
        val colorScheme = MaterialTheme.colorScheme
        CompactTextField(
            value = textValue.value,
            placeholder = {
              Text(text = "e.g. Bach 2 courante, mm. 1-8, half tempo", fontStyle = FontStyle.Italic)
            },
            modifier =
                Modifier.padding(top = 8.dp, bottom = 4.dp)
                    .focusRequester(descFocusRequester)
                    .fillMaxWidth()
                    .onFocusChanged { foc ->
                      dropDownExpanded.value = foc.isFocused
                      if (!foc.hasFocus) {
                        update(drill.copy(description = normalizeDrill(textValue.value.text)))
                        textValue.value =
                            textValue.value.copy(text = normalizeDrill(textValue.value.text))
                      }
                    },
            onValueChange = { v ->
              textValue.value = v
              update(drill.copy(description = v.text))
            },
            visualTransformation = { text ->
              TransformedText(
                  buildAnnotatedString {
                    text.split(",").dropLast(1).forEach { segment ->
                      append(segment)
                      withStyle(SpanStyle(color = colorScheme.primaryContainer)) { append(",") }
                    }
                    append(text.split(",").last())
                  },
                  OffsetMapping.Identity,
              )
            },
            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next),
            keyboardActions = KeyboardActions(onNext = { onDescNext() }),
        )
        ExposedDropdownMenu(
            expanded = dropDownExpanded.value && suggestions.isNotEmpty(),
            onDismissRequest = {},
            modifier = Modifier.menuAnchor(ExposedDropdownMenuAnchorType.PrimaryEditable),
        ) {
          suggestions.forEach { suggestion ->
            DropdownMenuItem(
                text = { Text(suggestion) },
                onClick = {
                  val newText =
                      applySuggestion(
                          textValue.value.text,
                          textValue.value.selection.start,
                          suggestion,
                      )
                  update(drill.copy(description = newText))
                  textValue.value =
                      textValue.value.copy(text = newText, selection = TextRange(newText.length))
                  dropDownExpanded.value = true
                },
            )
          }
        }
      }
      IconButton(
          onClick = { delete(drill) },
          modifier = Modifier.size(32.dp).padding(end = 4.dp, top = 8.dp),
      ) {
        Icon(
            imageVector = Icons.Default.Close,
            contentDescription = "delete this task without completing it",
        )
      }
    }
  }
}

@Preview(showBackground = true)
@Composable
fun PreviewDrillEditor() {
  DrillEditor(
      Drill(
          createdAt = 0,
          minutesStr = "20",
          description = "bang your head against the wall, 120bpm",
      ),
      done = false,
      checkAction = {},
      update = {},
      delete = {},
      dragModifier = Modifier,
      suggest = { listOf() },
      clearFocus = {},
  )
}

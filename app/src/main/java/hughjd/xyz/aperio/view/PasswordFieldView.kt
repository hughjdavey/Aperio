package hughjd.xyz.aperio.view

import android.content.Context
import android.support.constraint.ConstraintLayout
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.EditText
import android.widget.ImageButton
import android.widget.TextView
import hughjd.xyz.aperio.R

class PasswordFieldView(context: Context, attrs: AttributeSet) : ConstraintLayout(context, attrs) {

    private val nameView: TextView

    private val valueView: TextView

    private val valueEdit: EditText

    private val copyToClipboard: ImageButton

    private val editable: Boolean

    var value: String
        get() = valueView.text.toString()
        set(value) = valueEdit.setText(value)

    val name: String
        get() = nameView.text.toString()

    init {
        val styledAttrs = context.obtainStyledAttributes(attrs, R.styleable.PasswordFieldView)

        val nameText = styledAttrs.getString(R.styleable.PasswordFieldView_name)
        val valueText = styledAttrs.getString(R.styleable.PasswordFieldView_value)

        val inflater = getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val layout = inflater.inflate(R.layout.password_field_view, this, true) as ConstraintLayout

        nameView = layout.findViewById(R.id.field_name)
        valueView = layout.findViewById(R.id.field_value_view)
        valueEdit = layout.findViewById(R.id.field_value_edit)
        copyToClipboard = layout.findViewById(R.id.copy_to_clipboard)

        nameView.text = nameText
        valueView.text = valueText
        valueEdit.setText(valueText)

        editable = styledAttrs.getBoolean(R.styleable.PasswordFieldView_editable, false)
        if (editable) {
            valueView.visibility = View.GONE
            copyToClipboard.visibility = View.GONE
        }
        else {
            valueEdit.visibility = View.GONE
        }

        styledAttrs.recycle()
    }
}

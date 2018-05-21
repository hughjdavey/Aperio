package hughjd.xyz.aperio.activity

import android.app.Activity
import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.FrameLayout
import android.widget.ImageButton
import android.widget.Toast
import com.getbase.floatingactionbutton.FloatingActionButton
import com.getbase.floatingactionbutton.FloatingActionsMenu
import hughjd.xyz.aperio.R
import hughjd.xyz.aperio.password.Password
import hughjd.xyz.aperio.view.PasswordFieldView

class PasswordView : AperioActivity() {

    private lateinit var usernameField: PasswordFieldView
    private lateinit var emailField: PasswordFieldView
    private lateinit var passwordField: PasswordFieldView
    private lateinit var urlField: PasswordFieldView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_password_view)

        val viewedPassword = intent?.extras?.getSerializable(Password.BUNDLE_KEY) as Password? ?: Password.empty()
        title = viewedPassword.name
        initFields(viewedPassword)

        // copy value to clipboard when copy button is pressed
        listOf(usernameField, emailField, urlField).forEach { fieldView ->
            val copyButton = fieldView.findViewById<ImageButton>(R.id.copy_to_clipboard)
            copyButton.setOnClickListener { onCopy(fieldView.name.toLowerCase(), fieldView.value) }
        }

        // needs special handling because value is '*********' and we don't want to copy that to clipboard
        val copyPasswordButton = passwordField.findViewById<ImageButton>(R.id.copy_to_clipboard)
        copyPasswordButton.setOnClickListener { onCopy("password", viewedPassword.password) }

        // allow clicking anywhere to collapse the fab menu
        val fabMenu = findViewById<FloatingActionsMenu>(R.id.view_password_fab_menu)
        val frameLayout = findViewById<FrameLayout>(R.id.view_password_frame_layout)
        frameLayout.setOnClickListener { fabMenu.collapse() }

        // password edit and delete listeners
        val fabEdit = findViewById<FloatingActionButton>(R.id.view_password_fab_edit)
        fabEdit.setOnClickListener { onEdit(viewedPassword) }
        val fabDelete = findViewById<FloatingActionButton>(R.id.view_password_fab_delete)
        fabDelete.setOnClickListener { onDelete(viewedPassword) }
    }

    private fun onCopy(name: String, value: String) {
        val clipboard = getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
        clipboard.primaryClip = ClipData.newPlainText(name, value)
        Toast.makeText(this, "Copied to clipboard", Toast.LENGTH_SHORT).show()
    }

    private fun onEdit(password: Password) {
        val editPasswordIntent = Intent(this, PasswordEdit::class.java)
        editPasswordIntent.putExtra(Password.BUNDLE_KEY, password)
        startActivity(editPasswordIntent)
    }

    private fun onDelete(password: Password) {
        val returnIntent = Intent()
        returnIntent.putExtra(Password.BUNDLE_KEY, password)
        setResult(Activity.RESULT_OK, returnIntent)
        finish()
    }

    private fun initFields(viewedPassword: Password) {
        usernameField = findViewById(R.id.view_username)
        emailField = findViewById(R.id.view_email)
        passwordField = findViewById(R.id.view_password)
        urlField = findViewById(R.id.view_url)

        usernameField.value = viewedPassword.username
        emailField.value = viewedPassword.email
        passwordField.value = getString(R.string.hidden_password)
        urlField.value = viewedPassword.url
    }
}

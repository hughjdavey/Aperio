package hughjd.xyz.aperio.activity

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Button
import android.widget.CheckBox
import android.widget.NumberPicker
import android.widget.TextView
import android.widget.Toast
import com.afollestad.materialdialogs.MaterialDialog
import com.getbase.floatingactionbutton.FloatingActionButton
import hughjd.xyz.aperio.R
import hughjd.xyz.aperio.password.Password
import hughjd.xyz.aperio.password.PasswordGenerator
import hughjd.xyz.aperio.view.PasswordFieldView
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import timber.log.Timber

class PasswordEdit : AppCompatActivity() {

    private lateinit var nameField: PasswordFieldView
    private lateinit var usernameField: PasswordFieldView
    private lateinit var emailField: PasswordFieldView
    private lateinit var passwordField: PasswordFieldView
    private lateinit var urlField: PasswordFieldView

    private lateinit var dialogLengthPicker: NumberPicker
    private lateinit var dialogPassword: TextView
    private lateinit var dialogLetters: CheckBox
    private lateinit var dialogNumbers: CheckBox
    private lateinit var dialogSymbols: CheckBox
    private lateinit var dialogErrorText: TextView

    private lateinit var originalPassword: Password

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_password_edit)

        originalPassword = intent?.extras?.getSerializable(Password.BUNDLE_KEY) as Password? ?: Password.empty()
        Timber.e("Original password: %s", originalPassword)
        val editing = originalPassword != Password.empty()
        title = if (editing) "Editing " + originalPassword.name else "New Password"
        initFields()

        val save = findViewById<FloatingActionButton>(R.id.edit_password_fab_save)
        save.setOnClickListener { onSave(editing) }

        val discard = findViewById<FloatingActionButton>(R.id.edit_password_fab_discard)
        discard.setOnClickListener{ onDiscard() }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_password_edit, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId) {
            R.id.generate_password -> generatePasswordDialog()
            else -> return super.onOptionsItemSelected(item)
        }
        return true
    }

    private fun onSave(editing: Boolean) {
        val edited = Password(originalPassword.id, nameField.value, passwordField.value, emailField.value, urlField.value, usernameField.value)

        if (edited == originalPassword) {
            Toast.makeText(this, "No changes made", Toast.LENGTH_SHORT).show()
        }
        else {
            saveOrUpdate(edited, editing)
            Toast.makeText(this, "Changes saved", Toast.LENGTH_SHORT).show()
        }
        finish()
    }

    private fun saveOrUpdate(password: Password, editing: Boolean) {
        Single.fromCallable {
            if (editing) Aperio.db?.passwordDao()?.update(password) else Aperio.db?.passwordDao()?.insert(password)
        }
        .subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread()).subscribe()
    }

    private fun onDiscard() {
        Toast.makeText(this, "Changes discarded", Toast.LENGTH_SHORT).show()
        finish()
    }

    private fun generatePasswordDialog() {
        val dialog = MaterialDialog.Builder(this)
                .title("Generate Password")
                .customView(R.layout.dialog_generate_password, true)
                .positiveText("Accept")
                .neutralText("Generate")
                .onPositive { dialog, _ -> passwordField.value = dialogPassword.text.toString(); dialog.dismiss(); }
                .onNeutral { _, _ -> dialogPassword.text = generatePassword() }
                .autoDismiss(false)
                .build()

        initializeDialogViews(dialog.customView)
        dialog.show()
    }

    private fun generatePassword(): String {
        val chartypes = PasswordGenerator.chartypes(dialogLetters.isChecked, dialogNumbers.isChecked, dialogSymbols.isChecked)
        if (chartypes.isEmpty()) {
            dialogErrorText.visibility = View.VISIBLE
            return dialogPassword.text.toString()
        }

        dialogErrorText.visibility = View.GONE
        return PasswordGenerator.generatePassword(dialogLengthPicker.value, chartypes)
    }

    private fun initializeDialogViews(view: View?) {
        dialogLengthPicker = view?.findViewById(R.id.password_length_picker) as NumberPicker
        dialogLengthPicker.minValue = 8
        dialogLengthPicker.maxValue = 32
        dialogLengthPicker.wrapSelectorWheel = true
        dialogPassword = view.findViewById(R.id.generated_password) as TextView
        dialogLetters = view.findViewById(R.id.charset_letters) as CheckBox
        dialogNumbers = view.findViewById(R.id.charset_numbers) as CheckBox
        dialogSymbols = view.findViewById(R.id.charset_symbols) as CheckBox
        dialogErrorText = view.findViewById(R.id.generate_error_text) as TextView
    }

    private fun initFields() {
        nameField = findViewById(R.id.edit_name)
        usernameField = findViewById(R.id.edit_username)
        emailField = findViewById(R.id.edit_email)
        passwordField = findViewById(R.id.edit_password)
        urlField = findViewById(R.id.edit_url)

        nameField.value = originalPassword.name
        usernameField.value = originalPassword.username
        emailField.value = originalPassword.email
        passwordField.value = originalPassword.password
        urlField.value = originalPassword.url
    }
}

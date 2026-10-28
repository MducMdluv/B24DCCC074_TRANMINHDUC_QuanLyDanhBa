package com.example.quanlydanhba;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

/** Màn hình 3: Thêm liên hệ mới. */
public class AddContactActivity extends AppCompatActivity {

    private EditText etName;
    private EditText etPhone;
    private Spinner spGroup;
    private RadioGroup rgGender;
    private CheckBox cbFavorite;
    private EditText etNote;
    private Button btnSave;
    private ImageView btnBackHeader;

    private ArrayList<String> existingPhones = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_contact);

        btnBackHeader = findViewById(R.id.btnBackHeader);
        etName = findViewById(R.id.etName);
        etPhone = findViewById(R.id.etPhone);
        spGroup = findViewById(R.id.spGroup);
        rgGender = findViewById(R.id.rgGender);
        cbFavorite = findViewById(R.id.cbFavorite);
        etNote = findViewById(R.id.etNote);
        btnSave = findViewById(R.id.btnSave);

        btnBackHeader.setOnClickListener(v -> finish());

        ArrayList<String> phones = getIntent().getStringArrayListExtra(MainActivity.EXTRA_PHONES);
        if (phones != null) {
            existingPhones = phones;
        }

        ArrayAdapter<CharSequence> groupAdapter = ArrayAdapter.createFromResource(
                this, R.array.groups, android.R.layout.simple_spinner_item);
        groupAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spGroup.setAdapter(groupAdapter);

        btnSave.setOnClickListener(v -> saveContact());
    }

    private void saveContact() {
        String name = etName.getText().toString().trim();
        String rawPhone = etPhone.getText().toString().trim();
        String cleanPhone = rawPhone.replaceAll("[\\s.-]+", "");

        if (TextUtils.isEmpty(name)) {
            etName.setError(getString(R.string.error_name_empty));
            etName.requestFocus();
            return;
        }

        // 10 chữ số và bắt đầu bằng 0
        if (!cleanPhone.matches("0\\d{9}")) {
            etPhone.setError(getString(R.string.error_phone_invalid));
            etPhone.requestFocus();
            return;
        }

        if (existingPhones.contains(cleanPhone)) {
            etPhone.setError(getString(R.string.error_phone_duplicate));
            etPhone.requestFocus();
            return;
        }

        String group = spGroup.getSelectedItem() != null ? spGroup.getSelectedItem().toString() : "Bạn bè";

        String gender = "Nam";
        int checkedId = rgGender.getCheckedRadioButtonId();
        if (checkedId != -1) {
            RadioButton rb = findViewById(checkedId);
            if (rb != null) {
                gender = rb.getText().toString();
            }
        }

        boolean favorite = cbFavorite.isChecked();
        String note = etNote.getText().toString().trim();

        Contact contact = new Contact(name, cleanPhone, group, gender, favorite, note);

        Intent result = new Intent();
        result.putExtra(MainActivity.EXTRA_CONTACT, contact);
        setResult(RESULT_OK, result);
        Toast.makeText(this, R.string.msg_saved, Toast.LENGTH_SHORT).show();
        finish();
    }
}

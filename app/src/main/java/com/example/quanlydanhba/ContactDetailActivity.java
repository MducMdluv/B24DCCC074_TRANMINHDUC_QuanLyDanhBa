package com.example.quanlydanhba;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

/** Màn hình 4: Chi tiết liên hệ. */
public class ContactDetailActivity extends AppCompatActivity {

    private Contact contact;
    private int position;
    private TextView btnToggleFavorite;
    private boolean isFavorite;

    @Override
    @SuppressWarnings("deprecation")
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact_detail);

        contact = (Contact) getIntent().getSerializableExtra(MainActivity.EXTRA_CONTACT);
        position = getIntent().getIntExtra(MainActivity.EXTRA_POSITION, -1);
        if (contact == null) {
            finish();
            return;
        }

        isFavorite = contact.isFavorite();

        ImageView btnBackHeader = findViewById(R.id.btnBackHeader);
        TextView tvAvatar = findViewById(R.id.tvDetailAvatar);
        TextView tvName = findViewById(R.id.tvDetailName);
        TextView tvPhone = findViewById(R.id.tvDetailPhone);
        TextView tvGroup = findViewById(R.id.tvDetailGroup);
        TextView tvGender = findViewById(R.id.tvDetailGender);
        TextView tvNote = findViewById(R.id.tvDetailNote);
        btnToggleFavorite = findViewById(R.id.btnToggleFavorite);
        Button btnCall = findViewById(R.id.btnCall);
        Button btnBack = findViewById(R.id.btnBack);
        Button btnDelete = findViewById(R.id.btnDelete);

        // Header back navigation
        btnBackHeader.setOnClickListener(v -> finish());

        // Hiển thị Avatar & Tên & SĐT
        String initial = contact.getInitial();
        tvAvatar.setText(initial);
        tvAvatar.setBackground(ContactAdapter.createAvatarDrawable(initial));

        tvName.setText(contact.getName());
        tvPhone.setText(contact.getFormattedPhone());
        tvGroup.setText(contact.getGroup());
        tvGender.setText(contact.getGender());

        tvNote.setText(TextUtils.isEmpty(contact.getNote())
                ? getString(R.string.no_note)
                : contact.getNote());

        updateFavoriteBadgeUI();

        // Chạm vào badge để chuyển đổi yêu thích
        btnToggleFavorite.setOnClickListener(v -> {
            isFavorite = !isFavorite;
            contact.setFavorite(isFavorite);
            updateFavoriteBadgeUI();

            Intent result = new Intent();
            result.putExtra(MainActivity.EXTRA_POSITION, position);
            result.putExtra(MainActivity.EXTRA_FAVORITE, isFavorite);
            result.putExtra(MainActivity.EXTRA_DELETED, false);
            setResult(RESULT_OK, result);
        });

        // Intent ngầm định ACTION_DIAL
        btnCall.setOnClickListener(v -> {
            Intent dial = new Intent(Intent.ACTION_DIAL);
            dial.setData(Uri.parse("tel:" + contact.getPhone()));
            try {
                startActivity(dial);
            } catch (ActivityNotFoundException e) {
                Toast.makeText(this, R.string.error_no_dialer, Toast.LENGTH_SHORT).show();
            }
        });

        btnBack.setOnClickListener(v -> finish());

        btnDelete.setOnClickListener(v -> new AlertDialog.Builder(this)
                .setTitle(R.string.delete_title)
                .setMessage(getString(R.string.delete_message, contact.getName()))
                .setPositiveButton(R.string.delete, (dialog, which) -> {
                    Intent result = new Intent();
                    result.putExtra(MainActivity.EXTRA_POSITION, position);
                    result.putExtra(MainActivity.EXTRA_DELETED, true);
                    setResult(RESULT_OK, result);
                    finish();
                })
                .setNegativeButton(R.string.cancel, null)
                .show());
    }

    private void updateFavoriteBadgeUI() {
        GradientDrawable bg = new GradientDrawable();
        bg.setShape(GradientDrawable.RECTANGLE);
        bg.setCornerRadius(dpToPx(6));

        if (isFavorite) {
            bg.setColor(Color.parseColor("#005954")); // Deep teal
            btnToggleFavorite.setBackground(bg);
            btnToggleFavorite.setTextColor(Color.WHITE);
            btnToggleFavorite.setText(R.string.favorite_badge_text);
        } else {
            bg.setColor(Color.parseColor("#F3F4F6")); // Light grey
            bg.setStroke((int) dpToPx(1), Color.parseColor("#D1D5DB"));
            btnToggleFavorite.setBackground(bg);
            btnToggleFavorite.setTextColor(Color.parseColor("#4B5563"));
            btnToggleFavorite.setText(R.string.not_favorite_badge_text);
        }
    }

    private float dpToPx(float dp) {
        return dp * getResources().getDisplayMetrics().density;
    }
}

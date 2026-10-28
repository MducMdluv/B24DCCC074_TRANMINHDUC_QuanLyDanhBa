package com.example.quanlydanhba;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

/** Màn hình 2: Danh sách liên hệ + thống kê. */
public class MainActivity extends AppCompatActivity {

    public static final String EXTRA_CONTACT = "contact";
    public static final String EXTRA_POSITION = "position";
    public static final String EXTRA_PHONES = "phones";
    public static final String EXTRA_DELETED = "deleted";
    public static final String EXTRA_FAVORITE = "favorite";

    private final ArrayList<Contact> contacts = new ArrayList<>();
    private ContactAdapter adapter;
    private TextView tvTotalCount;
    private TextView tvFavoriteCount;
    private TextView tvEmpty;
    private TextView tvListHint;

    // Nhận kết quả từ màn hình Thêm liên hệ
    private final ActivityResultLauncher<Intent> addLauncher =
            registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
                if (result.getResultCode() == RESULT_OK && result.getData() != null) {
                    @SuppressWarnings("deprecation")
                    Contact newContact = (Contact) result.getData().getSerializableExtra(EXTRA_CONTACT);
                    if (newContact != null) {
                        contacts.add(newContact);
                        refresh();
                    }
                }
            });

    // Nhận kết quả từ màn hình Chi tiết (xóa / đổi yêu thích)
    private final ActivityResultLauncher<Intent> detailLauncher =
            registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
                if (result.getResultCode() == RESULT_OK && result.getData() != null) {
                    Intent data = result.getData();
                    int position = data.getIntExtra(EXTRA_POSITION, -1);
                    if (position < 0 || position >= contacts.size()) {
                        return;
                    }
                    if (data.getBooleanExtra(EXTRA_DELETED, false)) {
                        contacts.remove(position);
                    } else {
                        contacts.get(position).setFavorite(data.getBooleanExtra(EXTRA_FAVORITE, false));
                    }
                    refresh();
                }
            });

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tvTotalCount = findViewById(R.id.tvTotalCount);
        tvFavoriteCount = findViewById(R.id.tvFavoriteCount);
        tvEmpty = findViewById(R.id.tvEmpty);
        tvListHint = findViewById(R.id.tvListHint);
        ListView lvContacts = findViewById(R.id.lvContacts);
        Button btnAdd = findViewById(R.id.btnAddContact);

        loadSampleData();

        adapter = new ContactAdapter(this, contacts);
        lvContacts.setAdapter(adapter);

        lvContacts.setOnItemClickListener((parent, view, position, id) -> {
            Intent intent = new Intent(MainActivity.this, ContactDetailActivity.class);
            intent.putExtra(EXTRA_CONTACT, contacts.get(position));
            intent.putExtra(EXTRA_POSITION, position);
            detailLauncher.launch(intent);
        });

        btnAdd.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, AddContactActivity.class);
            // Gửi danh sách số điện thoại hiện có để kiểm tra trùng
            ArrayList<String> phones = new ArrayList<>();
            for (Contact c : contacts) {
                phones.add(c.getPhone().replaceAll("\\s+", ""));
            }
            intent.putStringArrayListExtra(EXTRA_PHONES, phones);
            addLauncher.launch(intent);
        });

        updateStats();
    }

    private void loadSampleData() {
        contacts.add(new Contact("Nguyễn Văn An", "0901234567", "Bạn bè", "Nam", false, ""));
        contacts.add(new Contact("Trần Thị Bình", "0912345678", "Gia đình", "Nữ", true, "Chị gái, hay gọi buổi tối"));
        contacts.add(new Contact("Lê Minh Châu", "0987654321", "Công việc", "Nam", false, ""));
    }

    private void refresh() {
        adapter.notifyDataSetChanged();
        updateStats();
    }

    private void updateStats() {
        int total = contacts.size();
        int favorites = 0;
        for (Contact c : contacts) {
            if (c.isFavorite()) {
                favorites++;
            }
        }
        tvTotalCount.setText(String.valueOf(total));
        tvFavoriteCount.setText(String.valueOf(favorites));

        if (total == 0) {
            tvEmpty.setVisibility(View.VISIBLE);
            tvListHint.setVisibility(View.GONE);
        } else {
            tvEmpty.setVisibility(View.GONE);
            tvListHint.setVisibility(View.VISIBLE);
        }
    }
}

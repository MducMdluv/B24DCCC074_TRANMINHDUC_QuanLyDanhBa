package com.example.quanlydanhba;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;

import java.util.List;

/** Adapter hiển thị mỗi liên hệ trên một card của ListView. */
public class ContactAdapter extends ArrayAdapter<Contact> {

    private final LayoutInflater inflater;
    private final Context context;

    public ContactAdapter(@NonNull Context context, @NonNull List<Contact> contacts) {
        super(context, 0, contacts);
        this.context = context;
        this.inflater = LayoutInflater.from(context);
    }

    private static class ViewHolder {
        TextView tvAvatar;
        TextView tvName;
        TextView tvPhone;
        TextView tvGroup;
        ImageView ivStar;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.item_contact, parent, false);
            holder = new ViewHolder();
            holder.tvAvatar = convertView.findViewById(R.id.tvAvatar);
            holder.tvName = convertView.findViewById(R.id.tvItemName);
            holder.tvPhone = convertView.findViewById(R.id.tvItemPhone);
            holder.tvGroup = convertView.findViewById(R.id.tvItemGroup);
            holder.ivStar = convertView.findViewById(R.id.ivItemStar);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        Contact contact = getItem(position);
        if (contact != null) {
            String initial = contact.getInitial();
            holder.tvAvatar.setText(initial);
            holder.tvAvatar.setBackground(createAvatarDrawable(initial));

            holder.tvName.setText(contact.getName());
            holder.tvPhone.setText(contact.getFormattedPhone());

            // Nhóm và màu badge
            String group = contact.getGroup() != null ? contact.getGroup() : "";
            holder.tvGroup.setText(group);
            applyGroupBadgeStyle(holder.tvGroup, group);

            // Ngôi sao yêu thích
            holder.ivStar.setVisibility(contact.isFavorite() ? View.VISIBLE : View.GONE);
        }
        return convertView;
    }

    /** Tạo hình tròn avatar với màu sắc chuẩn theo thiết kế. */
    public static GradientDrawable createAvatarDrawable(String initial) {
        GradientDrawable shape = new GradientDrawable();
        shape.setShape(GradientDrawable.OVAL);
        int color;
        if ("A".equalsIgnoreCase(initial)) {
            color = Color.parseColor("#00695C"); // Teal đậm
        } else if ("B".equalsIgnoreCase(initial)) {
            color = Color.parseColor("#C84B16"); // Cam cháy / Nâu đỏ
        } else if ("C".equalsIgnoreCase(initial)) {
            color = Color.parseColor("#5E35B1"); // Tím đậm
        } else {
            // Danh sách màu hài hòa cho các chữ cái khác
            int[] palette = new int[] {
                Color.parseColor("#00695C"),
                Color.parseColor("#C84B16"),
                Color.parseColor("#5E35B1"),
                Color.parseColor("#1565C0"),
                Color.parseColor("#2E7D32"),
                Color.parseColor("#D81B60"),
                Color.parseColor("#00838F")
            };
            int index = Math.abs(initial.hashCode()) % palette.length;
            color = palette[index];
        }
        shape.setColor(color);
        return shape;
    }

    /** Định kiểu màu sắc badge cho từng nhóm theo mẫu thiết kế. */
    private void applyGroupBadgeStyle(TextView tvGroup, String group) {
        GradientDrawable bg = new GradientDrawable();
        bg.setShape(GradientDrawable.RECTANGLE);
        bg.setCornerRadius(dpToPx(10));

        int bgColor;
        int textColor;

        if ("Bạn bè".equalsIgnoreCase(group)) {
            bgColor = ContextCompat.getColor(context, R.color.badge_banbe_bg);
            textColor = ContextCompat.getColor(context, R.color.badge_banbe_text);
        } else if ("Gia đình".equalsIgnoreCase(group)) {
            bgColor = ContextCompat.getColor(context, R.color.badge_giadinh_bg);
            textColor = ContextCompat.getColor(context, R.color.badge_giadinh_text);
        } else if ("Công việc".equalsIgnoreCase(group)) {
            bgColor = ContextCompat.getColor(context, R.color.badge_congviec_bg);
            textColor = ContextCompat.getColor(context, R.color.badge_congviec_text);
        } else {
            bgColor = ContextCompat.getColor(context, R.color.badge_khac_bg);
            textColor = ContextCompat.getColor(context, R.color.badge_khac_text);
        }

        bg.setColor(bgColor);
        tvGroup.setBackground(bg);
        tvGroup.setTextColor(textColor);
    }

    private float dpToPx(float dp) {
        return dp * context.getResources().getDisplayMetrics().density;
    }
}

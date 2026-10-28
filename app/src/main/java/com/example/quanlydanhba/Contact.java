package com.example.quanlydanhba;

import java.io.Serializable;

/**
 * Lớp biểu diễn một liên hệ trong danh bạ.
 * Implements Serializable để truyền qua Intent.
 */
public class Contact implements Serializable {
    private static final long serialVersionUID = 1L;

    private String name;
    private String phone;
    private String group;
    private String gender;
    private boolean favorite;
    private String note;

    public Contact(String name, String phone, String group, String gender,
                   boolean favorite, String note) {
        this.name = name;
        this.phone = phone;
        this.group = group;
        this.gender = gender;
        this.favorite = favorite;
        this.note = note;
    }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }

    public String getGroup() { return group; }
    public void setGroup(String group) { this.group = group; }

    public String getGender() { return gender; }
    public void setGender(String gender) { this.gender = gender; }

    public boolean isFavorite() { return favorite; }
    public void setFavorite(boolean favorite) { this.favorite = favorite; }

    public String getNote() { return note; }
    public void setNote(String note) { this.note = note; }

    /** Chữ cái đầu của tên (chữ cuối trong họ tên, theo cách đặt tên Việt Nam), viết hoa. */
    public String getInitial() {
        if (name == null || name.trim().isEmpty()) {
            return "?";
        }
        String trimmed = name.trim();
        int lastSpace = trimmed.lastIndexOf(' ');
        String lastWord = lastSpace >= 0 ? trimmed.substring(lastSpace + 1) : trimmed;
        return lastWord.substring(0, 1).toUpperCase();
    }

    /** Định dạng số điện thoại hiển thị đẹp mắt (0XXX XXX XXX). */
    public String getFormattedPhone() {
        if (phone == null) return "";
        String clean = phone.replaceAll("\\s+", "");
        if (clean.length() == 10) {
            return clean.substring(0, 4) + " " + clean.substring(4, 7) + " " + clean.substring(7);
        }
        return phone;
    }
}

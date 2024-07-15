package com.example.appnote;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.appnote.dal.FirebaseHelper;
import com.example.appnote.fragment.FragmentAddColor;
import com.example.appnote.model.Note;
import com.google.firebase.FirebaseApp;

public class Update_Delete_Ghi_Chu_Activity extends AppCompatActivity implements View.OnClickListener {
    private EditText eTieude,eGhichu;
    private ImageButton btUpdate,btDelete,btBack,btAddColor;
    private Note note;
    private View view;
    private Integer color;
    private FirebaseHelper firebaseHelper;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_delete_ghi_chu);
        initView();
        FirebaseApp.initializeApp(this);
        firebaseHelper = new FirebaseHelper();
        btUpdate.setOnClickListener(this);
        btDelete.setOnClickListener(this);
        btBack.setOnClickListener(this);
        Intent intent = getIntent();
        note = (Note) intent.getSerializableExtra("note");
        eTieude.setText(note.getTieude());
        eGhichu.setText(note.getGhichu());
        view.setBackgroundColor(note.getColor());
        color = note.getColor();
        btAddColor.setOnClickListener(view3 -> {
            FragmentAddColor fragmentAddColor = new FragmentAddColor();
            fragmentAddColor.setAddColorListener(x -> {
                view.setBackgroundColor(x);
                color = x;
            });
            fragmentAddColor.show(getSupportFragmentManager(), null);
        });
    }

    private void initView() {
        eTieude = findViewById(R.id.tvTieude);
        eGhichu = findViewById(R.id.tvGhichu);
        btUpdate = findViewById(R.id.btSave);
        btDelete = findViewById(R.id.btDelete);
        btBack = findViewById(R.id.btBack);
        btAddColor = findViewById(R.id.btAdd_color);
        view = findViewById(R.id.backgroundcolor);
    }

    @Override
    public void onClick(View view) {
        if (view == btBack) {
            finish();
        }
        if (view == btUpdate) {
            String tieude = eTieude.getText().toString();
            String ghichu = eGhichu.getText().toString();
            String dateTime = note.getDateTime(); // giữ nguyên dateTime hiện tại hoặc cập nhật tùy theo logic của bạn
            if (!tieude.isEmpty() && !ghichu.isEmpty()) {
                note.setTieude(tieude);
                note.setGhichu(ghichu);
                note.setColor(color);
                firebaseHelper.updateNote(note);
                finish();
            }
        }
        if (view == btDelete) {
            AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext());
            builder.setTitle("Thông báo xóa");
            builder.setMessage("Bạn có muốn xóa " + note.getTieude() + " không?");
            builder.setIcon(R.drawable.ic_thung_rac);
            builder.setPositiveButton("Có", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    firebaseHelper.markAsDeleted(note);
                    finish();
                }
            });
            builder.setNegativeButton("Không", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                }
            });
            AlertDialog dialog = builder.create();
            dialog.show();
        }
    }
}

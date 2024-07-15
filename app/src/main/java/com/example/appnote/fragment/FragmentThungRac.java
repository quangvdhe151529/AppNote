package com.example.appnote.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.appnote.R;
import com.example.appnote.Restore_Activity;
import com.example.appnote.adapter.RecycleViewAdapter;
import com.example.appnote.dal.FirebaseHelper;
import com.example.appnote.model.Note;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.List;

public class FragmentThungRac extends Fragment implements RecycleViewAdapter.NoteListener{
    private RecycleViewAdapter adapter;
    private RecyclerView recyclerView;
    private FloatingActionButton delete;
    private FirebaseHelper firebaseHelper;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_thungrac,container,false);
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        recyclerView = view.findViewById(R.id.rcv_thungrac);
        adapter = new RecycleViewAdapter();
        firebaseHelper = new FirebaseHelper();

        // Đọc danh sách các ghi chú đã đánh dấu xóa từ Firebase
        firebaseHelper.readDeletedNotes(new FirebaseHelper.DataStatus() {
            @Override
            public void DataIsLoaded(List<Note> notes, List<String> keys) {
                adapter.setList(notes);
            }

            @Override
            public void DataIsInserted() {}

            @Override
            public void DataIsUpdated() {}

            @Override
            public void DataIsDeleted() {}
        });

        LinearLayoutManager manager = new LinearLayoutManager(getContext(), RecyclerView.VERTICAL, false);
        recyclerView.setLayoutManager(manager);
        recyclerView.setAdapter(adapter);
        adapter.setNoteListener(this);

        delete = view.findViewById(R.id.delete);
        delete.setOnClickListener(v -> {
            // Xóa các ghi chú đã đánh dấu xóa khỏi Firebase
            List<Note> deletedNotes = adapter.getList();
            for (Note n : deletedNotes) {
                firebaseHelper.deletePermanently(n);
            }
            adapter.setList(null);
        });
    }
    @Override
    public void onNoteClick(View view, int position) {
        Note ghiChu = adapter.getNote(position);
        Intent intent = new Intent(getActivity(), Restore_Activity.class);
        intent.putExtra("note", ghiChu);
        startActivityForResult(intent, 1);
    }

    @Override
    public void onResume() {
        super.onResume();
        // Cập nhật lại danh sách các ghi chú đã đánh dấu xóa khi quay lại fragment
        firebaseHelper.readDeletedNotes(new FirebaseHelper.DataStatus() {
            @Override
            public void DataIsLoaded(List<Note> notes, List<String> keys) {
                adapter.setList(notes);
            }

            @Override
            public void DataIsInserted() {}

            @Override
            public void DataIsUpdated() {}

            @Override
            public void DataIsDeleted() {}
        });
    }
}

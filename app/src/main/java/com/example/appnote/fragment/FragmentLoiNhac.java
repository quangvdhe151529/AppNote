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
import com.example.appnote.AddLoiNhacAcitivity;
import com.example.appnote.R;
import com.example.appnote.Update_Delete_Loi_Nhac_Activity;
import com.example.appnote.adapter.RecycleViewAdapter;
import com.example.appnote.dal.FirebaseHelper;
import com.example.appnote.model.Note;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import java.util.ArrayList;
import java.util.List;

public class FragmentLoiNhac extends Fragment implements RecycleViewAdapter.NoteListener{

    private RecyclerView recyclerView;
    private RecycleViewAdapter adapter;
    private FloatingActionButton add;
    private FirebaseHelper firebaseHelper;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_loinhac,container,false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        recyclerView = view.findViewById(R.id.rcv_loinhacs);
        adapter = new RecycleViewAdapter();
        firebaseHelper = new FirebaseHelper();

        recyclerView.setLayoutManager(new LinearLayoutManager(getContext(), RecyclerView.VERTICAL, false));
        recyclerView.setAdapter(adapter);
        adapter.setNoteListener(this);

        add = view.findViewById(R.id.add);
        add.setOnClickListener(v -> {
            Intent intent = new Intent(getContext(), AddLoiNhacAcitivity.class);
            getActivity().startActivity(intent);
        });

        loadNotes();
    }

    private void loadNotes() {
        firebaseHelper.readNotes(new FirebaseHelper.DataStatus() {
            @Override
            public void DataIsLoaded(List<Note> notes, List<String> keys) {
                List<Note> listchuaxoa = new ArrayList<>();
                for (Note ln : notes) {
                    if (!ln.getDaxoa() && ln.getDateTime() != null) {
                        listchuaxoa.add(ln);
                    }
                }
                adapter.setList(listchuaxoa);
            }

            @Override
            public void DataIsInserted() {
                // Not used in this context
            }

            @Override
            public void DataIsUpdated() {
                // Not used in this context
            }

            @Override
            public void DataIsDeleted() {
                // Not used in this context
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        loadNotes();
    }

    @Override
    public void onNoteClick(View view, int position) {
        Note loinhac = adapter.getNote(position);
        Intent intent = new Intent(getActivity(), Update_Delete_Loi_Nhac_Activity.class);
        intent.putExtra("note", loinhac);
        startActivity(intent);
    }
}

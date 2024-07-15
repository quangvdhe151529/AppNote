package com.example.appnote.dal;

import androidx.annotation.NonNull;

import com.example.appnote.model.Note;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class FirebaseHelper {
    private DatabaseReference mDatabase;
    public interface NoteSearchListener {
        void onNoteSearch(List<Note> list);
    }

    public FirebaseHelper() {
        mDatabase = FirebaseDatabase.getInstance().getReference("notes");
    }
    public interface DataStatus {
        void DataIsLoaded(List<Note> notes, List<String> keys);
        void DataIsInserted();
        void DataIsUpdated();
        void DataIsDeleted();
    }
    public void addNote(Note note) {
        // Lấy id mới cho note
        String id = mDatabase.push().getKey();
        note.setId(id);

        // Thêm vào Firebase
        mDatabase.child(id).setValue(note);
    }
    public void updateNote(Note note) {
        mDatabase.child(note.getId()).setValue(note);
    }
    public void deletePermanently(Note note) {
        mDatabase.child(note.getId()).removeValue();
    }
    public void markAsDeleted(Note note) {
        note.setDaxoa(true);
        mDatabase.child(note.getId()).setValue(note);
    }
    public void readNotes(final DataStatus dataStatus) {
        mDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                List<Note> notes = new ArrayList<>();
                List<String> keys = new ArrayList<>();
                for (DataSnapshot keyNode : dataSnapshot.getChildren()) {
                    keys.add(keyNode.getKey());
                    Note note = keyNode.getValue(Note.class);
                    notes.add(note);
                }
                dataStatus.DataIsLoaded(notes, keys);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Log error
            }
        });
    }
    public void readDeletedNotes(final DataStatus dataStatus) {
        mDatabase.orderByChild("daxoa").equalTo(true).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                List<Note> notes = new ArrayList<>();
                List<String> keys = new ArrayList<>();
                for (DataSnapshot keyNode : dataSnapshot.getChildren()) {
                    keys.add(keyNode.getKey());
                    Note note = keyNode.getValue(Note.class);
                    notes.add(note);
                }
                dataStatus.DataIsLoaded(notes, keys);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Log error
            }
        });
    }
    public void searchNotesByTieuDe(String key, NoteSearchListener listener) {
        Query query = mDatabase.orderByChild("tieude").startAt(key).endAt(key + "\uf8ff");
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                List<Note> list = new ArrayList<>();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    Note note = dataSnapshot.getValue(Note.class);
                    if (note != null && note.getDaxoa() == false) {
                        list.add(note);
                    }
                }
                listener.onNoteSearch(list);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Handle errors here
            }
        });
    }

    public void searchNotesByDate(String from, String to, NoteSearchListener listener) {
        Query query = mDatabase.orderByChild("dateTime").startAt(from).endAt(to);
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                List<Note> list = new ArrayList<>();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    Note note = dataSnapshot.getValue(Note.class);
                    if (note != null && note.getDaxoa() == false) {
                        list.add(note);
                    }
                }
                listener.onNoteSearch(list);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Handle errors here
            }
        });
    }
    public void restoreNoteInFirebase(String noteId) {
        DatabaseReference noteRef = FirebaseDatabase.getInstance().getReference("notes").child(noteId);
        noteRef.child("daxoa").setValue(false); // Cập nhật trường 'daxoa' thành false
    }
}

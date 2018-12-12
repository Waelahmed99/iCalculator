package idev.mysimplecalculator;

import android.content.ClipData;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Build;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Vector;

public class HistoryActivity extends AppCompatActivity implements RecyclerItemTouchHelper.RecyclerItemTouchHelperListener {

    EquationsDbHelper db;
    RecyclerView mRecycler;
    ArrayList<EquationsModel> modelsList;
    EquationsRecyclerAdapter recyclerAdapter;
    RelativeLayout relativeLayout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);

        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        db = new EquationsDbHelper(this);
        mRecycler = findViewById(R.id.equationsRecycler);
        modelsList = new ArrayList<>();
        Cursor cursor = db.viewData();
        relativeLayout = findViewById(R.id.relativeLayout);

        ItemTouchHelper.SimpleCallback itemTouchHelperCallback = new RecyclerItemTouchHelper(0, ItemTouchHelper.LEFT, this);
        new ItemTouchHelper(itemTouchHelperCallback).attachToRecyclerView(mRecycler);

        if (cursor.getCount() != 0) {
            while (cursor.moveToNext()) {
                EquationsModel model = new EquationsModel(Integer.parseInt(cursor.getString(0)),
                        cursor.getString(1), cursor.getString(2));
                modelsList.add(model);
            }
            recyclerAdapter = new EquationsRecyclerAdapter(modelsList, R.layout.row_item);
            LinearLayoutManager mLayoutManager = new LinearLayoutManager(this);
            mLayoutManager.setReverseLayout(true);
            mLayoutManager.setStackFromEnd(true);
            mRecycler.setHasFixedSize(true);
            mRecycler.setLayoutManager(mLayoutManager);
            mRecycler.setItemAnimator(new DefaultItemAnimator());
            mRecycler.setAdapter(recyclerAdapter);
        }
        findViewById(R.id.deleteHistory).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                db.deleteData();
                Toast.makeText(HistoryActivity.this, "History Deleted", Toast.LENGTH_SHORT).show();
                onBackPressed();
            }
        });
    }

    @Override
    public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction, int position) {
        if (viewHolder instanceof EquationsRecyclerAdapter.PlaceHolder) {
            final int deletedIndex = viewHolder.getAdapterPosition();
            final EquationsModel deletedItem = modelsList.get(deletedIndex);

            recyclerAdapter.removeItem(deletedIndex);
            db.deleteItem(deletedItem.id);

            Snackbar snackbar = Snackbar
                    .make(relativeLayout, " removed from history!", Snackbar.LENGTH_LONG);
            snackbar.setAction("UNDO", new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    // undo is selected, restore the deleted item
                    recyclerAdapter.restoreItem(deletedItem, deletedIndex);
                    db.restoreItem(deletedItem, deletedItem.id);
                }
            });
            snackbar.setActionTextColor(Color.YELLOW);
            snackbar.show();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}

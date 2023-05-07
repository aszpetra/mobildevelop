package com.example.myapplication;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.res.TypedArray;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.MenuItemCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class ShoppingCartActivity extends AppCompatActivity {
    private FirebaseUser user;
    private FirebaseFirestore firestore;
    private CollectionReference cartItems;
    private int cartCount;
    private ArrayList<CartItem> itemList;
    private CartItemAdapter adapter;
    private FrameLayout redCircle;
    private TextView countTextView;

    private NotificationHandler notificationHandler;

    private TextView emptyCartTextView;

    private RecyclerView recyclerView;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shopping_cart);

        cartCount = getIntent().getIntExtra("cartCount", 0);
        emptyCartTextView = (TextView) findViewById(R.id.emptyCart);

        user = FirebaseAuth.getInstance().getCurrentUser();

        recyclerView = findViewById(R.id.cartRecyclerView);
        itemList = new ArrayList<>();

        adapter = new CartItemAdapter(this, itemList);
        recyclerView.setAdapter(adapter);

        firestore = FirebaseFirestore.getInstance();
        cartItems = firestore.collection("Cart");

        queryData();
        cartItemCount();
    }

    @Override
    protected void onStop() {
        super.onStop();
        notificationHandler = new NotificationHandler(this);
        notificationHandler.send("Ne feledkezz meg kosaradról!");
    }

    private void queryData() {
        itemList.clear();

        cartItems.whereEqualTo("user", user.getUid()).get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        for(QueryDocumentSnapshot document : queryDocumentSnapshots){
                            CartItem item = document.toObject(CartItem.class);
                            item.setId(document.getId());
                            itemList.add(item);
                        }

                        if(itemList.size() == 0){
                            emptyCartTextView.setText(R.string.emptyCart);
                        }else {
                            emptyCartTextView.setText(" ");
                        }
                        adapter.notifyDataSetChanged();
                    }
                });
    }

    public void cancel(View view) {
      finish();
    }

    public void logOut() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

    private void cartItemCount() {

        cartItems.whereEqualTo("user", user.getUid()).get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        int cartCount = 0;
                        for (QueryDocumentSnapshot document : queryDocumentSnapshots) {
                            cartCount = cartCount + 1;
                        }
                        countTextView.setText(String.valueOf(cartCount));
                        redCircle.setVisibility((cartCount > 0) ? VISIBLE : GONE);
                    }
                });
    }

    public void deleteItem(CartItem item){

       DocumentReference ref = this.cartItems.document(item._getId());

        ref.delete().addOnSuccessListener(success -> {
                    Toast.makeText(this, "Sikeresen eltávolítva a kosárból", Toast.LENGTH_LONG).show();
                })
                .addOnFailureListener(failure -> {
                    Toast.makeText(this, "Nem sikerült a törlés", Toast.LENGTH_LONG).show();
                });
        cartItemCount();
        queryData();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.shop_list_menu, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.log_out_button:
                FirebaseAuth.getInstance().signOut();
                logOut();
                return true;
            case R.id.cart:
                return true;
            case R.id.view_selector:
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        final MenuItem alertMenuItem = menu.findItem(R.id.cart);
        FrameLayout rootView = (FrameLayout) alertMenuItem.getActionView();

        redCircle = (FrameLayout) rootView.findViewById(R.id.view_alert_red_circle);
        countTextView = (TextView) rootView.findViewById(R.id.view_alert_count_textview);

        rootView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onOptionsItemSelected(alertMenuItem);
            }
        });
        return super.onPrepareOptionsMenu(menu);
    }


}

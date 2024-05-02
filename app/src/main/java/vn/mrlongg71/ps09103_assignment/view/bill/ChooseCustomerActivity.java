package vn.mrlongg71.ps09103_assignment.view.bill;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.google.android.gms.common.api.Status;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.widget.Autocomplete;
import com.google.android.libraries.places.widget.AutocompleteSupportFragment;
import com.google.android.libraries.places.widget.listener.PlaceSelectionListener;
import com.google.android.libraries.places.widget.model.AutocompleteActivityMode;

import java.util.Arrays;
import java.util.List;

import es.dmoral.toasty.Toasty;
import vn.mrlongg71.ps09103_assignment.R;
import vn.mrlongg71.ps09103_assignment.adapter.RecyclerCustomerAdapter;
import vn.mrlongg71.ps09103_assignment.model.objectclass.Customer;
import vn.mrlongg71.ps09103_assignment.presenter.bill.PresenterChooseCustomer;

public class ChooseCustomerActivity extends AppCompatActivity implements IViewChooseCustomer {

    private Toolbar toolbar_ChooseCustomer;
    private PresenterChooseCustomer presenterChooseCustomer;
    private RecyclerView recyclerCustomer;
    private AutocompleteSupportFragment autocompleteFragment;
    private Place place;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_customer);
        initView();
        setToolbar();
        presenterChooseCustomer.getListCustomer();
    }

    private void setToolbar() {
        setSupportActionBar(toolbar_ChooseCustomer);
        toolbar_ChooseCustomer.setTitle("Choose Customer");
//        toolbar_ChooseCustomer.setNavigationIcon(R.drawable.ic_clear_white_24dp);
//        toolbar_ChooseCustomer.setNavigationOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                onBackPressed();
//            }
//        });

    }

    private void initView() {
        toolbar_ChooseCustomer = findViewById(R.id.toolbar_ChooseCustomer);
        toolbar_ChooseCustomer.setTitle("dffdf");
        presenterChooseCustomer = new PresenterChooseCustomer(this);
        recyclerCustomer = findViewById(R.id.recyclerCustomer);
        autocompleteFragment = (AutocompleteSupportFragment)
                getSupportFragmentManager().findFragmentById(R.id.autocomplete_fragment);
        autocompleteFragment.getView().setVisibility(View.GONE);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.custom_menu_add, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.menu_add_type) {
            createCustomer();
        }
        return super.onOptionsItemSelected(item);
    }

    private void createCustomer() {
        final Dialog dialog = new Dialog(ChooseCustomerActivity.this);
        dialog.setContentView(R.layout.custom_dialog_add_customer);
        final EditText edtEmailCustomer, edtPhoneCustomer, edtNameCustomer, edtPlaceCustomer;
        Button btnAddCustomerNewDialog;
        edtEmailCustomer = dialog.findViewById(R.id.edtEmailCustomer);
        edtPhoneCustomer = dialog.findViewById(R.id.edtPhoneCustomer);
        edtNameCustomer = dialog.findViewById(R.id.edtNameCustomer);
        edtPlaceCustomer = dialog.findViewById(R.id.edtPlaceCustomer);

        btnAddCustomerNewDialog = dialog.findViewById(R.id.btnAddCustomerNewDialog);

        edtPlaceCustomer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                initChooosePlacesGooogeMap();
            }
        });
        if(place != null){
            edtPlaceCustomer.setText(place.getName());
        }else {
            edtPlaceCustomer.setText("null");
        }

        btnAddCustomerNewDialog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (edtEmailCustomer.getText().length() > 0 && edtPhoneCustomer.getText().length() > 0 && edtNameCustomer.getText().length() > 0 && edtPlaceCustomer.getText().length() > 0) {
                    Customer customer = new Customer("", edtNameCustomer.getText().toString().trim(),
                            edtPhoneCustomer.getText().toString().trim(),
                            edtEmailCustomer.getText().toString().trim(),
                            edtPlaceCustomer.getText().toString().trim()
                    );
                    presenterChooseCustomer.getAddCustomer(customer);
                    dialog.dismiss();
                } else {
                    Toasty.error(getApplicationContext(), getString(R.string.error), Toasty.LENGTH_SHORT).show();
                }
            }
        });

        dialog.show();
    }
    private void initChooosePlacesGooogeMap() {

        Places.initialize(getApplicationContext(), getString(R.string.apikey_google));

        autocompleteFragment.getView().setVisibility(View.VISIBLE);

        List<Place.Field> fields = Arrays.asList(Place.Field.LAT_LNG, Place.Field.NAME);
        autocompleteFragment.setPlaceFields(fields);



        autocompleteFragment.setOnPlaceSelectedListener(new PlaceSelectionListener() {
            @Override
            public void onPlaceSelected(Place place) {

            }


            @Override
            public void onError(Status status) {

            }
        });
        Intent intent = new Autocomplete.IntentBuilder(
                AutocompleteActivityMode.OVERLAY, fields)
                .build(this);
        startActivityForResult(intent, 111);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 111) {
            if (resultCode == RESULT_OK) {
                place = Autocomplete.getPlaceFromIntent(data);

            }
        }
    }

    @Override
    public void displayListCustomer(List<Customer> customerList) {
        RecyclerCustomerAdapter recyclerCustomerAdapter = new RecyclerCustomerAdapter(ChooseCustomerActivity.this, R.layout.custom_list_user, customerList);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext(), RecyclerView.VERTICAL, false);
        recyclerCustomer.setLayoutManager(layoutManager);
        recyclerCustomer.setAdapter(recyclerCustomerAdapter);
        autocompleteFragment.getView().setVisibility(View.GONE);

        recyclerCustomerAdapter.notifyDataSetChanged();
    }


    @Override
    public void onAddCustomerSuccess() {
        Toasty.success(getApplicationContext(), getString(R.string.success), Toasty.LENGTH_SHORT).show();
    }

    @Override
    public void onAddCustomerFailed() {
        Toasty.error(getApplicationContext(), getString(R.string.error), Toasty.LENGTH_SHORT).show();

    }
}

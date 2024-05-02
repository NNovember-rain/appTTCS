package vn.mrlongg71.ps09103_assignment.view.typebook;

import android.app.Dialog;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.PopupMenu;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.developer.kalert.KAlertDialog;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import java.util.List;

import es.dmoral.toasty.Toasty;
import vn.mrlongg71.ps09103_assignment.R;
import vn.mrlongg71.ps09103_assignment.adapter.RecyclerViewTypeBookAdapter;
import vn.mrlongg71.ps09103_assignment.library.ActionBarLib;
import vn.mrlongg71.ps09103_assignment.model.objectclass.TypeBook;
import vn.mrlongg71.ps09103_assignment.presenter.typebook.IPresenterTypeBookAdapter;
import vn.mrlongg71.ps09103_assignment.presenter.typebook.PresenterTypeBook;


public class TypebookFragment extends Fragment implements IViewTypeBook, IPresenterTypeBookAdapter {

    private PresenterTypeBook presenterTypeBook;
    private RecyclerView recyclerTypeBook;
    private RecyclerViewTypeBookAdapter recyclerViewTypeBookAdapter;
    private ProgressBar progressBarTypeBook;
    private SearchView searchView;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_typebook, container, false);
        setActionBar();
        initView(view);
        setHasOptionsMenu(true);
        presenterTypeBook = new PresenterTypeBook(this);
        presenterTypeBook.getTypeBook();
        progressBarTypeBook.setVisibility(View.VISIBLE);
        return view;
    }

    private void initView(View view) {
        recyclerTypeBook = view.findViewById(R.id.recyclerTypeBook);
        progressBarTypeBook = view.findViewById(R.id.progress_TypeBook);
    }

    private void setActionBar() {
        ActionBarLib.setSupportActionBar(getActivity(), getString(R.string.menu_tyle));
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull final MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.custom_menu_add, menu);
        searchView = (SearchView) menu.findItem(R.id.menu_search).getActionView();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {

                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                recyclerViewTypeBookAdapter.search(newText);
                recyclerViewTypeBookAdapter.notifyDataSetChanged();
                return false;
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        switch (item.getItemId()) {
            case R.id.menu_add_type:
                createDialogInputAddType();
                break;
        }
        return super.onOptionsItemSelected(item);

    }

    private void createDialogInputAddType() {
        Dialog dialog = new Dialog(getActivity());
        dialog.setContentView(R.layout.custom_dialog_add);
        EditText edtTypeCode, edtTypeName;
        Button btnAddTypeBookDialog;
        edtTypeCode = dialog.findViewById(R.id.edtTypeCode);
        edtTypeName = dialog.findViewById(R.id.edtTypeName);
        btnAddTypeBookDialog = dialog.findViewById(R.id.btnAddTypeBookDialog);
        dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        doAddTypeBook(edtTypeCode, edtTypeName, btnAddTypeBookDialog,dialog);
        dialog.show();
    }

    private void doAddTypeBook(final EditText edtTypeCode, final EditText edtTypeName, Button btnAddTypeBookDialog, final Dialog dialog) {
        btnAddTypeBookDialog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String typecode = edtTypeCode.getText().toString().trim();
                String typename = edtTypeName.getText().toString().trim();
                if (typecode.length() > 0 && typename.length() > 0) {
                    progressBarTypeBook.setVisibility(View.VISIBLE);
                    TypeBook typeBook = new TypeBook();
                    typeBook.setTypecode(typecode);
                    typeBook.setTypename(typename);
                    presenterTypeBook.getAddTypeBook(typeBook);
                    dialog.dismiss();
                } else {
                    Toasty.error(getActivity(), getString(R.string.error), Toasty.LENGTH_LONG).show();
                    dialog.dismiss();
                }
            }
        });

    }

    @Override
    public void displayListType(List<TypeBook> typeBookList) {
        if (typeBookList.size() == 0) {
            progressBarTypeBook.setVisibility(View.GONE);
            Toasty.warning(getActivity(), getString(R.string.notthingdata), Toasty.LENGTH_LONG).show();
        }
        progressBarTypeBook.setVisibility(View.GONE);
        recyclerViewTypeBookAdapter = new RecyclerViewTypeBookAdapter(getActivity(), R.layout.custom_recycler_typebook, typeBookList, this);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());
        recyclerTypeBook.setLayoutManager(layoutManager);
        recyclerTypeBook.setAdapter(recyclerViewTypeBookAdapter);
    }

    @Override
    public void displayAddTypeSucces() {
        progressBarTypeBook.setVisibility(View.GONE);
        Toasty.success(getActivity(), getActivity().getString(R.string.success), Toasty.LENGTH_LONG).show();
        recyclerViewTypeBookAdapter.notifyDataSetChanged();
    }

    @Override
    public void displayAddTypeFailed() {
        recyclerViewTypeBookAdapter.notifyDataSetChanged();
        Toasty.error(getActivity(), getActivity().getString(R.string.error), Toasty.LENGTH_LONG).show();

    }

    @Override
    public void displayDeleteItemTypeBookSuccess() {
        Toasty.success(getActivity(), getActivity().getString(R.string.success), Toasty.LENGTH_LONG).show();
        recyclerViewTypeBookAdapter.notifyDataSetChanged();
    }

    @Override
    public void displayDeleteItemTypeBookFailed() {
        recyclerViewTypeBookAdapter.notifyDataSetChanged();
        Toasty.error(getActivity(), getActivity().getString(R.string.error), Toasty.LENGTH_LONG).show();

    }

    @Override
    public void displayEditItemTypeBookSuccess() {
        recyclerViewTypeBookAdapter.notifyDataSetChanged();
        Toasty.success(getActivity(), getActivity().getString(R.string.success), Toasty.LENGTH_LONG).show();

    }

    @Override
    public void displayEditItemTypeBookFailed() {
        recyclerViewTypeBookAdapter.notifyDataSetChanged();
        Toasty.error(getActivity(), getActivity().getString(R.string.error), Toasty.LENGTH_LONG).show();

    }

    @Override
    public void onEventDeleteItemClickListenerTypeBook(final int position, final List<TypeBook> typeBookList) {
        final TypeBook typeBook = typeBookList.get(position);
        new KAlertDialog(getActivity(), KAlertDialog.ERROR_TYPE)
                .setContentText(getActivity().getString(R.string.wantDelete) + " loại " + typeBook.getTypecode())
                .setConfirmClickListener(new KAlertDialog.KAlertClickListener() {
                    @Override
                    public void onClick(KAlertDialog kAlertDialog) {
                        progressBarTypeBook.setVisibility(View.VISIBLE);
                        presenterTypeBook.getItemDelete(typeBook.getKey());
                        typeBookList.clear();
                        kAlertDialog.dismissWithAnimation();
                    }
                })
                .setCancelText("No")
                .setCancelClickListener(new KAlertDialog.KAlertClickListener() {
                    @Override
                    public void onClick(KAlertDialog kAlertDialog) {
                        kAlertDialog.dismissWithAnimation();
                    }
                })
                .show();

    }

    @Override
    public void onEventEditItemClickListenerTypeBook(int position, final List<TypeBook> typeBookList) {
        final TypeBook typeBook = typeBookList.get(position);
        final Dialog dialogEdit = new Dialog(getActivity());
        dialogEdit.setContentView(R.layout.custom_dialog_add);
        final EditText edtTypeCode, edtTypeName;
        Button btnAddTypeBookDialog;
        edtTypeCode = dialogEdit.findViewById(R.id.edtTypeCode);
        edtTypeName = dialogEdit.findViewById(R.id.edtTypeName);
        btnAddTypeBookDialog = dialogEdit.findViewById(R.id.btnAddTypeBookDialog);
        btnAddTypeBookDialog.setText(getActivity().getString(R.string.edit));
        edtTypeCode.setText(typeBook.getTypecode());
        edtTypeName.setText(typeBook.getTypename());
        dialogEdit.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        btnAddTypeBookDialog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                typeBook.setTypecode(edtTypeCode.getText().toString().trim());
                typeBook.setTypename(edtTypeName.getText().toString().trim());
                presenterTypeBook.getItemEdit(typeBook.getKey(), typeBook);
                typeBookList.clear();
                dialogEdit.dismiss();

            }
        });
        dialogEdit.show();
    }


}

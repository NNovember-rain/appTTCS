package vn.mrlongg71.ps09103_assignment.view.book;


import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.logging.SimpleFormatter;

import es.dmoral.toasty.Toasty;
import vn.mrlongg71.ps09103_assignment.R;
import vn.mrlongg71.ps09103_assignment.adapter.SpinerAdapter;
import vn.mrlongg71.ps09103_assignment.library.Dialog;
import vn.mrlongg71.ps09103_assignment.library.PopBack;
import vn.mrlongg71.ps09103_assignment.model.objectclass.Book;
import vn.mrlongg71.ps09103_assignment.model.objectclass.TypeBook;
import vn.mrlongg71.ps09103_assignment.presenter.book.PresenterBook;


public class AddBookFragment extends Fragment implements IViewBook {


    private Button btnAddBook;
    private TextInputEditText edtBookName, edtAuthor, edtPrice, edtAmount;
    private ImageView imgChooseImagesBook, imgChooseImagesBook2, imgChooseImagesBook3;
    private CardView cardCamera2, cardCamera3;
    private ProgressDialog progressDialog;
    private SpinerAdapter adapterSpiner;
    private Spinner spinnerTypeBook;
    private PresenterBook presenterBook;
    private int positionSpiner = 0;
    private List<TypeBook> typeBookLists;
    private ArrayList<Book> bookArrayList;
    private int REQUESTCODE = 123;
    private Bundle bundle;
    private boolean click = false;
    private Calendar calendar;
    private List<String> listPathImages = new ArrayList<>();
    private StorageReference storageReference = FirebaseStorage.getInstance().getReference().child("book");
    private FragmentManager fragmentManager;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_add_book, container, false);
        initView(view);
        initEventAdd();
        initEventEdit();
        fragmentManager = getActivity().getSupportFragmentManager();
        PopBack.callBack(view, fragmentManager);
        return view;
    }

    private void initEventEdit() {
        bookArrayList = new ArrayList<>();
        bundle = getArguments();
        if (bundle != null) {
            Book book = bundle.getParcelable("book");
            edtBookName.setText(book.getBookname());
            edtPrice.setText(book.getPrice() + "");
            edtAuthor.setText(book.getAuthor());
            edtAmount.setText(book.getAmount());
            listPathImages = book.getArrImagesBook();
            displayListImagesBookEdit();
            btnAddBook.setText("Edit");
            initEditBook(book);
        }
    }

    private void initView(View view) {
        btnAddBook = view.findViewById(R.id.btnAddBook);
        edtBookName = view.findViewById(R.id.edtBookName);
        edtAmount = view.findViewById(R.id.edtAmount);
        edtAuthor = view.findViewById(R.id.edtAuthor);
        edtPrice = view.findViewById(R.id.edtPrice);
        progressDialog = new ProgressDialog(getActivity());
        imgChooseImagesBook = view.findViewById(R.id.imgChoooseImagesBook);
        imgChooseImagesBook2 = view.findViewById(R.id.imgChoooseImagesBook2);
        imgChooseImagesBook3 = view.findViewById(R.id.imgChoooseImagesBook3);

        cardCamera2 = view.findViewById(R.id.cartCamera2);
        cardCamera3 = view.findViewById(R.id.cartCamera3);

        spinnerTypeBook = view.findViewById(R.id.spinerTypeBook);

        presenterBook = new PresenterBook(this);
        typeBookLists = new ArrayList<>();
        typeBookLists.add(new TypeBook("key", "code", "Chọn loại"));
        presenterBook.getTypeBook();
        initChooseImages();

    }

    private void initChooseImages() {
        imgChooseImagesBook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                click = true;
                Intent intent = new Intent(getActivity(), ChooseImageActivity.class);
                startActivityForResult(intent, REQUESTCODE);
            }
        });
    }

    private void initEditBook(final Book book) {
        btnAddBook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                positionSpiner = spinnerTypeBook.getSelectedItemPosition();
                String bookname = edtBookName.getText().toString().trim();
                String author = edtAuthor.getText().toString().trim();
                String amount = edtAmount.getText().toString().trim();
                String pricestr = edtPrice.getText().toString().trim();
                if (checkValid(bookname, author, amount, pricestr, positionSpiner, listPathImages)) {

                            book.setBookname(edtBookName.getText().toString().trim());
                            book.setAmount(edtAmount.getText().toString().trim());
                            book.setAuthor(edtAuthor.getText().toString().trim());

                            double price = Double.parseDouble(pricestr);
                            book.setPrice(price);
                            book.setTypecode(typeBookLists.get(positionSpiner).getKey());
                            if (click) {
                                presenterBook.getItemEditBook(book.getBookcode(), book, listPathImages);
                            }
                            presenterBook.getItemEditBook(book.getBookcode(), book, null);



                }else {
                    Toasty.error(getActivity(), getString(R.string.error), Toasty.LENGTH_LONG).show();
                }
            }
        });
    }

    private void initEventAdd() {
        if (bundle == null) {

            btnAddBook.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {


                    positionSpiner = spinnerTypeBook.getSelectedItemPosition();
                    String bookname = edtBookName.getText().toString().trim();
                    String author = edtAuthor.getText().toString().trim();
                    String amount = edtAmount.getText().toString().trim();
                    String pricestr = edtPrice.getText().toString().trim();

                    if (checkValid(bookname, author, amount, pricestr, positionSpiner, listPathImages)) {
                        if (checkValidAmount(amount, edtAmount)) {
                            if (checkValidAmount(pricestr, edtPrice)) {

                                Dialog.DialogLoading(progressDialog, true);
                                Book book = new Book();
                                book.setBookname(edtBookName.getText().toString().trim());
                                book.setAmount(edtAmount.getText().toString().trim());
                                book.setAuthor(edtAuthor.getText().toString().trim());

                                calendar = calendar.getInstance();
                                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MM-yyyy");
                                book.setDate(simpleDateFormat.format(calendar.getTime()));
                                double price = Double.parseDouble(pricestr);
                                book.setPrice(price);
                                book.setTypecode(typeBookLists.get(positionSpiner).getKey());

                                presenterBook.getAddBook(book, listPathImages);
                            }
                        }
                    } else {
                        Toasty.error(getActivity(), getString(R.string.error), Toasty.LENGTH_LONG).show();
                    }
                }
            });
        }
    }

    @Override
    public void displayListBook(ArrayList<Book> bookList, List<TypeBook> typeBookList) {

    }

    @Override
    public void displayListBookFailed() {

    }

    @Override
    public void displayListTypeBookSpiner(TypeBook typeBook) {
        typeBookLists.add(typeBook);
        adapterSpiner = new SpinerAdapter(getActivity(), R.layout.custom_spiner, typeBookLists);
        spinnerTypeBook.setAdapter(adapterSpiner);
    }


    @Override
    public void displayAddBookSucces() {
        Dialog.DialogLoading(progressDialog, false);
        Toasty.success(getActivity(), getString(R.string.success), Toasty.LENGTH_LONG).show();
        fragmentManager.popBackStack();
    }

    @Override
    public void displayAddBookFailed() {
        Toasty.error(getActivity(), getString(R.string.error), Toasty.LENGTH_LONG).show();

    }

    @Override
    public void displayDeleteItemBookSuccess() {

    }

    @Override
    public void displayDeleteItemBookFailed() {

    }

    @Override
    public void displayEditItemBookSuccess() {
        getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fram, new BookFragment()).commit();
        Toasty.success(getActivity(), getString(R.string.success), Toasty.LENGTH_LONG).show();
    }

    @Override
    public void displayEditItemBookFailed() {

    }

    public boolean checkValid(String bookname, String amount, String author, String price, int positionSpiner, List<String> listPathImages) {

        if (bookname.length() == 0 || author.length() == 0 || amount.length() == 0 || price.length() == 0 || positionSpiner == 0 || listPathImages.size() == 0) {
            return false;
        } else {
            return true;
        }
    }

    public boolean checkValidAmount(String amount,TextInputEditText edt) {
        if (amount.equals("0") || Integer.parseInt(amount) <= 0) {
            edt.setError("0?");
            return false;
        } else {
            return true;
        }



    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUESTCODE) {
            if (resultCode == getActivity().RESULT_OK && data != null) {
                listPathImages = data.getStringArrayListExtra("listpath");
            }
            if (listPathImages.size() > 0) {
                displayListImagesBook();
            }

        }
    }

    private void displayListImagesBook() {
        if (listPathImages.size() == 1) {
            imgChooseImagesBook.setMaxWidth(150);
            imgChooseImagesBook.setMaxHeight(150);
            imgChooseImagesBook.setImageURI(Uri.parse(listPathImages.get(0)));
        }
        if (listPathImages.size() == 2) {
            imgChooseImagesBook.setMaxWidth(100);
            imgChooseImagesBook.setMaxHeight(100);
            imgChooseImagesBook2.setMaxWidth(100);
            imgChooseImagesBook2.setMaxHeight(100);

            cardCamera2.setVisibility(View.VISIBLE);

            imgChooseImagesBook.setImageURI(Uri.parse(listPathImages.get(0)));
            imgChooseImagesBook2.setImageURI(Uri.parse(listPathImages.get(1)));
        }
        if (listPathImages.size() == 3) {
            imgChooseImagesBook.setMaxWidth(100);
            imgChooseImagesBook.setMaxHeight(100);

            imgChooseImagesBook2.setMaxWidth(100);
            imgChooseImagesBook2.setMaxHeight(100);

            imgChooseImagesBook3.setMaxWidth(100);
            imgChooseImagesBook3.setMaxHeight(100);

            cardCamera2.setVisibility(View.VISIBLE);
            cardCamera3.setVisibility(View.VISIBLE);


            imgChooseImagesBook.setImageURI(Uri.parse(listPathImages.get(0)));
            imgChooseImagesBook2.setImageURI(Uri.parse(listPathImages.get(1)));
            imgChooseImagesBook3.setImageURI(Uri.parse(listPathImages.get(2)));
        }
    }

    private void displayListImagesBookEdit() {
        int with = 250;
        if (listPathImages.size() == 1) {
            imgChooseImagesBook.setMaxWidth(with);
            imgChooseImagesBook.setMaxHeight(with);
            storageReference.child(listPathImages.get(0)).getDownloadUrl().addOnCompleteListener(new OnCompleteListener<Uri>() {
                @Override
                public void onComplete(@NonNull Task<Uri> task) {
                    Glide.with(getActivity())
                            .load(task.getResult().toString())
                            .into(imgChooseImagesBook);
                }
            });
        }
        if (listPathImages.size() == 2) {
            imgChooseImagesBook.setMaxWidth(with);
            imgChooseImagesBook.setMaxHeight(with);
            imgChooseImagesBook2.setMaxWidth(with);
            imgChooseImagesBook2.setMaxHeight(with);


            cardCamera2.setVisibility(View.VISIBLE);

            storageReference.child(listPathImages.get(0)).getDownloadUrl().addOnCompleteListener(new OnCompleteListener<Uri>() {
                @Override
                public void onComplete(@NonNull Task<Uri> task) {
                    Glide.with(getActivity())
                            .load(task.getResult().toString())
                            .into(imgChooseImagesBook);
                }
            });
            storageReference.child(listPathImages.get(1)).getDownloadUrl().addOnCompleteListener(new OnCompleteListener<Uri>() {
                @Override
                public void onComplete(@NonNull Task<Uri> task) {
                    Glide.with(getActivity())
                            .load(task.getResult().toString())
                            .into(imgChooseImagesBook2);
                }
            });
        }
        if (listPathImages.size() == 3) {
            imgChooseImagesBook.setMaxWidth(with);
            imgChooseImagesBook.setMaxHeight(with);

            imgChooseImagesBook2.setMaxWidth(with);
            imgChooseImagesBook2.setMaxHeight(with);

            imgChooseImagesBook3.setMaxWidth(with);
            imgChooseImagesBook3.setMaxHeight(with);


            cardCamera2.setVisibility(View.VISIBLE);
            cardCamera3.setVisibility(View.VISIBLE);


            storageReference.child(listPathImages.get(0)).getDownloadUrl().addOnCompleteListener(new OnCompleteListener<Uri>() {
                @Override
                public void onComplete(@NonNull Task<Uri> task) {
                    Glide.with(getActivity())
                            .load(task.getResult().toString())
                            .into(imgChooseImagesBook);
                }
            });
            storageReference.child(listPathImages.get(1)).getDownloadUrl().addOnCompleteListener(new OnCompleteListener<Uri>() {
                @Override
                public void onComplete(@NonNull Task<Uri> task) {
                    Glide.with(getActivity())
                            .load(task.getResult().toString())
                            .into(imgChooseImagesBook2);
                }
            });
            storageReference.child(listPathImages.get(2)).getDownloadUrl().addOnCompleteListener(new OnCompleteListener<Uri>() {
                @Override
                public void onComplete(@NonNull Task<Uri> task) {
                    Glide.with(getActivity())
                            .load(task.getResult().toString())
                            .into(imgChooseImagesBook3);
                }
            });
        }
    }
}

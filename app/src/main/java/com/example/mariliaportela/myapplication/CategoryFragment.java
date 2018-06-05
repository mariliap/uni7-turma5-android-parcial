package com.example.mariliaportela.myapplication;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.mariliaportela.myapplication.commons.CommonInfo;
import com.example.mariliaportela.myapplication.contrato.ItemClickListener;
import com.example.mariliaportela.myapplication.model.Category;
import com.example.mariliaportela.myapplication.viewHolder.CategoryViewHolder;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;



public class CategoryFragment extends Fragment {


    private View fragmento;
    private RecyclerView listCategory;
    private RecyclerView.LayoutManager layoutManager;

    private FirebaseRecyclerAdapter<Category, CategoryViewHolder> adapter;
    private FirebaseDatabase database;
    private DatabaseReference categorias;


    public CategoryFragment() {
        // Required empty public constructor
    }

    public static CategoryFragment newInstance() {
        CategoryFragment fragment = new CategoryFragment();

        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        database = FirebaseDatabase.getInstance();
        categorias = database.getReference("Category");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        fragmento = inflater.inflate(R.layout.fragment_category, container, false);
        listCategory = (RecyclerView) fragmento.findViewById(R.id.listCategory);
        listCategory.setHasFixedSize(true);

        layoutManager = new LinearLayoutManager(container.getContext());
        listCategory.setLayoutManager(layoutManager);

        carregarCategorias();
        return fragmento;
    }

    private void carregarCategorias() {
        adapter = new FirebaseRecyclerAdapter<Category, CategoryViewHolder>(
                Category.class, R.layout.category_layout,
                CategoryViewHolder.class, categorias) {
            @Override
            protected void populateViewHolder(CategoryViewHolder viewHolder, final Category model, int position) {
                viewHolder.category_name.setText(model.getName());
                Picasso.with(getActivity()).load(model.getImage()).into(viewHolder.category_image);

                viewHolder.setItemClickListener(new ItemClickListener() {
                    @Override
                    public void onClick(View view, int position, boolean isLongClick) {
                        //Toast.makeText(getActivity(), String.format("%s|%s"  , adapter.getRef(position).getKey(), model.getName()),Toast.LENGTH_SHORT).show();
                        Intent start = new Intent(getActivity(), StartActivity.class);
                        CommonInfo.categoryId = adapter.getRef(position).getKey();
                        start.putExtra("IMAGEM", model.getImage());
                        startActivity(start);

                        //Picasso.with(start).load(model.getImage()).into(viewHolder.category_image);
                    }
                });
            }
        };
        adapter.notifyDataSetChanged();
        listCategory.setAdapter(adapter);
    }




}

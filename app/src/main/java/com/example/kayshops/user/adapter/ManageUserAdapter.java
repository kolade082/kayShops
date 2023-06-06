package com.example.kayshops.user.adapter;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.kayshops.R;
import com.example.kayshops.user.Users;
import com.example.kayshops.user.model.Categories;

import java.util.List;

public class ManageUserAdapter extends RecyclerView.Adapter<ManageUserAdapter.UserViewHolder> {

    Context context;
    List<Users> usersList;
    public interface OnUserItemClickListener {
        void onEditUserClick(Users users);
        void onDeleteUserClick(Users users);
    }
    private ManageUserAdapter.OnUserItemClickListener clickListener;

    public ManageUserAdapter(Context context, List<Users> usersList, OnUserItemClickListener clickListener) {
        this.context = context;
        this.usersList = usersList;
        this.clickListener = clickListener;
    }

    @NonNull
    @Override
    public UserViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.user_listing, parent, false);
        return new UserViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull UserViewHolder holder, int position) {
        Users users = usersList.get(position);
        holder.userName.setText(users.getFullName());

        holder.editUser.setOnClickListener(v -> clickListener.onEditUserClick(users));
        holder.deleteUser.setOnClickListener(v -> clickListener.onDeleteUserClick(users));
    }


    @Override
    public int getItemCount() {
        return usersList.size();
    }
    public static final class UserViewHolder extends RecyclerView.ViewHolder{
        ImageView editUser, deleteUser;
        TextView userName;
        public UserViewHolder(@NonNull View itemView) {
            super(itemView);
            userName = itemView.findViewById(R.id.user_name);
            editUser = itemView.findViewById(R.id.edit_user);
            deleteUser = itemView.findViewById(R.id.delete_user);
        }
    }
}
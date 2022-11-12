package com.example.gym;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class UsersAdapter extends RecyclerView.Adapter<UsersAdapter.UsersAdapterVH> implements Filterable {

    private List<User> userList;
    private List<User> getUserListFiltered;
    private Context context;
    private SelectedUser selectedUser;

    public UsersAdapter(List<User> userList, SelectedUser selectedUser) {
        this.userList = userList;
        this.getUserListFiltered = userList;
        this.selectedUser = selectedUser;
    }

    @NonNull
    @Override
    public UsersAdapter.UsersAdapterVH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();
        return new UsersAdapterVH(LayoutInflater.from(context).inflate(R.layout.users_list_layout,null));
    }

    @Override
    public void onBindViewHolder(@NonNull UsersAdapter.UsersAdapterVH holder, int position) {
        User user = userList.get(position);
        String username = user.getFname();
        String lastname = user.getLname();
        //String prefix = user.getFname().substring(0,1);

        //doing checks before assigning
        if (TextUtils.isEmpty(username) && TextUtils.isEmpty(lastname)){
            holder.tvusername.setText(user.getEmail());
            holder.tvprefix.setText(user.getEmail().substring(0,1));
        }else {
            holder.tvusername.setText(username);
            holder.tvlname.setText(lastname);
            holder.tvprefix.setText(user.getFname().substring(0,1));
        }

    }

    @Override
    public int getItemCount() {
        return userList.size();
    }

    @Override
    public Filter getFilter() {
        Filter filter = new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                FilterResults filterResults = new FilterResults();

                if (constraint == null | constraint.length()==0 ){
                    filterResults.count = getUserListFiltered.size();
                    filterResults.values = getUserListFiltered;
                }else {
                    String searchchar = constraint.toString().toLowerCase();
                    List<User> resultsdata = new ArrayList<>();

                    for (User user:getUserListFiltered){
                        if (user.getFname().toLowerCase().contains(searchchar) | user.getLname().toLowerCase().contains(searchchar) |user.getEmail().toLowerCase().contains(searchchar)){
                            resultsdata.add(user);
                        }
                    }
                    filterResults.count = resultsdata.size();
                    filterResults.values = resultsdata;
                }
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                userList = (List<User>) results.values;
                notifyDataSetChanged();

            }
        };
        return filter;
    }

    public interface SelectedUser{
        void selectedUser(User user);
    }

    public class UsersAdapterVH extends RecyclerView.ViewHolder {

        TextView tvprefix,tvusername,tvlname;
        ImageView imageicon;

        public UsersAdapterVH(@NonNull View itemView) {
            super(itemView);

            tvprefix = itemView.findViewById(R.id.prefix);
            tvusername = itemView.findViewById(R.id.username);
            tvlname = itemView.findViewById(R.id.lname);
            imageicon = itemView.findViewById(R.id.imageview);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    selectedUser.selectedUser(userList.get(getBindingAdapterPosition()));
                }
            });
        }
    }
}

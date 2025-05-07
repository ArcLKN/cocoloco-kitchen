package com.example.cocolocokitchen;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.List;

public class RecipeAdapter extends RecyclerView.Adapter<RecipeAdapter.RecipeViewHolder> {

    private Context context;
    private List<Recipe> recipeList;

    private int currentViewType = -1;
    private OnRecipeClickListener listener;


    public interface OnRecipeClickListener {
        void onRecipeClick(Recipe recipe);
    }
    public RecipeAdapter(Context context, List<Recipe> recipeList) {
        this.context = context;
        this.recipeList = recipeList;
    }

    @NonNull
    @Override
    public RecipeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;

        // Inflate the appropriate layout based on the viewType
        switch (viewType) {
            case 0: // Small card
                view = LayoutInflater.from(context).inflate(R.layout.recipes_card_small, parent, false);
                break;
            default:
                view = LayoutInflater.from(context).inflate(R.layout.recipes_card, parent, false);
        }
        return new RecipeViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecipeViewHolder holder, int position) {
        Recipe recipe = recipeList.get(position);

        holder.titleTextView.setText(recipe.getTitle());
        if (holder.descriptionTextView != null) {
            holder.descriptionTextView.setText(recipe.getDescription());
        }
        holder.timeView.setText(recipe.getTimeInMinute() + " min");
        holder.priceView.setText(recipe.getCostDegree());
        holder.peopleView.setText(String.valueOf(recipe.getNumberOfServing()));

        // Load image using Glide
        String imageUrl = recipe.getImageUrl();
        int imageResId = recipe.getImageResId();

        if (imageUrl != null && !imageUrl.isEmpty()) {
            // Load from URL
            Glide.with(context)
                    .load(imageUrl) // Load from URL or file path
                    .into(holder.imageView);
        } else {
            // Load from drawable resource
            Glide.with(context)
                    .load(imageResId) // Load from drawable resource ID
                    .into(holder.imageView);
        }

        holder.itemView.setOnClickListener(v -> {
            Log.d("RecipeAdapter", "Item clicked: " + recipe.getTitle());

            if (listener != null) {
                int positionClicked = holder.getAdapterPosition(); // safer than external index
                Bundle bundle = new Bundle();
                bundle.putInt("recipeIndex", positionClicked);

                RecipeViewFragment fragment = new RecipeViewFragment();
                fragment.setArguments(bundle);

                ((FragmentActivity) context)
                        .getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.fragment_container, fragment)
                        .addToBackStack(null)
                        .commit();

                listener.onRecipeClick(recipe); // optional: for analytics, etc.
            }
        });
    }

    @Override
    public int getItemCount() {
        return recipeList.size();
    }

    // We call this function to change the variable currentViewType to globally change
    // the viewType of our recycler's items.
    public void setViewType(int viewType) {
        this.currentViewType = viewType;
        notifyDataSetChanged(); // Refresh all items
    }

    // Before creating the view it calls getItemViewType, by default the items have no specific
    // view type so it doesnt matters.
    // Here we want to replace the usual behavior of the getViewType to enable changing view type
    // dynamically. So the viewType returned is the one we have changed.
    // So before showing the item it will change is "view type" so it will correctly apply
    // the view we want.
    @Override
    public int getItemViewType(int position) {
        return currentViewType; // uses the global value for all items
    }

    public static class RecipeViewHolder extends RecyclerView.ViewHolder {

        ImageView imageView;
        TextView titleTextView;
        TextView descriptionTextView;
        TextView timeView;
        TextView priceView;
        TextView peopleView;

        public RecipeViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.recipe_image);
            titleTextView = itemView.findViewById(R.id.recipe_title);
            descriptionTextView = itemView.findViewById(R.id.recipe_description);
            timeView = itemView.findViewById(R.id.recipe_time);
            priceView = itemView.findViewById(R.id.recipe_price);
            peopleView = itemView.findViewById(R.id.recipe_people);
        }
    }
    public void setOnRecipeClickListener(OnRecipeClickListener listener) {
        this.listener = listener;
    }
}

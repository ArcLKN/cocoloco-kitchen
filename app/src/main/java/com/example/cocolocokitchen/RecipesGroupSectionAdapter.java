package com.example.cocolocokitchen;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class RecipesGroupSectionAdapter extends RecyclerView.Adapter<RecipesGroupSectionAdapter.RecipeGroupViewHolder> {

    private List<RecipesGroupSection> groupSections;
    private Context context;
    private final Set<Integer> expandedGroups = new HashSet<>();

    public RecipesGroupSectionAdapter(Context context, List<RecipesGroupSection> groupSections) {
        this.context = context;
        this.groupSections = groupSections;
    }

    @NonNull
    @Override
    public RecipeGroupViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.recipes_group_section, parent, false);
        return new RecipeGroupViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecipeGroupViewHolder holder, int position) {
        RecipesGroupSection section = groupSections.get(position);
        holder.groupButton.setText(section.getGroupName() + " (" + section.recipes.size() + ")");

        RecipeAdapter recipeAdapter = new RecipeAdapter(context, section.getRecipes());
        recipeAdapter.setViewType(1);

        // 🔧 Set the click listener here
        recipeAdapter.setOnRecipeClickListener(recipe -> {
            Log.d("RecipeGroupAdapter", "Clicked: " + recipe.getTitle());

            Bundle bundle = new Bundle();
            int recipeId = recipe.getId();
            bundle.putInt("recipeId", recipeId);

            RecipeViewFragment fragment = new RecipeViewFragment();
            fragment.setArguments(bundle);

            ((FragmentActivity) context)
                    .getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.fragment_container, fragment)
                    .addToBackStack(null)
                    .commit();
        });

        holder.groupRecyclerView.setLayoutManager(new LinearLayoutManager(context));
        holder.groupRecyclerView.setAdapter(recipeAdapter);

        boolean isExpanded = section.isExpanded();
        holder.groupRecyclerView.setVisibility(isExpanded ? View.VISIBLE : View.GONE);

        holder.groupButton.setOnClickListener(v -> {
            if (section.isExpanded()) {
                section.setExpanded(false);
                expandedGroups.remove(position);
                holder.groupRecyclerView.setVisibility(View.GONE);
            } else {
                section.setExpanded(true);
                expandedGroups.add(position);
                holder.groupRecyclerView.setVisibility(View.VISIBLE);
            }
        });
    }


    @Override
    public int getItemCount() {
        return groupSections.size();
    }

    public static class RecipeGroupViewHolder extends RecyclerView.ViewHolder {

        Button groupButton;
        RecyclerView groupRecyclerView;

        public RecipeGroupViewHolder(@NonNull View itemView) {
            super(itemView);
            groupButton = itemView.findViewById(R.id.recipes_group_button);
            groupRecyclerView = itemView.findViewById(R.id.recipes_group_recipes_recycler);
        }
    }
}
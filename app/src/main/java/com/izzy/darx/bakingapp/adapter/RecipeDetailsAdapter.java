package com.izzy.darx.bakingapp.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.izzy.darx.bakingapp.R;
import com.izzy.darx.bakingapp.model.Ingredient;
import com.izzy.darx.bakingapp.utils.BakingConstants;

import java.util.List;
import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;

/*Created by IsraelEmoi on 28/11/2018*/

public class RecipeDetailsAdapter extends RecyclerView.Adapter<RecipeDetailsAdapter.RecipeDetailsViewHolder> {

    private final Context mContext;
    private final List<Ingredient> mIngredietList;

    public RecipeDetailsAdapter(List<Ingredient> ingredientList, Context context) {
        mContext = context;
        mIngredietList = ingredientList;
    }

    @NonNull
    @Override
    public RecipeDetailsAdapter.RecipeDetailsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int position) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.ingredient_list_item, parent, false);
        return new RecipeDetailsViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final RecipeDetailsAdapter.RecipeDetailsViewHolder holder, int position) {

        Ingredient ingredient = mIngredietList.get(position);

        assert holder.ingredientName != null;
        holder.ingredientName.setText(ingredient.getIngredient());

        assert holder.unitNumber != null;
        holder.unitNumber.setText(String.valueOf(ingredient.getQuantity()));

        Objects.requireNonNull(holder.ingredientListNumber).setText(String.valueOf(position+1));

        String measure = ingredient.getMeasure();
        int unitNo = 0;

        for (int i=0; i < BakingConstants.units.length; i++) {
            if (measure.equals(BakingConstants.units[i])) {
                unitNo = i;
                break;
            }
        }

        int unitIcon  = BakingConstants.unitIcons[unitNo];
        Log.d("UNIT_NO: ", String.valueOf(unitIcon) );
        String unitFullName = BakingConstants.unitName[unitNo];

        assert holder.unitIcon != null;
        holder.unitIcon.setImageResource(unitIcon);

        assert holder.ingredientUnitFullName != null;
        holder.ingredientUnitFullName.setText(unitFullName);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                assert holder.ingredientChecked != null;
                if (holder.ingredientChecked.getVisibility() == View.GONE) {
                    holder.ingredientChecked.setVisibility(View.VISIBLE);
                } else {
                    holder.ingredientChecked.setVisibility(View.GONE);
                }
            }
        });



    }

    @Override
    public int getItemCount() {
        return mIngredietList.size();
    }

    class RecipeDetailsViewHolder extends RecyclerView.ViewHolder {

        @Nullable
        @BindView(R.id.iv_unit_icon)
        ImageView unitIcon;

        @Nullable
        @BindView(R.id.tv_ingredient_name)
        TextView ingredientName;

        @Nullable
        @BindView(R.id.tv_unit_number)
        TextView unitNumber;

        @Nullable
        @BindView(R.id.tv_ingredient_number)
        TextView ingredientListNumber;

        @Nullable
        @BindView(R.id.tv_unit_full_name)
        TextView ingredientUnitFullName;

        @Nullable
        @BindView(R.id.iv_ingredient_checked)
        ImageView ingredientChecked;

        RecipeDetailsViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}

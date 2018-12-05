package com.izzy.darx.bakingapp.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.izzy.darx.bakingapp.R;
import com.izzy.darx.bakingapp.model.Step;
import com.izzy.darx.bakingapp.ui.baking.BakingActivity;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class StepListAdapter extends RecyclerView.Adapter<StepListAdapter.StepListHolder> {

    private final Context mContext;
    private final ArrayList<Step> mStepArrayList;
    private OnStepClick mOnStepClick;
    private int rowNo = 0;

    public StepListAdapter(Context context, ArrayList<Step> stepArrayList, BakingActivity onStepClick, int rowNo ) {
       this.mContext = context;
       this.mStepArrayList = stepArrayList;
       this.mOnStepClick = (OnStepClick) onStepClick;
       this.rowNo = rowNo;

    }

    @NonNull
    @Override
    public StepListAdapter.StepListHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.step_list_item, parent, false);
        return new StepListHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final StepListAdapter.StepListHolder holder, int position) {

        assert holder.stepTitle != null;
        holder.stepTitle.setText(mStepArrayList.get(position).getShortDescription());

        assert holder.stepNumber != null;
        holder.stepNumber.setText(String.valueOf(position+1));

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mOnStepClick.onStepClick(holder.getAdapterPosition());
                rowNo = holder.getAdapterPosition();
                notifyDataSetChanged();
            }
        });

        if(rowNo == position) {
            holder.itemView.setBackgroundColor(mContext.getResources().getColor(R.color.colorPrimary));
        } else {
            holder.itemView.setBackgroundColor(mContext.getResources().getColor(R.color.colorPrimaryDark));
        }

    }

    @Override
    public int getItemCount() {
        return mStepArrayList.size();
    }

    class StepListHolder extends RecyclerView.ViewHolder {

        @Nullable
        @BindView(R.id.tv_step_number)
        TextView stepNumber;

        @Nullable
        @BindView(R.id.tv_step_title)
        TextView stepTitle;

        public StepListHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    public interface OnStepClick {
        void onStepClick(int position);
    }
}

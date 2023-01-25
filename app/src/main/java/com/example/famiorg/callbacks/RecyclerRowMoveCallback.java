package com.example.famiorg.callbacks;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;

import com.example.famiorg.adapters.Adapter_GroceryProduct;

public class RecyclerRowMoveCallback extends ItemTouchHelper.Callback {

    private RecyclerViewRowTouchHelperContract touchHelperContract;


    public RecyclerRowMoveCallback(RecyclerViewRowTouchHelperContract touchHelperContract) {
        this.touchHelperContract = touchHelperContract;
    }

    @Override
    public boolean isLongPressDragEnabled() {
        return true;
    }

    @Override
    public boolean isItemViewSwipeEnabled() {
        return false;
    }

    @Override
    public int getMovementFlags(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder) {
        int dragFlag = ItemTouchHelper.UP | ItemTouchHelper.DOWN;
        return makeMovementFlags(dragFlag, 0);
    }

    @Override
    public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
        this.touchHelperContract.onRowMoved(viewHolder.getAdapterPosition(), target.getAdapterPosition());

        return true;
    }

    @Override
    public void onSelectedChanged(@Nullable RecyclerView.ViewHolder viewHolder, int actionState) {
        if(actionState != ItemTouchHelper.ACTION_STATE_IDLE) {
            if(viewHolder instanceof Adapter_GroceryProduct.GroceryProductViewHolder) {
                Adapter_GroceryProduct.GroceryProductViewHolder myViewHolder = (Adapter_GroceryProduct.GroceryProductViewHolder)viewHolder;
                touchHelperContract.onRowSelected(myViewHolder);
            }
        }

        super.onSelectedChanged(viewHolder, actionState);
    }

    @Override
    public void clearView(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder) {
        if(viewHolder instanceof Adapter_GroceryProduct.GroceryProductViewHolder) {
            Adapter_GroceryProduct.GroceryProductViewHolder myViewHolder = (Adapter_GroceryProduct.GroceryProductViewHolder)viewHolder;
            touchHelperContract.onRowClear(myViewHolder);
        }

        super.clearView(recyclerView, viewHolder);
    }

    @Override
    public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {

    }

    public interface RecyclerViewRowTouchHelperContract {
        void onRowMoved(int from, int to);
        void onRowSelected(Adapter_GroceryProduct.GroceryProductViewHolder myViewHolder);
        void onRowClear(Adapter_GroceryProduct.GroceryProductViewHolder myViewHolder);
    }
}

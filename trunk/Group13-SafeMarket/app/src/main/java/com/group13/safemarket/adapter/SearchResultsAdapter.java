package com.group13.safemarket.adapter;



/**
 * Created by Khanh Tran-Cong on 5/8/2018.
 * Email: congkhanh197@gmail.com
 */
public class SearchResultsAdapter{
//public class SearchResultsAdapter extends RecyclerView.Adapter<SearchResultsAdapter.ViewHolder> {
//
//    private List<ColorWrapper> mDataSet = new ArrayList<>();
//
//    private int mLastAnimatedItemPosition = -1;
//
//    public interface OnItemClickListener{
//        void onClick(ColorWrapper colorWrapper);
//    }
//
//    private OnItemClickListener mItemsOnClickListener;
//
//    public static class ViewHolder extends RecyclerView.ViewHolder {
//        public final TextView mColorName;
//        public final TextView mColorValue;
//        public final View mTextContainer;
//
//        public ViewHolder(View view) {
//            super(view);
//            mColorName = (TextView) view.findViewById(R.id.color_name);
//            mColorValue = (TextView) view.findViewById(R.id.color_value);
//            mTextContainer = view.findViewById(R.id.text_container);
//        }
//    }
//
//    public void swapData(List<ColorWrapper> mNewDataSet) {
//        mDataSet = mNewDataSet;
//        notifyDataSetChanged();
//    }
//
//    public void setItemsOnClickListener(OnItemClickListener onClickListener){
//        this.mItemsOnClickListener = onClickListener;
//    }
//
//    @Override
//    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
//        View view = LayoutInflater.from(parent.getContext())
//                .inflate(R.layout.search_results_list_item, parent, false);
//        return new ViewHolder(view);
//    }
//
//    @Override
//    public void onBindViewHolder(ViewHolder holder, final int position) {
//
//        ColorWrapper colorSuggestion = mDataSet.get(position);
//        holder.mColorName.setText(colorSuggestion.getName());
//        holder.mColorValue.setText(colorSuggestion.getHex());
//
//        int color = Color.parseColor(colorSuggestion.getHex());
//        holder.mColorName.setTextColor(color);
//        holder.mColorValue.setTextColor(color);
//
//        if(mLastAnimatedItemPosition < position){
//            animateItem(holder.itemView);
//            mLastAnimatedItemPosition = position;
//        }
//
//        if(mItemsOnClickListener != null){
//            holder.itemView.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    mItemsOnClickListener.onClick(mDataSet.get(position));
//                }
//            });
//        }
//    }
//
//    @Override
//    public int getItemCount() {
//        return mDataSet.size();
//    }
//
//    private void animateItem(View view) {
//        view.setTranslationY(Util.getScreenHeight((Activity) view.getContext()));
//        view.animate()
//                .translationY(0)
//                .setInterpolator(new DecelerateInterpolator(3.f))
//                .setDuration(700)
//                .start();
//    }
}

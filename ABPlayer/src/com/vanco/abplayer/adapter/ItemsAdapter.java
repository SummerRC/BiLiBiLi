package com.vanco.abplayer.adapter;


import com.minisea.bilibli.R;

import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class ItemsAdapter extends ArrayAdapter<Integer>{

    Context context; 
    LayoutInflater inflater;
    String[] texts;
    int layoutResourceId;
    float imageWidth;
    
    public ItemsAdapter(Context context, int layoutResourceId, Integer[] items,String[] texts) {
        super(context, layoutResourceId, items);
        this.context = context;
        this.layoutResourceId = layoutResourceId;
        this.texts = texts;
        
        float width = ((Activity)context).getWindowManager().getDefaultDisplay().getWidth();
        float margin = (int)convertDpToPixel(10f, (Activity)context);
        // two images, three margins of 10dips
		imageWidth = ((width - (3 * margin)) / 2);
    }

	@Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LinearLayout row = (LinearLayout) convertView;
        ItemHolder holder;
        Integer item = getItem(position);
        
		if (row == null) {
			holder = new ItemHolder();
            inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            row = (LinearLayout) inflater.inflate(layoutResourceId, parent, false);
            ImageView itemImage = (ImageView)row.findViewById(R.id.item_image);
            TextView itemText = (TextView)row.findViewById(R.id.bankumi_item_title);
			holder.itemImage = itemImage;
			holder.itemText = itemText;
		} else {
			holder = (ItemHolder) row.getTag();
		}
		
		row.setTag(holder);
		setImageBitmap(item, holder.itemImage);
		holder.itemText.setText(texts[position]);
        return row;
    }

    public static class ItemHolder
    {
    	ImageView itemImage;
    	TextView itemText;
    }
	
    // resize the image proportionately so it fits the entire space
	private void setImageBitmap(Integer item, ImageView imageView){
		Bitmap bitmap = BitmapFactory.decodeResource(getContext().getResources(), item);
		float i = ((float) imageWidth) / ((float) bitmap.getWidth());
		float imageHeight = i * (bitmap.getHeight());
		LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) imageView.getLayoutParams();
		params.height = (int) imageHeight;
		params.width = (int) imageWidth;
		imageView.setLayoutParams(params);
		imageView.setImageResource(item);
	}
	
	public static float convertDpToPixel(float dp, Context context){
	    Resources resources = context.getResources();
	    DisplayMetrics metrics = resources.getDisplayMetrics();
	    float px = dp * (metrics.densityDpi/160f);
	    return px;
	}

}
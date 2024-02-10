package com.example.texttospeech;


import androidx.viewpager.widget.PagerAdapter;
import androidx.annotation.NonNull;
import android.content.Context;
import android.widget.*;
import android.view.*;

public class ViewPagerAdapter extends PagerAdapter {
    Context context;

    int images[] = {
            R.drawable.slide_01,
            R.drawable.slide_02,
            R.drawable.slide_03
    };
    int titles[] = {
            R.string.title_one,
            R.string.title_two,
            R.string.title_three
    };
    int descriptions[] = {
            R.string.description_one,
            R.string.description_two,
            R.string.description_three
    };

    public ViewPagerAdapter(Context context){
        this.context = context;
    }

    @Override
    public int getCount() {
        return  titles.length;
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view == (LinearLayout) object;
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
        View view = layoutInflater.inflate(R.layout.page_layout,container,false);

        ImageView slidetitleimage = (ImageView) view.findViewById(R.id.slideImage);
        TextView slideHeading = (TextView) view.findViewById(R.id.slideTitle);
        TextView slideDesciption = (TextView) view.findViewById(R.id.slideDescription);

        slidetitleimage.setImageResource(images[position]);
        slideHeading.setText(titles[position]);
        slideDesciption.setText(descriptions[position]);

        container.addView(view);

        return view;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((LinearLayout)object);
    }
}

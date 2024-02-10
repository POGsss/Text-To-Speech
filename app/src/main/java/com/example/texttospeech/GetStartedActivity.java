package com.example.texttospeech;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;
import android.content.*;
import android.widget.*;
import android.text.*;
import android.view.*;
import android.os.*;

public class GetStartedActivity extends AppCompatActivity {
    ViewPagerAdapter viewPagerAdapter;
    ViewPager viewPager;
    View divider;
    LinearLayout pageIndicator;
    Button nextbtn, skipbtn;
    TextView[] dots;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        setContentView(R.layout.activity_get_started);

        skipbtn = findViewById(R.id.skipBtn);
        divider = findViewById(R.id.divider);
        nextbtn = findViewById(R.id.nextBtn);

        skipbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(GetStartedActivity.this, HomeActivity.class);
                startActivity(i);
                finish();
            }
        });

        nextbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (getitem(0) < 2)
                    viewPager.setCurrentItem(getitem(1),true);
                else {
                    Intent i = new Intent(GetStartedActivity.this, HomeActivity.class);
                    startActivity(i);
                    finish();
                }
            }
        });

        viewPager = (ViewPager) findViewById(R.id.viewPager);
        pageIndicator = (LinearLayout) findViewById(R.id.pageIndicator);

        viewPagerAdapter = new ViewPagerAdapter(this);
        viewPager.setAdapter(viewPagerAdapter);

        setUpindicator(0);
        viewPager.addOnPageChangeListener(viewListener);
    }

    public void setUpindicator(int position){
        dots = new TextView[3];
        pageIndicator.removeAllViews();

        for (int i = 0 ; i < dots.length ; i++){
            dots[i] = new TextView(this);
            dots[i].setText(Html.fromHtml("&#8226"));
            dots[i].setTextSize(35);
            dots[i].setTextColor(getResources().getColor(R.color.light));
            pageIndicator.addView(dots[i]);

        }

        dots[position].setTextColor(getResources().getColor(R.color.primary));
    }

    ViewPager.OnPageChangeListener viewListener = new ViewPager.OnPageChangeListener() {
        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

        }

        @Override
        public void onPageSelected(int position) {
            setUpindicator(position);
            if(position < 2) {
                skipbtn.setVisibility(View.VISIBLE);
                divider.setVisibility(View.VISIBLE);
                nextbtn.setText("Next");
            } else {
                skipbtn.setVisibility(View.GONE);
                divider.setVisibility(View.GONE);
                nextbtn.setText("Get Started");
            }
        }

        @Override
        public void onPageScrollStateChanged(int state) {

        }
    };

    private int getitem(int i){
        return viewPager.getCurrentItem() + i;
    }
}
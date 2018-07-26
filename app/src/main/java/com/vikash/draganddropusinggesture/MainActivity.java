package com.vikash.draganddropusinggesture;

import android.graphics.Rect;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;

public class MainActivity extends AppCompatActivity {

    private RelativeLayout rl_chatBot;
    private ImageView mAvatarIcon;
    private ImageView trash;
    private Rect outRect = new Rect();
    private int[] location = new int[2];
    private boolean mIsScrolling = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        rl_chatBot = (RelativeLayout) findViewById(R.id.rl_chatBot);
        mAvatarIcon = findViewById(R.id.bn_avatar);
        trash = findViewById(R.id.trash);

        mAvatarIcon.setOnTouchListener(new MoveViewTouchListener(rl_chatBot));

        trash.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                rl_chatBot.setVisibility(View.VISIBLE);
                trash.setVisibility(View.GONE);
                trash.setBackgroundResource(R.drawable.open);
            }
        });
    }

    public class MoveViewTouchListener implements View.OnTouchListener {
        private GestureDetector mGestureDetector;
        private View mView;

        public MoveViewTouchListener(View view) {
            mGestureDetector = new GestureDetector(view.getContext(), mGestureListener);
            mView = view;
        }

        @Override
        public boolean onTouch(View v, MotionEvent event) {

            if (event.getAction() == MotionEvent.ACTION_UP && isViewInBounds(trash, (int) event.getRawX(), (int) event.getRawY())) {
                rl_chatBot.setVisibility(View.GONE);
                trash.setVisibility(View.VISIBLE);
                trash.setBackgroundResource(R.drawable.chat_icon);
                mIsScrolling = false;
            }

            if (mGestureDetector.onTouchEvent(event)) {
                return true;
            }
            if (event.getAction() == MotionEvent.ACTION_UP) {
                if (mIsScrolling) {
                    mIsScrolling = false;
                    trash.setVisibility(View.GONE);
                }
            }
            return false;
        }

        private GestureDetector.OnGestureListener mGestureListener = new GestureDetector.SimpleOnGestureListener() {
            private float mMotionDownX, mMotionDownY;

            @Override
            public boolean onDown(MotionEvent e) {
                mMotionDownX = e.getRawX() - mView.getTranslationX();
                mMotionDownY = e.getRawY() - mView.getTranslationY();
                return true;
            }

            @Override
            public boolean onSingleTapUp(MotionEvent e) {
                return true;
            }

            @Override
            public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
                mIsScrolling = true;
                trash.setVisibility(View.VISIBLE);
                mView.setTranslationX(e2.getRawX() - mMotionDownX);
                mView.setTranslationY(e2.getRawY() - mMotionDownY);
                return true;
            }
        };
    }

    private boolean isViewInBounds(View view, int x, int y) {
        view.getDrawingRect(outRect);
        view.getLocationOnScreen(location);
        outRect.offset(location[0], location[1]);
        return outRect.contains(x, y);
    }
}

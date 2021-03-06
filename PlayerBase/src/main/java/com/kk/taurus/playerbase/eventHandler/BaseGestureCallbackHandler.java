/*
 * Copyright 2017 jiajunhui<junhui_jia@163.com>
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */

package com.kk.taurus.playerbase.eventHandler;

import android.view.GestureDetector;
import android.view.MotionEvent;

import com.kk.taurus.playerbase.callback.OnPlayerGestureListener;
import com.kk.taurus.playerbase.utils.PLog;

/**
 * Created by Taurus on 2017/11/20.
 */

public class BaseGestureCallbackHandler extends GestureDetector.SimpleOnGestureListener {

    private final String TAG = "GestureCallbackHandler";
    protected OnPlayerGestureListener mOnPlayerGestureListener;

    protected int mWidth, mHeight;
    protected boolean firstTouch;
    protected boolean horizontalSlide;
    protected boolean rightVerticalSlide;
    private boolean mGestureEnable = true;

    public BaseGestureCallbackHandler(OnPlayerGestureListener onPlayerGestureListener){
        this.mOnPlayerGestureListener = onPlayerGestureListener;
    }

    public void onLayoutSizeChanged(int w, int h, int oldw, int oldh){
        mWidth = w;
        mHeight = h;
        PLog.d(TAG,"width = " + mWidth + " height = " + mHeight);
    }

    public void setGestureEnable(boolean enable){
        this.mGestureEnable = enable;
    }

    @Override
    public boolean onSingleTapUp(MotionEvent e) {
        if(mOnPlayerGestureListener!=null){
            mOnPlayerGestureListener.onSingleTapUp(e);
        }
        return super.onSingleTapUp(e);
    }

    @Override
    public boolean onDoubleTap(MotionEvent e) {
        if(mOnPlayerGestureListener!=null){
            mOnPlayerGestureListener.onDoubleTap(e);
        }
        return super.onDoubleTap(e);
    }

    @Override
    public boolean onDown(MotionEvent e) {
        PLog.d(TAG,"onDown...");
        firstTouch = true;
        if(mOnPlayerGestureListener!=null){
            mOnPlayerGestureListener.onDown(e);
        }
        return mGestureEnable;
    }

    @Override
    public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
        float mOldX = e1.getX(), mOldY = e1.getY();
        float deltaY = mOldY - e2.getY();
        float deltaX = mOldX - e2.getX();
        if (firstTouch) {
            horizontalSlide = Math.abs(distanceX) >= Math.abs(distanceY);
            rightVerticalSlide = mOldX > mWidth * 0.5f;
            firstTouch = false;
        }

        if(horizontalSlide){
            if(mOnPlayerGestureListener!=null){
                mOnPlayerGestureListener.onHorizontalSlide(-deltaX / mWidth ,e1, e2, distanceX, distanceY);
            }
        }else{
            if(Math.abs(deltaY) > mHeight)
                return super.onScroll(e1, e2, distanceX, distanceY);
            if(rightVerticalSlide){
                if(mOnPlayerGestureListener!=null){
                    mOnPlayerGestureListener.onRightVerticalSlide(deltaY / mHeight, e1, e2, distanceX, distanceY);
                }
            }else{
                if(mOnPlayerGestureListener!=null){
                    mOnPlayerGestureListener.onLeftVerticalSlide(deltaY / mHeight, e1, e2, distanceX, distanceY);
                }
            }
        }
        if(mOnPlayerGestureListener!=null){
            mOnPlayerGestureListener.onScroll(e1, e2, distanceX, distanceY);
        }
        return super.onScroll(e1, e2, distanceX, distanceY);
    }

    public void onEndGesture(MotionEvent event){
        if(mOnPlayerGestureListener!=null){
            mOnPlayerGestureListener.onEndGesture();
        }
    }
}

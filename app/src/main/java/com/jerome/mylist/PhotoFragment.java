package com.jerome.mylist;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


public class PhotoFragment extends Fragment {
    /**
     * Create a new instance of DetailsFragment, initialized to
     * show the text at 'index'.
     */
    public static PhotoFragment newInstance(String title, String url) {
        PhotoFragment f = new PhotoFragment();

        // Supply index input as an argument.
        Bundle args = new Bundle();
        args.putString("title", title);
        args.putString("url", url);
        Log.e("my_args", title + " - " + url);
        f.setArguments(args);

        return f;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        if (container == null) {
            // We have different layouts, and in one of them this
            // fragment's containing frame doesn't exist.  The fragment
            // may still be created from its saved state, but there is
            // no reason to try to create its view hierarchy because it
            // won't be displayed.  Note this is not needed -- we could
            // just run the code below, where we would create and return
            // the view hierarchy; it would just never be used.
            return null;
        }
        String title = getArguments().getString("title");
        String url = getArguments().getString("url");
        Log.e("my_args", title + " - " + url);
        //        text.setText("eeeee");
//        return scroller;
        return getView();
    }
}

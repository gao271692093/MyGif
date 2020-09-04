package com.glg.mygif.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.glg.mygif.R;

/**
 * Description:
 *
 * @package: com.glg.mygif
 * @className: ConsiderFragment
 * @author: gao
 * @date: 2020/8/4 18:25
 */
public class ConsiderFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.consider_layout, container, false);
        return view;
    }
}

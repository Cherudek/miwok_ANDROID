package com.example.android.miwok;


import android.content.Context;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

public class FamilyFragment extends Fragment {

    private MediaPlayer mMediaPlayer;
    private AudioManager mAudioManager;

    private MediaPlayer.OnCompletionListener mCompletionListener = new MediaPlayer.OnCompletionListener() {
        @Override
        public void onCompletion(MediaPlayer mediaPlayer) {
            releaseMediaPlayer();
        }
    };

    private AudioManager.OnAudioFocusChangeListener mOnAudioFocusChangeListener = new AudioManager.OnAudioFocusChangeListener() {
        @Override
        public void onAudioFocusChange(int focusChange) {
            if (focusChange == AudioManager.AUDIOFOCUS_LOSS_TRANSIENT || focusChange == AudioManager.AUDIOFOCUS_LOSS_TRANSIENT_CAN_DUCK) {
                mMediaPlayer.pause();
                mMediaPlayer.seekTo(0);
            } else if (focusChange == AudioManager.AUDIOFOCUS_GAIN) {
                mMediaPlayer.start();
            } else if (focusChange == AudioManager.AUDIOFOCUS_LOSS) {
                releaseMediaPlayer();
            }
        }
    };

    private void releaseMediaPlayer() {
        if (mMediaPlayer != null) {
            mMediaPlayer.release();
        }
        mMediaPlayer = null;
        mAudioManager.abandonAudioFocus(mOnAudioFocusChangeListener);
    }


    public FamilyFragment() {
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.word_list, container, false);

        mAudioManager = (AudioManager) getActivity().getSystemService(Context.AUDIO_SERVICE);

        final ArrayList<Word> words = new ArrayList<>();
        words.add(new Word(getResources().getString(R.string.family_father_eng), getResources().getString(R.string.family_father_miwok), R.drawable.family_father, R.raw.family_father));
        words.add(new Word(getResources().getString(R.string.family_mother_eng), getResources().getString(R.string.family_mother_miwok), R.drawable.family_mother, R.raw.family_mother));
        words.add(new Word(getResources().getString(R.string.family_son_eng), getResources().getString(R.string.family_son_miwok), R.drawable.family_son, R.raw.family_son));
        words.add(new Word(getResources().getString(R.string.family_daughter_eng), getResources().getString(R.string.family_daughter_miwok), R.drawable.family_daughter, R.raw.family_daughter));
        words.add(new Word(getResources().getString(R.string.family_older_brother_eng), getResources().getString(R.string.family_older_brother_miwok), R.drawable.family_older_brother, R.raw.family_older_brother));
        words.add(new Word(getResources().getString(R.string.family_younger_brother_eng), getResources().getString(R.string.family_younger_brother_miwok), R.drawable.family_younger_brother, R.raw.family_younger_brother));
        words.add(new Word(getResources().getString(R.string.family_older_sister_eng), getResources().getString(R.string.family_older_sister_miwok), R.drawable.family_older_sister, R.raw.family_older_sister));
        words.add(new Word(getResources().getString(R.string.family_younger_sister_eng), getResources().getString(R.string.family_younger_sister_miwok), R.drawable.family_younger_sister, R.raw.family_younger_sister));
        words.add(new Word(getResources().getString(R.string.family_grandmother_eng), getResources().getString(R.string.family_grandmother_miwok), R.drawable.family_grandmother, R.raw.family_grandmother));
        words.add(new Word(getResources().getString(R.string.family_grandfather_eng), getResources().getString(R.string.family_grandfather_miwok), R.drawable.family_grandfather, R.raw.family_grandfather));

        WordAdapter wordsAdapter = new WordAdapter(getActivity(), words, R.color.category_family);

        ListView listView = (ListView) rootView.findViewById(R.id.list);

        listView.setAdapter(wordsAdapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {

                releaseMediaPlayer();

                Word word = words.get(position);

                int result = mAudioManager.requestAudioFocus(mOnAudioFocusChangeListener, AudioManager.STREAM_MUSIC, AudioManager.AUDIOFOCUS_GAIN_TRANSIENT);

                if (result == AudioManager.AUDIOFOCUS_REQUEST_GRANTED) {
                    mMediaPlayer = MediaPlayer.create(getActivity(), word.getAudioResourceId());
                    mMediaPlayer.start();
                    mMediaPlayer.setOnCompletionListener(mCompletionListener);
                }
            }
        });

        return rootView;
    }

    @Override
    public void onStop() {
        super.onStop();
        releaseMediaPlayer();
    }
}

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

import java.util.ArrayList;

public class NumbersFragment extends Fragment {

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

    public NumbersFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.word_list, container, false);

        mAudioManager = (AudioManager) getActivity().getSystemService(Context.AUDIO_SERVICE);

        final ArrayList<Word> words = new ArrayList<>();
        words.add(new Word(getResources().getString(R.string.number_one_eng), getResources().getString(R.string.number_one_miwok), R.drawable.number_one, R.raw.number_one));
        words.add(new Word(getResources().getString(R.string.number_two_eng), getResources().getString(R.string.number_two_miwok), R.drawable.number_two, R.raw.number_two));
        words.add(new Word(getResources().getString(R.string.number_three_eng), getResources().getString(R.string.number_three_miwok), R.drawable.number_three, R.raw.number_three));
        words.add(new Word(getResources().getString(R.string.number_four_eng), getResources().getString(R.string.number_four_miwok), R.drawable.number_four, R.raw.number_four));
        words.add(new Word(getResources().getString(R.string.number_five_eng), getResources().getString(R.string.number_five_miwok), R.drawable.number_five, R.raw.number_five));
        words.add(new Word(getResources().getString(R.string.number_six_eng), getResources().getString(R.string.number_six_miwok), R.drawable.number_six, R.raw.number_six));
        words.add(new Word(getResources().getString(R.string.number_seven_eng), getResources().getString(R.string.number_seven_miwok), R.drawable.number_seven, R.raw.number_seven));
        words.add(new Word(getResources().getString(R.string.number_eight_eng), getResources().getString(R.string.number_eight_miwok), R.drawable.number_eight, R.raw.number_eight));
        words.add(new Word(getResources().getString(R.string.number_nine_eng), getResources().getString(R.string.number_nine_miwok), R.drawable.number_nine, R.raw.number_nine));
        words.add(new Word(getResources().getString(R.string.number_ten_eng), getResources().getString(R.string.number_ten_miwok), R.drawable.number_ten, R.raw.number_ten));

        WordAdapter wordsAdapter = new WordAdapter(getActivity(), words, R.color.category_numbers);

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

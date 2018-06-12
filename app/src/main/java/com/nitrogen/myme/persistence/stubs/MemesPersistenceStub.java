package com.nitrogen.myme.persistence.stubs;

import android.net.Uri;

import com.nitrogen.myme.R;
import com.nitrogen.myme.objects.ImageMeme;
import com.nitrogen.myme.objects.Tag;
import com.nitrogen.myme.persistence.MemesPersistence;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import com.nitrogen.myme.objects.Meme;

public class MemesPersistenceStub implements MemesPersistence {

    private final int NUM_MEMES = 32; // 8 * 4
    private List<Meme> memes;

    private List<Tag> tags;

    private int[] memeNames = {R.mipmap.meme1,R.mipmap.meme2,R.mipmap.meme3,R.mipmap.meme4,
                                      R.mipmap.meme5,R.mipmap.meme6,R.mipmap.meme7,R.mipmap.meme8};

    public MemesPersistenceStub() {
        this.memes = new ArrayList<>();
        this.tags = new TagsPersistenceStub().getTags();

        for(int i = 0 ; i < NUM_MEMES ; i++) {
            memes.add(new ImageMeme("meme", Uri.parse("android.resource://com.nitrogen.myme/"
                                    + memeNames[i%memeNames.length]), randomTags(i)));
        }
    }

    /* randomTags
     *
     * purpose: Assign pseudo-random tags to a meme.
     *          This ensures each meme has at least 1 tag.
     *
     */
    private ArrayList<Tag> randomTags(int num) {
        ArrayList<Tag> result = new ArrayList<>();

        if(num%3 == 0) {
            result.add(tags.get(0));
        }
        if(num%2 == 0) {
            result.add(tags.get(1));
        }
        if(num%2 == 1) {
            result.add(tags.get(0));
        }
        if(num%5 == 0) {
            result.add(tags.get(2));
        }
        if(num%7 == 0) {
            result.add(tags.get(3));
        }

        return result;
    }

    @Override
    public List<Meme> getMemeSequential() {

        return Collections.unmodifiableList(memes);
    }

    @Override
    public List<Meme> getMemeRandom(Meme currentMeme) {
        List<Meme> newMemes = new ArrayList<>();
        int index;

        index = memes.indexOf(currentMeme);
        if (index >= 0)
        {
            newMemes.add(memes.get(index));
        }
        return newMemes;
    }

    /* getMemesByTags
     *
     * purpose: Filters through the meme database to return a list of Memes where each
     *          Meme has one or more of the tags in the list of tags provided.
     */
    @Override
    public List<Meme> getMemesByTags(List<Tag> tags) {
        List<Meme> memeList = new ArrayList<>();
        List<Tag> currMemeTags;

        for(int i = 0 ; i< this.memes.size() ; i++) {
            currMemeTags = (this.memes.get(i)).getTags();

            for(int j = 0 ; j < tags.size() ; j++) {
                if (currMemeTags.contains(tags.get(j))) {
                    memeList.add(this.memes.get(i));
                }
            }
        }
        return memeList;
    }

    @Override
    public Meme insertMeme(Meme currentMeme) {
        // don't bother checking for duplicates
        memes.add(currentMeme);
        return currentMeme;
    }

    @Override
    public Meme updateMeme(Meme currentMeme) {
        int index;

        index = memes.indexOf(currentMeme);
        if (index >= 0)
        {
            memes.set(index, currentMeme);
        }
        return currentMeme;
    }

    @Override
    public void deleteMeme(Meme currentMeme) {
        int index;

        index = memes.indexOf(currentMeme);
        if (index >= 0)
        {
            memes.remove(index);
        }
    }
}

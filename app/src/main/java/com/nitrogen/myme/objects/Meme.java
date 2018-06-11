package com.nitrogen.myme.objects;

import android.net.Uri;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public abstract class Meme {
    private String name;
    private String description;
    private Author author;
    private Date creationDate;
    private List<Tag> tags;
    private int memeID;
    private static int lastMemeID = 0;
    public abstract Uri getThumbnailPath();

    public Meme(final String name, final List<Tag> tags)
    {
        this.name = name;
        this.description = null;
        this.author = null;
        this.creationDate = new Date();
        this.tags = tags;
        this.memeID = lastMemeID++;
    }

    // accessors
    public String getName() { return name; }

    public int getMemeID() { return memeID; }

    @Override
    public String toString() {
        return name;
    }


}

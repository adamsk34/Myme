package com.nitrogen.myme.objects;

import java.util.List;
import java.util.ArrayList;
import java.util.Date;

/* Meme
 *
 * purpose: This abstract class provides a base of metadata and functionality
 *          for subclass implementations of a specific media format (e.g. ImageMeme)
 */
public abstract class Meme {
    private String name;
    private String description;
    private List<Tag> tags;
    private boolean isFavourite = false;
    private final Author author;
    private final Date creationDate;
    private final int memeID = lastMemeID++; //unique ID

    private static int lastMemeID = 0; //used to ensure all memes have a unique ID

    //**************************************************
    // Constructors
    //**************************************************

    public Meme(String name) {
        this.name = name;
        this.description = "";
        this.author = new Author();
        this.creationDate = new Date();
        this.tags = new ArrayList<>();
    }

    public Meme(String name, String description, Author author) {
        this.name = name;
        this.description = description;
        this.author = author;
        this.creationDate = new Date();
        this.tags = new ArrayList<>();
    }

    public Meme(String name, String description, Author author, Date creationDate) {
        this.name = name;
        this.description = description;
        this.author = author;
        this.creationDate = creationDate;
        this.tags = new ArrayList<>();
    }

    public Meme(String name, String description, Author author, Date creationDate, List<Tag> tags) {
        this.name = name;
        this.description = description;
        this.author = author;
        this.creationDate = creationDate;
        this.tags = tags;
    }

    //**************************************************
    // Abstract Methods
    //**************************************************

    /* getThumbnailPath
     *
     * purpose: A method to be implemented by subclasses to allow for the displaying
     *          of a thumbnail of this meme by providing a path to an image.
     */
    public abstract String getThumbnailPath();

    //**************************************************
    // Helper Methods
    //**************************************************

    /* hasTag
     *
     * purpose: To check if this meme has a given tag.
     */
    public boolean hasTag(Tag tag) {
        return tags.contains(tag);
    }

    /* addTag
     *
     * purpose: To add a tag to this meme.
     */
    public boolean addTag(Tag tag) {
        return tags.add(tag);
    }

    /* removeTag
     *
     * purpose: To remove a tag from this meme.
     */
    public boolean removeTag(Tag tag) {
        return tags.remove(tag);
    }

    @Override
    public boolean equals(Object otherMeme) {
        return otherMeme instanceof Meme && memeID == ((Meme) otherMeme).getMemeID();
    }

    @Override
    public String toString() {
        return "Meme{" +
                "name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", author=" + author +
                ", creationDate=" + creationDate +
                ", tags=" + tags +
                ", isFavourite=" + isFavourite +
                ", memeID=" + memeID +
                '}';
    }

    //**************************************************
    // Mutator Methods
    //**************************************************

    public void setName(String name) { this.name = name; }

    public void setDescription(String description) { this.description = description; }

    public void setFavourite(boolean favourite) { isFavourite = favourite; }

    public void setTags(List<Tag> tags) { this.tags = tags; }

    //**************************************************
    // Accessor Methods
    //**************************************************

    public String getName() { return name; }

    public String getDescription() { return description; }

    public Author getAuthor() { return author; }

    public Date getCreationDate() { return creationDate; }

    public boolean isFavourite() { return isFavourite; }

    public int getMemeID() { return memeID; }

    public List<Tag> getTags() { return this.tags; }

}

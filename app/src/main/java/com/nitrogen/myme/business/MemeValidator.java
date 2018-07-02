package com.nitrogen.myme.business;

import com.nitrogen.myme.application.Services;
import com.nitrogen.myme.business.Exceptions.InvalidMemeException;
import com.nitrogen.myme.objects.Meme;
import com.nitrogen.myme.objects.Tag;
import com.nitrogen.myme.persistence.MemesPersistence;
import com.nitrogen.myme.persistence.TagsPersistence;

import java.util.List;

public class MemeValidator {
    private final int MAX_NAME_LEN = 32;
    private TagsPersistence tagsPersistence;
    private MemesPersistence memesPersistence;

    public MemeValidator () {
        tagsPersistence = Services.getTagsPersistence();
        memesPersistence = Services.getMemesPersistence();
    }

    /* hasValidName
     *
     * purpose: decide if a meme's name is valid. valid length is >= 1 and <= 32
     */
    public void validateName(final Meme memeGiven) {
        if(memeGiven == null)
            throw new InvalidMemeException("Meme cannot be null");
        else if(memeGiven.getName() == null)
            throw new InvalidMemeException("Name cannot be null");
        else if(memeGiven.getName().length() <= 0)
            throw new InvalidMemeException("Meme name length must be > 0");
        else if(memeGiven.getName().length() > MAX_NAME_LEN)
            throw new InvalidMemeException("Meme name length must be <= " + MAX_NAME_LEN);
        else if(!originalMemeName(memeGiven.getName()))
            throw new InvalidMemeException("Meme name already exists");
    }

    private boolean originalMemeName (final String name) {
        boolean isOriginal = true;
        List<Meme> memeList = memesPersistence.getMemes();

        for(int i = 0 ; i < memeList.size() && isOriginal ; i++) {
            if(memeList.get(i).getName().equals( name ))
                isOriginal = false;
        }

        return isOriginal;
    }

    /* hasValidTags
     *
     * purpose: decide if a meme's tags are valid. valid length is >= 1 and <= 32
     */
    public void validateTags(final Meme memeGiven) {
        if(memeGiven == null)
            throw new InvalidMemeException("Meme cannot be null");
        else if(memeGiven.getTags() == null)
            throw new InvalidMemeException("Tags cannot be null");
        else if(memeGiven.getTags().size() <= 0)
            throw new InvalidMemeException("Number of tags must be > 0");
        else if(memeGiven.getTags().size() > tagsPersistence.getTags().size())
            throw new InvalidMemeException("Number of tags must be <= " + tagsPersistence.getTags().size());
        else if(containsNonExistentTag(memeGiven)) {
            throw new InvalidMemeException("Some tags in Meme do not exist in app");
        } else if(containsDuplicateTags(memeGiven)) {
            throw new InvalidMemeException("Meme contains multiples of the same tag");
        }
    }

    private boolean containsDuplicateTags (Meme memeGiven) {
        boolean hasDuplicate = false;
        List<Tag> tagList = memeGiven.getTags();

        // loops through memeGiven.getTags()
        for(int indSlow = 0 ; indSlow < tagList.size() ; indSlow++) {
            // for each tag, check if it exists later
            for(int indFast = indSlow +1 ; indFast < tagList.size() ; indFast++) {
                if(tagList.get(indSlow).equals(tagList.get(indFast))) {
                    hasDuplicate = true;
                    break;
                }
            }

            if(hasDuplicate)
                break;
        }

        return hasDuplicate;
    }

    private boolean containsNonExistentTag(Meme memeGiven) {
        boolean result = false;

        // check if ANY tags within memeGiven is not in tagsPersistence
        for(Tag currMemeTag : memeGiven.getTags()) {
            boolean contains = false;

            // check if currMemeTag is not in tagsPersistence
            for(Tag currPersTag : tagsPersistence.getTags()) {

                // contains = true, if both tags have the same name
                if(currMemeTag.toString().equals(currPersTag.toString())) {
                    contains = true;
                    break;
                }
            }

            if(!contains) {
                result = true;
                break;
            }
        }

        return result;
    }
}
